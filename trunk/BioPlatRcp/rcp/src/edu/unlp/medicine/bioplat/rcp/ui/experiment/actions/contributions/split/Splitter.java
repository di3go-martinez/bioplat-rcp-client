package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.split;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import gnu.trove.map.hash.TObjectDoubleHashMap;

//TODO tests y refactor (mejores abstracciones...)!
public class Splitter {

	private Double training = 33.0;
	private Double testing = 33.0;
	private Double validation = 34.0;

	boolean setup(Double training, Double testing, Double validation) {
		this.training = training;
		this.testing = testing;
		this.validation = validation;
		return training > 0 && training < 99 && (training + testing + validation) == 100.0;
	}

	List<Experiment> split(AbstractExperiment dataset) {
		Long ctraining = resolveAmountOfSamples(dataset, training);
		Long ctesting = resolveAmountOfSamples(dataset, testing) - 1;
		Long cvalidation = resolveAmountOfSamples(dataset, validation);

		List<Sample> samples = dataset.getSamples();
		Collections.shuffle(samples);
		
		Sample[] source = samples.toArray(new Sample[0]);
		//shuffleArray(source);

		Sample[] trainingPart = new Sample[ctraining.intValue()];
		Sample[] testingPart = new Sample[ctesting.intValue()];
		Sample[] validationPart = new Sample[cvalidation.intValue()];

		// (src , src-offset , dest , offset, count)
		System.arraycopy(source, 0, trainingPart, 0, trainingPart.length);
		System.arraycopy(source, trainingPart.length, testingPart, 0, testingPart.length);
		System.arraycopy(source, testingPart.length, validationPart, 0, validationPart.length);

		return Lists.newArrayList(createExperiment(dataset, "training", trainingPart),
				createExperiment(dataset, "testing", testingPart),
				createExperiment(dataset, "validation", validationPart));

	}

	private Experiment createExperiment(AbstractExperiment dataset, String stage, Sample[] samples) {
		String name = dataset.getName() + " " + stage;
		String author = dataset.getAuthor();
		String email = dataset.getEmail();
		ARNmPlatform arnMlatform = dataset.getArnmPlatform();
		Map<String, Sample> samplesmap = findSamplesMap(dataset, samples);
		Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles = findSampleExpressionProfiles(dataset,
				samples);
		return new Experiment(name, author, email, arnMlatform, samplesmap, samplesExpressionProfiles);

	}

	private Map<Sample, TObjectDoubleHashMap<Gene>> findSampleExpressionProfiles(AbstractExperiment dataset,
			Sample[] samples) {

		Map<Sample, TObjectDoubleHashMap<Gene>> expresionProfiles = dataset.getSamplesExplessionProfiles();

		final List<Sample> sortedSamples = Lists.newArrayList(samples);
		Collections.sort(sortedSamples);

		return Maps.filterKeys(expresionProfiles, new Predicate<Sample>() {

			@Override
			public boolean apply(Sample input) {
				int index = Collections.binarySearch(sortedSamples, input);
				boolean found = index >= 0;
				return found;
			}
		});

	}

	private Map<String, Sample> findSamplesMap(AbstractExperiment dataset, Sample[] samples) {
		Map<String, Sample> sampleMap = dataset.getSampleMap();

		final List<Sample> sortedSamples = Lists.newArrayList(samples);
		Collections.sort(sortedSamples);

		return Maps.filterValues(sampleMap, new Predicate<Sample>() {

			@Override
			public boolean apply(Sample input) {
				int index = Collections.binarySearch(sortedSamples, input);
				boolean found = index >= 0;
				return found;
			}
		});
	}

	private long resolveAmountOfSamples(AbstractExperiment experiment, Double percentage) {
		final int totalsamples = experiment.getSampleCount();
		Double elements = ((percentage * totalsamples) / 100);
		return Math.round(elements);
	}

	// Implementing Fisher–Yates shuffle
	private static void shuffleArray(Sample[] ar) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			Sample a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public Double remaining() {
		return 100 - (this.training + this.testing+this.validation);
	}

}

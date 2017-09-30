package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.split;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import gnu.trove.map.hash.TObjectDoubleHashMap;

//TODO tests y refactor (mejores abstracciones...)!
public class Splitter {

	//TODO usar
	private static enum stages{
		training, testing, validation;
	}
	
	//poner en un mapa y revisar los resolveAmountOfSamples
	private Double trainingPercentage = 33.0;
	private Double testingPercentage = 33.0;
	private Double validationPercentage = 34.0;
	private AbstractExperiment dataset;

	
    SplitterSetupResult setup(AbstractExperiment expertiment, Double trainingPercentage, Double testingPercentage, Double validationPercentage) {
		this.dataset = expertiment;
		this.trainingPercentage = trainingPercentage;
		this.testingPercentage = testingPercentage;
		this.validationPercentage = validationPercentage;
		return validate();
	}


	private SplitterSetupResult validate() {
		boolean percentageok= trainingPercentage > 0 && trainingPercentage < 99 && (trainingPercentage + testingPercentage + validationPercentage) == 100.0;
		if (!percentageok)
			return SplitterSetupResult.fail("Invalid porcentages. \nPorcentages remaining to assign is "+ remaining());
	
		
		Long ctraining = resolveAmountOfSamples(dataset, trainingPercentage);
		Long ctesting = resolveAmountOfSamples(dataset, testingPercentage);
		Long cvalidation = resolveAmountOfSamples(dataset, validationPercentage);
		//TODO validation o testing pueden ser 0, pero no a la vez
		boolean atleastOneSample = ctraining > 0 && ctesting > 0 && cvalidation > 0;
		
		if (!atleastOneSample)
			return SplitterSetupResult.fail("The current configuration is not a valid partition. \nDataset can not be empty");
		
		return SplitterSetupResult.ok();
	}

	List<Experiment> split() {
		
		//problemas de redondeo revisar que puede quedar la suma mayor al número de samples
		Long ctraining = resolveAmountOfSamples(dataset, trainingPercentage);
		Long ctesting = resolveAmountOfSamples(dataset, testingPercentage) ; 
		Long cvalidation = resolveAmountOfSamples(dataset, validationPercentage);

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

	//TODO en realidad se puede haber pasado del 100, da un número negativo
	private Double remaining() {
		return 100 - (this.trainingPercentage + this.testingPercentage+this.validationPercentage);
	}

	

}

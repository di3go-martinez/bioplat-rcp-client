package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.experiment.collapse.strategy.media.MediaCollapseStrategy;
import edu.unlp.medicine.entity.gene.Gene;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import zinbig.bioplatcloud.api.MolecularDataType;
import zinbig.bioplatcloud.api.dto.DatasetDTO;
import zinbig.bioplatcloud.api.dto.SampleDTO;

/**
 * @see #createExperiment(DatasetDTO)
 */
public enum Experiments {

	factory;

	public Experiment createExperiment(final DatasetDTO dataset) {
		final Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles = samplesExpressionProfiles(dataset);

		// TODO llevar este loop al constructor de experimentos y simplificar sus parámetros
		final Map<String, Sample> samples = Maps.newHashMap();
		for (Sample sample : samplesExpressionProfiles.keySet())
			samples.put(sample.getName(), sample);// TODO confirmar si el nombre es la clave del mapa que espera el
													// constructor

		Experiment e = new Experiment(dataset.getName(), dataset.getAuthor(), null, null, samples,
				samplesExpressionProfiles);

		setClinicalAttributes(e, dataset);

		return e;
	}

	private void setClinicalAttributes(Experiment e, DatasetDTO dataset) {
		for (SampleDTO sample : dataset.getAllSamples())
			for (Entry<String, String> cd : sample.getClinicalData().entrySet())
				e.setClinicalAttribute(sample.getSampleID(), cd.getKey(), cd.getValue());
	}

	private Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles(final DatasetDTO dataset) {
		final Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles = Maps.newHashMap();

		for (SampleDTO sampledto : samples(dataset)) {

			Sample samplemodel = new Sample(sampledto.getSampleID(), collapseStrategy());

			for (Gene gene : genes(dataset))
				samplemodel.setGeneExpressionLevel(gene, findExpression(sampledto, gene));

			samplesExpressionProfiles.put(samplemodel, samplemodel.collapseExpressionData());
		}
		return samplesExpressionProfiles;
	}

	private Double findExpression(SampleDTO sampledto, Gene gene) {
		Optional<Number> result = sampledto.expression(MolecularDataType.genome, gene.getName());
		if (result.isPresent())
			return result.get().doubleValue();
		throw new RuntimeException("no value for gene " + gene.getName());
	}

	private Set<Gene> genes(DatasetDTO dataset) {
		// TODO dataset.genes(), usarlo directo y borrar este método
		return Collections.emptySet();
	}

	// TODO parametrizar
	private MediaCollapseStrategy collapseStrategy() {
		return new MediaCollapseStrategy();
	}

	/**
	 * @return todos los samples del dataset
	 */
	// TODO revisar, este método debería estar en DatasetDTO
	// TODO getSamples debería ser un set de samples con todos los samples
	private Collection<SampleDTO> samples(final DatasetDTO dataset) {
		Collection<SampleDTO> samplesdto = Sets.newHashSet();
		for (Collection<SampleDTO> part : dataset.getSamples().values())
			samplesdto.addAll(part);
		return samplesdto;
	}

}

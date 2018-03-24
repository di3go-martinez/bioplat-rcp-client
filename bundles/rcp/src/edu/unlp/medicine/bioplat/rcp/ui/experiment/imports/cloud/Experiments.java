package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.exceptions.GeneNotFoundByIdException;
import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
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

		Experiment e = new Experiment(dataset.getName(), dataset.getAuthor(), email(dataset.getAuthor()), arnmPlatform(),
				samples, samplesExpressionProfiles);

		setClinicalAttributes(e, dataset);

		return e;
	}

	private ARNmPlatform arnmPlatform() {
		// TODO de donde se saca?
		return new ARNmPlatform();
	}

	private String email(String author) {
	    // TODO de donde se saca?
	    return author.replace(" ", "_")+"@mail.com";
	}

	// TODO parametrizar? el dataset yalo tendría que tener?
	private MediaCollapseStrategy collapseStrategy() {
		return new MediaCollapseStrategy();
	}

	private void setClinicalAttributes(Experiment e, DatasetDTO dataset) {
		for (SampleDTO sample : dataset.getAllSamples())
			for (Entry<String, String> cd : sample.getClinicalData().entrySet())
				e.setClinicalAttribute(sample.getSampleID(), cd.getKey(), cd.getValue());
	}

	private Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles(final DatasetDTO dataset) {
		final Map<Sample, TObjectDoubleHashMap<Gene>> samplesExpressionProfiles = Maps.newHashMap();
		
		for (SampleDTO sampledto : samples(dataset)) {
			Sample sample = createSample(dataset, sampledto);
			samplesExpressionProfiles.put(sample, sample.collapseExpressionData());
		}
		return samplesExpressionProfiles;
	}

	private Sample createSample(final DatasetDTO dataset, SampleDTO sampledto) {
		Sample samplemodel = new Sample(sampledto.getSampleID(), collapseStrategy());

		for (Gene gene : genes(dataset, sampledto))
			samplemodel.setGeneExpressionLevel(gene, findExpression(sampledto, gene));
		return samplemodel;
	}

	private Double findExpression(SampleDTO sampledto, Gene gene) {
		Optional<Number> result = sampledto.expression(MolecularDataType.mrna, gene.getName());
		if (result.isPresent())
			return result.get().doubleValue();
		throw new RuntimeException("no value for gene " + gene.getName());
	}

	private Set<Gene> genes(DatasetDTO dataset, SampleDTO sampledto) {
		Set<Gene> genes = Sets.newHashSet();
		Map<String, Number> expressions = sampledto.getMolecularsData().get(MolecularDataType.mrna);
		// TODO confirmar que es el genename
		for (String genename : expressions.keySet()) 
			try {
				genes.add(MetaPlat.getInstance().getGeneByName(genename));
			} catch (GeneNotFoundByIdException re) {
				logger.error("Gene not found", re);
			}
		
		return genes;

	}

	private static final Logger logger = LoggerFactory.getLogger(Experiments.class);

	/**
	 * @return todos los samples del dataset.  
	 *
	 */
	//FIXME reemplazar con dataset.getAllSamples(). pedir cambio en la api de bioplat-cloud...
	private Collection<SampleDTO> samples(final DatasetDTO dataset) {
		Collection<SampleDTO> samplesdto = Sets.newHashSet();
		for (Collection<SampleDTO> part : dataset.getSamples().values())
			samplesdto.addAll(part);
		return samplesdto;
	}

}

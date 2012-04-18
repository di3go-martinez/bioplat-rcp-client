package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CellValueResolver;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

class ExpressionLevelResolver implements CellValueResolver<Double> {

	private static Logger logger = LoggerFactory.getLogger(ExpressionLevelResolver.class);

	private static Double _DEFAULT = 0d;

	private AbstractExperiment experiment;
	private Sample sample;
	private Gene gene;

	public ExpressionLevelResolver(AbstractExperiment experiment, Sample sample, Gene gene) {
		this.experiment = experiment;
		this.sample = sample;
		this.gene = gene;
	}

	@Override
	public void doSet(Double value) {
		if (!experiment.getSamples().contains(sample))
			logger.warn("El sample " + sample + " no está ya en el experimento " + experiment);

		// TODO revisar que no sea por name, sino por id...
		experiment.setExpressionLevelForAGene(sample.getName(), gene, value);
	}

	@Override
	public Double doGet() {
		if (!experiment.getSamples().contains(sample))
			return _DEFAULT;
		return experiment.getExpressionLevelForAGene(sample.getName(), gene);
	}

}

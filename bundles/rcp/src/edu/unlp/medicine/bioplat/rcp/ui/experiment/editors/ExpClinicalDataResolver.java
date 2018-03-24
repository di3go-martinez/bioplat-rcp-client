package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CellValueResolver;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;

public class ExpClinicalDataResolver implements CellValueResolver<String> {

	private AbstractExperiment experiment;
	private String attributeName;
	private Sample sample;
	private String sampleId;
	private static final String _DEFAULT = "0";

	public ExpClinicalDataResolver(AbstractExperiment e, String a, Sample s) {
		this.experiment = e;
		this.attributeName = a;
		this.sample = s;
		this.sampleId = s.getName(); // TODO revisar que sea un buen id desde el
										// modelo lo define así.....
	}

	// FIXME acomodar comparator en ColumnBuilder
	@Override
	public void doSet(String value) {
		experiment.setClinicalAttribute(sampleId, attributeName, value);
	}

	// FIXME acomodar comparator en ColumnBuilder
	@Override
	public String doGet() {
		if (!experiment.getSamples().contains(sample))
			return _DEFAULT;
		return experiment.getClinicalAttribute(sampleId, attributeName);
	}

}

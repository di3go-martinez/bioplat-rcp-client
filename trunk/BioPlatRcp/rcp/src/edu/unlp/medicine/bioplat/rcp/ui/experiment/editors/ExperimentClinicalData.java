package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class ExperimentClinicalData extends AbstractEditorPart<Experiment> {

	@Override
	protected void doCreatePartControl(Composite parent) {
		List<ClinicalDataModel> model = makeModel(model());

		TableBuilder tb = TableBuilder.create(parent).input(model);

		tb.addColumn(ColumnBuilder.create().property("data[0]"));
		int index = 1;
		for (Sample s : model().getSamples())
			tb.addColumn(ColumnBuilder.create().title(s.getName()).property("data[" + index++ + "]"));

		tb.build();

		setPartName("Clinical Data");
		yaseteado = true;
	}

	// FIXME revisar por qué se actualiza este editor cuando se guarda el editor
	// contenedor.
	private boolean yaseteado = false;

	@Override
	protected void setPartName(String partName) {
		if (!yaseteado)
			super.setPartName(partName);

	}

	private List<ClinicalDataModel> makeModel(Experiment model) {
		return ClinicalDataModel.create(model);
	}
}

class ClinicalDataModel extends AbstractEntity {

	private Object[] data;

	public Object[] getData() {
		return data;
	}

	public ClinicalDataModel(Object[] data) {
		this.data = data;
	}

	public static List<ClinicalDataModel> create(Experiment e) {
		List<ClinicalDataModel> result = Lists.newArrayList();

		// el nombre del atributo
		for (String a : e.getClinicalAttributeNames()) {
			// uno más para que entre
			Object[] data = new Object[e.getSamples().size() + 1];
			int index = 0;
			data[index++] = a;
			for (Sample s : e.getSamples())
				data[index++] = e.getClinicalAttribute(s.getName(), a);
			result.add(new ClinicalDataModel(data));

		}

		return result;
	}
}

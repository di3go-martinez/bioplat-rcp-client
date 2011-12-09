package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

class ExperimentEditor0 extends AbstractEditorPart<Experiment> {

	private TableReference tr;

	@Override
	protected void doCreatePartControl(Composite parent) {

		// CTabFolder tf = new CTabFolder(parent, SWT.BOTTOM);

		// CTabItem ti = new CTabItem(tf, SWT.BORDER);

		Composite container = new Composite(parent /* tf */, SWT.BORDER);// Widgets.createDefaultContainer(parent)
		/*
		 * ti.setText("Cabecera:"); ti.setControl(container);
		 */

		Composite c = new Composite(container, SWT.BORDER);
		c.setLayout(new GridLayout(4, false));
		c.setLayoutData(GridDataFactory.fillDefaults().span(1, 2).create());
		Widgets.createTextWithLabel(c, "Nombre", model(), "name");
		Widgets.createTextWithLabel(c, "Genes", model(), "numberOfGenes");
		Widgets.createTextWithLabel(c, "Autor", model(), "author");

		// ejecuta la creación del input en background...
		final Holder<List<ClinicalDataModel>> inputHolder = new Holder<List<ClinicalDataModel>>(null);

		// construyo el input para el tablebuilder
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {
				inputHolder.hold(ClinicalDataModel.create(model()));
			}

		});

		TableBuilder tb = TableBuilder.create(container) //
				.model(new AbstractEntity() {
					public List<ClinicalDataModel> getData() {
						return inputHolder.value();
					}
				}, "data")
		// .input(inputHolder.value())
		;

		tb.addColumn(ColumnBuilder.create().title("Gen id").numeric().property("data[0]")); //
		int index = 1;
		for (Sample s : model().getSamples())
			tb.addColumn(ColumnBuilder.create().numeric().title(s.getName()).property("data[" + index++ + "]"));

		tr = tb.build();

		GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(container);
	}

	@Override
	protected Observer createModificationObserver() {

		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				// nada eficiente...
				List<ClinicalDataModel> data = ClinicalDataModel.create(model());
				tr.input(data);
			}
		};
	}
}

// TODO revisar que es obligatorio que extienda abstractEntity... @see
// TableBuilder#input
class ClinicalDataModel extends AbstractEntity {

	public static List<ClinicalDataModel> create(Experiment e) {
		List<ClinicalDataModel> result = Lists.newArrayList();

		for (Gene g : e.getGenes()) {
			final int sampleCount = e.getSamples().size();
			Object[] data = new Object[sampleCount + 1];
			data[0] = g.getEntrezId();
			int index = 1;
			for (Sample s : e.getSamples())
				// TODO revisar que no sea por name, si no por id...
				data[index++] = e.getExpressionLevelForAGene(s.getName(), g);

			result.add(new ClinicalDataModel(data));
		}
		return result;
	}

	// columnas: gen columna1 columna2 columna3

	// [0]=> genid; [1..n]=>expressión génica del sample 1 al n para el gen
	// data[0]
	Object[] data;

	public Object[] getData() {
		return data;
	}

	public ClinicalDataModel(Object[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Arrays.toString(data);
	}
}

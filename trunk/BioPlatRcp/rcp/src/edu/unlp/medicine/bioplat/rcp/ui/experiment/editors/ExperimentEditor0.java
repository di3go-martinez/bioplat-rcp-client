package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

class ExperimentEditor0 extends AbstractEditorPart<Experiment> {

	private List<ClinicalDataModel> data;
	private TableReference tr;

	@Override
	protected void doCreatePartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.BORDER);// Widgets.createDefaultContainer(parent)

		Composite c = new Composite(container, SWT.BORDER);
		c.setLayout(new GridLayout(4, false));
		c.setLayoutData(GridDataFactory.fillDefaults().span(1, 2).create());
		Widgets.createTextWithLabel(c, "Nombre", model(), "name");
		Widgets.createTextWithLabel(c, "Genes", model(), "numberOfGenes", true);
		Widgets.createTextWithLabel(c, "Autor", model(), "author");

		// construyo el input para el tablebuilder en background
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {
				data = ClinicalDataModel.create(model());
			}

		});

		TableBuilder tb = TableBuilder.create(container)//
				.keyLimit(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_GENES)//
				.model(new AbstractEntity() {
					public List<ClinicalDataModel> getData() {
						return data;
					}
				}, "data")
		// .input(inputHolder.value())

		;

		tb.addColumn(ColumnBuilder.create().title("Gen id").numeric().property("data[0]")); //
		int index = 1;
		final List<Sample> sampleToLoad = resolveSamplesToLoad();
		for (Sample s : sampleToLoad)
			// TODO externalizar el límite máximo
			tb.addColumn(ColumnBuilder.create().numeric().title(s.getName()).property("data[" + index++ + "]"));

		tr = tb.build();

		GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(container);
	}

	private List<Sample> resolveSamplesToLoad() {
		int max = ep().getInt(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_SAMPLES, 20);
		return model().getSamples().subList(0, max - 1);// -1 porque empiezo de
														// 0
	}

	private IEclipsePreferences ep;

	private IEclipsePreferences ep() {
		if (ep == null)
			ep = ConfigurationScope.INSTANCE.getNode(Activator.id());
		return ep;
	}

	@Override
	protected Observer createModificationObserver() {

		return new Observer() {

			private int counter = 0;
			private boolean alwaysUpdate = false;

			@Override
			public void update(Observable o, Object arg) {

				if (!autorefresh())
					return;

				// FIXME nada eficiente crear el modelo cada vez, por eso se
				// hace un merge...
				if (!alwaysUpdate) {
					if (counter != 50) // FIXME no actualiza, sino cada 100...
					// problema:
					// quedan datos sin actualizar si la
					// cantidad de datos no es múltiplo de 100
					{
						counter++;
						return;
					}
					counter = 0;
				}

				data = ClinicalDataModel.merge(data, model());
				tr.input(data);
			}

			private boolean autorefresh() {
				return ep().getBoolean(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_AUTO_REFRESH, true);
			}
		};
	}
}

// TODO revisar que es obligatorio que extienda abstractEntity... @see
// TableBuilder#input
class ClinicalDataModel extends AbstractEntity {

	/**
	 * Usar con cuidado, puede colgar la memoria de la aplicación...
	 * 
	 * @see #merge(Experiment)
	 * 
	 * @param e
	 * @return
	 */
	public static List<ClinicalDataModel> create(Experiment e) {
		List<ClinicalDataModel> result = Lists.newArrayList();

		for (Gene g : e.getGenes()) {
			final int sampleCount = e.getSamples().size();
			Object[] data = new Object[sampleCount + 1];
			data[0] = g.getEntrezId();
			int index = 1;

			for (Sample s : e.getSamples()) {
				// TODO revisar que no sea por name, si no por id...
				data[index++] = e.getExpressionLevelForAGene(s.getName(), g);
			}

			result.add(new ClinicalDataModel(data));

		}
		return result;
	}

	/**
	 * 
	 * 
	 * @param current
	 *            es el modelo del experimento transformado, puede ser null, en
	 *            este caso se crea uno a partir de e (debería pasar solo la
	 *            primera vez que se invoca)
	 * @param e
	 *            es el nuevo experimento
	 * @return el objeto current actualizado con el experimento e
	 */
	public static List<ClinicalDataModel> merge(List<ClinicalDataModel> current, Experiment e) {
		if (current == null)
			current = create(e);
		else {
			int index0 = 0;
			for (Gene g : e.getGenes()) {
				int index1 = 1;
				ClinicalDataModel cdm = current.get(index0);

				for (Sample s : e.getSamples()) {

					if (!cdm.data[index1].equals(e.getExpressionLevelForAGene(s.getName(), g)))
						cdm.data[index1] = e.getExpressionLevelForAGene(s.getName(), g);
					index1++;

				}
				index0++;
			}
		}
		return current;
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

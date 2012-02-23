package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.AbstractSelectionService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

class ExperimentEditor0 extends AbstractEditorPart<Experiment> implements ISelectionChangedListener {

	private List<ExpressionDataModel> data;
	private TableReference tr;

	@Override
	protected void doCreatePartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.BORDER);// Widgets.createDefaultContainer(parent)

		Composite c = new Composite(container, SWT.BORDER);
		c.setLayout(new GridLayout(4, false));
		c.setLayoutData(GridDataFactory.fillDefaults().span(1, 2).create());
		Widget w = Widgets.createTextWithLabel(c, "Nombre", model(), "name");

		model().addPropertyChangeListener("name", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setPartName(evt.getNewValue().toString());
			}
		});

		Widgets.createTextWithLabel(c, "Genes", model(), "numberOfGenes", true);
		Widgets.createTextWithLabel(c, "Autor", model(), "author");

		// construyo el input para el tablebuilder en background
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {
				data = ExpressionDataModel.create(model());
			}

		});

		TableBuilder tb = TableBuilder.create(container)//
				.keyLimit(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_GENES)//
				.model(new AbstractEntity() {
					public List<ExpressionDataModel> getData() {
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
		tr.addSelectionChangeListener(this);
		GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(container);

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// FIXME sacar el downcast, avisar a de una manera prolija
		AbstractSelectionService ass = (AbstractSelectionService) getSite().getWorkbenchWindow().getSelectionService();
		ass.setActivePart(null);
		ass.setActivePart(this);
	}

	private List<Sample> resolveSamplesToLoad() {
		int max = ExperimentEditor.getSampleCountToLoad();
		return model().getSamples().subList(0, max - 1);// -1 porque empiezo de
														// // 0
	}

	@Override
	protected Map<Object, IStructuredSelection> getAdditionalSelections() {
		Map<Object, IStructuredSelection> selections = Maps.newHashMap();

		List<ExpressionDataModel> l = tr.focusedElements();

		List<Gene> genes = Lists.transform(l, new Function<ExpressionDataModel, Gene>() {
			@Override
			public Gene apply(ExpressionDataModel input) {
				return input.findGene();
			}
		});

		selections.put(Constants.GENES, new StructuredSelection(genes));
		return selections;
	}

	@Override
	protected Observer createModificationObserver() {

		return new Observer() {

			private int counter = 0;

			@Override
			public void update(Observable o, Object arg) {

				if (!autorefresh())
					return;

				// FIXME nada eficiente crear el modelo cada vez, por eso se
				// hace un merge...

				// if (counter != 50) // FIXME no actualiza, sino cada 50...
				// // problema:
				// // quedan datos sin actualizar si la
				// // cantidad de datos no es múltiplo de 100
				// {
				// counter++;
				// return;
				// }
				counter = 0;

				data = ExpressionDataModel.merge(data, model());
				tr.input(data);
			}

			private boolean autorefresh() {
				return ExperimentEditor.ep().getBoolean(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_AUTO_REFRESH, true);
			}
		};
	}

	public void showGene(Gene selectedGene) {
		tr.show(selectedGene);

	}
}

// TODO revisar que es obligatorio que extienda abstractEntity... @see
// TableBuilder#input
class ExpressionDataModel extends AbstractEntity {

	/**
	 * Usar con cuidado, puede colgar la memoria de la aplicación...
	 * 
	 * @see #merge(Experiment)
	 * 
	 * @param e
	 * @return
	 */
	public static List<ExpressionDataModel> create(Experiment e) {
		List<ExpressionDataModel> result = Lists.newArrayList();

		for (Gene g : e.getGenes()) {
			final int sampleCount = e.getSamples().size();
			Object[] data = new Object[sampleCount + 1];
			data[0] = g.getEntrezId();
			int index = 1;

			for (Sample s : e.getSamples()) {
				// TODO revisar que no sea por name, si no por id...
				data[index++] = e.getExpressionLevelForAGene(s.getName(), g);
			}

			result.add(new ExpressionDataModel(data));

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
	public static List<ExpressionDataModel> merge(List<ExpressionDataModel> current, Experiment e) {
		if (current == null)
			current = create(e);
		else {
			int index0 = 0;
			for (Gene g : e.getGenes()) {
				int index1 = 1;
				ExpressionDataModel cdm = current.get(index0);

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

	public ExpressionDataModel(Object[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Arrays.toString(data);
	}

	public Gene findGene() {
		final long id = Long.parseLong(data[0].toString());
		return MetaPlat.getInstance().getGeneByEntrezId(id);
	}
}

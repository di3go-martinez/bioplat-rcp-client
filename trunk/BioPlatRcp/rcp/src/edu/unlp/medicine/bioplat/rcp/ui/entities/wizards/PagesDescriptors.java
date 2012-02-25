package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

public class PagesDescriptors {

	// clave para el wizardModel que indica la lista de elementos seleccionados.
	// Es un common value, no un IObservableValue...
	public static final String SELECTED = "selected";

	public static WizardPageDescriptor clusterPageDescriptor() {
		return new WizardPageDescriptor("Cluster") {

			@Override
			public Control create(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				return new Composite(parent, SWT.NONE);
			}
		};
	}

	/**
	 * Define y mantiene la variable SELECTED para indicar la lista de
	 * experimentos seleccionados
	 * 
	 * @return
	 */
	public static WizardPageDescriptor experimentsWPD() {
		return new WizardPageDescriptor("Experimentos") {

			@Override
			public Control create(Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
				List<AbstractExperiment> editors = Lists.newArrayList();
				for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
					for (IWorkbenchPage page : window.getPages()) {
						for (IEditorReference editor : page.getEditorReferences()) {
							IEditorPart ed = editor.getEditor(false);
							ModelProvider e;
							if (ed instanceof ModelProvider && (e = (ModelProvider) ed).model() instanceof AbstractExperiment)
								editors.add((AbstractExperiment) e.model());
						}
					}
				}

				Composite container = new Composite(parent, SWT.BORDER);
				final TableReference tr = TableBuilder.create(container).input(editors).hideTableLines()//
						.addColumn(ColumnBuilder.create().title("Experimentos cargados").property("name"))//
						.build();
				tr.addSelectionChangeListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						wmodel.set(SELECTED, tr.selectedElements());
					}
				});

				GridLayoutFactory.fillDefaults().margins(10, 10).applyTo(container);
				return container;
			}
		};
	}
}

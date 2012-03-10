package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.AttributeTypeEnum;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

public class PagesDescriptors {

	// clave para el wizardModel que indica la lista de elementos seleccionados.
	// Es un common value, no un IObservableValue...
	public static final String SELECTED = "SELECTED";

	public static final String ATTRIBUTE_TYPE = "ATTRIBUTE_TYPE";
	public static final String GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE = "GENERATE_CLUSTER_&_CALCULATE_BIOLOGICAL_VALUE";
	public static final String NUMBER_OF_CLUSTERS = "NUMBER_OF_CLUSTERS";
	public static final String TIMES_TO_REPEAT_CLUSTERING = "TIMES_TO_REPEAT_CLUSTERING";
	public static final String VALIDATION_TYPE = "VALIDATION_TYPE";
	public static final String STATISTICAL_TEST_VALUE = "STATISTICAL TEST VALUE";
	public static final String ATTRIBUTE_NAME_TO_VALIDATION = "ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String SECOND_ATTRIBUTE_NAME_TO_VALIDATION = "SECOND_" + ATTRIBUTE_NAME_TO_VALIDATION;
	public static final String REMOVE_GENES_IN_BIOMARKER = "REMOVE_GENES_IN_BIOMARKER";

	private static WizardPageDescriptor clusterPageDescriptor() {
		return new WizardPageDescriptor("Cluster") {

			@Override
			public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
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
	// TODO agregar desde distintas fuentas, inSilico e archivo
	public static WizardPageDescriptor experimentsWPD() {
		return new WizardPageDescriptor("Experimentos") {

			@Override
			public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
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
						.addColumn(ColumnBuilder.create().title("Experimentos cargados").width(200).property("name"))//
						.build();
				tr.addSelectionChangeListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						wmodel.set(SELECTED, tr.selectedElements());
						wp.setPageComplete(isPageComplete(wmodel));
					}
				});

				GridLayoutFactory.fillDefaults().margins(10, 10).applyTo(container);
				return container;
			}

			@Override
			public boolean isPageComplete(@Nullable WizardModel model) {
				if (model == null)
					return false;
				List<?> l = model.value(SELECTED);
				if (l == null)
					return false;
				return !l.isEmpty();
			}
		};
	}

	public static WizardPageDescriptor configurationPage() {
		return new WizardPageDescriptor("Configuración") {

			@Override
			public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
				Composite result = new Composite(parent, SWT.None);

				Button check = new Button(result, SWT.CHECK);
				check.setText("Generate cluster and calculate biological value");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE));
				check.setSelection(true);

				new Label(result, SWT.NONE).setText("Number of clusters:");
				Text t = new Text(result, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(NUMBER_OF_CLUSTERS));

				new Label(result, SWT.NONE).setText("Times to repeat de k-means clustering (& keep the best):");
				Text t2 = new Text(result, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(t2, SWT.Modify), wmodel.valueHolder(TIMES_TO_REPEAT_CLUSTERING));

				new Label(result, SWT.NONE).setText("Validation Type:");
				ComboViewer collapseStrategyCombo = new ComboViewer(result, SWT.BORDER | SWT.READ_ONLY);
				collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
				collapseStrategyCombo.setInput(AttributeTypeEnum.values());

				new Label(result, SWT.NONE).setText("Statistical Significance Test:");
				final ComboViewer detail = new ComboViewer(result, SWT.BORDER | SWT.READ_ONLY);
				detail.setContentProvider(new ArrayContentProvider());

				collapseStrategyCombo.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						ComboViewer csc = (ComboViewer) event.getSource();
						AttributeTypeEnum ate = (AttributeTypeEnum) ((StructuredSelection) csc.getSelection()).getFirstElement();
						if (ate != null)
							detail.setInput(ate.getStatisticsSignificanceTestsThatCouldBeAppliedToThisType());
					}
				});
				// bind value del "maestro", notar el listener de selección
				IObservableValue collapseStrategySelected = ViewersObservables.observeSingleSelection(collapseStrategyCombo);
				dbc.bindValue(collapseStrategySelected, wmodel.valueHolder(VALIDATION_TYPE), UpdateStrategies.nonNull("Validation Type"), UpdateStrategies.nullStrategy());
				// bind value del "detalle"
				IObservableValue selectedStatisticalValue = ViewersObservables.observeSingleSelection(detail);
				dbc.bindValue(selectedStatisticalValue, wmodel.valueHolder(STATISTICAL_TEST_VALUE), UpdateStrategies.nonNull("Statistical Value"), UpdateStrategies.nullStrategy());

				// hook para ejecutar el

				ComboViewer cv = Utils.newComboViewer(result, "Validation Attribute Name", "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab", Arrays.asList("OS_Months", "recurrence", "timeUntilEventOccured"));
				dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Attribute Name"), UpdateStrategies.nullStrategy());

				cv = Utils.newComboViewer(result, "Second Validation Attribute Name", "Second attribute name (just to complete if the type of validation is for \"event occured after time\" attribute)", Arrays.asList("none", "OS_Event", "status"));
				dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(SECOND_ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Second Attribute Name"), UpdateStrategies.nullStrategy());

				return result;
			}

		};
	}

}

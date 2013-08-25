package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.NUMBER_OF_CLUSTERS;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OS_EVENT;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OS_MONTHS;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OTHER;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OTHER_ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.RFS_EVENT;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.RFS_MONTHS;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.STATISTICAL_TEST_VALUE;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.VALIDATION_TYPE;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.AttributeTypeEnum;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.IStatisticsSignificanceTest;

public class ConfigurationWizardPageDescriptor extends WizardPageDescriptor {

	private ValidationTestGUIProvider validationTestGUIProvider;
	private boolean forManualClustering;

	// private Button useExistingCluster;

	public ConfigurationWizardPageDescriptor(ValidationTestGUIProvider validationTestGUIProvider, String string, boolean forManualClustering) {
		super(string);
		this.validationTestGUIProvider = validationTestGUIProvider;
		this.forManualClustering = forManualClustering;
	}

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {

		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		Composite container = new Composite(parent, SWT.CENTER);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(20, 10).create());

		// createGroup4ClusteringChoise(container, gdf, dbc, wmodel);
		createGroup4CLusteringInfo(container, gdf, dbc, wmodel);
		createGroup4AdditionalParameters(container, gdf, dbc, wmodel);

		return container;
	}

	// private void createGroup4ClusteringChoise(Composite container,
	// GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel)
	// {
	// useExistingCluster = new Button(container, SWT.RADIO);
	// useExistingCluster.setText("Use sample cluster configured on experiment");
	//
	// useExistingCluster.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// for (Control c : clusterginGroup.getChildren())
	// c.setEnabled(!useExistingCluster.getSelection());
	// }
	// });
	//
	// dbc.bindValue(SWTObservables.observeSelection(useExistingCluster),
	// wmodel.valueHolder(USE_EXISTING_CLUSTER), null, null);
	//
	// Button calculateNewCluster = new Button(container, SWT.RADIO);
	// calculateNewCluster.setText("Calculate new cluster");
	//
	// // dbc.bindValue(SWTObservables.observeSelection(b),
	// // wmodel.valueHolder(USE_EXISTING_CLUSTER), new
	// // UpdateValueStrategy() {
	// // @Override
	// // public Object convert(Object value) {
	// // return false;
	// // }
	// // }, null);
	//
	// calculateNewCluster.setSelection(true);
	//
	// }

	private void createGroup4AdditionalParameters(Composite container, GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {

		if (validationTestGUIProvider.isThereAdditionalParameters()) {
			new Label(container, SWT.NONE).setText("\n\n");
			Group additionalParameters = new Group(container, SWT.NONE);
			additionalParameters.setText("Additional parameters of " + validationTestGUIProvider.getName());
			additionalParameters.setFont(GUIUtils.getFontForGrouptTitle(container));
			additionalParameters.setLayout(GridLayoutFactory.fillDefaults().margins(20, 15).create());
			additionalParameters.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

			validationTestGUIProvider.addAdditionComposite(additionalParameters, wmodel, dbc, gdf);
		}

	}

	private void createGroup4CLusteringInfo(Composite container, GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {

		Group clusterginGroup = new Group(container, SWT.SHADOW_OUT);
		clusterginGroup.setText("Clustering parameters");
		clusterginGroup.setFont(GUIUtils.getFontForGrouptTitle(container));
		clusterginGroup.setLayout(GridLayoutFactory.fillDefaults().margins(20, 10).create());
		clusterginGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Button check = new Button(result, SWT.CHECK);
		// check.setText("Generate cluster and calculate biological value");
		// dbc.bindValue(SWTObservables.observeSelection(check),
		// wmodel.valueHolder(GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE));
		// check.setSelection(true);

		new Label(clusterginGroup, SWT.NONE).setText("Number of clusters:");
		Text t = new Text(clusterginGroup, SWT.BORDER);
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(NUMBER_OF_CLUSTERS), uvsNumberOfClusters(), null);
		GridData gdClusters = gdf.grab(false, false).create();
		t.setLayoutData(gdClusters);
		t.setEnabled(!forManualClustering);

		if (!forManualClustering) {
			Label l = new Label(clusterginGroup, SWT.NONE);
			l.setText("\nTimes to repeat de k-means clustering (It keeps the best):");
			Text t2 = new Text(clusterginGroup, SWT.BORDER);
			// t2.setText("10");
			dbc.bindValue(SWTObservables.observeText(t2, SWT.Modify), wmodel.valueHolder(TIMES_TO_REPEAT_CLUSTERING));
			GridData gdRepeat = gdf.grab(false, false).create();
			t2.setLayoutData(gdRepeat);
		}
		// addValidationTypeAndStatissticaSigTestComponents(result, dbc,
		// wmodel);

		// hook para ejecutar el
		// ComboViewer validationAttrName =
		// Utils.newComboViewer(clusterginGroup,
		// "\nValidation Attribute Name",
		// "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab",
		// Arrays.asList(OS_MONTHS, RFS_MONTHS, "recurrence",
		// "timeUntilEventOccured"));
		// dbc.bindValue(ViewersObservables.observeSingleSelection(validationAttrName),
		// wmodel.valueHolder(ATTRIBUTE_NAME_TO_VALIDATION),
		// UpdateStrategies.nonNull("Attribute Name"),
		// UpdateStrategies.nullStrategy());
		// validationAttrName.getCombo().setLayoutData(gdf.create());
		// validationAttrName.setSelection(new
		// StructuredSelection(OS_MONTHS));
		//

		// Group clusterginGroup2 = new Group(container,
		// SWT.SHADOW_OUT);
		// clusterginGroup2.setLayout(GridLayoutFactory.fillDefaults().margins(20,
		// 10).create());
		// clusterginGroup2.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// false).create());

		new Label(clusterginGroup, SWT.NONE).setText("\n\"Time\" Attribute Name");
		Composite groupForValidationAtt1 = getGroupFoRClusteringAttribute(clusterginGroup);
		ComboViewer validationAttrName = Utils.newComboViewerWithoutLabel(groupForValidationAtt1, "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab. If it is not in the list, select 'other' for writing it in the text field.", Arrays.asList(OS_MONTHS, RFS_MONTHS, "recurrence", "timeUntilEventOccured", OTHER));
		dbc.bindValue(ViewersObservables.observeSingleSelection(validationAttrName), wmodel.valueHolder(ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Attribute Name"), UpdateStrategies.nullStrategy());
		validationAttrName.getCombo().setLayoutData(gdf.create());
		validationAttrName.setSelection(new StructuredSelection(OS_MONTHS));
		Text other = new Text(groupForValidationAtt1, SWT.BORDER);
		GridData gd = gdf.grab(true, false).create();
		other.setLayoutData(gd);
		dbc.bindValue(SWTObservables.observeText(other, SWT.Modify), wmodel.valueHolder(OTHER_ATTRIBUTE_NAME_TO_VALIDATION));

		new Label(clusterginGroup, SWT.NONE).setText("\n\"Status\" Attribute Name");
		Composite groupForValidationAtt2 = getGroupFoRClusteringAttribute(clusterginGroup);
		ComboViewer validationAttrName2 = Utils.newComboViewerWithoutLabel(groupForValidationAtt2, "Status attribute name (just to complete if the type of validation is for \"event occured after time\" attribute). Pick up one appearing in the clinical tab. If it is not in the list, select 'other' for writing it in the text field.", Arrays.asList(OS_EVENT, RFS_EVENT, "Evento", OTHER));
		dbc.bindValue(ViewersObservables.observeSingleSelection(validationAttrName2), wmodel.valueHolder(SECOND_ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Second Attribute Name"), UpdateStrategies.nullStrategy());
		validationAttrName2.getCombo().setLayoutData(gdf.create());
		validationAttrName2.setSelection(new StructuredSelection(OS_EVENT));
		Text other2 = new Text(groupForValidationAtt2, SWT.BORDER);
		GridData gd2 = gdf.grab(true, false).create();
		other2.setLayoutData(gd2);
		dbc.bindValue(SWTObservables.observeText(other2, SWT.Modify), wmodel.valueHolder(OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION));

	}

	private Composite getGroupFoRClusteringAttribute(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(2).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		return container;
	}

	private void addValidationTypeAndStatissticaSigTestComponents(Composite result, DataBindingContext dbc, WizardModel wmodel) {
		new Label(result, SWT.NONE).setText("Validation Type:");
		ComboViewer collapseStrategyCombo = new ComboViewer(result, SWT.BORDER | SWT.READ_ONLY);
		collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());

		collapseStrategyCombo.setInput(AttributeTypeEnum.values());
		// String[] methods =
		// {AttributeTypeEnum.EVENT_OCCURED_AFTER_TIME.name()};
		// collapseStrategyCombo.setInput(methods);

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

		// set defaults values después de haber hecho los bindings
		final AttributeTypeEnum attr = AttributeTypeEnum.EVENT_OCCURED_AFTER_TIME;
		collapseStrategyCombo.setSelection(new StructuredSelection(attr));

		final List<IStatisticsSignificanceTest> s = attr.getStatisticsSignificanceTestsThatCouldBeAppliedToThisType();
		if (!s.isEmpty())
			detail.setSelection(new StructuredSelection(s.get(0)));

	}

	private UpdateValueStrategy uvsNumberOfClusters() {
		return new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value.toString().matches("\\d+..\\d+") || value.toString().matches("\\d+")) {
					return ValidationStatus.ok();
				} else
					return ValidationStatus.error("'" + value + "'" + " no es un rango o número valido");
			}

		});
	}

}

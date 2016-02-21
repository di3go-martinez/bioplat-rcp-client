package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.CLUSTERING_STRATEGY;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.NUMBER_OF_CLUSTERS;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OS_EVENT;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OS_MONTHS;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.OTHER;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.RFS_EVENT;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.STATISTICAL_TEST_VALUE;
import static edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors.VALIDATION_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;
import org.slf4j.Logger;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.AttributeTypeEnum;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.IStatisticsSignificanceTest;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.ClustererFactory;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.ClusterersEnum;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringResult;
import edu.unlp.medicine.entity.experiment.Experiment;

public class ConfigurationWizardPageDescriptor extends WizardPageDescriptor {
	

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(ConfigurationWizardPageDescriptor.class);
	private ValidationTestGUIProvider validationTestGUIProvider;
	private boolean forManualClustering;
		
	private WizardModel wmodel;
	private DataBindingContext dbc;
	private ComboViewer validationAttrName;
	private GridDataFactory gdf;
	private Composite container;
	private Group survivalGroup; 
	
	public ConfigurationWizardPageDescriptor(ValidationTestGUIProvider validationTestGUIProvider, String string, boolean forManualClustering) {
		super(string);
		this.validationTestGUIProvider = validationTestGUIProvider;
		this.forManualClustering = forManualClustering;
	}
	
	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		this.wmodel = wmodel;
		this.dbc = dbc;
		try {
			gdf = GridDataFactory.fillDefaults().grab(true, false);

			container = new Composite(parent, SWT.NONE);
			container.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 0).margins(20, 20).create());

			// createGroup4ClusteringChoise(container, gdf, dbc, wmodel);
			
			createGroup4CLusteringInfo(container, gdf, dbc, wmodel, wp);
			//createGroup4AdditionalParameters(container, gdf, dbc, wmodel);

			container.redraw();
			container.pack(true);
			return container;
		} catch (Exception e) {
			logger.error("Error creating Configuration Page", e);
			return new Composite(parent, SWT.NONE);
		}
	}

	
	
	
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

	
	
	private void createGroup4CLusteringInfo(final Composite container, GridDataFactory gdf, final DataBindingContext dbc, final WizardModel wmodel, final WizardPage wp) {

		final Group clusterginGroup = new Group(container, SWT.SHADOW_OUT);
		clusterginGroup.setText("Clustering parameters");
		clusterginGroup.setFont(GUIUtils.getFontForGrouptTitle(container));
		clusterginGroup.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 10).margins(20, 20).create());
		clusterginGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		new Label(clusterginGroup, SWT.NONE).setText("Number of clusters:");
		final Text numberOfClusterText = new Text(clusterginGroup, SWT.BORDER);
		dbc.bindValue(SWTObservables.observeText(numberOfClusterText, SWT.Modify), wmodel.valueHolder(NUMBER_OF_CLUSTERS), uvsNumberOfClusters(), null);
		GridData gdClusters = gdf.grab(false, false).create();
		numberOfClusterText.setLayoutData(gdClusters);
		numberOfClusterText.setEnabled(!forManualClustering);
		logger.trace("Number of clusters created");

		if (!forManualClustering) {

			final Group clusterginStGroup = new Group(clusterginGroup, SWT.SHADOW_OUT);
			clusterginStGroup.setText("Clustering strategy");
			clusterginStGroup.setFont(GUIUtils.getFontForGrouptTitle(container));
			clusterginStGroup.setLayout(GridLayoutFactory.fillDefaults().margins(20, 20).create());
			clusterginStGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			
			//new Label(clusterginStGroup, SWT.NONE).setText("\nClustering strategy");
			
			// david: Mejorar como se carga el combo
			List<String> names = ClustererFactory.getInstance().getClustererNames();
			names.remove(ClusterersEnum.MANUAL_SETTING.getFriendlyName());
			
			
			final ComboViewer clusteringStrategy = Utils.newComboViewerWithoutLabel(clusterginStGroup, "Select the strategy to do your clustering.", names);
			dbc.bindValue(ViewersObservables.observeSingleSelection(clusteringStrategy), wmodel.valueHolder(CLUSTERING_STRATEGY), UpdateStrategies.nonNull("Clustering strategy"), UpdateStrategies.nullStrategy());
			clusteringStrategy.getCombo().setLayoutData(gdf.create());
			final StructuredSelection defaultStrategySelection = new StructuredSelection(ClusterersEnum.PAM.getFriendlyName());
			
			
			final Group clusterParameters = new Group(clusterginStGroup, SWT.NONE);
			clusterParameters.setText("Strategy parameters");
			clusterParameters.setFont(GUIUtils.getFontForGrouptTitle(container));
			clusterParameters.setLayout(GridLayoutFactory.fillDefaults().margins(20, 10).create());
			clusterParameters.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			
				
			clusteringStrategy.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					// es una lista porque en otros casos puede haber varios
					// seleccionados, acá habría solo uno
					//Experiment exp = ((List<Experiment>) wmodel.value(PagesDescriptors.SELECTED)).get(0);
					
					
					
					// Realiza elimina todas las estrategias 
					for(Control c : clusterParameters.getChildren()){
						c.dispose();
					}									
					
					ComboViewer csc = (ComboViewer) event.getSource();
					String clusteringStrategyStr = (String) ((StructuredSelection) csc.getSelection()).getFirstElement();
					
					/*if (clusteringStrategyStr.equals(RClustererManualSetting.getClustererName())) {
						clusterParameters.setText("Manual settings");
						numberOfClusterText.setEnabled(false);
						createManualSettings(clusterParameters,wmodel,wp);
						
						/*if (!exp.hasGotEnoughClustersForValidation()) {
							// recambio la estategia, ya que la manual no es
							// válida
							clusteringStrategy.setSelection(defaultStrategySelection);
							putmessage(wp, "The experiment you have selected must have at least two clusters. Your experiment have just " + exp.getNumberOfClusters() + ". Please go to the experiment and set at least 2 clusters manually.");

						} else {
							// por qué string? porque en ese campo se puede
							// poner un rango (ej: 2..3)... y eso es un string
							// TODO alternativa, el que crea el wmodel debería
							// saber qué value devolver...
							Object value;
							if (wmodel.valueHolder(NUMBER_OF_CLUSTERS).getValueType().equals(Integer.class))
								value = exp.getNumberOfClusters();
							else
								value = String.valueOf(exp.getNumberOfClusters());

							wmodel.update(PagesDescriptors.NUMBER_OF_CLUSTERS, value);
						}
					} else {
						// la estrategia no es manual, entonces activo el input
						numberOfClusterText.setEnabled(true);
					}*/
										
					
					if (clusteringStrategyStr.equals(ClusterersEnum.KMEANS.getFriendlyName())) {
						clusterParameters.setText("Kmeans parameters");
						createKmeansParameters(clusterParameters,dbc,wmodel);
						clusterParameters.setVisible(true);
					}
					
					if (clusteringStrategyStr.equals(ClusterersEnum.KMEANS_WITH_HCLUST.getFriendlyName())) {
						clusterParameters.setText("Kmeans using as centers the hclust media groups");
						createKmeansHClustParameters(clusterParameters,dbc,wmodel);
						clusterParameters.setVisible(true);
					}
					
					if (clusteringStrategyStr.equals(ClusterersEnum.PAM.getFriendlyName())) {
						clusterParameters.setText("PAM parameters");
						createPamParameters(clusterParameters,dbc,wmodel);
						clusterParameters.setVisible(true);
					}
					
					
					clusterParameters.layout(true);

				}

			});
			logger.trace("Clustering strategy created");
			
			clusteringStrategy.setSelection(defaultStrategySelection);
			
		
		}else{
			createManualSettings(clusterginGroup,wmodel,wp);
		}
			
		
	
		
		
		
	
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
					return ValidationStatus.error("'" + value + "'" + " is not a valid range or a valid number");
			}

		});
	}
	
	private UpdateValueStrategy notEmpty() {
		return new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value.toString().trim().isEmpty()) {
					return ValidationStatus.ok();
				} else
					return ValidationStatus.error("There's an empty field");
			}

		});
	}
	
	private UpdateValueStrategy uvsIsNumber(final String label) {
		return new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value.toString().trim().matches("\\d+")) {
					return ValidationStatus.ok();
				} else
					return ValidationStatus.error("Value in '" + label + "'" + " is not a valid number");
			}

		});
	}

	private void createPamParameters(final Group clusterParameters,DataBindingContext dbc, final WizardModel wmodel) {
		final Composite pamParameters = new Composite(clusterParameters, 0);
		pamParameters.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(2).create());
		pamParameters.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		
		Label labelMetric = new Label(pamParameters, SWT.NONE);
		labelMetric.setText("Metric:");
		List<String> names = new ArrayList<String>();
		names.add("euclidean"); names.add("manhattan"); 
		final ComboViewer metricStrategy = Utils.newComboViewerWithoutLabel(pamParameters, "Metric", names);
		dbc.bindValue(ViewersObservables.observeSingleSelection(metricStrategy), wmodel.valueHolder(PagesDescriptors.PAM_METRIC));
		metricStrategy.setSelection(new StructuredSelection(names.get(0)));
		
		Button btnStand = new Button(pamParameters, SWT.CHECK);
		btnStand.setText("Standardized");
		dbc.bindValue(SWTObservables.observeSelection(btnStand), wmodel.valueHolder(PagesDescriptors.PAM_STANDARDIZED));
		
		
				
	}
	
	private void createKmeansHClustParameters(final Group clusterParameters,DataBindingContext dbc, final WizardModel wmodel) {
		final Composite kmeansHClustParameters = new Composite(clusterParameters, SWT.NONE);
		kmeansHClustParameters.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(2).create());
		kmeansHClustParameters.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		Label labelHClustIter = new Label(kmeansHClustParameters, SWT.NONE);
		labelHClustIter.setText("Hclust Method:");
		// Note: ward is ward.D1 in R, ward2 is ward.D2
		List<String> names = new ArrayList<String>();
		names.add("ward"); names.add("ward.D2");names.add("single");names.add("complete");names.add("average");
		names.add("mcquitty");names.add("median") ;names.add("centroid");
		final ComboViewer metricStrategy = Utils.newComboViewerWithoutLabel(kmeansHClustParameters, "Metric", names);
		dbc.bindValue(ViewersObservables.observeSingleSelection(metricStrategy), wmodel.valueHolder(PagesDescriptors.KMEANSHCLUST_METRIC));
		metricStrategy.setSelection(new StructuredSelection(names.get(0)));
	}

	private void createKmeansParameters(final Group clusterParameters,DataBindingContext dbc, final WizardModel wmodel) {
		final Composite kmeansParameters = new Composite(clusterParameters, 0);
		kmeansParameters.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(2).create());
		kmeansParameters.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		
		Label labelIter = new Label(kmeansParameters, SWT.NONE);
		labelIter.setText("Iterations:");
		final Text textIter = new Text(kmeansParameters, SWT.BORDER);
		textIter.setTextLimit(5);
		dbc.bindValue(SWTObservables.observeText(textIter,SWT.Modify), wmodel.valueHolder(PagesDescriptors.KMEANS_ITER),uvsIsNumber("Iterations"),UpdateStrategies.nullStrategy());
		textIter.setText("1000 ");
		
		Label labelAlgo = new Label(kmeansParameters, SWT.NONE);
		labelAlgo.setText("Algorithm:");
		List<String> names = new ArrayList<String>();
		names.add("Hartigan-Wong"); names.add("Lloyd"); names.add("Forgy"); names.add("MacQueen");
		final ComboViewer algoStrategy = Utils.newComboViewerWithoutLabel(kmeansParameters, "Algorithm", names);
		dbc.bindValue(ViewersObservables.observeSingleSelection(algoStrategy), wmodel.valueHolder(PagesDescriptors.KMEANS_ALGORITHM));
		algoStrategy.setSelection(new StructuredSelection(names.get(0)));
	}

	private void createManualSettings(final Group clusterginGroup, final WizardModel wmodel,final WizardPage wp) {
		final Experiment exp = (Experiment) wmodel.value(PagesDescriptors.SELECTED);
		
		
		
		Button clusterButton = new Button(clusterginGroup,SWT.PUSH);
		clusterButton.setImage(PlatformUIUtils.findImage("clustering.png"));
		clusterButton.setToolTipText("Press button to configure cluster");
		clusterButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClusteringResult clusteringResult = null;
				try{
					clusteringResult = (ClusteringResult) wmodel.value(PagesDescriptors.MANUAL_CLUSTERING);
				}catch(Exception ex){
					logger.info("No cluster defined");
				}
				ConfigureClusterDialog ccd = new ConfigureClusterDialog(exp, clusteringResult);
				ClusteringResult result = ccd.getClusteringResult();
				wmodel.update(NUMBER_OF_CLUSTERS, result.getNumberOfClusters());
				
				if(Dialog.OK == ccd.open()){
					wmodel.set(PagesDescriptors.MANUAL_CLUSTERING, result);
					wmodel.update(PagesDescriptors.NUMBER_OF_CLUSTERS, result.getNumberOfClusters());
					if(!result.isAllSamplesAssignedToCluster()){
						putmessage(wp, "The experiment you have selected must have all the samples assigned to a cluster.");
					}					
					if(!result.hasGotEnoughClustersForValidation()){
						putmessage(wp, "The experiment you have selected must have at least two clusters. Your experiment " + result.getExperiment().getName() + " have just " + result.getNumberOfClusters() + ". Please  set at least 2 clusters manually.");
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
		
	
	/**
	 * pone un mensaje en la página del wizard y luego de un
	 * determinado tiempo lo borra
	 * 
	 * @param wp
	 * @param message
	 */
	private void putmessage(final WizardPage wp, String message) {
		MessageManager.INSTANCE.add(Message.warn(message));
		wp.setMessage(message, IMessageProvider.WARNING);
		new UIJob("") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				// limpio el mensaje
				wp.setMessage(null);
				return ValidationStatus.ok();
			}
		}.schedule(9000);
	}

	@Override
	public boolean isPageComplete(WizardModel wmodel) {
		boolean isComplete = false;
		if(this.forManualClustering){
			try{
				ClusteringResult cr = (ClusteringResult) wmodel.value(PagesDescriptors.MANUAL_CLUSTERING);
				isComplete = cr.hasGotEnoughClustersForValidation() && checkIfCombosAreSelected();
			}catch(Exception e){
				isComplete = false;
			}
		}else{
			isComplete = checkIfCombosAreSelected();
			
			//isComplete = super.isPageComplete(wmodel); 
		}
		return isComplete;
	}
	
	private boolean checkIfCombosAreSelected() {
		return wmodel.valueHolder(ATTRIBUTE_NAME_TO_VALIDATION) != null &&
				wmodel.valueHolder(SECOND_ATTRIBUTE_NAME_TO_VALIDATION) != null;
		
	}

	@Override
	public void doOnEnter() {
		List<Experiment> experiments = wmodel.value(PagesDescriptors.SELECTED);
		HashSet<String> survivalAttr = new HashSet<String>();
		survivalAttr.addAll(experiments.get(0).getClinicalAttributeNames());
		for(Experiment exp : experiments ){
			survivalAttr.retainAll(exp.getClinicalAttributeNames());
		}
		
		if(survivalGroup != null){
			survivalGroup.dispose();
			survivalGroup = null;
		}
		
		//Experiment exp = ((List<Experiment>) wmodel.value(PagesDescriptors.SELECTED)).get(0);
		survivalGroup = new Group(container, SWT.SHADOW_OUT);
		survivalGroup.setText("Survival attributes");
		survivalGroup.setFont(GUIUtils.getFontForGrouptTitle(container));
		survivalGroup.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 2).margins(10, 8).create());
		survivalGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		
		new Label(survivalGroup, SWT.NONE).setText("\n\"Time\" Attribute Name");
		Composite groupForValidationAtt1 = getGroupFoRClusteringAttribute(survivalGroup);
		//ComboViewer validationAttrName = Utils.newComboViewerWithoutLabel(groupForValidationAtt1, "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab. If it is not in the list, select 'other' for writing it in the text field.", Arrays.asList(OS_MONTHS, RFS_MONTHS, "recurrence", "timeUntilEventOccured", OTHER));
		validationAttrName = Utils.newComboViewerWithoutLabel(groupForValidationAtt1, "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab. If it is not in the list, select 'other' for writing it in the text field.", Arrays.asList(survivalAttr.toArray()));
		//ComboViewer validationAttrName = Utils.newComboViewerWithoutLabel(groupForValidationAtt1, "Attribute name over which the validation (hipotesis test) will be done. Pick up one appearing in the clinical tab.", exp.getClinicalAttributeNames());
		dbc.bindValue(ViewersObservables.observeSingleSelection(validationAttrName), wmodel.valueHolder(ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Attribute Name"), UpdateStrategies.nullStrategy());
		validationAttrName.getCombo().setLayoutData(gdf.create());
		//validationAttrName.setSelection(new StructuredSelection(OS_MONTHS));
		/*Text other = new Text(groupForValidationAtt1, SWT.BORDER);
		GridData gd = gdf.grab(true, false).create();
		other.setLayoutData(gd);
		dbc.bindValue(SWTObservables.observeText(other, SWT.Modify), wmodel.valueHolder(OTHER_ATTRIBUTE_NAME_TO_VALIDATION));*/
		logger.trace("Time attribute name created");

		new Label(survivalGroup, SWT.NONE).setText("\n\"Status\" Attribute Name");
		Composite groupForValidationAtt2 = getGroupFoRClusteringAttribute(survivalGroup);
		ComboViewer validationAttrName2 = Utils.newComboViewerWithoutLabel(groupForValidationAtt2, "Status attribute name (just to complete if the type of validation is for \"event occured after time\" attribute). Pick up one appearing in the clinical tab. If it is not in the list, select 'other' for writing it in the text field.", Arrays.asList(survivalAttr.toArray()));
		//ComboViewer validationAttrName2 = Utils.newComboViewerWithoutLabel(groupForValidationAtt2, "Status attribute name (just to complete if the type of validation is for \"event occured after time\" attribute). Pick up one appearing in the clinical tab.", exp.getClinicalAttributeNames());
		dbc.bindValue(ViewersObservables.observeSingleSelection(validationAttrName2), wmodel.valueHolder(SECOND_ATTRIBUTE_NAME_TO_VALIDATION), UpdateStrategies.nonNull("Second Attribute Name"), UpdateStrategies.nullStrategy());
		validationAttrName2.getCombo().setLayoutData(gdf.create());
		//validationAttrName2.setSelection(new StructuredSelection(OS_EVENT));
		/*Text other2 = new Text(groupForValidationAtt2, SWT.BORDER);
		GridData gd2 = gdf.grab(true, false).create();
		other2.setLayoutData(gd2);
		dbc.bindValue(SWTObservables.observeText(other2, SWT.Modify), wmodel.valueHolder(OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION));*/
		logger.trace("Status attribute name created");
		
		container.redraw();
		container.pack(false);
	}
	
}

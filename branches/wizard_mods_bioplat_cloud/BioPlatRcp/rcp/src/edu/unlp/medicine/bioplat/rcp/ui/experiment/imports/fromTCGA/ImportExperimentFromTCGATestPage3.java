package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;
import java.util.List;

import org.bioplat.r4j.R4JCore.values.R4JStringDataMatrix;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;

public class ImportExperimentFromTCGATestPage3 extends WizardPageDescriptor {

	private static final String ATTRIBUTES = "ATTRIBUTES";
	public static String STUDY = "STUDY";
	public static String PROFILE = "PROFILE";
	public static String CASENAME = "CASENAME";
	private ComboViewer comboCaseName;
	private ComboViewer comboGeneticProfile;
	private WizardModel model;
	private WizardPage wPage;
	private String selectedStudy;
	private boolean warningShown = true;
	MessageBox messageBox;
	
	
	public ImportExperimentFromTCGATestPage3(WizardModel wmodel) {
		super("Select Case subset and mRNA genetic profile (step 2 of 4)");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		wizardPage.setDescription("Select the case subset for the selected study, and the mRNA genetic profile");
		model=wmodel;
		wPage=wizardPage;
		
		Composite container = new Composite(parent, SWT.NONE);
		
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).spacing(5, 5).create());
//		container.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		generateComboBoxes(container,wmodel);
		createNoAttributeWarning();
		
		return container;
	}

	private GridData generateGridData(){
		GridData gridData = new GridData();
		gridData.horizontalAlignment=SWT.CENTER;
		gridData.grabExcessHorizontalSpace=true;
		return gridData;
	}
	
	private void generateComboBoxes(Composite container, WizardModel wmodel){
		Composite group1 = new Group(container, SWT.CENTER);
		group1.setLayoutData(generateGridData());
		group1.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).spacing(5, 5).create());
		
		Label labelCaseName = new Label(group1, SWT.NONE);
		comboCaseName = new ComboViewer(group1, SWT.DROP_DOWN);
		
		Composite group2 = new Group(container, SWT.CENTER);
		group2.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).spacing(5, 5).create());
		group2.setLayoutData(generateGridData()); 
		
		Label labelGeneticProfile = new Label(group2, SWT.NONE);
		comboGeneticProfile = new ComboViewer(group2, SWT.DROP_DOWN);
		
		labelCaseName.setText("Case list name:");
		labelGeneticProfile.setText("Genetic Profile:");
		
		comboCaseName.setContentProvider(ArrayContentProvider.getInstance());
		comboGeneticProfile.setContentProvider(ArrayContentProvider.getInstance());
		
		comboCaseName.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[1];
			}
		});
		
		comboGeneticProfile.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[1];
			}
		});
		
		comboCaseName.setInput(generateSelectLabel());
		comboGeneticProfile.setInput(generateSelectLabel());
		
	}

	
	/* (non-Javadoc)
	 * @see edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor#doOnEnter()
	 */
	@Override
	public void doOnEnter() {
		
		List<String[]> auxArray;
		R4JStringDataMatrix matrix;
		
		//This is a validation that checks if the main study was changed. It's made to prevent 
		//the loss of the selected case and genetic profile.
		//If the study was changed, it should reload the cases and profiles.
		if (selectedStudy != null && !selectedStudy.isEmpty() && selectedStudy.equalsIgnoreCase(((String[]) model.value(STUDY))[0]) ) {
			//If the study stored on the model is the same one stored on this page, then we shouldnt reload anything
			return;
		} else {
			//If it wasnt initialized, or it changed, we should set the new study
			selectedStudy = ((String[]) model.value(STUDY))[0];
		}
		
		
		matrix = TCGAApi.getInstance().get_subsets_for_study( ((String[]) model.value(STUDY))[0] );
		comboCaseName.setInput(matrixToArray(matrix));
		
		matrix = TCGAApi.getInstance().get_mrna_profiles( ((String[]) model.value(STUDY))[0] );
		comboGeneticProfile.setInput(matrixToArray(matrix));
		
		
		comboCaseName.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				model.set(CASENAME, (String[])((StructuredSelection) comboCaseName.getSelection()).getFirstElement() );
				warningShown = false;
				wPage.setPageComplete(isPageComplete(model));
			}
		});
		
		comboGeneticProfile.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				model.set(PROFILE, (String[])((StructuredSelection) comboGeneticProfile.getSelection()).getFirstElement() );
				wPage.setPageComplete(isPageComplete(model));
			}
		});
		
		if (comboCaseName.getCombo().getItemCount() > 0){
			comboCaseName.setSelection((StructuredSelection) new StructuredSelection(comboCaseName.getElementAt(0)) );
		}
		if (comboGeneticProfile.getCombo().getItemCount() > 0){
			comboGeneticProfile.setSelection((StructuredSelection) new StructuredSelection(comboGeneticProfile.getElementAt(0)) );
		} else {
			auxArray = new ArrayList<String[]>();
			auxArray.add(new String[] {"none","No genetic profile exists"});
			comboGeneticProfile.setInput(auxArray);
			comboGeneticProfile.setSelection((StructuredSelection) new StructuredSelection(comboGeneticProfile.getElementAt(0)),true );
		}
		
		comboCaseName.refresh();
		comboGeneticProfile.refresh();
		super.doOnEnter();
	}
	
	private ArrayList<String[]> matrixToArray(R4JStringDataMatrix matrix){
		ArrayList<String[]> arrayReturn = new ArrayList<String[]>();
		for (String[] row : matrix.getData()){
			arrayReturn.add(row);
		}
		return arrayReturn;
	}
	
	private ArrayList<String[]> generateSelectLabel() {
		ArrayList<String[]> arrayReturn = new ArrayList<String[]>();
		arrayReturn.add(new String[] {"", "Select one from the list...                      "});
		return arrayReturn;
	}
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		if (comboCaseName.getSelection().isEmpty()){
			return false;
		}
		
		if (comboGeneticProfile.getSelection().isEmpty()){
			return false;
		} else if(((String[])((StructuredSelection)comboGeneticProfile.getSelection()).getFirstElement())[0].equals("none")){
			return false;
		}
		
		if(model.value(ATTRIBUTES) == null){
			try{
				List<String> atributos = TCGAApi.getInstance().get_clinical_data_attribute_names( ((String[]) model.value(CASENAME))[0] );
				model.set(ATTRIBUTES, atributos.toArray(new String[0]));	
			}catch(ExperimentBuildingException e){
				createCDALError(((String[]) model.value(CASENAME))[0] );
				messageBox.open();
				return false;
			}
		}
		
		
		return true;
	}
	
	private void createNoAttributeWarning(){
		messageBox = new MessageBox(PlatformUIUtils.findShell(), SWT.ICON_WARNING | SWT.OK);
        messageBox.setText("Warning");
        messageBox.setMessage("Selected case list contains no Clinical Data. Only experiment values will be imported.");
	}
	
	
	/**
	 * 
	 */
	private void createCDALError(String casename){
		messageBox = new MessageBox(PlatformUIUtils.findShell(), SWT.ICON_ERROR | SWT.OK);
        messageBox.setText("Error");
        messageBox.setMessage("There are some problems with this dataset ("+ casename + ") in the TCGA database. Please, select another dataset.");
	}
	
	

	/* (non-Javadoc)
	 * @see edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor#doOnExit()
	 */
	@Override
	public void doOnExit() {
	try {			
		if (((String[]) model.value(ATTRIBUTES)).length == 0  && !warningShown) {
	        this.messageBox.open();
	        warningShown = true;
		}
	} catch (Exception e) {
	//do nothing
	}
		super.doOnExit();
	}
	
	
}


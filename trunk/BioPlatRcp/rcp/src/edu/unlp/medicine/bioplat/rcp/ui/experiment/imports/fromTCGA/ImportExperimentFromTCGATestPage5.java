package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;

public class ImportExperimentFromTCGATestPage5 extends WizardPageDescriptor {

	public ImportExperimentFromTCGATestPage5(WizardModel wizardModel) {
		super("Select Clinical data (step 4 of 4)");
	}

	public static String STUDY = "STUDY";
	public static String PROFILE = "PROFILE";
	public static String CASENAME = "CASENAME";
	public static String ATTRIBUTES = "ATTRIBUTES";
	private WizardModel model;
	private org.eclipse.swt.widgets.List availableAttributesList;
	private org.eclipse.swt.widgets.List selectedAttributesList;
	private GridData gd;
	
	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		wizardPage.setDescription("Select the clinical attributes you would like to import. Remember that for doing survival analysis you need at leaste the time and event attributes.");
		
		Composite container = new Composite(parent, SWT.NONE);
		
		gd = new GridData(GridData.FILL);
		gd.heightHint =250;
		gd.widthHint = 250;
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(30, 30).spacing(20, 20).create());
//		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		this.model = wmodel;
		generateAvailableAttributes(wmodel, container);
		generateActionButtons(wmodel, container);
		generateSelectedAttributes(wmodel, container);
		return container;
	}
	
	private void generateAvailableAttributes(WizardModel wmodel, Composite container){
		Group atributesGroup = new Group(container, SWT.FILL|SWT.CENTER);
		atributesGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).spacing(5,5).create());
		atributesGroup.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		atributesGroup.setText("Available clinical attributes");
		this.availableAttributesList = new org.eclipse.swt.widgets.List(atributesGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		this.availableAttributesList.setLayoutData(gd);
	}
	
	private void generateSelectedAttributes(WizardModel wmodel, Composite container){
		Group atributesGroup = new Group(container, SWT.FILL|SWT.CENTER);
		atributesGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).spacing(5,5).create());
		atributesGroup.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		atributesGroup.setText("Selected clinical attributes");
		this.selectedAttributesList = new org.eclipse.swt.widgets.List(atributesGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		this.selectedAttributesList.setLayoutData(gd);
	}

	private void generateActionButtons(WizardModel wmodel, Composite container){
		Composite actionButtons = new Group(container, SWT.NONE);
		actionButtons.setLayout(GridLayoutFactory.fillDefaults().margins(5, 5).create());
		actionButtons.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		
		Button addSelectedAttributes = new Button(actionButtons, SWT.PUSH);
		addSelectedAttributes.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		addSelectedAttributes.setText("Select >");
		addSelectedAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(selectedAttributesList, availableAttributesList.getSelection());
				removeAttributesToList(availableAttributesList, availableAttributesList.getSelection());
				model.set(ATTRIBUTES, selectedAttributesList.getItems());
				super.widgetSelected(e);
//				changeSizeNOW();
			}
			
		});
		
		Button addAllAttributes = new Button(actionButtons, SWT.PUSH);
		addAllAttributes.setText("Select All >>");
		addAllAttributes.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		addAllAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(selectedAttributesList, availableAttributesList.getItems());
				availableAttributesList.removeAll();
				model.set(ATTRIBUTES, selectedAttributesList.getItems());
				super.widgetSelected(e);
//				changeSizeNOW();
			}
			
		});
		
		Button removeSelectedAttributes = new Button(actionButtons, SWT.PUSH);
		removeSelectedAttributes.setText("< Remove");
		removeSelectedAttributes.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		removeSelectedAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(availableAttributesList, selectedAttributesList.getSelection());
				removeAttributesToList(selectedAttributesList, selectedAttributesList.getSelection());
				model.set(ATTRIBUTES, selectedAttributesList.getItems());
				super.widgetSelected(e);
//				changeSizeNOW();
			}
			
		});
		
		Button removeAllAttributes = new Button(actionButtons, SWT.PUSH);
		removeAllAttributes.setText("<< Remove All");
		removeAllAttributes.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		removeAllAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(availableAttributesList, selectedAttributesList.getItems());
				selectedAttributesList.removeAll();
				model.set(ATTRIBUTES, selectedAttributesList.getItems());
				super.widgetSelected(e);
//				changeSizeNOW();
			}
		});
		
		
	}
	
	@Override
	public void doOnEnter() {
		List<String> atributos = null;
		try{
			atributos = TCGAApi.getInstance().get_clinical_data_attribute_names( ((String[]) model.value(CASENAME))[0] );
		}catch(ExperimentBuildingException e){
			createNoAttributeWarning().open();
		}
		
		setAttributesToList(selectedAttributesList, atributos);
		setAttributesToList(availableAttributesList, new ArrayList<String>());
		model.set(ATTRIBUTES, selectedAttributesList.getItems());
		availableAttributesList.setSize(selectedAttributesList.getSize());
		changeSizeNOW();
		super.doOnEnter();
	}

	
	private MessageBox createNoAttributeWarning(){
		MessageBox messageBox = new MessageBox(PlatformUIUtils.findShell(), SWT.ICON_WARNING | SWT.OK);
        messageBox.setText("Warning");
        messageBox.setMessage("Selected case list contains no Clinical Data. Only experiment values will be imported.");
        return messageBox;
        
	}
	
	
	
	private void addAttributesToList(org.eclipse.swt.widgets.List list, String[] attributes){
		for (int i = 0; i < attributes.length; i++) {
			list.add(attributes[i]);
		}
	}

	private void removeAttributesToList(org.eclipse.swt.widgets.List list, String[] attributes){
		for (int i = 0; i < attributes.length; i++) {
			list.remove(attributes[i]);
		}
	}
	
	private void setAttributesToList(org.eclipse.swt.widgets.List list, List<String> attributes) {
		list.setItems((String[]) attributes.toArray(new String[0]) );
	}
	

	private void changeSizeNOW() {
		this.selectedAttributesList.getParent().getParent().layout(true, true);
		this.selectedAttributesList.getParent().layout(true, true);
		this.selectedAttributesList.getParent().redraw();
		this.selectedAttributesList.redraw();
		this.selectedAttributesList.update();
		this.availableAttributesList.getParent().layout(true,true);
		this.availableAttributesList.getParent().redraw();
		this.availableAttributesList.redraw();
		this.availableAttributesList.update();
	}

}

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;

public class ImportExperimentFromTCGATestPage5 extends WizardPageDescriptor {

	public ImportExperimentFromTCGATestPage5(WizardModel wizardModel) {
		super("Get Studios");
	}

	public static String STUDY = "STUDY";
	public static String PROFILE = "PROFILE";
	public static String CASENAME = "CASENAME";
	private WizardModel model;
	private org.eclipse.swt.widgets.List availableAttributesList;
	private org.eclipse.swt.widgets.List selectedAttributesList;
	
	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(30, 30).spacing(20, 20).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		this.model = wmodel;
		generateAvailableAttributes(wmodel, container);
		generateActionButtons(wmodel, container);
		generateSelectedAttributes(wmodel, container);
		return container;
	}
	
	private void generateAvailableAttributes(WizardModel wmodel, Composite container){
		Group atributesGroup = new Group(container, SWT.FILL);
		atributesGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		atributesGroup.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).minSize(200, 100).create());
		atributesGroup.setText("Available clinical attributes");
		this.availableAttributesList = new org.eclipse.swt.widgets.List(atributesGroup, SWT.MULTI);
		this.availableAttributesList.setSize(200,500);
//		this.availableAttributesList.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
	}
	
	private void generateSelectedAttributes(WizardModel wmodel, Composite container){
		Group atributesGroup = new Group(container, SWT.FILL);
		atributesGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		atributesGroup.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).minSize(200, 100).create());
		atributesGroup.setText("Selected clinical attributes");
		this.selectedAttributesList = new org.eclipse.swt.widgets.List(atributesGroup, SWT.MULTI);
		this.selectedAttributesList.setSize(200,500);
//		this.selectedAttributesList.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
	}

	private void generateActionButtons(WizardModel wmodel, Composite container){
		Composite actionButtons = new Composite(container, SWT.CENTER);
		actionButtons.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		actionButtons.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		
		Button addSelectedAttributes = new Button(actionButtons, SWT.PUSH);
		addSelectedAttributes.setText("Select >");
		addSelectedAttributes.setSize(100, 20);
		addSelectedAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(selectedAttributesList, availableAttributesList.getSelection());
				removeAttributesToList(availableAttributesList, availableAttributesList.getSelection());
				selectedAttributesList.redraw();
				availableAttributesList.redraw();
				super.widgetSelected(e);
			}
			
		});
		
		Button addAllAttributes = new Button(actionButtons, SWT.PUSH);
		addAllAttributes.setText("Select All >>");
		addAllAttributes.setSize(100, 20);
		addAllAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(selectedAttributesList, availableAttributesList.getItems());
				availableAttributesList.removeAll();
				selectedAttributesList.redraw();
				availableAttributesList.redraw();
				super.widgetSelected(e);
			}
			
		});
		
		Button removeSelectedAttributes = new Button(actionButtons, SWT.PUSH);
		removeSelectedAttributes.setText("< Remove");
		removeSelectedAttributes.setSize(100, 20);
		removeSelectedAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(availableAttributesList, selectedAttributesList.getSelection());
				removeAttributesToList(selectedAttributesList, selectedAttributesList.getSelection());
				selectedAttributesList.redraw();
				availableAttributesList.redraw();
				super.widgetSelected(e);
			}
			
		});
		
		Button removeAllAttributes = new Button(actionButtons, SWT.PUSH);
		removeAllAttributes.setText("<< Remove All");
		removeAllAttributes.setSize(100, 20);
		removeAllAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAttributesToList(availableAttributesList, selectedAttributesList.getItems());
				selectedAttributesList.removeAll();
				selectedAttributesList.redraw();
				availableAttributesList.redraw();
				super.widgetSelected(e);
			}
			
		});
		
		
	}
	
	@Override
	public void doOnEnter() {
		List<String> atributos = TCGAApi.getInstance().get_clinical_data_attribute_names( ((String[]) model.value(CASENAME))[0] );
		setAttributesToList(availableAttributesList, atributos);
		setAttributesToList(selectedAttributesList, new ArrayList<String>());
		super.doOnEnter();
	}

//	private void addAttributesToList(org.eclipse.swt.widgets.List list, List<String> attributes){
//		for (String attribute : attributes) {
//			list.add(attribute);
//		}
//	}
	
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
	
//	private void setAttributesToList(org.eclipse.swt.widgets.List list, String[] attributes) {
//		list.setItems(attributes);
//	}
	

}

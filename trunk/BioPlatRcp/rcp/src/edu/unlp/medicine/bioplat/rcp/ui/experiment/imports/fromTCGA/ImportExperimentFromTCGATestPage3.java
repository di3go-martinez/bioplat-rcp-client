package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;
import edu.unlp.medicine.r4j.values.R4JStringDataMatrix;

public class ImportExperimentFromTCGATestPage3 extends WizardPageDescriptor {

	public static String STUDY = "STUDY";
	public static String PROFILE = "PROFILE";
	public static String CASENAME = "CASENAME";
	private ComboViewer comboCaseName;
	private ComboViewer comboGeneticProfile;
	private WizardModel model;
	private WizardPage wPage;
	private String selectedStudy;
	
	
	public ImportExperimentFromTCGATestPage3(WizardModel wmodel) {
		super("Get Studios");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		model=wmodel;
		wPage=wizardPage;
//		Composite container = Widgets.createDefaultContainer(parent, 2);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(5, 5).create());
		generateComboBoxes(container,wmodel);
		
		return container;
	}

	private void generateComboBoxes(Composite container, WizardModel wmodel){
		
		Label labelCaseName = new Label(container, SWT.NONE);
		comboCaseName = new ComboViewer(container, SWT.DROP_DOWN);
		Label labelGeneticProfile = new Label(container, SWT.NONE);
		comboGeneticProfile = new ComboViewer(container, SWT.DROP_DOWN);
		
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
		
		comboCaseName.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				model.set(PROFILE, (String[])((StructuredSelection) comboCaseName.getSelection()).getFirstElement() );
				wPage.setPageComplete(isPageComplete(model));
			}
		});
		
		comboGeneticProfile.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				model.set(CASENAME, (String[])((StructuredSelection) comboCaseName.getSelection()).getFirstElement() );
				wPage.setPageComplete(isPageComplete(model));
			}
		});
		
		matrix = TCGAApi.getInstance().get_mrna_profiles( ((String[]) model.value(STUDY))[0] );
		comboGeneticProfile.setInput(matrixToArray(matrix));
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
		if (comboCaseName.getCombo().getItemCount() > 0) {
			if (comboCaseName.getSelection().isEmpty()){
				return false;
			}
		}
		if (comboGeneticProfile.getCombo().getItemCount() > 0){
			if (comboGeneticProfile.getSelection().isEmpty()){
				return false;
			}
		}
		return true;
	}
}


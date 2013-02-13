package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

/**
 * 
 * Descriptor de página de wizard para la selección de algoritmos a aplicar.
 * 
 * @author diego martínez
 * 
 */
public class GMSPage4Algorithm extends WizardPageDescriptor {

	static final String BASOS = "BASOS (Based on significance value)";
	static final String CONT = "CONT (Considering Ontrology)";
	static final String SMS = "SMS (Smart Selector)";
	static final String IES = "IES (In every Signature)";
	static final String UNION = "UNION";
	static final String ALGORITHM = "ALGORITHM";
	static final String SEPARATOR = "#";
	static final String ALIX_X_PARAMETER = "Please specify the value for X: ";
	static final String  ALIX_DESCRIPTION = "The algorithm selects all the genes which were proposed by, at least, X of the selected Gene Signatures.";
	static final String  UNION_DESCRIPTION = "The algorithm does a union of all the gene sets proposed by the selected Gene Signatures.";
	static final String  IES_DESCRIPTION = "The algorithm selects the genes which are proposed by all the Gene Signatures you have selected.";

	
	// Algorithm names
	static final String ALIX = "ALIX (At least in X signatures)";

	private Composite alixComposite;
	Label descriptionLabel;
	ComboViewer cv; 

	public GMSPage4Algorithm() {
		super("Select the algorithm");
	}

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 50).spacing(0,30).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		//wp.setDescription("Select the algorithm for processing the input Gene Signatures you have selected (" + calculateNumberOfSelectedGS(wmodel) + "). It will generate the metasignature."); 
		wp.setDescription("Select the algorithm for processing the input Gene Signatures you have selected. It will generate the Metasignature.");

		Composite algorithmPanel = createALgorithmPanel(container, dbc, wmodel);
		
		createLabelOfWorkingInAlgorithms(container);
		

		return container;
	}

	private Composite createALgorithmPanel(Composite container, DataBindingContext dbc, WizardModel wmodel) {
		Composite algorithmPanel = new Group(container, SWT.NONE);
		algorithmPanel.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(30, 30).spacing(5, 20).create());
		
		cv = Utils.newComboViewer(algorithmPanel, "Algorithm", Arrays.asList(ALIX, UNION, IES));//, SMS, CONT, BASOS));
		
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(GMSPage4Algorithm.ALGORITHM));
		
		cv.addSelectionChangedListener(new ISelectionChangedListener() {
		
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object s = ((StructuredSelection) event.getSelection()).getFirstElement();
				alixComposite.setVisible(ALIX.equals(s));
				changeAlgorithmDescription(s);
			}

		});
		
		createAlixComposite(algorithmPanel, dbc, wmodel);
		createLabelForALgorithmDescription(algorithmPanel, dbc, wmodel);
		setDefaultElement();
		
		
		return algorithmPanel;
	}

	private void setDefaultElement() {
		cv.setSelection(new StructuredSelection(ALIX));
		//descriptionLabel.setText(ALIX_DESCRIPTION);
	}

	protected void changeAlgorithmDescription(Object selection) {
			if (ALIX.equals(selection)) descriptionLabel.setText(ALIX_DESCRIPTION);
			else if (UNION.equals(selection)) descriptionLabel.setText(UNION_DESCRIPTION);
			else if (IES.equals(selection)) descriptionLabel.setText(IES_DESCRIPTION);
	}

	private void createLabelForALgorithmDescription(Composite algorithmPanel,
			DataBindingContext dbc, WizardModel wmodel) {
		
		descriptionLabel = new Label(algorithmPanel, SWT.WRAP);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		descriptionLabel.setLayoutData(gridData);
		
		FontData[] fD = descriptionLabel.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.ITALIC);
		descriptionLabel.setFont( new Font(algorithmPanel.getDisplay(),fD[0]));
		
	}

	private void createLabelOfWorkingInAlgorithms(Composite container) {
		Label introdudctionLabel = new Label(container, SWT.WRAP);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL); 
		introdudctionLabel.setLayoutData(gridData);
		
		FontData[] fD = introdudctionLabel.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.ITALIC);
		introdudctionLabel.setFont( new Font(container.getDisplay(),fD[0]));
		introdudctionLabel.setText("Bioplat was designed to easily incorporate new algorithms for datamining Gene Signatures. We are working on new algorithms. Any idea? Tell us");
		
		
	}

	private int calculateNumberOfSelectedGS(WizardModel wmodel) {
		
		List<GeneSignature> externalSelectedGS =  wmodel.value(GMSPage2FIlterExternalDBs.SELECTED_EXTERNAL_SIGNATURES);
		int numberOfExternalSelected = 0;
		if (externalSelectedGS!=null) numberOfExternalSelected = externalSelectedGS.size(); 
		
		List<Biomarker> openedBiomarkers =  wmodel.value(GMSPage3LocalGSs.OPENED_SELECTED_BIOMARKERS);
		int numberOfopenedBiomarkersSelected = 0;
		if (openedBiomarkers!=null) numberOfopenedBiomarkersSelected = externalSelectedGS.size();
		
		return numberOfExternalSelected + numberOfopenedBiomarkersSelected;
	}

	private void createAlixComposite(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		
		
		alixComposite = new Composite(parent, SWT.NONE);
		alixComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(5,0).spacing(3, 0).create());
		alixComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		alixComposite.setVisible(false); // TODO revisar
		
		new Label(alixComposite, SWT.NONE).setText(ALIX_X_PARAMETER);
		Text t = new Text(alixComposite, SWT.BORDER);
		t.setLayoutData(GridDataFactory.fillDefaults().minSize(50, SWT.DEFAULT).grab(true, false).create());
		// TODO definir mejor el id de la key a poner en el wizard model
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(ALIX + SEPARATOR + ALIX_X_PARAMETER));
		
	}

	public WizardPageDescriptor addParameters(WizardModel wizardModel) {
		wizardModel.add(ALGORITHM);
		wizardModel.add(ALIX + SEPARATOR + ALIX_X_PARAMETER, new WritableValue(5, int.class));
		return this;
	}
	
	

	
		
	
}

package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

/**
 * 
 * Descritor de página de wizard para los proveedores de genes signatures
 * 
 * @author diego martínez
 * 
 */
public class GMSPage1Introduction extends WizardPageDescriptor {

	private static Logger logger = LoggerFactory.getLogger(GMSPage2FIlterExternalDBs.class);
	
	public GMSPage1Introduction() {
		super("What is a Metasignature? ");
	}

	
	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		wp.setDescription("What is a metasignature? How does it work?                                                                                                                                                                ");
		
		Composite container = Widgets.createDefaultContainer(parent, 1);

		createFiltersGroup(container, wp, parent, dbc, wmodel);
		
		return container;
	}

	private void createFiltersGroup(Composite container, final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 50).create());
		
		Group filterGroup = new Group(container, SWT.NONE);
		//filterGroup.setText("Description");
		
		//new Image(filterGroup.getDisplay(),"resources/icons/gene.gif");
		
		filterGroup.setLayout(GridLayoutFactory.fillDefaults().margins(50, 50).create());
		filterGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		//GridData layoutdata = new GridData(1000, SWT.DEFAULT);
		
		
		Label introdudctionLabel = new Label(filterGroup, SWT.WRAP);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL); 
		introdudctionLabel.setLayoutData(gridData);
		
		FontData[] fD = introdudctionLabel.getFont().getFontData();
		fD[0].setHeight(10);
		fD[0].setStyle(SWT.ITALIC);
		introdudctionLabel.setFont( new Font(filterGroup.getDisplay(),fD[0]));
		
		
		introdudctionLabel.setText("A Bioplat metasignature is the result of datamining many input Gene Signatures you selected.\n\n You can select the input Gene Signatures from external databases buy you can also use the GeneSignatures you have previously open in Bioplat. The algorithm will take as input all the GeneSignatures you provided it and will suggest a list of genes which conforms the MEtaSIgnature");
		

	}


	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
		
	}

	
	
}

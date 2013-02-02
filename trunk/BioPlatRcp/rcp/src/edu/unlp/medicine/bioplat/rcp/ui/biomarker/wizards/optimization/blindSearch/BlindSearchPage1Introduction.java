package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch;

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

import edu.unlp.medicine.bioplat.core.Activator;
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
public class BlindSearchPage1Introduction extends WizardPageDescriptor {

	private static Logger logger = LoggerFactory.getLogger(BlindSearchPage1Introduction.class);
	
	public BlindSearchPage1Introduction() {
		super("Look the best Gene Siganture using Blind Search");
	}

	
	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		wp.setDescription("Do you have a Gene Signature but you arent sure each gene provides information for prognostic? Here you are.");
		wp.setImageDescriptor(Activator.imageDescriptorFromPlugin("blindSearch-page.png"));
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
		fD[0].setHeight(9);
		fD[0].setStyle(SWT.ITALIC);
		introdudctionLabel.setFont( new Font(filterGroup.getDisplay(),fD[0]));
		
		
		introdudctionLabel.setText("Many times you have a Gene Signature in hands, but you are not sure that every gene is useful for progonostic/prediction. This algorithm will look in the whole space solution for a given Gene Signature, the element (Gene Signature) which maximizes the value for measuring prognostic/predictive value. Each element of this solution space is a different Gene Signature. \n\nWhich Gene Signatures are part of this solution space for a given GeneSignature GS1? Any Gene Signature as a result of removing a gene subset of GS1. For example if you have the GeneSignature with genes AURKA, BCL2, BRG3 the solution space will have 6 different elements (Gene Signature): \n\tGene signature containing AURKA, BCL2 \n\tGene signature containing AURKA BRG3 \n\tGene signature containing AURKA BRG3 \n\tGene signature containing AURKA alone \n\tGene signature containing BRG3 alone \n\tGene signature containing BCL2 alone");
		

	}


	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
		
	}

	
	
}

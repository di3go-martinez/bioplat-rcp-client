package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

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
public class PSOPage1Introduction extends WizardPageDescriptor {

	private static Logger logger = LoggerFactory.getLogger(PSOPage1Introduction.class);
	
	public PSOPage1Introduction() {
		super("Look the best Gene Siganture using PSO");
	}

	
	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		wp.setDescription("Do you have a Gene Signature but you are not sure whether each gene provides information for prognostic? Here you are an optimizer.");
		wp.setImageDescriptor(Activator.imageDescriptorFromPlugin("pso.png"));
		Composite container = Widgets.createDefaultContainer(parent, 1);

		createFiltersGroup(container, wp, parent, dbc, wmodel);
		
		return container;
	}

	private void createFiltersGroup(Composite container, final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 10).create());
		
		Group filterGroup = new Group(container, SWT.NONE);
		//filterGroup.setText("Description");
		
		//new Image(filterGroup.getDisplay(),"resources/icons/gene.gif");
		
		filterGroup.setLayout(GridLayoutFactory.fillDefaults().margins(50, 10).create());
		filterGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		//GridData layoutdata = new GridData(1000, SWT.DEFAULT);
		
		
		Label introdudctionLabel = new Label(filterGroup, SWT.WRAP);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL); 
		introdudctionLabel.setLayoutData(gridData);
		
		FontData[] fD = introdudctionLabel.getFont().getFontData();
		fD[0].setHeight(9);
		fD[0].setStyle(SWT.ITALIC);
		introdudctionLabel.setFont( new Font(filterGroup.getDisplay(),fD[0]));
		
		
		introdudctionLabel.setText("Many times you have a Gene Signature in hands, but you are not sure whether every proposed gene is useful for progonostic/prediction. This metaheuristic will look for the Gene Signature which maximizes the value for measuring prognostic/predictive value for a given Gene Signature in the whole space solution. Each element of this solution space is a different Gene Signature. The difference with blind search is that this metaheuristic reduces the space solution \n\nWhich Gene Signatures are part of this solution space for a given GeneSignature GS1? Any Gene Signature as a result of removing a gene subset of GS1. For example, if you have the GeneSignature with genes AURKA, BCL2, BRG3 the solution space will have 6 different elements (Gene Signature): \n\tGene signature containing AURKA, BCL2 \n\tGene signature containing AURKA BRG3 \n\tGene signature containing AURKA BRG3 \n\tGene signature containing only AURKA \n\tGene signature containing only BRG3 \n\tGene signature containing only BCL2 \n\nThe metric for comparing the prognostic/prediction value of two gene signatures is concordance.index that is the probability that, for a pair of randomly chosen comparable samples, the sample with the higher risk prediction will experience an event before the other sample or belongs to a higher binary class. \nConcordance.index references: Harrel Jr, F. E. and Lee, K. L. and Mark, D. B. (1996) \"Tutorial in biostatistics: multivariable prognostic models: issues in developing models, evaluating assumptions and adequacy, and measuring and reducing error\", Statistics in Medicine, 15, pages 361–387. Pencina, M. J. and D’Agostino, R. B. (2004) \"Overall C as a measure of discrimination in survival analysis: model specific population value and confidence interval estimation\", Statistics in Medicine, 23, pages 2109–2123, 2004 ");
		

	}


	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
		
	}

	
	
}

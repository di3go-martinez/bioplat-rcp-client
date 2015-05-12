package edu.unlp.medicine.bioplat.rcp.ui.utils.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

/**
 * 
 * Página genérica para hacer una introducción de la operación. Resuelve el problema del maximizado automático cuando se accede por diálogo de Eclipse.
 * 
 * @author Matias Butti
 * 
 */
public class GenericPage1ForIntroduction extends WizardPageDescriptor {

	private static Logger logger = LoggerFactory.getLogger(GenericPage1ForIntroduction.class);
	
	
	String inBlankSegmentBigTitle, inBlankSegmentSmallTitle, introductionText;
	

	
	public GenericPage1ForIntroduction(String inBlankSegmentBigTitle, String inBlankSegmentSmallTitle, String introductionText) {
		super(inBlankSegmentBigTitle);
		this.inBlankSegmentBigTitle = inBlankSegmentBigTitle;
		this.inBlankSegmentSmallTitle = inBlankSegmentSmallTitle;
		this.introductionText = introductionText;
	}


	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		wp.setDescription(inBlankSegmentSmallTitle);
		
		Composite container = Widgets.createDefaultContainer(parent, 1);

		addIntroductionText(container, wp, parent, dbc, wmodel);
		
		return container;
	}

	
	private void addIntroductionText(Composite container, final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 100).create());
		
		Group filterGroup = new Group(container, SWT.NONE);
		filterGroup.setLayout(GridLayoutFactory.fillDefaults().margins(70, 50).create());
		filterGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		
		GUIUtils.addWrappedText(filterGroup, introductionText, 10, true);
		
		
		

	}

	
		
	
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
		
	}

	
	
}

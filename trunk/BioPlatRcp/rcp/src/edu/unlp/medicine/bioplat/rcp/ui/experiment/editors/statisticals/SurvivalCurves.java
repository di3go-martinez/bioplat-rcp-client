package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import java.io.ByteArrayInputStream;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

/**
 * 
 * @author Diego Martínez
 * 
 */
class SurvivalCurves extends CompositeGenerator {
	
	private final Validation experiment;

	SurvivalCurves(FormToolkit toolkit, Validation experiment) {
		super(toolkit);
		this.experiment = experiment;
	}

	@Override
	protected void fill(Composite container) throws Exception {
		final Composite cmain = toolkit().createComposite(container, SWT.FLAT);
		cmain.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		final Composite c = toolkit().createComposite(cmain, SWT.FLAT);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		showScriptResult(c, "Survival Curve Differences Test", String.valueOf(experiment.getValidationResult().getSurvdiff_chisq())); 
		showScriptResult(c, "Survival Curve Differences P-value", String.valueOf(experiment.getValidationResult().getSurvdiff_pvalue())); 
		final Composite cimage = toolkit().createComposite(cmain, SWT.FLAT);
		try {
			cimage.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
			final byte[] stream = this.experiment.getValidationResult().getKaplan_meier_image(); 
			
			final Image image2 = new Image(PlatformUIUtils.findDisplay(), new ByteArrayInputStream(stream));
			final Button imageButton = new Button(cimage, SWT.FLAT);
			imageButton.setImage(image2);
			imageButton.setSize(image2.getImageData().width, image2.getImageData().width);

			
			c.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					image2.dispose();
				}
			});
		} catch (Exception e) {
			MessageManager.INSTANCE.add(Message.error("Error trying to get the survival curves", e));
		}
	}	
	
	private void showScriptResult(Composite container, String label, String result) {
		int style = SWT.READ_ONLY;
		try {
			
			toolkit().createLabel(container, label);
			toolkit().createText(container, result, style)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			
		} catch (Exception e) {
			result = "Error evaluating the script '" + label + "'...";
			toolkit().createLabel(container, result, style).setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		} 
	}
	
}

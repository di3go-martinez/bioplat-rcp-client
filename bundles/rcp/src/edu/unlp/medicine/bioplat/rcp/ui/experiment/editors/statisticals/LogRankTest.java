package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

public class LogRankTest extends CompositeGenerator {
	
	private final Validation experiment;

	LogRankTest(FormToolkit toolkit, Validation experiment) {
		super(toolkit);
		this.experiment = experiment;
	}

	@Override
	public void fill(Composite container) throws Exception {
		final Composite cmain = toolkit().createComposite(container, SWT.FLAT);
		cmain.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		final Composite c = toolkit().createComposite(cmain, SWT.FLAT);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		showScriptResult(c, "Log Rank Test P-Value", String.valueOf(experiment.getValidationResult().getSurvdiff_pvalue())); //getClusteringResult().getScriptForLogRankTestChiSquaredPValue());  // DCAMBIAR!
		showScriptResult(c, "Log Rank Test Chi-Square", String.valueOf(experiment.getValidationResult().getSurvdiff_chisq())); //getClusteringResult().getScriptForLogRankTestChiSqured());
		/*final Composite cimage = toolkit().createComposite(cmain, SWT.FLAT);
		try {
			cimage.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
			final byte[] stream = this.experiment.getValidationResult().getRoc_image(); //getSurvivalCurve().getScript();
			
			File imageFile = ConvertByteImageUtils.toImage(stream, stream.hashCode() + ".jpg");
			imageFile.deleteOnExit();

			final Image image2 = new Image(PlatformUIUtils.findDisplay(), new FileInputStream(imageFile));
			final Button imageButton = new Button(cimage, SWT.FLAT);
			imageButton.setImage(image2);
			imageButton.setSize(image2.getImageData().width, image2.getImageData().width);

			createCopyTextButton(cimage, "ee");

			c.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					image2.dispose();
				}
			});
		} catch (Exception e) {
			MessageManager.INSTANCE.add(Message.error("Error trying to get the survival curves", e));
		}*/
		
		
	}

	private void showScriptResult(Composite container, String label, String result) {
		int style = SWT.READ_ONLY;
		try {
			/*REXP r = RRunnerFromBioplatUsingR4J.getInstance().getConnection().eval(script).getNativeValue();
			String result = r.asString();*/

			toolkit().createLabel(container, label);
			toolkit().createText(container, result, style)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			/*createCopyTextButton(container, result)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());*/
		} catch (Exception e) {
			result = "Error evaluating the script '" + label + "'...";
			toolkit().createLabel(container, result, style).setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		} 
	}
}

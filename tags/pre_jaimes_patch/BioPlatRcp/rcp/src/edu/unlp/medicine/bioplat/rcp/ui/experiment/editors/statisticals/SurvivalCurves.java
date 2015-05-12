package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.rosuda.REngine.REXP;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.ConvertByteImageUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.statistics.rIntegration.jri.RRunnerFromBioplatUsingR4J;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.r4j.exceptions.R4JScriptExecutionException;
import edu.unlp.medicine.r4jServer.BioplatR4JServer;

/**
 * 
 * @author Diego Martínez
 * 
 */
class SurvivalCurves extends CompositeGenerator {
	private final ExperimentAppliedToAMetasignature experiment;

	SurvivalCurves(FormToolkit toolkit, ExperimentAppliedToAMetasignature experiment) {
		super(toolkit);
		this.experiment = experiment;
	}

	@Override
	protected void fill(Composite c) throws Exception {
		try {
			c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
			final String script = experiment.getScriptForSurvivalCurves();

			File imageFile = resolveImage(script);
			imageFile.deleteOnExit();

			final Image image2 = new Image(PlatformUIUtils.findDisplay(), new FileInputStream(imageFile));
			final Button imageButton = new Button(c, SWT.FLAT);
			imageButton.setImage(image2);
			imageButton.setSize(image2.getImageData().width, image2.getImageData().width);

			createCopyTextButton(c, script);

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

	private File resolveImage(String script) throws IOException {
		
		try {
			
			RRunnerFromBioplatUsingR4J.getInstance().getConnection().voidEval(linesBeforePlotLine(script));
			byte[] image = RRunnerFromBioplatUsingR4J.getInstance().getConnection().executePlotAndGetImage(plotLine(script));
			//RRunnerFromBioplatUsingR4J.getInstance().getConnection().voidEval(linesAfterPlotLine(script));
			
			File imageFile = null;
			if (image != null) {
				imageFile = ConvertByteImageUtils.toImage(image, script.hashCode() + ".jpg");
			}
			// TODO retornar una imágen de "no disponible" en vez de null
			return imageFile;

		} catch (R4JScriptExecutionException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	private String plotLine(String script) {
		
		return "plot" + script.split("plot")[1].split("\n")[0];
	}

	private String linesBeforePlotLine(String script) {
		
		return script.split("plot")[0];
		
	}
	
	
	
	

	

}

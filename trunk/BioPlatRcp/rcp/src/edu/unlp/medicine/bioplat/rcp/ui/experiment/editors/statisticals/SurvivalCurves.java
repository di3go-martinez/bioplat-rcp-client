package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import edu.unlp.medicine.bioplat.rcp.utils.ConvertByteImageUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.statistics.rIntegration.jri.RRunnerUsingJRI;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.r4j.exceptions.R4JConnectionException;

public class SurvivalCurves extends CompositeGenerator {
	private final ExperimentAppliedToAMetasignature experiment;

	SurvivalCurves(FormToolkit toolkit, ExperimentAppliedToAMetasignature experiment) {
		super(toolkit);
		this.experiment = experiment;
	}

	@Override
	protected void fill(Composite c) throws Exception {

		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		final String script = experiment.getScriptForSurvivalCurves();

		File imageFile = resolveImage(script);
		imageFile.deleteOnExit();

		final Image image2 = new Image(PlatformUIUtils.findDisplay(), new FileInputStream(imageFile));
		final Button imageButton = new Button(c, SWT.FLAT);
		imageButton.setImage(image2);

		final Clipboard cb = new Clipboard(PlatformUIUtils.findDisplay());

		Button copy = new Button(c, SWT.FLAT);
		copy.setText("Copy Script");
		copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String textData = script;
				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { textData }, new Transfer[] { textTransfer });
			}
		});

		c.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				image2.dispose();
			}
		});
	}

	private File resolveImage(String script) throws R4JConnectionException, IOException {
		// final R4JSession r4jSession = new R4JSession("ExperimentFactory");
		// r4jSession.open();
		// byte[] image = r4jSession.plot(script);
		// r4jSession.close();
		byte[] image = RRunnerUsingJRI.getInstance().plotSurvivalCurve(script);
		File imageFile = null;
		if (image != null) {
			imageFile = ConvertByteImageUtils.toImage(image, script.hashCode() + ".jpg");
		}
		return imageFile;
	}

}

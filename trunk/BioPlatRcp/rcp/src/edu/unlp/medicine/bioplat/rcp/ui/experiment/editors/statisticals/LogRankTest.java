package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.rosuda.REngine.REXP;

import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.domainLogic.framework.statistics.rIntegration.jri.RRunnerFromBioplatUsingR4J;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class LogRankTest extends CompositeGenerator {
	
	private final Validation experiment;

	LogRankTest(FormToolkit toolkit, Validation experiment) {
		super(toolkit);
		this.experiment = experiment;
	}

	@Override
	public void fill(Composite container) throws Exception {
		final Composite c = toolkit().createComposite(container, SWT.FLAT);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		showScriptResult(c, "pvalue", experiment.getClusteringResult().getScriptForLogRankTestChiSquaredPValue());
		showScriptResult(c, "Chi Squared", experiment.getClusteringResult().getScriptForLogRankTestChiSqured());		
	}

	private void showScriptResult(Composite container, String label, String script) {
		int style = SWT.READ_ONLY;
		try {
			REXP r = RRunnerFromBioplatUsingR4J.getInstance().getConnection().eval(script).getNativeValue();
			String result = r.asString();

			toolkit().createLabel(container, label);
			toolkit().createText(container, result, style)//
					.setLayoutData(GridDataFactory.fillDefaults().align(GridData.FILL, GridData.CENTER).grab(true, false).create());
			createCopyTextButton(container, script)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		} catch (Exception e) {
			script = "Error evaluating the script '" + label + "'...";
			toolkit().createLabel(container, script, style).setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		} 
	}
}

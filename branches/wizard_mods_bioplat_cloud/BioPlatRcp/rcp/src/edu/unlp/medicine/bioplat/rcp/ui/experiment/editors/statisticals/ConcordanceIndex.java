package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

public class ConcordanceIndex extends CompositeGenerator {

	private final Validation experiment;
	
	public ConcordanceIndex(FormToolkit toolkit, Validation model) {
		super(toolkit);
		this.experiment = model;
	}

	@Override
	protected void fill(Composite container) throws Exception {
		final Composite c = toolkit().createComposite(container, SWT.FLAT);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		showScriptResult(c, "C_index ", String.valueOf(experiment.getValidationResult().getConcordance_index_cindex())); 
		showScriptResult(c, "Concordance Index P-value", String.valueOf(experiment.getValidationResult().getConcordance_index_pvalue()));

	}
	
	private void showScriptResult(Composite container, String label, String result) {
		int style = SWT.READ_ONLY;
		try {
			
			toolkit().createLabel(container, label);
			toolkit().createText(container, result, style)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			/*createCopyTextButton(container, script)//
					.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());*/
		} catch (Exception e) {
			result = "Error evaluating the script '" + label + "'...";
			toolkit().createLabel(container, result, style).setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		} 
	}
	
}

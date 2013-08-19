package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class EnrichrDialog extends Dialog {

	private Browser browser;

	public EnrichrDialog() {
		super(PlatformUIUtils.findShell());
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 600);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);
		browser = new Browser(container, SWT.NONE);
		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		return container;
	}

	public void openWith(Biomarker model) {
		StringBuilder ac = new StringBuilder("{list: \"");
		for (Gene g : model.getGenes())
			ac.append(g.getName() + "\\n");
		ac.append("\"}");
		this.setBlockOnOpen(false);
		open();
		browser.setText("<head><script>" + ENRICHR_JS_FUNCTION + "</script></head> <body onload='enrich(" + ac.toString() + ")'></body>");
		// File tmpf;
		// try {
		// tmpf = File.createTempFile("_kjkj", "tmp");
		// String tmpfs = tmpf.getAbsolutePath().replace(":", "").replace("\\",
		// "/");
		// browser.setUrl("file:/" + tmpfs);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	private static final String ENRICHR_JS_FUNCTION = "function enrich(options) { var defaultOptions = {		description: \"\",		popup: false	};	if (typeof options.description == 'undefined')		options.description = defaultOptions.description;	if (typeof options.popup == 'undefined')		options.popup = defaultOptions.popup;	if (typeof options.list == 'undefined')		alert('No genes defined.');	var form = document.createElement('form');	form.setAttribute('method', 'post');	form.setAttribute('action', 'http://amp.pharm.mssm.edu/Enrichr/enrich');	if (options.popup)		form.setAttribute('target', '_blank');	form.setAttribute('enctype', 'multipart/form-data');	var listField = document.createElement('input');	listField.setAttribute('type', 'hidden');	listField.setAttribute('name', 'list');	listField.setAttribute('value', options.list);	form.appendChild(listField);	var descField = document.createElement('input');	descField.setAttribute('type', 'hidden');	descField.setAttribute('name', 'description');	descField.setAttribute('value', options.description);	form.appendChild(descField);	document.body.appendChild(form);	form.submit();	document.body.removeChild(form);};";
}

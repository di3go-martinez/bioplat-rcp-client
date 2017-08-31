package org.bioplat.multiomics;


import static edu.unlp.medicine.utils.parameters.ApplicationParametersHolder.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class MultiomicsWebPort extends ViewPart {

	
	public static final String ID = "org.bioplat.multiomics.web-port";
	private Browser browser;
	
	public MultiomicsWebPort() {
	}

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(parameters.multiomicsUrl());
	}


	@Override
	public void setFocus() {
		browser.setFocus();
	}

}

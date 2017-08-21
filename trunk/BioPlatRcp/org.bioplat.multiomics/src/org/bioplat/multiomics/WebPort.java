package org.bioplat.multiomics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class WebPort extends ViewPart {

	
	public static final String ID = "org.bioplat.multiomics.web-port";
	private Browser browser;
	
	public WebPort() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(url());
	}

	private String url() {
		return "http://192.155.227.67:3838/multiomics/multiomics/";
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

}

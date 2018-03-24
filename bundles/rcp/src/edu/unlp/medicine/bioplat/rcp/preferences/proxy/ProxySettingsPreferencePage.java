package edu.unlp.medicine.bioplat.rcp.preferences.proxy;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxySettingsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private StringFieldEditor proxyUrl;
	private IntegerFieldEditor proxyPort;

	public ProxySettingsPreferencePage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		proxyUrl = new StringFieldEditor("Proxy Server", "Proxy Server", StringFieldEditor.UNLIMITED,
				StringFieldEditor.VALIDATE_ON_FOCUS_LOST, getFieldEditorParent());
		proxyUrl.setEmptyStringAllowed(true);
		proxyUrl.setStringValue(System.getProperty("http.proxyHost",""));
		addField(proxyUrl);
		
		proxyPort = new IntegerFieldEditor("Proxy Port", "Proxy Port", getFieldEditorParent(), 6){
			@Override
			protected boolean checkState() {
				return proxyPort.getStringValue().trim().isEmpty()|| super.checkState();
			}
		};
		proxyPort.setEmptyStringAllowed(true);
		proxyPort.setStringValue(System.getProperty("http.proxyPort",""));
		addField(proxyPort);
	}

	@Override
	public boolean performOk() {
		String newProxy = proxyUrl.getStringValue();
		String oldPoxy = System.setProperty("http.proxyHost", newProxy);
		String newProxyPort = proxyPort.getStringValue();
		String oldProxyPort = System.setProperty("http.proxyPort", (newProxy==null)?null:newProxyPort);
		if (oldPoxy != null)
			logger.info("Previous proxy was " + oldPoxy + ":" + ((oldProxyPort == null) ? "" : oldProxyPort));
		if (newProxy == null)
			logger.info("Setting the proxy setting to no proxy");
		else
			logger.info("Setting the proxy server to " + newProxy + ":" + ((newProxyPort == null) ? "" : newProxyPort));
		return super.performOk();
	}

	private static final Logger logger = LoggerFactory.getLogger(ProxySettingsPreferencePage.class);
}

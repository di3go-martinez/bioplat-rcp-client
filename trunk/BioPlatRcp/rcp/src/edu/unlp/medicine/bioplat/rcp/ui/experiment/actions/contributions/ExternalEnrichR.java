package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ExternalEnrichR {

	public void openWith(Biomarker model) {
		String html = EnrichrDialog.buildEnrichRHtml(model.getGenes());
		try {
			File f = File.createTempFile("tmp", ".html");
			f.deleteOnExit();
			new FileWriter(f).append(html).flush();

			openSystemBrowser("file:///" + f.getAbsolutePath());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static IWebBrowser openSystemBrowser(String url) {
		try {
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.getExternalBrowser();
			browser.openURL(new URL(url));
			return browser;
		} catch (Exception e) {
			System.out.println(e);
			return null;
			
		}
	}

}

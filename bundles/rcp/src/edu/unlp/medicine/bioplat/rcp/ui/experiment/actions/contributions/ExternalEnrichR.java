package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * 
 * 
 * Abre la integración con EnrichR en el browser configurado del sistema operativo
 * 
 * @author diego
 * @see EnrichActionContribution
 *
 */
public class ExternalEnrichR {

  public void openWith(Biomarker model) {
    String html = EnrichrDialog.buildEnrichRHtml(model.getGenes());
    try {
      File f = File.createTempFile("tmp", ".html");
      f.deleteOnExit();
      FileWriter fileWriter = new FileWriter(f);
      fileWriter.append(html);
      fileWriter.close();

      openSystemBrowser("file:///" + f.getAbsolutePath());

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public static void openSystemBrowser(String url) {
    try {
      IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
      IWebBrowser browser = browserSupport.getExternalBrowser();
      browser.openURL(new URL(url));
    } catch (Exception e) {
      logger.error("Error openning the external web browser, with url: "+url, e);
    }
  }
  
  private static final Logger logger = LoggerFactory.getLogger(ExternalEnrichR.class);

}

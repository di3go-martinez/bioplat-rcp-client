package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class EnrichActionContribution extends AbstractActionContribution<Biomarker> {

  private static final String USE_INTERNAL_ENRICHR_BROWSER_PARAMETER = "INTERNAL_ENRICHR_BROWSER";
  private static final String _DEFAULT_VALUE = "false";

  @Override
  public void run() {
    PlatformUIUtils.findDisplay().syncExec(new Runnable() {

      @Override
      public void run() {
        if (showEmbeded())
          //TODO por ahora no anda con el internal, hacer pruebas en distintos browsers
          new EnrichrDialog().openWith(model());
        else
          new ExternalEnrichR().openWith(model());
      }

      private boolean showEmbeded() {
        String value = System.getProperty(USE_INTERNAL_ENRICHR_BROWSER_PARAMETER, _DEFAULT_VALUE);
        return "true".equals(value);
      }
    });
  }

}

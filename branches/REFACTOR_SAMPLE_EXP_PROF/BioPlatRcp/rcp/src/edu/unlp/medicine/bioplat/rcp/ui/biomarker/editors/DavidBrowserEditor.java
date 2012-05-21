package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class DavidBrowserEditor extends AbstractEditorPart<Biomarker> {

	private Browser browser;
	private Text url;

	@Override
	protected void doCreatePartControl(Composite parent) {
		Composite c = Widgets.createDefaultContainer(parent);

		url = new Text(c, SWT.READ_ONLY | SWT.BORDER);
		url.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		browser = new Browser(c, SWT.NONE);
		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
	}

	@Override
	protected Observer createModificationObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				String newUrl = model().getDavidURLForFunctionalAnalysis();
				if (!newUrl.equals(browser.getUrl())) {
					browser.setUrl(model().getDavidURLForFunctionalAnalysis());
					url.setText(newUrl);
				}
			}
		};
	}

}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

/**
 * 
 * @author Diego Martínez
 * 
 */
public class StatisticalDataEditor extends AbstractEditorPart<Validation> {

	public StatisticalDataEditor(boolean autoUpdatableTitle) {
		super(autoUpdatableTitle);
	}

	private static FormToolkit toolkit;

	@Override
	protected void doCreatePartControl(final Composite parent) {
		makeView(parent, model());
	}

	private static void createExpandable(final ScrolledForm parent, String title, CompositeGenerator compositeGenerator) {
		final ExpandableComposite ec = toolkit.createExpandableComposite(parent.getBody(), ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);

		try {
			ec.setText(title);
			Composite client = compositeGenerator.generate(ec);
			ec.setClient(client);
			ec.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(ExpansionEvent e) {
					parent.reflow(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	public static void makeView(Composite parent, Validation model) {
		toolkit = new FormToolkit(parent.getDisplay());

		ScrolledForm container = toolkit.createScrolledForm(parent);
		// container.setLayout(GridLayoutFactory.fillDefaults().create());
		container.getBody().setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		createExpandable(container, "Log Rank Test Chi-Squred (The same as Survival Curve Differences with rho = 0)", new LogRankTest(toolkit, model));
		
		createExpandable(container, "Concordance Index", new ConcordanceIndex(toolkit, model));
		
		createExpandable(container, "ROC", new ROC(toolkit, model));
		
		createExpandable(container, "Kaplan-Meier", new SurvivalCurves(toolkit, model));

		
		
		

	}
}

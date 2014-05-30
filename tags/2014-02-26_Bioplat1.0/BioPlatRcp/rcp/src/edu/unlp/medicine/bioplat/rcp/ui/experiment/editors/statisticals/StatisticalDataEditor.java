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
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

/**
 * 
 * @author Diego Martínez
 * 
 */
public class StatisticalDataEditor extends AbstractEditorPart<ExperimentAppliedToAMetasignature> {

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

	public static void makeView(Composite parent, ExperimentAppliedToAMetasignature model) {
		toolkit = new FormToolkit(parent.getDisplay());

		ScrolledForm container = toolkit.createScrolledForm(parent);
		// container.setLayout(GridLayoutFactory.fillDefaults().create());
		container.getBody().setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		final ExperimentAppliedToAMetasignature experiment = model;

		createExpandable(container, "Survival Curves", new SurvivalCurves(toolkit, experiment));

		createExpandable(container, "LogRankTestChiSqured", new LogRankTest(toolkit, experiment));

	}
}
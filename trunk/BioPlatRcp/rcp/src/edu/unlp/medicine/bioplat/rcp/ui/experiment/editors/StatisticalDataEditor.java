package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class StatisticalDataEditor extends AbstractEditorPart<ExperimentAppliedToAMetasignature> {
	public StatisticalDataEditor(boolean autoUpdatableTitle) {
		super(autoUpdatableTitle);
	}

	private FormToolkit toolkit;

	@Override
	protected void doCreatePartControl(final Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		Composite container = toolkit.createComposite(parent);
		container.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());

		ExperimentAppliedToAMetasignature experiment = model();
		// TODO parametrizar y hacer un expandable por test
		createExpandable(container, experiment);
	}

	private void createExpandable(final Composite parent, ExperimentAppliedToAMetasignature experiment) {
		final ExpandableComposite ec = toolkit.createExpandableComposite(parent, ExpandableComposite.TREE_NODE | ExpandableComposite.CLIENT_INDENT);

		ec.setText("LogRankTestChiSqured");

		Composite client = toolkit.createComposite(ec);
		client.setLayout(GridLayoutFactory.fillDefaults().create());

		String script_LogRankTestChiSqured = "Couldn't generate the script...";
		try {
			script_LogRankTestChiSqured = experiment.generateScript4LogRankTestChiSqured();
		} catch (Exception e) {
			e.printStackTrace();
		}
		toolkit.createLabel(client, script_LogRankTestChiSqured, SWT.WRAP);

		ec.setClient(client);
		ec.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				parent.layout(true);
			}
		});
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
}

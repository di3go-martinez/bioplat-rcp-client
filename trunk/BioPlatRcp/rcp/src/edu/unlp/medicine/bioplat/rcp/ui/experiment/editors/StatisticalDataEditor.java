package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.concurrent.Callable;

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

		final ExperimentAppliedToAMetasignature experiment = model();

		createExpandable(container, experiment, "Script For Dooing The Cluster", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return experiment.getScriptForDoingTheCluster();
			}
		});

		createExpandable(container, experiment, "LogRankTestChiSqured", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return experiment.generateScript4LogRankTestChiSqured();
			}
		});

	}

	private void createExpandable(final Composite parent, ExperimentAppliedToAMetasignature experiment, String title, Callable<String> scriptGenerator) {
		final ExpandableComposite ec = toolkit.createExpandableComposite(parent, ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT/*
																																				 * TREE_NODE
																																				 */);

		ec.setText(title);

		Composite client = toolkit.createComposite(ec);
		client.setLayout(GridLayoutFactory.fillDefaults().create());

		String $script = "Couldn't generate the script...";
		try {
			$script = scriptGenerator.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		toolkit.createLabel(client, $script, SWT.WRAP);

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

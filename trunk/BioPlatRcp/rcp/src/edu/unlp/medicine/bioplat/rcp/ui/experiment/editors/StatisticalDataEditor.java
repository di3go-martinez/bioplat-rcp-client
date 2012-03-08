package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

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

		ScrolledForm container = toolkit.createScrolledForm(parent);
		// container.setLayout(GridLayoutFactory.fillDefaults().create());
		container.getBody().setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());

		final ExperimentAppliedToAMetasignature experiment = model();

		createExpandable(container, experiment, "Script For Dooing The Cluster", new CompositeGenerator() {

			@Override
			public Composite generate(Composite parent) throws Exception {
				ScrolledForm result = toolkit.createScrolledForm(parent);
				result.setLayout(GridLayoutFactory.fillDefaults().create());

				Composite c = result.getBody();
				c.setLayout(GridLayoutFactory.fillDefaults().create());

				Text t = new Text(c, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
				t.setText(experiment.getScriptForDoingTheCluster());

				return result;
			}
		});

		createExpandable(container, experiment, "LogRankTestChiSqured", new CompositeGenerator() {

			@Override
			public Composite generate(Composite parent) throws Exception {

				ScrolledForm result = toolkit.createScrolledForm(parent);
				result.setLayout(GridLayoutFactory.fillDefaults().create());

				Composite c = result.getBody();
				c.setLayout(GridLayoutFactory.fillDefaults().create());
				String script;
				try {
					script = experiment.generateScript4LogRankTestChiSqured();
				} catch (Exception e) {
					script = "Couldn't generate the script...";
				}
				Text t = new Text(c, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
				t.setText(script);

				return result;

			}
		});

	}

	private void createExpandable(final ScrolledForm parent, ExperimentAppliedToAMetasignature experiment, String title, CompositeGenerator compositeGenerator) {
		final ExpandableComposite ec = toolkit.createExpandableComposite(parent.getBody(), ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT/*
																																						 * TREE_NODE
																																						 */);
		ec.setText(title);

		Composite client;
		// = toolkit.createComposite(ec);
		// client.setLayout(GridLayoutFactory.fillDefaults().create());
		//
		// String $script = "Couldn't generate the script...";
		try {
			client = compositeGenerator.generate(ec);
		} catch (Exception e) {
			e.printStackTrace();
			client = toolkit.createComposite(ec);
			new Label(client, SWT.NONE).setText("Couldn't generate....");
		}
		// toolkit.createLabel(client, $script, SWT.WRAP);

		ec.setClient(client);
		ec.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				parent.reflow(true);
			}
		});
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
}

interface CompositeGenerator {
	Composite generate(Composite parent) throws Exception;
}
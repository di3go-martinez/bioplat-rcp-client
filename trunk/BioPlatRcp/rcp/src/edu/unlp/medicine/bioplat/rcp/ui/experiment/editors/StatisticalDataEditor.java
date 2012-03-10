package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
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

		createExpandable(container, "Script For Dooing The Cluster", new CompositeGenerator(toolkit) {

			@Override
			protected void fill(Composite c) throws Exception {

				c.setLayout(GridLayoutFactory.fillDefaults().create());

				Text t = toolkit.createText(c, experiment.getScriptForDoingTheCluster(), SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
				t.setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 200).grab(true, false).create());
			}
		});

		createExpandable(container, "LogRankTestChiSqured", new CompositeGenerator(toolkit) {

			@Override
			public void fill(Composite container) throws Exception {

				String script;
				int style = SWT.BORDER | SWT.READ_ONLY;
				try {
					script = experiment.generateScript4LogRankTestChiSqured();
					style |= SWT.V_SCROLL | SWT.MULTI;
				} catch (Exception e) {
					script = "Couldn't generate the script...";
				}

				toolkit.createText(container, script, style);

			}
		});

	}

	private void createExpandable(final ScrolledForm parent, String title, CompositeGenerator compositeGenerator) {
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
}

abstract class CompositeGenerator {

	private FormToolkit toolkit;

	public CompositeGenerator(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}

	Composite generate(Composite parent) throws Exception {
		ScrolledForm result = toolkit.createScrolledForm(parent);
		result.setLayout(GridLayoutFactory.fillDefaults().create());

		Composite c = result.getBody();
		c.setLayout(GridLayoutFactory.fillDefaults().create());
		fill(c);

		return result;

	}

	protected abstract void fill(Composite container) throws Exception;
}
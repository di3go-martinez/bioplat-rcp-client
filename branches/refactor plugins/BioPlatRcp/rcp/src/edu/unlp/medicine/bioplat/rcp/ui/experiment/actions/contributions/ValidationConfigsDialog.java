package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ApplyExperimentsOnMetasignatureCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * 
 * @author diego martínez
 * 
 */
public class ValidationConfigsDialog extends TitleAreaDialog {

	protected ValidationConfigsDialog(Shell parentShell, Biomarker biomarker) {
		super(parentShell);
		this.biomarker = biomarker;

		// TODO mejorar
		experimentsWizard = createExperimentSelectorWizard(biomarker);
		// TODO conseguir la selección actual
		// experimentsWizard.init(PlatformUI.getWorkbench(),
		// StructuredSelection.EMPTY);

	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Apply experiment for Gene Signature validation");
	}

	private final List<ValidationConfig> data = Lists.newArrayList();

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		Composite c = Widgets.createDefaultContainer(container);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());

		tr = TableBuilder.create(c)//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("experimentToValidate.name").title("experiment"))//
				// .addColumn(ColumnBuilder.create().property("shouldGenerateCluster").checkbox().centered().width(20).title("cluster?"))//
				.addColumn(ColumnBuilder.create().property("numberOfClusters").title("clusters"))//
				// .addColumn(ColumnBuilder.create().property("numberOfTimesToRepeatTheCluster").title("times"))//
				// .addColumn(ColumnBuilder.create().property("statisticsSignificanceTest.friendlyName").title("Statistics Significance Test"))//
				.addColumn(ColumnBuilder.create().property("attribtueNameToDoTheValidation").title("Time"))//
				.addColumn(ColumnBuilder.create().property("secondAttribtueNameToDoTheValidation").title("Event"))//

				.input(data).build();

		// Composite buttons = new Composite(c, SWT.BORDER);
		// buttons.setLayout(GridLayoutFactory.fillDefaults().create());
		// buttons.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());

		setMessage("In this dialog you can set up all the validation you would like to do. A validation will be applied on a particular experiment previously loaded in the platform. The experiment must have follow up clinical data (the event and the time). The validation will be done in two steps: 1-Cluster experiment samples by its gene expression profiles 2-Measure the correlation between resulted clusters and follow up data. You will see statistical result on the experiments applied tab of the GeneSignature editor.");
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		Composite c = new Composite(parent, SWT.None);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		Button add = new Button(c, SWT.NONE);
		// add.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).align(SWT.END, SWT.END).create());
		add.setText("+");
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// wd = new WizardDialog(PlatformUIUtils.findShell(),
				// experimentsWizard);
				// wd.setPageSize(400, 400);
				// wd.setTitle("Agregar Validation Configs");
				// wd.open();
				experimentsWizard.open();
			}
		});
		super.createButtonsForButtonBar(c);

	}

	@Override
	protected void okPressed() {
		close();
		final String mmsg = "Applying Validation Configs";
		Job j = new Job(mmsg) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("", experimentsWizard.commands2apply().size());
				int count = 0;
				for (ApplyExperimentsOnMetasignatureCommand command : experimentsWizard.commands2apply()) {
					try {
						command.execute();
						count++;
					} catch (Exception e) {
						MessageManager.INSTANCE.add(Message.error("Unexpected error applying the experiment " + command.getExperimentsAppliedToAMetasignature(), e));
					}
					monitor.worked(1);
				}
				final String msg = "Succesfully commands applied: " + count;
				MessageManager.INSTANCE.add(Message.info(msg));
				if (count == experimentsWizard.commands2apply().size())
					PlatformUIUtils.openInformation("Experiments Applied", "Experiments Successfully Applied, see the results on message view");
				monitor.done();
				return ValidationStatus.ok();
			}
		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

	}

	private Biomarker biomarker;
	private ValidationConfigWizard experimentsWizard;
	private WizardDialog wd;
	private TableReference tr;

	private ValidationConfigWizard createExperimentSelectorWizard(Biomarker biomarker) {
		return new ValidationConfigWizard(biomarker) {
			@Override
			protected void register(ValidationConfig validationConfig) {
				data.add(validationConfig);
			}

			@Override
			protected void afterExecution() {
				tr.refresh();
			}
		};
	}
}

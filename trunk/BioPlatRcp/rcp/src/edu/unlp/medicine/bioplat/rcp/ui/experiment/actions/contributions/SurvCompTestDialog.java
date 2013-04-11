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
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringException;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * 
 * @author diego martínez
 * 
 */
public class SurvCompTestDialog extends TitleAreaDialog {

	protected SurvCompTestDialog(Shell parentShell, Biomarker biomarker) {
		super(parentShell);
		this.biomarker = biomarker;

		//copy&paste!!!
		
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
		newShell.setText("Surv comp validation");
	}

	private final List<ValidationConfig4DoingCluster> data = Lists.newArrayList();

	@Override
	protected Control createDialogArea(Composite parent) {
		this.setTitle("Validate biomarker using survComp");
		setMessage("In this dialog you can set up all the validations you would like to do using survComp. Each validation will be applied on a particular experiment previously loaded in the platform. ");
	
		Composite container = (Composite) super.createDialogArea(parent);
		Composite c = Widgets.createDefaultContainer(container);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());

		String help = "Help: In the table you can see the validations you have configured. For adding new configurations, you can use the \"+\" button at the bottom of the table\n\n";
		GUIUtils.addWrappedText(c, help, 8, true);

		
		tr = TableBuilder.create(c)//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("experimentToValidate.name").title("experiment"))//
				// .addColumn(ColumnBuilder.create().property("shouldGenerateCluster").checkbox().centered().width(20).title("cluster?"))//
				.addColumn(ColumnBuilder.create().property("numberOfClusters").title("clusters"))//
				// .addColumn(ColumnBuilder.create().property("numberOfTimesToRepeatTheCluster").title("times"))//
				// .addColumn(ColumnBuilder.create().property("statisticsSignificanceTest.friendlyName").title("Statistics Significance Test"))//
				.addColumn(ColumnBuilder.create().property("attribtueNameToDoTheValidation").title("Validation Attributte Name"))//
				.addColumn(ColumnBuilder.create().property("secondAttribtueNameToDoTheValidation").title("Event"))//

				.input(data).build();

		// Composite buttons = new Composite(c, SWT.BORDER);
		// buttons.setLayout(GridLayoutFactory.fillDefaults().create());
		// buttons.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());


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
		final String mmsg = "SurvComp validation";
		// TODO migrar a EJob cuando este esté
		Job j = new Job(mmsg) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				int cantOK = 0;
				
				monitor.beginTask("", experimentsWizard.commands2apply().size());
				int count = 0;
				for (OneBiomarkerCommand command : experimentsWizard.commands2apply()) {
					try {
						command.execute();
						count++;
					} catch (ClusteringException e) {
						//Agrego el mensaje de error.
						MessageManager.INSTANCE.add(Message.error(e.getSpecificError() + ". Details: " + e.getGenericError(), e));
						//PlatformUIUtils.openInformation("Experiments Applied", e.getSpecificError() + ". Details: " + e.getGenericError());
						
					}
					catch (Exception e) {
						MessageManager.INSTANCE.add(Message.error("Unexpected error applying survComp test......" , e));
						//PlatformUIUtils.openInformation("Experiments Applied", "Unexpected error applying an experiment......");
					}
					monitor.worked(1);
				}

				
				
				if (count == experimentsWizard.commands2apply().size()){
					String msg = "All survComp validations (" + count + ") were  sucessfully executed. You can see the results on the 'SurvComp' tab of the Gene Siganture"; 
					PlatformUIUtils.openInformation("SurvComp Validation", msg);
					MessageManager.INSTANCE.add(Message.info(msg));
				}
					
				else{
					PlatformUIUtils.openWarning("SurvComp Validation", " SurvComp validations succesfully executed: " + count + ". \n SurvComp validations with error: " + (experimentsWizard.commands2apply().size() - count)  + ". \n For error details, see rows above in this the Message view.");
				}
				
				
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
		return new ApplySurvCompTestWizard(biomarker, true) {
			@Override
			protected void register(ValidationConfig4DoingCluster validationConfig) {
				data.add(validationConfig);
			}

			@Override
			protected void afterExecution() {
				tr.refresh();
			}
		};
	}
}

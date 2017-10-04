/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.StatusValidValues;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringException;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Juan
 * 
 */
public class ValidationTestDialog extends TitleAreaDialog {

	private Biomarker biomarker;
	private ValidationConfigWizard experimentsWizard;
	private WizardDialog wd;
	private TableReference tr;
	private final List<ValidationConfig4DoingCluster> data = Lists
			.newArrayList();

	/**
	 * @param biomarker
	 * @param parentShell
	 */
	public ValidationTestDialog(Shell parentShell, Biomarker biomarker) {
		super(parentShell);
		this.biomarker = biomarker;
		this.experimentsWizard = this.createExperimentSelectorWizard();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Gene Signature Statistic Analysis of your gene signature");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.setTitle("Survival Statistic Analysis");
		setMessage("Add all the experiments you would like to use for validating statistically, the prognostic value that your gen signature has got over these data. You can... add more than once the same experiment for evaluating different statistics configurations. Each configuration will be a row in the table. For each configuration the statistically process will be the same: calculate the cluster considering expression data and then calculate all the statistics.");

		
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
		//container.setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());
		String help1 = "\nHelp: In the table you can see the statistic validations you have configured. For adding new configurations, you can use the \"+\" button at the bottom of the table\n";
		String help2 = "\nHelp 2: The samples of validation experiment having incorrect values in time attribute or status attribute, will be taken off for validation.\n";
		String help3 = "\nHelp 3:" + StatusValidValues.getMessageForEventAttributeForGUI();
		String help=help1+help2+help3;
		GUIUtils.addBoldText(container, help, 8);

		Composite containerTabla = (Composite) super.createDialogArea(container);
		containerTabla.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 3).create());
		containerTabla.setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());
		
	
		
		tr = TableBuilder
				.create(containerTabla)
				.hideSelectionColumn()
				.addColumn(
						ColumnBuilder.create()
								.property("experimentToValidate.name")
								.title("Experiment"))
				.addColumn(
						ColumnBuilder.create().property("numberOfClusters")
								.title("Clusters"))
				.addColumn(
						ColumnBuilder.create()
								.property("attribtueNameToDoTheValidation")
								.title("Validation Attributte Name"))
				.addColumn(
						ColumnBuilder
								.create()
								.property(
										"secondAttribtueNameToDoTheValidation")
								.title("Event")).input(data).build();

		GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(containerTabla);
		tr.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());
		
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10)
				.create());
		Composite c = new Composite(parent, SWT.None);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		Button add = new Button(c, SWT.NONE);
		add.setText("+");
		//add.setImage(new Image(c.getDisplay(), "resources/icons/plus.png"));
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				experimentsWizard.open();
			}
		});
		super.createButtonsForButtonBar(c);
		maximizeDialog();
	}

	private void maximizeDialog() {
		getShell().pack();
		getShell().setMaximized(true);
	}

	@Override
	protected void okPressed() {
		close();
		final String mmsg = "Test validation";
		// TODO migrar a EJob cuando este estÃ©
		Job j = new Job(mmsg) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				monitor.beginTask("", experimentsWizard.commands2apply().size());
				int count = 0;
				for (OneBiomarkerCommand command : experimentsWizard
						.commands2apply()) {
					try {
						if (command.prevalidation()) {
							command.execute();
							count++;
							//(ValidationsTestCommand)command.
							//MessageManager.INSTANCE.add(Message.info(command. ""));

						}
						else {
							//MDB 2017/01/10. If there are some problems with biomarker or experiment (for example no genes in common)
							MessageManager.INSTANCE.add(Message.error("There are no genes in common between the gene signature and the Dataset expression data. It is not possible to do the validation" + ". Gene Signature: "  + command.getBiomarker().getName() + ". " + command.getInfoForError()));
							
						}
					} catch (ClusteringException e) {
						// Agrego el mensaje de error.
						MessageManager.INSTANCE.add(Message.error(e.getSpecificError() + ". Details: " + e.getGenericError(), e));

					} 
					catch (NumberFormatException e) {
						String msg = "There are some values in the expression data matrix that are not valid numbers. Perhaps, blank values. Please use the \"change value by anothe value\" operation for changing them by valid numbers. Then do the statistc analysis again";
						MessageManager.INSTANCE.add(Message.error(msg, e));
						//PlatformUIUtils.openError("Log rank test validation error",	msg);
						
					}
					catch (Exception e) {
						MessageManager.INSTANCE
								.add(Message
										.error("Unexpected error applying test validation......",
												e));
					}
					monitor.worked(1);
				}

				if (count == experimentsWizard.commands2apply().size()) {
					String msg = "All test validations ("
							+ count
							+ ") were  sucessfully executed. You can see the results on the gene signature 'Statistic Analysis' tab";
					PlatformUIUtils.openInformation("Log rank test validation",
							msg);
					MessageManager.INSTANCE.add(Message.info(msg));
				}
				
				else {
					PlatformUIUtils
							.openWarning(
									"Validation",
									" Validations succesfully executed: "
											+ count
											+ ". \n Validations with error: "
											+ (experimentsWizard
													.commands2apply().size() - count)
											+ ". \n For error details, see rows above in this the Message view.");

				}
				monitor.done();
				return ValidationStatus.ok();
			}
		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.setRule(ValidationConfigWizard.getMutexRule());
		j.schedule();

	}

	private ValidationConfigWizard createExperimentSelectorWizard() {

		return new ApplyValidationTestWizard(biomarker, true) {
			@Override
			protected void register(
					ValidationConfig4DoingCluster validationConfig) {
				data.add(validationConfig);
			}

			@Override
			protected void afterExecution() {
				tr.refresh();
			}
		};
	}
}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.ExperimentFromInSilicoDBImporter;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ImportExperimentFromInSilicoWizard extends Wizard implements IImportWizard {

	private static final String PAGE_NAME = "GSE";
	private static Logger logger = LoggerFactory.getLogger(ImportExperimentFromInSilicoWizard.class);

	public ImportExperimentFromInSilicoWizard() {
		this.setWindowTitle("Import experiment from InSilico using GSE (GEO Series)");
	}

	
	public int getWizardWidth(){
		return 700;
	}
	
	
	public int getWizardHeight(){
		return 500;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(createFirstPage());
		
	}

	private WizardModel model = createWizardModel();

	private WizardModel model() {
		return model;
	}

	private WizardModel createWizardModel() {
		final WizardModel wizardModel = new WizardModel();
		wizardModel.add("importClinicalData", new WritableValue(false, Boolean.class));
		wizardModel.add("normalized", new WritableValue(false, Boolean.class));
		return wizardModel.add("GSE", new WritableValue("GSE4635", String.class));
	}

	private WizardPage createFirstPage() {
		return new WizardPage(PAGE_NAME, "Which experiment to import from In Silico?", null) {

			@Override
			public void createControl(Composite parent) {

				this.setDescription("Bioplat will connect with InSilicoDB for importing the experiment you are asking for. Please specify the following information.");
				
				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				//GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
				Composite group = new Group(parent, SWT.NONE);
				group.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(50,50).spacing(7, 20).create());

				new CLabel(group, SWT.BOLD).setText("GSE (GEO Series):");
				Text gseHolder = new Text(group, SWT.BORDER);

				Button clinicalDataHolder = new Button(group, SWT.CHECK);

				// clinicalDataHolder.setText("Import Clinical Data");

				clinicalDataHolder.setText("Import clinical data");

				Button normalizedHolder = new Button(group, SWT.CHECK);

				// normalizedHolder.setText("Normalized (FRMA) ");

				normalizedHolder.setText("Normalized (FRMA)");


				String text = "\n\n\n\nImportante Note: Take into account that the connection between both systems it is not as faster as downloading the file from the page. So, if the experiment is so m uch big, you should download it from the inSilico page and then import it in Bioplat using the 'Import Experiment From Text file'.";
				GUIUtils.addWrappedText(group, text,8,true);

				
				//Label for explaining the possible
				String text2 = "\nGene Collapse Strategy: If the experiment has got probes instead of genes, a collapse strategy will be applied automatically. The gene will be represented by the probe with the highest average...";
				GUIUtils.addWrappedText(group, text2,8,true);
				
				
				dbc.bindValue(SWTObservables.observeText(gseHolder, SWT.Modify), model().valueHolder("GSE"), new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("GSE")), null);

				dbc.bindValue(SWTObservables.observeSelection(clinicalDataHolder), model().valueHolder("importClinicalData"));

				dbc.bindValue(SWTObservables.observeSelection(normalizedHolder), model().valueHolder("normalized"));

				GridLayoutFactory.swtDefaults().numColumns(1).generateLayout(group);
				setControl(group);
				
				
				Point size = getShell().computeSize(getWizardWidth(), getWizardHeight());
				getShell().setSize(size);

			}
		};
	}

	private static ExecutorService exec = Executors.newFixedThreadPool(1);

	@Override
	public boolean performFinish() {

		MessageManager.INSTANCE.clear();

		configureParamenters();

		Job j = new Job("Import experiment " + gse + " from InSilicoDB") {

			@Override
			protected IStatus run(final IProgressMonitor progressMonitor) {
				try {
					progressMonitor.beginTask("Importing experiment " + gse + " from InSilicoDB", IProgressMonitor.UNKNOWN);
					Future<Experiment> holder = exec.submit(new Callable<Experiment>() {
						@Override
						public Experiment call() throws Exception {
							Monitor m = Monitors.adapt(progressMonitor);
							return execute(m);
						}

					});

					try {
						final Experiment e = holder.get(); // join
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								PlatformUIUtils.openEditor(e, ExperimentEditor.id());
							}
						});
					} catch (Exception e) {
						final String msg = "The experiment could not be imported from inSilicoDB.";
						MessageManager.INSTANCE.add(Message.error(msg, e));
						return ValidationStatus.error(msg, e);
					}
					String importClinicalDataString="NO"; if (importClinicalData) importClinicalDataString="YES";
					String normalizedString="NO"; if (normalized) normalizedString="YES";
					
					MessageManager.INSTANCE.add(Message.info("Experiment " + gse + " imported succesfully from inSilicoDB. Normalized(FRMA)? " + normalizedString + ". Clinical data Imported? " + importClinicalDataString + "."  ));
					return ValidationStatus.ok();
				} finally {
					progressMonitor.done();
				}
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

		return true;

	}

	/**
	 * Permite configurar los parámetros dentro del Realm, el cual es necesario
	 * para poder acceder a los valores del model...
	 * 
	 */
	// TODO revisar si se puede resolver dentro del WizardModel el acceso con el
	// realm que va
	private void configureParamenters() {
		this.gse = this.model().value("GSE").toString();
		this.normalized = this.model().value("normalized");
		this.importClinicalData = this.model().value("importClinicalData");
	}

	private String gse;
	private Boolean normalized;
	private Boolean importClinicalData;

	private Experiment execute(Monitor m) {
		ExperimentFromInSilicoDBImporter importer = new ExperimentFromInSilicoDBImporter(this.gse, this.importClinicalData, this.normalized);
		Experiment experiment = null;
		try {
			experiment = importer.monitor(m).execute();
		} catch (ExperimentBuildingException e) {
			logger.error("Experiment Building Exception:", e);
			throw e;
		}

		return experiment;

	}
	
	
	
	
}

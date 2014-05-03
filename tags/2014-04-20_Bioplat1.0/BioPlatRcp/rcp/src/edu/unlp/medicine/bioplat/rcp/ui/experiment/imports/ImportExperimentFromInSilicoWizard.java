package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.util.Arrays;
import java.util.List;
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
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
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
		wizardModel.add("platform", new WritableValue(this.getFirstPlatform(), String.class));
		return wizardModel.add("GSE", new WritableValue("GSE4635", String.class));
	}

	private WizardPage createFirstPage() {
		return new WizardPage(PAGE_NAME, "Which experiment to import from In Silico?", null) {

			@Override
			public void createControl(Composite parent) {

				this.setDescription("BioPlat will connect with InSilicoDB for importing the experiment you are asking for. Please specify the following information.");
				
				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				//GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
				Composite group = new Group(parent, SWT.NONE);
				//group.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(50,50).spacing(7, 20).create());

				
				Composite gseGroup = new Group(group, SWT.NONE);
				gseGroup.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).margins(0, 10).spacing(10, 0).create());
				new CLabel(gseGroup, SWT.BOLD).setText("GSE (GEO Series):");
				Text gseHolder = new Text(gseGroup, SWT.BORDER);
				gseHolder.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());
				Button clinicalDataHolder = new Button(gseGroup, SWT.CHECK);
				clinicalDataHolder.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());
				clinicalDataHolder.setText("Import clinical data");

				Composite plGroup = new Group(group, SWT.NONE);
				plGroup.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(0, 10).spacing(10, 0).create());
				//GridDataFactory gdf = GridDataFactory.fillDefaults().grab(false, false);
				new CLabel(plGroup, SWT.BOLD).setText("Platform:");
				ComboViewer cv = Utils.newComboViewerWithoutLabel(plGroup, "Select the platform.", getAllPlatforms());
				cv.setSelection(new StructuredSelection(getFirstPlatform()));
				final Button normalizedHolder ;
				normalizedHolder = new Button(plGroup, SWT.CHECK);
				normalizedHolder.setText("Normalized (FRMA)");

				
				cv.addSelectionChangedListener(new ISelectionChangedListener() {
					
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						String newSelection = (String)((IStructuredSelection) event.getSelection()).getFirstElement();
						if (getPlatformsCanBeNormalized().contains(newSelection)){
							normalizedHolder.setSelection(normalizedHolder.getSelection());
							normalizedHolder.setEnabled(true);
						}
						else{
							normalizedHolder.setSelection(false);
							normalizedHolder.setEnabled(false);
						}
						
					}
				});

				

//				String text = "\n\n\n\nImportante Note: Take into account that the connection between both systems it is not as faster as downloading the file from the page. So, if the experiment is so m uch big, you should download it from the inSilico page and then import it in Bioplat using the 'Import Experiment From Text file'.";
//				GUIUtils.addWrappedText(group, text,8,true);

				
				//Label for explaining the possible
				String text2 = "\nGene Collapse Strategy: If the experiment has got probes instead of genes, a collapse strategy will be applied automatically. The gene will be represented by the probe with the highest average...";
				GUIUtils.addWrappedText(group, text2,8,true);
				
				
				dbc.bindValue(SWTObservables.observeText(gseHolder, SWT.Modify), model().valueHolder("GSE"), new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("GSE")), null);

				dbc.bindValue(SWTObservables.observeSelection(clinicalDataHolder), model().valueHolder("importClinicalData"));

				dbc.bindValue(SWTObservables.observeSelection(normalizedHolder), model().valueHolder("normalized"));

				GridLayoutFactory.swtDefaults().numColumns(1).generateLayout(group);
				setControl(group);
				
				dbc.bindValue(ViewersObservables.observeSingleSelection(cv), model().valueHolder("platform"));
				
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
		this.platform = this.model().value("platform");
		
		
	}

	private String gse;
	private Boolean normalized;
	private Boolean importClinicalData;
	private String platform;

	private Experiment execute(Monitor m) {
		
		String platformName = this.platform.split(" ")[0];
		ExperimentFromInSilicoDBImporter importer = new ExperimentFromInSilicoDBImporter(this.gse, this.importClinicalData, this.normalized, platformName);
		Experiment experiment = null;
		try {
			experiment = importer.monitor(m).execute();
		} catch (ExperimentBuildingException e) {
			logger.error("Experiment Building Exception:", e);
			throw e;
		}

		return experiment;

	}
	

	private List<?> getAllPlatforms() {
		return Arrays.asList("GPL96 (HG-U133A)", "GPL97 (HG-U133B)", "GPL570 (HG-U133PLUS2)","GPL91 (HG-U95A)", "GPL571 (HGU133-A)");
		
	}
	
	
	private String getFirstPlatform() {
		return "GPL96 (HG-U133A)";
		
	}
	
	private List<?> getPlatformsCanBeNormalized() {
		return Arrays.asList("GPL96 (HG-U133A)", "GPL570 (HG-U133PLUS2)");
		
	}
	
	
}

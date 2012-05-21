package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_AVERAGE;
import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_LAST_ONE_IN_THE_FILE;
import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_MAX;
import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_MEDIA;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.FilePathValidator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromFileExperimentDescriptor;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.FromFileExperimentFactory;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * 
 * @author diego
 * 
 */
public class FileExperimentImport extends Wizard implements IImportWizard {

	private static ExecutorService exec = Executors.newFixedThreadPool(1);

	public FileExperimentImport() {
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {

		// deben ser accedidas desde el "Realm"
		final String filePath = str(wm.filePath);
		final String collapseStrategy = str(wm.collapseStrategy);
		final int[] lines = split(str(wm.lines));

		Job j = new Job("Importando experimento") {

			@Override
			protected IStatus run(final IProgressMonitor progressMonitor) {

				Future<Experiment> holder = exec.submit(new Callable<Experiment>() {
					@Override
					public Experiment call() throws Exception {
						Monitor m = Monitors.adapt(progressMonitor);

						return new FromFileExperimentFactory(new FromFileExperimentDescriptor(filePath, lines[0], lines[1], lines[2], lines[3], "\t", collapseStrategy)).monitor(m).createExperiment();
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
					e.printStackTrace();
				}

				return ValidationStatus.ok();
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

		return true;
	}

	private int[] split(String str) {
		int[] result = new int[4];
		String[] input = str.split(",");
		for (int i = 0; i < 4; i++)
			result[i] = Integer.valueOf(input[i].trim());
		return result;
	}

	private static String str(IObservableValue valueHolder) {
		return valueHolder.getValue().toString();
	}

	private WizardModel wm = new WizardModel();

	private class WizardModel {
		IObservableValue filePath = WritableValue.withValueType(File.class);
		IObservableValue collapseStrategy = WritableValue.withValueType(String.class);
		IObservableValue lines = WritableValue.withValueType(String.class);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(new WizardPage("conf", "Configuración", null) {

			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.NONE);

				new Label(c, SWT.NONE).setText("File:");
				FileText filePath = new FileText(c, SWT.NONE);
				Map<String, String> filters = Maps.newHashMap();
				filters.put("*.csv", "CSV File");
				filters.put("*", "All");
				filePath.setFilter(filters);

				dbc.bindValue(SWTObservables.observeText(filePath.textControl(), SWT.Modify), wm.filePath, //
						new UpdateValueStrategy().setAfterConvertValidator(//
								FilePathValidator.create().fileMustExist()), null);

				new Label(c, SWT.NONE).setText("Estrategia de colapsado:");
				ComboViewer collapseStrategyCombo = new ComboViewer(c, SWT.BORDER | SWT.READ_ONLY);
				collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
				collapseStrategyCombo.setInput(//
						Arrays.asList(COLLAPSE_STRATEGY_LAST_ONE_IN_THE_FILE, COLLAPSE_STRATEGY_AVERAGE, COLLAPSE_STRATEGY_MAX, COLLAPSE_STRATEGY_MEDIA));

				IObservableValue widgetObservable = ViewersObservables.observeSingleSelection(collapseStrategyCombo);
				dbc.bindValue(widgetObservable, wm.collapseStrategy, //
						new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Estrategia de colapsado")), null);

				wm.collapseStrategy.setValue(COLLAPSE_STRATEGY_LAST_ONE_IN_THE_FILE);

				new Label(c, SWT.NONE).setText("cfl, cll, snl, efl");
				Text t = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wm.lines, new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {

					@Override
					public IStatus validate(Object value) {
						String l = value.toString();
						if (l.matches("\\d+,\\d+,\\d+,\\d+"))
							return ValidationStatus.ok();
						else
							return ValidationStatus.error("Formato no válido: ###,###,###,###");
					}
				}), null);
				t.setText("1,2,3,4");

				GridLayoutFactory.fillDefaults().numColumns(2).generateLayout(c);
				setControl(c);

			}
		});
	}
	// TODO hacer fluent!
	// WizardFactory.createWizard().addPage().addPage()
	// WizardPage.create(WizardPage.class).
}

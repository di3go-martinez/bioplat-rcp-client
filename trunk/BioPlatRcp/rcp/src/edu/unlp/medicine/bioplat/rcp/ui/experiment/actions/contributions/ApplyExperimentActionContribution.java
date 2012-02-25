package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.MultiPageBiomarkerEditor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ApplyExperimentsOnMetasignatureCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class ApplyExperimentActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		final AbstractWizard w = new AbstractWizard() {

			@Override
			public boolean performFinish() {
				try {
					List<Experiment> experiments = ((List<Experiment>) model().get(PagesDescriptors.SELECTED));
					for (Experiment experiment : experiments) {
						FromMemoryExperimentDescriptor d = new FromMemoryExperimentDescriptor(experiment);
						new ApplyExperimentsOnMetasignatureCommand(ApplyExperimentActionContribution.this.model(), Arrays.asList(new ValidationConfig(d, false, 1, "", "", null, 1, false))).execute();

						// FIXME hacer un poquito más generico con una
						// interface MultipageEditor#addPage(Editor, Input,
						// [title])...
						MultiPageBiomarkerEditor multipleEditor = (MultiPageBiomarkerEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

						final ExperimentAppliedToAMetasignature appliedExperiment = ApplyExperimentActionContribution.this.model().getExperimentsApplied().get(0);
						multipleEditor.addEditorPage(new ExperimentEditor(), EditorInputFactory.createDefaultEditorInput(appliedExperiment));

						// PlatformUIUtils.openEditor(appliedExperiment,
						// ExperimentEditor.id());
					}
					return true;
				} catch (Exception e) {
					// TODO hacer excepciones más específicas
					throw new RuntimeException(e);
				}
			}

			@Override
			protected WizardModel createWizardModel() {
				return new WizardModel();
			}

			@Override
			protected List<WizardPageDescriptor> createPagesDescriptors() {
				return Arrays.asList(PagesDescriptors.experimentsWPD());
			}
		};
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				w.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
				new WizardDialog(Display.getDefault().getActiveShell(), w).open();
			}
		});

	}
}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.List;
import java.util.Set;

import org.eclipse.ui.INewWizard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.utils.monitor.Monitor;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

public class ImportDatasetFromCloudWizard extends AbstractWizard<Set<Experiment>> implements INewWizard {

	public ImportDatasetFromCloudWizard() {
		skipFirstPageInitialization();
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		return Lists.<WizardPageDescriptor>newArrayList(new SearchDatasetsOnTheCloudPage());
	}

	@Override
	protected String getTaskName() {
		return "Fetching Dataset From Bioplat Cloud";
	}

	@Override
	public int getWizardWidth() {
		return 600;
	}

	@Override
	protected Set<Experiment> backgroundProcess(Monitor monitor) throws Exception {
		Set<Experiment> experiments = Sets.newHashSet();

		for (DatasetDTO dataset : findDataset())
			experiments.add(Experiments.factory.createExperiment(dataset));
		return experiments;
	}

	private Set<DatasetDTO> findDataset() {
		return wizardModel().value(SearchDatasetsOnTheCloudPage.SELECTED_DATASETS);
	}

	@Override
	protected void doInUI(Set<Experiment> result) throws Exception {
		for (Experiment e : result)
			PlatformUIUtils.openEditor(e, EditorsId.experimentEditorId());
	}

}

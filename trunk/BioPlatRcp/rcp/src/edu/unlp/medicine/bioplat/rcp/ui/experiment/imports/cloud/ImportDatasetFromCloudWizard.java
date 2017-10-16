package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.List;
import java.util.Set;

import org.eclipse.ui.INewWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls.Messages;
import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.utils.monitor.Monitor;
import zinbig.bioplatcloud.api.client.BioPlatCloudClient;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

public class ImportDatasetFromCloudWizard extends AbstractWizard<Set<Experiment>> implements INewWizard {

	private BioPlatCloudClient cloudclient;

	public ImportDatasetFromCloudWizard() {
		skipFirstPageInitialization();
		cloudclient = new BioPlatCloudClient(url());
	}

	// TODO agregar a ApplicationParametersHolder??
	private String url() {
		return System.getProperty("org.bioplat.cloud.url", "http://z-bioplat-cloud.herokuapp.com");
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		return Lists.<WizardPageDescriptor>newArrayList(new SearchDatasetsOnTheCloudPage(cloudclient));
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

		for (DatasetDTO dataset : findDatasets())
			experiments.add(Experiments.factory.createExperiment(dataset));
		return experiments;
	}

	private List<DatasetDTO> findDatasets() {
		List<DatasetDTO> headerdatasets = wizardModel().value(SearchDatasetsOnTheCloudPage.SELECTED_HEADERS_DATASETS);
		List<DatasetDTO> fullDatasets = Lists.newArrayList();
		for (DatasetDTO dataset : headerdatasets) {
			try {
				fullDatasets.add(cloudclient.findDatasetByName(dataset.getName()));
			} catch (Exception e) {
				logger.error("Ocurrió un error trayendo el dataset "+dataset.getName(), e);
				MessageManager.INSTANCE.add(Message.error("Could not retrieve the dataset "+dataset.getName()));
			}
		}
		return fullDatasets;

	}

	
	private static final Logger logger = LoggerFactory.getLogger(ImportDatasetFromCloudWizard.class);
	@Override
	protected void doInUI(Set<Experiment> result) throws Exception {
		for (Experiment e : result)
			PlatformUIUtils.openEditor(e, EditorsId.experimentEditorId());
	}

}

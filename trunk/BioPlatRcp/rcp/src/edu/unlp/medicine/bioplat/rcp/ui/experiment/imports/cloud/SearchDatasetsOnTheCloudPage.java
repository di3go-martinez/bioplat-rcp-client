package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import zinbig.bioplatcloud.api.client.BioPlatCloudClient;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

//FIXME usar mejor las capacidades del AbstractWizard! actualizar documentación en dropbox
public class SearchDatasetsOnTheCloudPage extends WizardPageDescriptor {

	private static final String FOUND_DATASETS = "FOUND_DATASETS";
	static final String SELECTED_DATASETS = "SELECTED_DATASETS";
	private SearchModel model;
	private Label label;

	public SearchDatasetsOnTheCloudPage() {
		super("Search datasets on the Cloud");
		this.model = new SearchModel();
		this.cloudclient = new BioPlatCloudClient(url());
	}

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite result = Widgets.createDefaultContainer(parent);
		input(wizardPage, result, "Name of Dataset to search: ", "key");
		input(wizardPage, result, "Tag to search: ", "tag");
		return result;
	}

	private void input(final WizardPage wizardPage, Composite result, String text, String property) {
		Widgets.createTextWithLabel(result, text, model, property);
		model.addPropertyChangeListener(property, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				fireUpdateButtons(wizardPage);
			}
		});
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return !Strings.isNullOrEmpty(this.model.getKey()) || !Strings.isNullOrEmpty(this.model.getTag());
	}

	@Override
	public boolean hasResultPage() {
		return true;
	}

	@Override
	public boolean isResultPageComplete(WizardModel wizardModel) {
		return wizardModel.value(SELECTED_DATASETS) != null;
	}

	@Override
	protected void createResultPage(Composite parent, final WizardModel wizardModel, IWizard wizard,
			final WizardPage resultPage) {

		Composite container = Widgets.createDefaultContainer(parent);

		label = new Label(container, SWT.BOLD);

		TableBuilder.create(container).addColumn(ColumnBuilder.create().property("name"))
				.input(Lists.newArrayList(findDatasets())).build();

	}

	@Override
	public void refreshResultPage(final WizardModel wizardModel, IWizard wizard) {
		Set<DatasetDTO> result = findDatasets();
		label.setText(result.size() + " dataset(s) found");
		wizardModel.set(SearchDatasetsOnTheCloudPage.FOUND_DATASETS, result);
	}

	private Set<DatasetDTO> findDatasets() {
		try {
			return Sets.union(cloudclient.findDatasetsHeadersForName(model.getKey()),
					cloudclient.findDatasetsHeadersForTag(model.getTag()));
		} catch (Exception e) {
			logger.error("Some error occurred searching the dataset '" + model.getKey() + "'", e);
			return Collections.emptySet();
		}
	}

	// TODO agregar a ApplicationParametersHolder??
	private String url() {
		return System.getProperty("org.bioplat.cloud.url", "http://z-bioplat-cloud.herokuapp.com");
	}

	private static final Logger logger = LoggerFactory.getLogger(SearchDatasetsOnTheCloudPage.class);
	private BioPlatCloudClient cloudclient;

}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import static com.google.common.collect.ImmutableList.copyOf;
import static edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import zinbig.bioplatcloud.api.client.BioPlatCloudClient;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

//FIXME usar mejor las capacidades del AbstractWizard! actualizar documentación en dropbox
public class SearchDatasetsOnTheCloudPage extends WizardPageDescriptor {

	private static final String FOUND_DATASETS = "FOUND_DATASETS";
	static final String SELECTED_HEADERS_DATASETS = "SELECTED_DATASETS";
	private SearchModel model;
	private Label label;

	public SearchDatasetsOnTheCloudPage(BioPlatCloudClient cloudclient) {
		super("Search datasets on the Cloud");
		this.model = new SearchModel();
		this.cloudclient = cloudclient;
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
		List<?> selectedDatasets = wizardModel.value(SELECTED_HEADERS_DATASETS);
		return selectedDatasets != null && !selectedDatasets.isEmpty();
	}

	@Override
	protected void createResultPage(Composite parent, final WizardModel wizardModel, IWizard wizard,
			final WizardPage resultPage) {

		Composite container = Widgets.createDefaultContainer(parent);

		label = new Label(container, SWT.BOLD);

		tref = tableBuilder(container)
				.addColumn(ColumnBuilder.create().property("name").title("Dataset"))
				.addColumn(ColumnBuilder.create().property("tags").title("Tags"))
				.addColumn(ColumnBuilder.create().property("samplesCount").title("Samples"))
				.input(copyOf((findHeaderDatasets()))).build();

		tref.addSelectionChangeListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				wizardModel.set(SELECTED_HEADERS_DATASETS, tref.selectedElements());
				fireUpdateButtons(resultPage);
			}
		});

	}

	@Override
	public void refreshResultPage(final WizardModel wizardModel, IWizard wizard) {
		Set<DatasetDTO> result = findHeaderDatasets();
		label.setText(result.size() + " dataset(s) found");
		wizardModel.set(SearchDatasetsOnTheCloudPage.FOUND_DATASETS, result);
		tref.input(copyOf(result));
	}

	// FIXME proponer cambio de tipo de datos a HeaderDatasetDTO porque así es
	// confuso para usarlo
	private Set<DatasetDTO> findHeaderDatasets() {
		try {
			return Sets.union(findByName(), findByTag());
		} catch (Exception e) {
			logger.error("Some error occurred searching the dataset '" + model.getKey() + "'", e);
			return Collections.emptySet();
		}
	}

	private Set<DatasetDTO> findByTag() throws Exception {
		return model.byTag() ? cloudclient.findDatasetsHeadersForTag(model.getTag())
				: Collections.<DatasetDTO>emptySet();
	}

	private Set<DatasetDTO> findByName() throws Exception {
		return model.byName() ? cloudclient.findDatasetsHeadersForName(model.getKey())
				: Collections.<DatasetDTO>emptySet();
	}

	private static final Logger logger = LoggerFactory.getLogger(SearchDatasetsOnTheCloudPage.class);
	private BioPlatCloudClient cloudclient;
	private TableReference tref;

}

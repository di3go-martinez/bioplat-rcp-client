package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.ImmutableList;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import zinbig.bioplatcloud.api.client.BioPlatCloudClient;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

/**
 * Por ahora no se va a usar así. definir y llegado el caso borrar
 */
@Deprecated
public class AllDataSetsOnTheCloud extends WizardPageDescriptor {

	public AllDataSetsOnTheCloud() {
		super("Listing all Datasets in the Cloud!");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite result = Widgets.createDefaultContainer(parent);
		createTable(result);
		return result;
	}

	private void createTable(Composite result) {
		TableBuilder.create(result).input(allDatasetsOnCloud()).addColumn(ColumnBuilder.create().property("name"))
				.build();
	}

	private List<DatasetDTO> allDatasetsOnCloud() {
		try {
			Set<DatasetDTO> datasets = new BioPlatCloudClient(url()).findDatasetsHeaders();
			return ImmutableList.<DatasetDTO>copyOf(datasets);
		} catch (Exception e) {
			throw new RuntimeException("No se encontraron datasets", e);
		}
	}

	// TODO agregar a ApplicationParametersHolder??
	private String url() {
		return System.getProperty("org.bioplat.cloud.url");
	}

}

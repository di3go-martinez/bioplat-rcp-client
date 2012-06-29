package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.providers.geneSigDB.fromSecondaryDBImprotedInBioplat.ProviderFromSecondaryDBImportedInBioplat;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.constants.Constants;

public class Providers2 extends Providers {

	private static final String GENESIGDB = "GENESIGDB";
	private static final String MSIGDB = "MSIGDB";

	/**
	 * Crea el grupo de proveedores de base de datos
	 * 
	 * @param container
	 * @param glf
	 * @param gdf
	 * @param wmodel
	 * @param dbc
	 */
	@Override
	protected void createSecondaryProvidersGroup(Composite container, GridLayoutFactory glf, GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {
		Group providersGroup = new Group(container, SWT.NONE);
		providersGroup.setText("External Gene Signatures Databases");
		providersGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		providersGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		Button radio;
		radio = new Button(providersGroup, SWT.CHECK);
		radio.setSelection(true);
		radio.setText("GeneSigDb");
		dbc.bindValue(SWTObservables.observeSelection(radio), wmodel.valueHolder(GENESIGDB));

		radio = new Button(providersGroup, SWT.CHECK);
		radio.setText("MSigDb");
		dbc.bindValue(SWTObservables.observeSelection(radio), wmodel.valueHolder(MSIGDB));

	}

	@Override
	public Providers addParameters(WizardModel wizardModel) {
		wizardModel.//
				add(GENESIGDB, new WritableValue(true, Boolean.class))//
				.add(MSIGDB, new WritableValue(true, Boolean.class));
		return this;

	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return isOpenedAvailable(model) || isAnySecondaryAvailable(model);
	}

	private boolean isAnySecondaryAvailable(WizardModel model) {
		return model.value(GENESIGDB) != null || model.value(MSIGDB) != null;
	}

	@Override
	public List<IGeneSignatureProvider> resolveProviders(WizardModel model) {

		List<IGeneSignatureProvider> result = Lists.newArrayList();
		resolveOpenedProvider(model, result);

		if (isAnySecondaryAvailable(model)) {
			ProviderFromSecondaryDBImportedInBioplat provider = new ProviderFromSecondaryDBImportedInBioplat();
			List<String> dbs = Lists.newArrayList();
			if (model.value(GENESIGDB))
				dbs.add(Constants.GENE_SIG_DB);
			if (model.value(MSIGDB))
				dbs.add(Constants.MOL_SIG_DB);
			provider.setExternalDatabaseNames(dbs);
			provider.setOrganism(model.value(Filters.ORGANISM).toString());

			result.add(provider);
		}

		return result;

	}
}

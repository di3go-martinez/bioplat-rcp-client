package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.io.File;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.DBImportes.geneSignature.msigDB.MSigDBProviderFromFile;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.ext.providers.geneSigDB.fromFile.generatedByMetaPlatXML.XMLLocalFileProvider;
import edu.unlp.medicine.domainLogic.ext.providers.geneSigDB.online.GeneSigDBProvider;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.exceptions.ProblemsGettingTheGeneSiganturesException;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

/**
 * 
 * Descritor de página de wizard para los proveedores de genes signatures
 * 
 * @author diego martínez
 * 
 */
public class Providers extends WizardPageDescriptor {

	static final String OPENED_BIOMARKERS = "OPENED_BIOMARKERS";

	@Deprecated
	static final String GENESIGDB_SAVE_FILE = "GENESIGDB_SAVE_FILE";
	@Deprecated
	static final String MSIGDB_PROVIDER_FILE = "MSIGDB_PROVIDER_FILE";
	@Deprecated
	static final String GENSIGDB_XML_FILE = "GENSIGDB_XML_FILE";
	@Deprecated
	static final String MAX_TIME_FOR_PROCESSING = "MAX_TIME_FOR_PROCESSING";
	@Deprecated
	static final String PUBLICATION_KEYWORDS = "GENSIGDB_SIGNATURES";

	public Providers() {
		super("Generate a metasignature (integration, filtering and computation of many gene signature)");
	}

	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		wp.setDescription("It integrates gene signatures from different databases and it applies algorithms for getting a potential gene list with prognostic/predictive value. You can then validate it using imported experiments.");
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		GridLayoutFactory glf = GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5);

		Composite container = Widgets.createDefaultContainer(parent, 1);

		createSecondaryProvidersGroup(container, glf, gdf, dbc, wmodel);

		final List<Biomarker> openedBiomarkers = PlatformUIUtils.loadedModels(Biomarker.class);
		if (!openedBiomarkers.isEmpty()) {
			Group openedBiomarker = new Group(container, SWT.NONE);
			openedBiomarker.setLayout(glf.create());
			openedBiomarker.setLayoutData(gdf.create());
			openedBiomarker.setText("Editing Gene Signatures");
			final TableReference tr = TableBuilder.create(openedBiomarker).addColumn(ColumnBuilder.create().property("name")).input(openedBiomarkers).build();
			tr.addSelectionChangeListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					wmodel.set(OPENED_BIOMARKERS, tr.selectedElements());
					wp.setPageComplete(isPageComplete(wmodel));
				}
			});
		}

		return container;
	}

	protected void createSecondaryProvidersGroup(Composite container, GridLayoutFactory glf, GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {

		Group xmlsigdbgroup = new Group(container, SWT.NONE);
		xmlsigdbgroup.setLayout(glf.create());
		xmlsigdbgroup.setLayoutData(gdf.create());
		xmlsigdbgroup.setText("GenSigDB Signatures, XML generated using the GenSigDb online provider");
		FileText.create(xmlsigdbgroup).bind(dbc, wmodel.valueHolder(GENSIGDB_XML_FILE));

		Group msigdbgroup = new Group(container, SWT.NONE);
		msigdbgroup.setLayout(glf.create());
		msigdbgroup.setLayoutData(gdf.create());
		msigdbgroup.setText("MSigDB Provider, from downloaded file");
		FileText.create(msigdbgroup).bind(dbc, wmodel.valueHolder(MSIGDB_PROVIDER_FILE));

		Group onlineSigdbgroup = new Group(container, SWT.NONE);
		onlineSigdbgroup.setLayout(glf.create());
		onlineSigdbgroup.setLayoutData(gdf.create());
		onlineSigdbgroup.setText("GenSigDB signatures (online access)");
		Text t = createTextHolderWithLabel(onlineSigdbgroup, "Publication keyword");
		bind(dbc, wmodel, t, PUBLICATION_KEYWORDS);
		t = createTextHolderWithLabel(onlineSigdbgroup, "Limit time for processing (in minutes)");
		bind(dbc, wmodel, t, MAX_TIME_FOR_PROCESSING);
		FileText.create(onlineSigdbgroup).bind(dbc, wmodel.valueHolder(GENESIGDB_SAVE_FILE));

	}

	private void bind(DataBindingContext dbc, WizardModel wmodel, Text t, String key) {
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(key));
	}

	private Text createTextHolderWithLabel(Composite c, String label) {
		new Label(c, SWT.NONE).setText(label);
		Text t = new Text(c, SWT.BORDER);
		t.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		return t;
	}

	public Providers addParameters(WizardModel wizardModel) {
		wizardModel//
				.add(PUBLICATION_KEYWORDS)//
				.add(MAX_TIME_FOR_PROCESSING, new WritableValue(0, Integer.class))//
				.add(GENESIGDB_SAVE_FILE)//
				.add(GENESIGDB_SAVE_FILE)//
				.add(GENSIGDB_XML_FILE)//
				.add(MSIGDB_PROVIDER_FILE);

		return this;
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		boolean g0, g1, g2, g3;
		g0 = g1 = g2 = g3 = false;
		g0 = isOpenedAvailable(model);
		g1 = isgensigAvailable(model);
		g2 = isgensigdbxmlAvailable(model);
		g3 = ismsigfbAvailable(model);
		return g0 || g1 || g2 || g3;
	}

	protected boolean isOpenedAvailable(WizardModel model) {
		List<?> l = model.value(OPENED_BIOMARKERS);
		return l != null && !l.isEmpty();
	}

	private boolean ismsigfbAvailable(WizardModel model) {
		boolean g3;
		String value;
		value = model.value(MSIGDB_PROVIDER_FILE);
		g3 = value != null && new File(value).exists();
		return g3;
	}

	private boolean isgensigdbxmlAvailable(WizardModel model) {
		boolean g2;
		String value;
		value = model.value(GENSIGDB_XML_FILE);
		g2 = value != null && new File(value).exists();
		return g2;
	}

	private boolean isgensigAvailable(WizardModel model) {
		boolean g1;
		String value = model.value(PUBLICATION_KEYWORDS);
		g1 = value != null && !value.isEmpty();
		return g1;
	}

	public List<IGeneSignatureProvider> resolveProviders(WizardModel model) {
		List<IGeneSignatureProvider> result = Lists.newArrayList();

		resolveOpenedProvider(model, result);

		// genesigdb online provider
		if (isgensigAvailable(model)) {
			GeneSigDBProvider provider = new GeneSigDBProvider();
			provider.setKey((String) model.value(PUBLICATION_KEYWORDS));
			provider.setMinutes((Integer) model.value(MAX_TIME_FOR_PROCESSING));

			String saveToFile = model.value(GENESIGDB_SAVE_FILE);
			if (saveToFile != null && !saveToFile.trim().isEmpty()) {
				provider.setXMLfilePathToSaveTheSignatures(saveToFile);
			}
			result.add(provider);
		}

		// genesig db our generated xml provider
		if (isgensigdbxmlAvailable(model)) {
			XMLLocalFileProvider provider = new XMLLocalFileProvider((String) model.value(GENSIGDB_XML_FILE));
			result.add(provider);
		}

		// genesig db our generated xml provider
		if (ismsigfbAvailable(model)) {
			MSigDBProviderFromFile provider = new MSigDBProviderFromFile();
			provider.setInputFileName((String) model.value(MSIGDB_PROVIDER_FILE));
			result.add(provider);
		}
		return result;
	}

	protected void resolveOpenedProvider(WizardModel model, List<IGeneSignatureProvider> result) {
		// opened Editors Provider
		// FIXME un biomarcador no es un GeneSignature
		final List<Biomarker> l = model.value(OPENED_BIOMARKERS);
		if (l != null && !l.isEmpty()) {
			result.add(new IGeneSignatureProvider() {

				@Override
				public List<GeneSignature> getGeneSignatures() throws ProblemsGettingTheGeneSiganturesException {
					List<GeneSignature> gs = Lists.newArrayList();
					for (Biomarker b : l)
						gs.add(new GeneSignature(b));
					return gs;
				}

				@Override
				public List<GeneSignature> getGeneSignatures(
						List<String> keywordOnName, List<String> signatureIdsOrNames,
						List<String> geneNameOrEntrez)
						throws ProblemsGettingTheGeneSiganturesException {
					// TODO Auto-generated method stub
					return this.getGeneSignatures();
				}

				@Override
				public String getFriendlyDescription() {
					return "Editing Gene Signatures";
				}
			});
		}
	}
}

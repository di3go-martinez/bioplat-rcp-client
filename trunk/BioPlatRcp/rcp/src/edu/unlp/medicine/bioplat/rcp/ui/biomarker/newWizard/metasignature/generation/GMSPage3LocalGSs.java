package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.exceptions.ProblemsGettingTheGeneSiganturesException;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class GMSPage3LocalGSs extends WizardPageDescriptor {

	
	static final String OPENED_SELECTED_BIOMARKERS = "OPENED_SELECTED_BIOMARKERS";
	
	private static Logger logger = LoggerFactory.getLogger(GMSPage3LocalGSs.class);

private TableReference tref;

	public GMSPage3LocalGSs() {
		super("Filter");
		//this.provider = pagedescriptor;
	}


	private WizardModel wizardModel;


	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		wp.setDescription("Would you like to add Gene Signatures from your Bioplat Desktop?");
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(30, 30).spacing(20, 20).create());
		
		createLocalBiomarkersSelector(wp, container, gdf, dbc, wmodel);
		
		return container;
	}

	
		private void createLocalBiomarkersSelector(final WizardPage wp, Composite container, GridDataFactory gdf, DataBindingContext dbc, final WizardModel wmodel) {
		final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);
		if (!openedBiomarkers.isEmpty()) {
			Group openedBiomarker = new Group(container, SWT.NONE);
			openedBiomarker.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).create());
			openedBiomarker.setLayoutData(gdf.create());
			openedBiomarker.setText("Gene Signatures in your Bioplat desktop");
			final TableReference tr = TableBuilder.create(openedBiomarker).input(openedBiomarkers)
			.addColumn(ColumnBuilder.create().property("name").title("biomarker name"))
			.addColumn(ColumnBuilder.create().property("numberOfGenes").title("Number of genes"))
			.build();
			tr.addSelectionChangeListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					wmodel.set(OPENED_SELECTED_BIOMARKERS, tr.selectedElements());
					wp.setPageComplete(isPageComplete(wmodel));
				}
			});
		}

		
	}

//	public GMSPage4LocalGSs addParameters(WizardModel wizardModel) {
//		wizardModel.add(GMSPage4LocalGSs.ORGANISM)//
//				.add(KEYWORD_ON_NAME, new WritableValue("", String.class))//
//				.add(SIGNATURES_ID_OF_NAMES)//
//				.add(GENE_NAME_OR_ENTREZ)
//				.add(GENESIGDB, new WritableValue(true, Boolean.class))//
//				.add(MSIGDB, new WritableValue(true, Boolean.class));
//
//
//		return this;
//	}

	@Override
	public boolean hasResultPage() {
		return false;
	}

	
	
//	@Override
//	protected void createResultPage(Composite parent, final WizardModel wizardModel, IWizard wizard, final WizardPage resultPage) {
//
//		this.wizardModel = wizardModel;
//		this.resultPage = resultPage;
//		try {
//			final Holder<List<GeneSignature>> holder = recalculateHolder(wizardModel, wizard);
//
//			Composite c = Widgets.createDefaultContainer(parent, 1);
//
//			// FIXME los newss
//			tref = TableBuilder.create(c).input(Lists.newArrayList(new HashSet(holder.value())))//
//					.addColumn(ColumnBuilder.create().property("name").title("Name"))//
//					.addColumn(ColumnBuilder.create().property("geneCount").title("Genes"))//
//					.addColumn(ColumnBuilder.create().property("author").title("Author"))//
//					// .noPaging()
//					.build();
//
//			tref.addSelectionChangeListener(new ISelectionChangedListener() {
//
//				@Override
//				public void selectionChanged(SelectionChangedEvent event) {
//					if (!includeAll)
//						wizardModel.set(SELECTED_SIGNATURES, tref.selectedElements());
//					else
//						selection = tref.selectedElements();
//					resultPage.setPageComplete(isResultPageComplete(wizardModel));
//				}
//			});
//
//			tref.breakPaging();
//
//			createFilter(c);
//
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}


//	private boolean includeAll = false;
//
//	private Holder<List<GeneSignature>> recalculateHolder(final WizardModel wizardModel, IWizard wizard) throws InvocationTargetException, InterruptedException {
//		final List<GeneSignature> value = Collections.emptyList();
//		final Holder<List<GeneSignature>> holder = Holder.create(value);
//		final SingleMetasignatureGenerator smg = createGenerator(wizardModel);
//
//		final String geneNameOrEntrez = wizardModel.value(GENE_NAME_OR_ENTREZ);
//		final String signatures_id_or_names = wizardModel.value(SIGNATURES_ID_OF_NAMES);
//		final String keyword = wizardModel.value(KEYWORD_ON_NAME);
//
//		wizard.getContainer().run(true, false, new IRunnableWithProgress() {
//
//			@Override
//			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//				monitor.beginTask("Calculating...", IProgressMonitor.UNKNOWN);
//
//				holder.hold(smg/* .monitor(monitor) *///
//						.getSignaturesFromProvidersAndFilterThem(keyword, //
//								toList(signatures_id_or_names), //
//								toList(geneNameOrEntrez)));
//				monitor.done();
//			}
//
//			/**
//			 * 
//			 * @param value
//			 *            es un string de "claves" separadas por coma
//			 * @return
//			 */
//			private List<String> toList(String value) {
//				if (value == null)
//					return Collections.emptyList();
//				else
//					return Lists.newArrayList(StringUtils.split(value, ","));
//			}
//		});
//		elements = holder.value();
//		return holder;
//	}

//	private SingleMetasignatureGenerator createGenerator(WizardModel wizardModel) {
//		SingleMetasignatureGenerator smg = new SingleMetasignatureGenerator();

//		for (IGeneSignatureProvider gsp : this.resolveProviders(wizardModel))
//			smg.addGeneSigntaureProvider(gsp);


		
				
//		for (IGeneSignatureProvider gsp : lista)
//			smg.addGeneSigntaureProvider(gsp);

				
		//TODO habilitar filtro
		//smg.setGeneSignatureFilters(filterResolver(wizardModel));

//		return smg;
//	}

//	@Override
//	public void refreshResultPage(WizardModel wizardModel, IWizard wizard) {
//		if (initializatePhase)
//			return;
//		// TODO re input si se cambio algo, no va a recalcular lo mismo...
//		try {
//			//elements = recalculateHolder(wizardModel, wizard).value();
//			//tref.input(elements);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	public boolean isResultPageComplete(WizardModel model) {
//		if (tref == null)
//			return false;
//
//		if (includeAll) {
//			model.set(SELECTED_SIGNATURES, elements);
//			return !elements.isEmpty();
//		} else {
//			final List<?> selectedElements = tref.selectedElements();
//			model.set(SELECTED_SIGNATURES, selectedElements);
//			return !selectedElements.isEmpty();
//		}
//
//	}

//	private List<StringFilter> filterResolver(WizardModel wizardmodel) {
//		String organism = wizardModel.value(ORGANISM);
//		StringFilter sf = new StringFilter("getOrganism", organism);
//		return Lists.newArrayList(sf);
//	}
//
//	
	
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
	}
	
	
//	public List<IGeneSignatureProvider> resolveProviders(WizardModel model) {
//
//		List<IGeneSignatureProvider> result = Lists.newArrayList();
//		resolveOpenedProvider(model, result);
//		
//		
//		
////		if (isAnySecondaryAvailable(model)) {
////			ProviderFromSecondaryDBImportedInBioplat provider = new ProviderFromSecondaryDBImportedInBioplat();
////			List<String> dbs = Lists.newArrayList();
////			if (model.value(GENESIGDB))
////				dbs.add(Constants.GENE_SIG_DB);
////			if (model.value(MSIGDB))
////				dbs.add(Constants.MOL_SIG_DB);
////			provider.setExternalDatabaseNames(dbs);
////			provider.setOrganism(model.value(GMSPage3LocalGSs.ORGANISM).toString());
////
////			result.add(provider);
////		}
//
//		return result;
//
//	}	
	
	
	protected void resolveOpenedProvider(WizardModel model, List<IGeneSignatureProvider> result) {
		// opened Editors Provider
		// FIXME un biomarcador no es un GeneSignature
		final List<Biomarker> l = model.value(OPENED_SELECTED_BIOMARKERS);
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
				public String getFriendlyDescription() {
					return "Editing Gene Signatures";
				}
			});
		}
	}

	protected boolean isOpenedAvailable(WizardModel model) {
		List<?> l = model.value(OPENED_SELECTED_BIOMARKERS);
		return l != null && !l.isEmpty();
	}
	
	
	
	
	
}

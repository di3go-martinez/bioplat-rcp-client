package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.ext.providers.geneSigDB.fromSecondaryDBImprotedInBioplat.ProviderFromSecondaryDBImportedInBioplat;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.constants.Constants;
import edu.unlp.medicine.domainLogic.framework.exceptions.ProblemsGettingTheGeneSiganturesException;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.SingleMetasignatureGenerator;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.StringFilter;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class GMSPage2FIlterExternalDBs extends WizardPageDescriptor {

	
	private static final String GENESIGDB = "GENESIGDB";
	private static final String MSIGDB = "MSIGDB";
	
	private static final String GENE_NAME_OR_ENTREZ = "GENE_NAME_OR_ENTREZ";

	private static final String SIGNATURES_ID_OF_NAMES = "SIGNATURES_ID_OF_NAMES";

	private static final String KEYWORD_ON_NAME = "KEYWORD_ON_NAME";

	public static final String ORGANISM = "ORGANISM";
	
	static final String SELECTED_EXTERNAL_SIGNATURES = "EXTERNAL_SELECTED_SIGNATURES";

	private static Logger logger = LoggerFactory.getLogger(GMSPage2FIlterExternalDBs.class);

	
	private TableReference tref;
	int resultSize;
	
	//////////////////////////INITIALIZATION OF THE PAGE. It is executed when the wizard is started.////////////////////////////////
	public GMSPage2FIlterExternalDBs(WizardModel wizardModel) {
		super("External Gene Signature databases");
		addParameters(wizardModel);
	}
	
	

	public GMSPage2FIlterExternalDBs addParameters(WizardModel wizardModel) {
		wizardModel.add(GMSPage2FIlterExternalDBs.ORGANISM)//
				.add(KEYWORD_ON_NAME, new WritableValue("", String.class))//
				.add(SIGNATURES_ID_OF_NAMES)//
				.add(GENE_NAME_OR_ENTREZ)
				.add(GENESIGDB, new WritableValue(true, Boolean.class))//
				.add(MSIGDB, new WritableValue(true, Boolean.class));


		return this;
	}
	

	private List<GeneSignature> selection;

	private List<GeneSignature> elements;

	private WizardModel wizardModel;

	private WizardPage resultPage;



	
	////////////////////////PAGE UI.////////////////////////////////
	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		wp.setDescription("You can import Gene Signatures from GeneSigDB database and MolSigDB database. Use the filter to bring only Geene Signatures of interest.");
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(30, 100).spacing(20, 20).create());
		
		createCheckBoxOfGeneSignatureDBs(container,  gdf, dbc, wmodel);
		createFilterGroup(container, gdf, dbc, wmodel);
		
		return container;
	}

	
	private void createFilterGroup(Composite container, 
			GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {

		Group filterGroup = new Group(container, SWT.NONE);
		filterGroup.setText("Gene signature filters");
		filterGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		filterGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		ComboViewer cv = Utils.newComboViewer(filterGroup, "Organism", Arrays.asList("Human", "Mouse", "ALL"));
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(ORGANISM), UpdateStrategies.nonNull(ORGANISM), UpdateStrategies.nullStrategy());
		cv.setSelection(new StructuredSelection("Human"));
		cv.getCombo().setLayoutData(gdf.create());

		new Label(filterGroup, SWT.NONE).setText("Cancer Location/Keyword Name");
		Text t = new Text(filterGroup, SWT.BORDER);
		t.setLayoutData(gdf.create());
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(KEYWORD_ON_NAME));

		new Label(filterGroup, SWT.NONE).setText("Having The following genes (Gene name or Entrez (separated by commas))");
		t = new Text(filterGroup, SWT.BORDER);
		t.setToolTipText("keywords by comma separated");
		t.setLayoutData(gdf.minSize(SWT.DEFAULT, 100).create());
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(GENE_NAME_OR_ENTREZ));

		new Label(filterGroup, SWT.NONE).setText("Signature Id or Name (separated by commas)");
		t = new Text(filterGroup, SWT.BORDER);
		t.setLayoutData(gdf.minSize(SWT.DEFAULT, 100).create());
		t.setToolTipText("keywords by comma separated");
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(SIGNATURES_ID_OF_NAMES));


		
	}

	protected void createCheckBoxOfGeneSignatureDBs(Composite container, GridDataFactory gdf, DataBindingContext dbc, WizardModel wmodel) {

		//new CLabel(container, SWT.BOLD).setText("\nThe configuration you have to do for getting a metasignatures is: \n1-(This wizzard page): Select the providers. You can pick up GeneSigDB, MolSigDB and any of the biomarkers you have previously opened (if there is someone) \n2-(Next wizzard Page): Select the filters. You can filter by cancer location, or giving a gene list which have to be in the signature. \n3-(Last wizzard page): Pick up the algorithm responsible of getting all the gene signature which have passed the filter (step 2) and applying its logic for get the Metasignature                                              \n\n");

		Group providersGroup = new Group(container, SWT.NONE);
		providersGroup.setText("External Gene Signatures Databases");
		providersGroup.setLayout(GridLayoutFactory.fillDefaults().margins(20, 20).create());
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
	public boolean isPageComplete(WizardModel model) {
		
		return true;
	}
	
	
	private boolean isAnySecondaryAvailable(WizardModel model) {
		return model.value(GENESIGDB) != null || model.value(MSIGDB) != null;
	}
	
	
	
		

	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	///////////////////////////////RESULTA PAGE///////////////////////
	
	private boolean includeAll = false;
	
	// FIXME FIXME parche de entrega!!
	private boolean initializatePhase = false;	
	@Override
	public void initializeResultPage(Composite parent, WizardModel wizardModel, IWizard wizard, WizardPage resultPage) {
		initializatePhase = true;
		super.initializeResultPage(parent, wizardModel, wizard, resultPage);
		initializatePhase = false;
	}

	
	@Override
	public boolean hasResultPage() {
		return true;
	}

	@Override
	protected void createResultPage(Composite parent, final WizardModel wizardModel, IWizard wizard, final WizardPage resultPage) {

		this.wizardModel = wizardModel;
		this.resultPage = resultPage;
		this.resultPage.setTitle("Select the input Gene Signatures");
		
		try {
			final Holder<List<GeneSignature>> holder = getGeneSignaturesPassedTheFilters(wizardModel, wizard);
			resultSize = holder.value().size();
			
			
			this.resultPage.setDescription(resultSize + " Gene signatures found. Please select the ones you want to take as input for doing the Metasignature generation.");
			
			Composite c = Widgets.createDefaultContainer(parent, 1);

			// FIXME los newss
			tref = TableBuilder.create(c).input(Lists.newArrayList(new HashSet(holder.value())))//
					.addColumn(ColumnBuilder.create().property("name").title("Name").width(400))//
					.addColumn(ColumnBuilder.create().property("geneCount").title("Genes"))//
					//.addColumn(ColumnBuilder.create().property("author").title("Author"))//
					// .noPaging()
					.build();

			tref.addSelectionChangeListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (!includeAll)
						wizardModel.set(SELECTED_EXTERNAL_SIGNATURES, tref.selectedElements());
					else
						selection = tref.selectedElements();
					resultPage.setPageComplete(isResultPageComplete(wizardModel));
				}
			});

			tref.breakPaging();

			createFilter(c);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private void createFilter(Composite c) {
		// Group ec = new Group(c, SWT.NONE);
		// ec.setText("Filters");
		// ec.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());
		// ec.setLayout(GridLayoutFactory.fillDefaults().create());

		final Button b = new Button(c, SWT.CHECK);
		b.setText("Include All (" + resultSize + " gene Signatures)");

		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				includeAll = b.getSelection();
				if (includeAll) {
					selection = tref.selectedElements();
					wizardModel.set(SELECTED_EXTERNAL_SIGNATURES, elements);
				} else
					wizardModel.set(SELECTED_EXTERNAL_SIGNATURES, selection);
				resultPage.setPageComplete(isResultPageComplete(wizardModel));
			}
		});
		b.setSelection(includeAll);

	}


	private Holder<List<GeneSignature>> getGeneSignaturesPassedTheFilters(final WizardModel wizardModel, IWizard wizard) throws InvocationTargetException, InterruptedException {
		final List<GeneSignature> value = Collections.emptyList();
		final Holder<List<GeneSignature>> holder = Holder.create(value);
		final SingleMetasignatureGenerator smg = createGenerator(wizardModel);

		final String geneNameOrEntrez = wizardModel.value(GENE_NAME_OR_ENTREZ);
		final String signatures_id_or_names = wizardModel.value(SIGNATURES_ID_OF_NAMES);
		final String keyword = wizardModel.value(KEYWORD_ON_NAME);

		wizard.getContainer().run(true, false, new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Querying Gene Signature Databases...", IProgressMonitor.UNKNOWN);

				holder.hold(smg/* .monitor(monitor) *///
						.getSignaturesFromProvidersAndFilterThem(keyword, //
								toList(signatures_id_or_names), //
								toList(geneNameOrEntrez)));
				monitor.done();
			}

			/**
			 * 
			 * @param value
			 *            es un string de "claves" separadas por coma
			 * @return
			 */
			private List<String> toList(String value) {
				if (value == null)
					return Collections.emptyList();
				else
					return Lists.newArrayList(StringUtils.split(value, ","));
			}
		});
		elements = holder.value();
		return holder;
	}


	private SingleMetasignatureGenerator createGenerator(WizardModel wizardModel) {
		SingleMetasignatureGenerator smg = new SingleMetasignatureGenerator();

		for (IGeneSignatureProvider gsp : this.resolveProviders(wizardModel))
			smg.addGeneSigntaureProvider(gsp);

		//TODO habilitar filtro
		//smg.setGeneSignatureFilters(filterResolver(wizardModel));

		return smg;
	}


	@Override
	public void refreshResultPage(WizardModel wizardModel, IWizard wizard) {
		if (initializatePhase)
			return;
		// TODO re input si se cambio algo, no va a recalcular lo mismo...
		try {
			elements = getGeneSignaturesPassedTheFilters(wizardModel, wizard).value();
			tref.input(elements);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isResultPageComplete(WizardModel model) {
		//return true;
		if (tref == null)
			return false;

		if (PlatformUIUtils.openedEditors(Biomarker.class).isEmpty()){
		
		if (includeAll) {
			//model.set(SELECTED_SIGNATURES, elements);
			return !elements.isEmpty();
		} else {
			final List<?> selectedElements = tref.selectedElements();
			//model.set(SELECTED_SIGNATURES, selectedElements);
			return !selectedElements.isEmpty();
		}
		}
		return true;

	}

	
	public List<IGeneSignatureProvider> resolveProviders(WizardModel model) {

		List<IGeneSignatureProvider> result = Lists.newArrayList();
		

		if (isAnySecondaryAvailable(model)) {
			ProviderFromSecondaryDBImportedInBioplat provider = new ProviderFromSecondaryDBImportedInBioplat();
			List<String> dbs = Lists.newArrayList();
			if (model.value(GENESIGDB))
				dbs.add(Constants.GENE_SIG_DB);
			if (model.value(MSIGDB))
				dbs.add(Constants.MOL_SIG_DB);
			provider.setExternalDatabaseNames(dbs);
			provider.setOrganism(model.value(GMSPage2FIlterExternalDBs.ORGANISM).toString());

			result.add(provider);
		}

		return result;

	}	
	
	
	

	
}

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
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.SingleMetasignatureGenerator;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.StringFilter;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class Filters extends WizardPageDescriptor {

	private static final String GENE_NAME_OR_ENTREZ = "GENE_NAME_OR_ENTREZ";

	private static final String SIGNATURES_ID_OF_NAMES = "SIGNATURES_ID_OF_NAMES";

	private static final String KEYWORD_ON_NAME = "KEYWORD_ON_NAME";

	static final String SELECTED_SIGNATURES = "SELECTED_SIGNATURES";

	private static Logger logger = LoggerFactory.getLogger(Filters.class);

	public static final String ORGANISM = "ORGANISM";
	private TableReference tref;

	public Filters(Providers pagedescriptor) {
		super("Filter");
		this.provider = pagedescriptor;
	}

	// TODO no iría esto aca.... DESACOPLAR...
	private Providers provider;

	// mantiene la selección actual cuando el filtro de incluir todos está
	// activo, esto es para que cuando se desactive se vuelva a la selcción que
	// estaba
	private List<GeneSignature> selection;

	private List<GeneSignature> elements;

	private WizardModel wizardModel;

	private WizardPage resultPage;

	// FIXME FIXME parche de entrega!!
	private boolean initializatePhase = false;

	@Override
	public void initializeResultPage(Composite parent, WizardModel wizardModel, IWizard wizard, WizardPage resultPage) {
		initializatePhase = true;
		super.initializeResultPage(parent, wizardModel, wizard, resultPage);
		initializatePhase = false;
	}

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {

		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

		ComboViewer cv = Utils.newComboViewer(container, "Organism", Arrays.asList("Human", "Mouse", "ALL"));
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(ORGANISM), UpdateStrategies.nonNull(ORGANISM), UpdateStrategies.nullStrategy());
		cv.setSelection(new StructuredSelection("Human"));
		cv.getCombo().setLayoutData(gdf.create());

		new Label(container, SWT.NONE).setText("Cancer Location/Keyword Name");
		Text t = new Text(container, SWT.BORDER);
		t.setLayoutData(gdf.create());
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(KEYWORD_ON_NAME));

		new Label(container, SWT.NONE).setText("Having The Following (Genes name or Entrez, separated by commas)");
		t = new Text(container, SWT.BORDER);
		t.setToolTipText("keywords by comma separated");
		t.setLayoutData(gdf.minSize(SWT.DEFAULT, 100).create());
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(GENE_NAME_OR_ENTREZ));

		new Label(container, SWT.NONE).setText("Signatures Id of Names (separated by commas)");
		t = new Text(container, SWT.BORDER);
		t.setLayoutData(gdf.minSize(SWT.DEFAULT, 100).create());
		t.setToolTipText("keywords by comma separated");
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(SIGNATURES_ID_OF_NAMES));

		return container;
	}

	private Filters addParameters(WizardModel wizardModel) {
		wizardModel.add(Filters.ORGANISM)//
				.add(KEYWORD_ON_NAME, new WritableValue("", String.class))//
				.add(SIGNATURES_ID_OF_NAMES)//
				.add(GENE_NAME_OR_ENTREZ);

		return this;
	}

	@Override
	public boolean hasResultPage() {
		return true;
	}

	@Override
	protected void createResultPage(Composite parent, final WizardModel wizardModel, IWizard wizard, final WizardPage resultPage) {

		this.wizardModel = wizardModel;
		this.resultPage = resultPage;
		try {
			final Holder<List<GeneSignature>> holder = recalculateHolder(wizardModel, wizard);

			Composite c = Widgets.createDefaultContainer(parent, 1);

			// FIXME los newss
			tref = TableBuilder.create(c).input(Lists.newArrayList(new HashSet(holder.value())))//
					.addColumn(ColumnBuilder.create().property("name").title("Name"))//
					.addColumn(ColumnBuilder.create().property("geneCount").title("Genes"))//
					.addColumn(ColumnBuilder.create().property("author").title("Author"))//
					// .noPaging()
					.build();

			tref.addSelectionChangeListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (!includeAll)
						wizardModel.set(SELECTED_SIGNATURES, tref.selectedElements());
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
		b.setText("Include All");

		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				includeAll = b.getSelection();
				if (includeAll) {
					selection = tref.selectedElements();
					wizardModel.set(SELECTED_SIGNATURES, elements);
				} else
					wizardModel.set(SELECTED_SIGNATURES, selection);
				resultPage.setPageComplete(isResultPageComplete(wizardModel));
			}
		});
		b.setSelection(includeAll);

	}

	private boolean includeAll = false;

	private Holder<List<GeneSignature>> recalculateHolder(final WizardModel wizardModel, IWizard wizard) throws InvocationTargetException, InterruptedException {
		final List<GeneSignature> value = Collections.emptyList();
		final Holder<List<GeneSignature>> holder = Holder.create(value);
		final SingleMetasignatureGenerator smg = createGenerator(wizardModel);

		final String geneNameOrEntrez = wizardModel.value(GENE_NAME_OR_ENTREZ);
		final String signatures_id_or_names = wizardModel.value(SIGNATURES_ID_OF_NAMES);
		final String keyword = wizardModel.value(KEYWORD_ON_NAME);

		wizard.getContainer().run(true, false, new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Calculating...", IProgressMonitor.UNKNOWN);

				holder.hold(smg/* .monitor(monitor) *///
						.getSignaturesFromProvidersAndFilterThem(toList(keyword), //
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

		for (IGeneSignatureProvider gsp : provider.resolveProviders(wizardModel))
			smg.addGeneSigntaureProvider(gsp);

		// TODO habilitar filtro
		// smg.setGeneSignatureFilters(filterResolver(wizardModel));

		return smg;
	}

	@Override
	public void refreshResultPage(WizardModel wizardModel, IWizard wizard) {
		if (initializatePhase)
			return;
		// TODO re input si se cambio algo, no va a recalcular lo mismo...
		try {
			elements = recalculateHolder(wizardModel, wizard).value();
			tref.input(elements);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isResultPageComplete(WizardModel model) {
		if (tref == null)
			return false;

		if (includeAll) {
			model.set(SELECTED_SIGNATURES, elements);
			return !elements.isEmpty();
		} else {
			final List<?> selectedElements = tref.selectedElements();
			model.set(SELECTED_SIGNATURES, selectedElements);
			return !selectedElements.isEmpty();
		}

	}

	private List<StringFilter> filterResolver(WizardModel wizardmodel) {
		String organism = wizardModel.value(ORGANISM);
		StringFilter sf = new StringFilter("getOrganism", organism);
		return Lists.newArrayList(sf);
	}

}

package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class Filters extends WizardPageDescriptor {

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

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite c = new Composite(parent, SWT.NONE);
		// new Label(c, SWT.NONE).setText("Organismo:");
		// ComboViewer collapseStrategyCombo = new ComboViewer(c, SWT.BORDER |
		// SWT.READ_ONLY);
		// collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
		// collapseStrategyCombo.setInput(//
		// Arrays.asList("Human", "Mouse", "ALL"));

		ComboViewer cv = Utils.newComboViewer(c, "Organism:", Arrays.asList("Human", "Mouse", "ALL"));
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(ORGANISM), UpdateStrategies.nonNull(ORGANISM), UpdateStrategies.nullStrategy());
		cv.setSelection(new StructuredSelection("Human"));

		// IObservableValue widgetObservable =
		// ViewersObservables.observeSingleSelection(collapseStrategyCombo);
		// dbc.bindValue(widgetObservable, wmodel.get(ORGANISM), new
		// UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Organismo")),
		// null);
		return c;
	}

	public WizardPageDescriptor addParameters(WizardModel wizardModel) {
		wizardModel.add(Filters.ORGANISM);
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

			tref = TableBuilder.create(c).input(holder.value())//
					.addColumn(ColumnBuilder.create().property("name").title("Name"))//
					.addColumn(ColumnBuilder.create().property("geneCount").title("Gene Count"))//
					.addColumn(ColumnBuilder.create().property("author").title("Author"))//
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
		wizard.getContainer().run(true, false, new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Calculating...", IProgressMonitor.UNKNOWN);
				holder.hold(smg/* .monitor(monitor) */.calculateFilteredSignatures());
				monitor.done();
			}
		});
		return holder;
	}

	private SingleMetasignatureGenerator createGenerator(WizardModel wizardModel) {
		SingleMetasignatureGenerator smg = new SingleMetasignatureGenerator();

		for (IGeneSignatureProvider gsp : provider.resolveProviders(wizardModel))
			smg.addGeneSigntaureProvider(gsp);

		// TODO filter

		return smg;
	}

	@Override
	public void refreshResultPage(WizardModel wizardModel, IWizard wizard) {
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

}

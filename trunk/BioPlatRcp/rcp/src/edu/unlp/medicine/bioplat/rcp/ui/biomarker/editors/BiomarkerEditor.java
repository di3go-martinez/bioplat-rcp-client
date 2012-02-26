package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.AbstractSelectionService;

import com.google.common.collect.ImmutableMap;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls.Messages;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationTextEvent;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditor extends AbstractEditorPart<Biomarker> implements ISelectionChangedListener {

	public static String id() {
		return "bio.plat.biomarker.editor"; //$NON-NLS-1$
	}

	private boolean autoUpdateTitle = true;

	public BiomarkerEditor(boolean autoUpdateTitle) {
		super(autoUpdateTitle);
		this.autoUpdateTitle = autoUpdateTitle;
	}

	public BiomarkerEditor() {
		this(true);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {

		Composite container = Widgets.createDefaultContainer(parent);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(true).margins(10, 10).create());

		Biomarker model = model();

		// FIXME analizar no sacar ya que las acciones dependenden de que haya
		// un biomarcador activo de esta manera.
		Models.getInstance().setActiveBiomarker(model());

		Widget w = Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_name_label, model, "name");
		// TODO mover a la superclase
		if (autoUpdateTitle)
			w.addModificationListener(new ModificationListener() {

				@Override
				public void modify(ModificationTextEvent event) {
					setPartName(event.getNewText());
				}
			});
		Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_gene_count_label, model, "geneCount").readOnly();
		Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_author_label, model, "author");
		Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_original_gene_count_label, model, "originalNumberOfGenes").readOnly();
		Widgets.createMultiTextWithLabel(container, Messages.BiomarkerEditor_description_label, model, "description");
		Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_significance_value, model, "significanceValue.pvalue").readOnly();

		Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_david_URL, model, "davidURLForFunctionalAnalysis").readOnly()//
		// .setLayoutData(GridDataFactory.fillDefaults().span(3,
		// 1).align(GridData.CENTER, GridData.FILL).grab(true, false).create())
		;

		Composite subcontainer = Widgets.createDefaultContainer(container);
		subcontainer.setLayoutData(GridDataFactory.fillDefaults().span(4, 1).grab(true, true).create());
		createTable(subcontainer);

	}

	private void createTable(Composite parent) {

		final TableBuilder tb = TableBuilder.create(parent).input(model().getGenes());

		tb.addColumn(ColumnBuilder.create().numeric().property("entrezId").title("entrezId"))//
				.addColumn(ColumnBuilder.create().editable(true).title("Nombre").centeredText().accesor(OgnlAccesor.createFor("name")))//
				.addColumn(ColumnBuilder.create().property("description").title("Descripci\u00f3n"));

		tr = tb.build();

		tr.addSelectionChangeListener(this);
	}

	private TableReference tr;

	@Override
	protected Observer createModificationObserver() {

		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				tr.refresh();
			}
		};
	}

	@Override
	protected Map<Object, IStructuredSelection> getAdditionalSelections() {
		final IStructuredSelection element = new StructuredSelection(tr.focusedElements());
		return ImmutableMap.of((Object) Constants.GENES, element);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// FIXME sacar el downcast, avisar a de una manera prolija
		AbstractSelectionService ass = (AbstractSelectionService) getSite().getWorkbenchWindow().getSelectionService();
		ass.setActivePart(null);
		ass.setActivePart(this);
	}

	// /**
	// * @deprecated siempre guardar el root, la implementación por default...
	// */
	// @Deprecated
	// @Override
	// protected void doSave0() {
	// for (Gene g : model().getGenes())
	// RepositoryFactory.getRepository().save(g);
	// }
}

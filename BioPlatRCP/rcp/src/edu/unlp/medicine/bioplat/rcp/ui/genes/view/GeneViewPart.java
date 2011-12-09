package edu.unlp.medicine.bioplat.rcp.ui.genes.view;

import java.util.List;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GeneViewPart extends ViewPart {

	public static String id() {
		return "edu.medicine.bioplat.rcp.gene.view";
	}

	private ISelectionListener listener;

	@Override
	public void createPartControl(final Composite parent) {

		getSelectionService().addSelectionListener(listener = new ISelectionListener() {

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				Biomarker b = null;

				if (!isMySelf(part) && isValidSelection(selection))
					b = (Biomarker) ((StructuredSelection) selection).getFirstElement();

				createComposite(parent, b);

			}

			private boolean isValidSelection(ISelection s) {
				return !s.isEmpty() && //
						s instanceof StructuredSelection && //
						((StructuredSelection) s).getFirstElement() instanceof Biomarker;
			}

			private boolean isMySelf(IWorkbenchPart part) {
				return part == GeneViewPart.this;
			}

		});
	}

	@Override
	public void dispose() {
		getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}

	private ISelectionService getSelectionService() {
		return getSite().getWorkbenchWindow().getSelectionService();
	}

	private Composite c;
	private List<Widget> ws = Lists.newArrayList();
	private TableReference tr;

	private void createComposite(Composite parent, Biomarker input) {

		// c.dispose();
		// c = null;

		// c = new PageBook(parent, SWT.BORDER);
		// c.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());
		if (c == null) {
			c = Widgets.createDefaultContainer(parent);
			c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		}
		if (input != null) {
			if (!ws.isEmpty()) {
				for (Widget w : ws)
					w.retarget(input);
				tr.refresh();
			} else {
				ws.add(Widgets.createTextWithLabel(c, "nombre", input, "name", true));
				tr = TableBuilder.create(c).model(input, "genes").//
						addColumn(ColumnBuilder.create().property("name").title("Nombre")).//
						addColumn(ColumnBuilder.create().property("entrezId").title("ID").labelprovider(new ColumnLabelProvider() {
							@Override
							public String getText(Object element) {
								return "asdfadsf";
							}
						})).//
						build();

			}
		}

		parent.layout();
		parent.redraw();
		parent.update();
	}

	@Override
	public void setFocus() {
	}

}

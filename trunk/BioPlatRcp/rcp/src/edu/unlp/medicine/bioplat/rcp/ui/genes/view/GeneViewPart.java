package edu.unlp.medicine.bioplat.rcp.ui.genes.view;

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.gene.Gene;

public class GeneViewPart extends ViewPart {

	private static final String REST_URL_NCBI = "http://www.ncbi.nlm.nih.gov/gene/?term=";

	public static String id() {
		return "edu.medicine.bioplat.rcp.gene.view";
	}

	private ISelectionListener listener;

	@Override
	public void createPartControl(final Composite parent) {

		getSelectionService().addSelectionListener(listener = new ISelectionListener() {

			private Gene oldGene;

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				Gene gene = null;

				if (!isMySelf(part) && isValidSelection(selection))
					gene = getGeneSelection(selection);

				if (gene != null && (oldGene == null || !gene.equals(oldGene)))
					updateComposite(parent, gene);

				oldGene = gene;

			}

			private Gene getGeneSelection(ISelection selection) {
				Object result = null;
				if (selection instanceof MultipleSelection)
					result = ((MultipleSelection) selection).get(Constants.GENES).getFirstElement();

				// result ((StructuredSelection) selection).getFirstElement();
				return (Gene) result;
			}

			private boolean isValidSelection(ISelection s) {
				return !s.isEmpty() && //
						s instanceof MultipleSelection //
				// && ((StructuredSelection) s).getFirstElement()
				// instanceof Gene
				;
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

	// TODO mover a un abstract...?
	protected ISelectionService getSelectionService() {
		return getSite().getWorkbenchWindow().getSelectionService();
	}

	private Composite c;
	private List<Widget> ws = Lists.newArrayList();
	private Browser browser;

	private void updateComposite(Composite parent, Gene gene) {

		// c.dispose();
		// c = null;

		// c = new PageBook(parent, SWT.BORDER);
		// c.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());
		if (c == null) {
			c = Widgets.createDefaultContainer(parent);
			c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

		}

		c.setVisible(gene != null);

		if (gene != null) {
			if (!ws.isEmpty()) {
				for (Widget w : ws)
					w.retarget(gene);

				browser.setUrl(makeRestUrl(gene));
			} else {
				ws.add(Widgets.createTextWithLabel(c, "nombre", gene, "name", true));
				browser = new Browser(c, SWT.BORDER);
				browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
				browser.setUrl(makeRestUrl(gene));
			}
		}

		// FIXME no se repinta bien la primera vez que aparece si no se crea el
		// composite container, se fuerza con el -1
		parent.setSize(parent.getSize().x, parent.getSize().y);
		parent.layout();
		parent.redraw();
		parent.update();

	}

	private String makeRestUrl(Gene input) {
		return REST_URL_NCBI + input.getEntrezId();
	}

	@Override
	public void setFocus() {
	}

}

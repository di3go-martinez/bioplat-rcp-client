package edu.unlp.medicine.bioplat.rcp.ui.genes;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.gene.Gene;

public class GeneSearch extends Dialog {

	private Gene model;

	private GeneSearch(Shell parentShell, Gene model) {
		super(parentShell);
		this.model = model;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Gen a agregar");
	}

	public static GeneSearch createDialog() {
		return new GeneSearch(null, createDefaultGene());
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 110);
	}

	private static Gene createDefaultGene() {
		Gene result = new Gene(-1);
		result.setName("default name");
		return result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);

		Widgets.createTextWithLabel(container, "Entrez Id", model, "entrezId");
		// Widgets.createTextWithLabel(container, "Nombre", model, "name");

		GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).generateLayout(container);
		return container;
	}

	public Gene selectedGene() {
		return MetaPlat.getInstance().findGene(String.valueOf(model.getEntrezId()));
	}
}
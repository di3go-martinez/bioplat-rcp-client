package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

abstract class CompositeGenerator {

	private FormToolkit toolkit;

	public CompositeGenerator(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}

	Composite generate(Composite parent) throws Exception {
		ScrolledForm result = toolkit.createScrolledForm(parent);
		result.setLayout(GridLayoutFactory.fillDefaults().create());

		Composite c = result.getBody();
		c.setLayout(GridLayoutFactory.fillDefaults().create());
		fill(c);

		return result;

	}

	protected FormToolkit toolkit() {
		return toolkit;
	}

	protected abstract void fill(Composite container) throws Exception;
}
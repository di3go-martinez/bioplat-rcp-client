package edu.unlp.medicine.bioplat.rcp.personas;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

public class PersonaEditor extends AbstractEditorPart<Persona> implements IEditorPart, ISelectionProvider {

	@Override
	protected void doCreatePartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.BORDER);
		Widgets.createTextWithLabel(container, "apellido", model(), "apellido", true);

		GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).generateLayout(container);

		getSite().setSelectionProvider(this);

	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	@Override
	public ISelection getSelection() {
		return new StructuredSelection(model());
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {

	}

	@Override
	public void setSelection(ISelection selection) {

	}
}

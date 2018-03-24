package edu.unlp.medicine.bioplat.poc.rcp.personas;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;

public class PersonaViewPart extends ViewPart {

	public static String id() {
		return "edu.unlp.medicine.bioplat.rcp.personas.persona.view";
	}

	public PersonaViewPart() {
		// TODO Auto-generated constructor stub
	}

	private Persona p = Persona.nullPersona();;
	private TableReference tr;
	private ISelectionListener listener;

	@Override
	public void createPartControl(Composite parent) {

		createGrid(parent);

		getSelectionService().addSelectionListener(listener = new ISelectionListener() {

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				p = Persona.nullPersona();

				if (!selection.isEmpty() && selection instanceof StructuredSelection) {
					Object first = ((StructuredSelection) selection).getFirstElement();
					if (first instanceof Persona)
						p = (Persona) first;
				}

				tr.input(p.getNombres());
			}
		});
	}

	@Override
	public void dispose() {
		getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}

	private void createGrid(Composite parent) {

		tr = TableBuilder.create(parent).model(p, "nombres").addColumn(//
				ColumnBuilder.create().accesor(new Accesor() {

					@Override
					public void set(Object element, Object value) {
						// System.out.println("element " + element + "; value" +
						// value);
						// p.getNombres().set(p.getNombres().indexOf(element),
						// value.toString());
						// tr.refresh();
					}

					@Override
					public Object get(Object element) {
						return element;
					}
				})/* .editable() */)//
				.build();
	}

	protected ISelectionService getSelectionService() {
		return getSite().getWorkbenchWindow().getSelectionService();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

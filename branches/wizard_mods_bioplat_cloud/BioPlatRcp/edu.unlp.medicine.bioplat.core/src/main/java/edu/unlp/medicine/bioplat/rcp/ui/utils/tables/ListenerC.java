package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;

public class ListenerC implements ISelectionChangedListener {

	private TableViewer viewer;

	public ListenerC(final TableViewer viewer) {
		this.viewer = viewer;

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(new ISelectionListener() {
			private boolean avoidRecursionFlag = true;

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (selection.isEmpty())
					return;
				MultipleSelection ms = (MultipleSelection) selection;
				StructuredSelection s = (StructuredSelection) ms.get(Constants.GENES);
				avoidRecursionFlag = !avoidRecursionFlag;
				if (!avoidRecursionFlag && !s.isEmpty()) {
					Object[] objects = viewer.getColumnProperties();

				}
			}
		});
	}

	private boolean avoidRecursionFlag = true;

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		System.out.println("selection changed");
		// viewer.setSelection(StructuredSelection.EMPTY);
		avoidRecursionFlag = !avoidRecursionFlag;
		if (!avoidRecursionFlag) // para testear contra el valor anterior, el
									// original, debe estar cambiado para la
									// llamada a setSelection
			viewer.setSelection(event.getSelection(), true);

		viewer.refresh();
	}

}

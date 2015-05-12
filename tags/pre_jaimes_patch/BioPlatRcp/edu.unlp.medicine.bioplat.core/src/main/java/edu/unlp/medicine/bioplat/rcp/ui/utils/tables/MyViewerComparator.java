package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.Comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

//TODO revisar bien
public class MyViewerComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	private Accesor accesor;
	private Comparator comparator;

	public MyViewerComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
		accesor = createDefaultAccesor();
		comparator = createDefaultComparator();
	}

	private Comparator createDefaultComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column, Accesor accesor, Comparator comparator) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING; // default
			this.accesor = accesor;
			if (accesor == null)
				this.accesor = createDefaultAccesor();
			this.comparator = comparator;
		}
	}

	private Accesor createDefaultAccesor() {
		return new Accesor() {

			@Override
			public void set(Object element, Object value) {
				// no se puede... tirar un warning
			}

			@Override
			public Object get(Object element) {
				return element;
			}
		};
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Object o1 = accesor.get(e1);
		Object o2 = accesor.get(e2);

		rc = comparator.compare(o1, o2);

		// If descending order, flip the direction
		if (direction == DESCENDING)
			rc = -rc;

		return rc;
	}
}
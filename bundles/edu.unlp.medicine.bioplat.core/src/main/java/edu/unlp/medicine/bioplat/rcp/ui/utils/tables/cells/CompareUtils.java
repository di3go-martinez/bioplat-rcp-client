package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

public class CompareUtils {

	public static int compareTo(Object o1, Object o2) {

		if (o1 instanceof Comparable && o2 instanceof Comparable) {
			return ((Comparable) o1).compareTo(o2);
		}

		throw new RuntimeException("Se debe implementar comparable");

	}

}

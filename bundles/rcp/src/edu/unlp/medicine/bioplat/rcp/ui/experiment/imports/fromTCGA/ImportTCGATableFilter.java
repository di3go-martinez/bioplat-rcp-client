package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ImportTCGATableFilter extends ViewerFilter {

	private String searchString;

	public ImportTCGATableFilter(String text) {
		setSearchText(text);
	}

	private void setSearchText(String s) {
		// ensure that the value can be used for matching
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		try {
			// type -> String[3] >>> name, description, extended description
			String[] row = (String[]) element;
			return matches(row[0]) || matches(row[1]) || matches(row[2]);
		} catch (Exception e) {
			return true;
		}
	}

	private boolean matches(String value) {
		String VALUE = value.toUpperCase();
		String SEARCHSTRING = searchString.toUpperCase();
		return VALUE.matches(SEARCHSTRING);
	}
}

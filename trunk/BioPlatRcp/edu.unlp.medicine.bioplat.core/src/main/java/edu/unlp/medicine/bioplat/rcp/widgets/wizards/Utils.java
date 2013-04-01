package edu.unlp.medicine.bioplat.rcp.widgets.wizards;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Utils {
	private Utils() {
	}

	public static ComboViewer newComboViewer(Composite parent, String label, List<?> values) {
		return newComboViewer(parent, label, "", values);
	}

	public static ComboViewer newComboViewer(Composite parent, String label, String tooltip, List<?> values) {
		new Label(parent, SWT.NONE).setText(label);
		ComboViewer cv = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);
		cv.setContentProvider(ArrayContentProvider.getInstance());
		cv.setInput(values);
		cv.getCombo().setToolTipText(tooltip);
		return cv;

	}
	
	
	public static ComboViewer newComboViewerWithoutLabel(Composite parent, String tooltip, List<?> values) {
		ComboViewer cv = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);
		cv.setContentProvider(ArrayContentProvider.getInstance());
		cv.setInput(values);
		cv.getCombo().setToolTipText(tooltip);
		return cv;

	}

	
}

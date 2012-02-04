package edu.unlp.medicine.bioplat.rcp.widgets;

import java.util.Map;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class FileText {

	private Text text;
	private Button b;
	private FileDialog fd;

	public FileText(Composite parent, int style) {
		Composite c = new Composite(parent, SWT.NONE);

		fd = new FileDialog(parent.getShell());

		text = new Text(c, SWT.BORDER);
		b = new Button(c, SWT.NONE);
		b.setText("...");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filename = fd.open();
				text.setText((filename == null) ? "" : filename);
			}
		});

		GridLayoutFactory.fillDefaults().numColumns(2).generateLayout(c);
	}

	public Control textControl() {
		return text;
	}

	/**
	 * 
	 * @param filters
	 *            es un mapa donde la clave es el pattern del arhivo y el value
	 *            una descripción
	 * @return
	 */
	public FileText setFilter(Map<String, String> filters) {
		final String[] EMPTY_STRING_ARRAY = new String[0];
		fd.setFilterExtensions(filters.keySet().toArray(EMPTY_STRING_ARRAY));
		fd.setFilterNames(filters.values().toArray(EMPTY_STRING_ARRAY));
		return this;
	}
}
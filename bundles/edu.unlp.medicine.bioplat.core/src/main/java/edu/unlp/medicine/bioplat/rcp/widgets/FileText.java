package edu.unlp.medicine.bioplat.rcp.widgets;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

/**
 * Abre un diálogo que permite seleccionar un archivo
 * 
 * @author diego
 * 
 */
public class FileText extends TextWithSelectionButton {

	private FileDialog fd;

	public FileText(Composite parent, int style) {
		super(parent);
	}

	public FileText(Composite container) {
		this(container, SWT.NONE);
	}

	@Override
	protected FileDialog createDialog(Composite parent) {
		fd = new FileDialog(parent.getShell());
		return fd;
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

	public static FileText create(Composite c) {
		new CLabel(c, SWT.NONE).setText("Archivo:");
		return new FileText(c, SWT.NONE);
	}

	@Override
	protected String getSelection() {
		return fd.open();
	}

}

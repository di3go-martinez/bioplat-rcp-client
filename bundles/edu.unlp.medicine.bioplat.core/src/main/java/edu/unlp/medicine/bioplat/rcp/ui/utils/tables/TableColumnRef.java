package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.TableColumn;

public class TableColumnRef implements TableColumnReference {

	private String id;
	private TableViewerColumn tvc;

	public TableColumnRef(String id, TableViewerColumn tvc) {
		this.id = id;
		this.tvc = tvc;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public TableColumn column() {
		return tvc.getColumn();
	}

}

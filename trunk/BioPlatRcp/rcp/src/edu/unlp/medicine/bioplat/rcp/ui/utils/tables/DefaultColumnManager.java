package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

public class DefaultColumnManager implements ColumnManager {

	private TableReference tableRef;
	private TableBuilder tbuilder;

	public DefaultColumnManager(TableBuilder tableBuilder, TableReference tableRef) {
		this.tableRef = tableRef;
		this.tbuilder = tableBuilder;
	}

	public static DefaultColumnManager createOn(TableBuilder tb, TableReference tableReference) {
		return new DefaultColumnManager(tb, tableReference);
	}

	@Override
	public void remove(String columnId) {

	}

}

package edu.unlp.medicine.bioplat.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Snippet1 {
	public static void main(String[] args) {

		Display display = new Display();

		Shell shell = new Shell(display);

		Table table = new Table(shell, SWT.BORDER);

		TableColumn column = new TableColumn(table, SWT.NONE);

		column.setWidth(100);

		for (int i = 0; i < 1000; i++) {

			TableItem item = new TableItem(table, SWT.NULL);

			item.setText("Item " + i);

		}
		table.setHeaderVisible(true);
		table.setSize(200, 200);

		shell.pack();

		shell.open();

		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();

		}

		display.dispose();

	}
}

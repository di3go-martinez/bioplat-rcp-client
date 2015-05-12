package edu.unlp.medicine.bioplat.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Snippet {
	static final int COLUMNS = 8, ROWS = 8;

	public static void main(String[] args) {

		Display display = new Display();

		Shell shell = new Shell(display);

		final Table table = new Table(shell, SWT.BORDER);

		table.setHeaderVisible(true);

		table.setLinesVisible(true);

		for (int i = 0; i < COLUMNS; i++) {

			TableColumn column = new TableColumn(table, SWT.NONE);

			column.setText("Column " + i);

		}

		for (int i = 0; i < ROWS; i++) {

			TableItem item = new TableItem(table, SWT.NULL);

			for (int j = 0; j < COLUMNS; j++) {

				item.setText(j, "Item " + i + "-" + j);

			}

		}

		for (int i = 0; i < COLUMNS; i++) {

			TableColumn column = table.getColumn(i);

			column.pack();

		}

		table.addListener(SWT.MouseMove, new Listener() {

			@Override
			public void handleEvent(Event event) {

				Rectangle rect = table.getClientArea();

				int itemCount = table.getItemCount();

				int columnCount = table.getColumnCount();

				int i = table.getTopIndex();

				while (i < itemCount) {

					TableItem item = table.getItem(i);

					for (int j = 0; j < columnCount; j++) {

						Rectangle bounds = item.getBounds(j);

						if (bounds.y > rect.height)
							return;

						if (bounds.contains(event.x, event.y)) {

							System.out.println(item.getText(j));

							return;

						}

					}

					i++;

				}

			}

		});
		table.pack();

		shell.pack();

		shell.open();

		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();

		}

		display.dispose();

	}
}

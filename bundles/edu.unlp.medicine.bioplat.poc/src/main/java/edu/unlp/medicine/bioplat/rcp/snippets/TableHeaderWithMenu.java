package edu.unlp.medicine.bioplat.rcp.snippets;

/*
 * Table example snippet: show a menu in a table header
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableHeaderWithMenu {

	static String[][] files = { { "ver.txt", "1 KB", "Text Document", "28/09/2005 9:57 AM", "admin", }, { "Thumbs.db", "76 KB", "Data Base file", "13/03/2006 3:56 PM", "john", }, { "daddy.bmp", "148 MB", "Bitmap", "27/10/2008 1:34 PM", "bill", }, { "io.sys", "48 KB", "File System", "16/12/2008 6:14 AM", "admin", }, { "Programs", "0 KB", "File Folder", "04/02/2009 12:18 PM", "anne", }, { "test.rnd", "55 MB", "RND File", "19/02/2009 5:49 PM", "john", }, { "arial.ttf", "94 KB", "True Type Font", "25/08/2008 1:25 PM", "john", }, };

	static void createMenuItem(Menu parent, final TableColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(150);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Table table = new Table(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setHeaderVisible(true);
		final Menu headerMenu = new Menu(shell, SWT.POP_UP);
		final TableColumn columnName = new TableColumn(table, SWT.NONE);
		columnName.setText("Name");
		columnName.setWidth(150);
		createMenuItem(headerMenu, columnName);
		final TableColumn columnSize = new TableColumn(table, SWT.NONE);
		columnSize.setText("Size");
		columnSize.setWidth(150);
		createMenuItem(headerMenu, columnSize);
		final TableColumn columnType = new TableColumn(table, SWT.NONE);
		columnType.setText("Type");
		columnType.setWidth(150);
		createMenuItem(headerMenu, columnType);
		final TableColumn columnDate = new TableColumn(table, SWT.NONE);
		columnDate.setText("Date");
		columnDate.setWidth(150);
		createMenuItem(headerMenu, columnDate);
		final TableColumn columnOwner = new TableColumn(table, SWT.NONE);
		columnOwner.setText("Owner");
		columnOwner.setWidth(0);
		columnOwner.setResizable(false);
		createMenuItem(headerMenu, columnOwner);

		for (int i = 0; i < files.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(files[i]);
		}

		final Menu tableMenu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Open");
		item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Open With");
		new MenuItem(tableMenu, SWT.SEPARATOR);
		item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Cut");
		item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Copy");
		item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Paste");
		new MenuItem(tableMenu, SWT.SEPARATOR);
		item = new MenuItem(tableMenu, SWT.PUSH);
		item.setText("Delete");

		table.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point pt = display.map(null, table, new Point(event.x, event.y));
				Rectangle clientArea = table.getClientArea();
				boolean header = clientArea.y <= pt.y && pt.y < (clientArea.y + table.getHeaderHeight());
				table.setMenu(header ? headerMenu : tableMenu);
			}
		});

		/*
		 * IMPORTANT: Dispose the menus (only the current menu, set with
		 * setMenu(), will be automatically disposed)
		 */
		table.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				headerMenu.dispose();
				tableMenu.dispose();
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
package edu.unlp.medicine.bioplat.tables;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.swt.snippets;
/*
 * Table example snippet: sort a table by column
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableColumnSelectionListener {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);

		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column 1");

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column 2");

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { "a", "3" });
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { "b", "2" });
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { "c", "1" });

		column1.setWidth(100);
		column2.setWidth(100);

		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableColumn column = (TableColumn) e.widget;
				System.out.println(column);
			}
		};
		column1.addListener(SWT.Selection, sortListener);
		column2.addListener(SWT.Selection, sortListener);

		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
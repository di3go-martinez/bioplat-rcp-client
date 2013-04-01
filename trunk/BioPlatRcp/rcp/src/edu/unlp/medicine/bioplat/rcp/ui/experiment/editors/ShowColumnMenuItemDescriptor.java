package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;

public class ShowColumnMenuItemDescriptor implements MenuItemDescriptor {

	private static final int DEFAULT = 50;
	private static final String SHOW = "Show";
	private static final String HIDE = "Hide";

	private ExperimentEditor0 editor;
	private String title;
	private String columnId;
	private boolean startedHide;

	public ShowColumnMenuItemDescriptor(ExperimentEditor0 editor, String title, String columnId) {
		this(editor, title, columnId, true);
	}

	public ShowColumnMenuItemDescriptor(ExperimentEditor0 editor, String title, String columnId, boolean startedHide) {
		this.editor = editor;
		this.title = title;
		this.columnId = columnId;
		this.startedHide = startedHide;
	}

	@Override
	public MenuContribution createOn(Menu menu, TableViewerColumn column) {

		final MenuItemContribution mic = MenuItemContribution.create(menu);

		mic.text((startedHide) ? showText() : hideText());

		mic.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableColumn c = editor.tableref().column(columnId);
				if (c.getWidth() != 0) {
					c.setWidth(0);
					c.setResizable(false);
					mic.text(showText());
				} else {
					// TODO no deberían asumirse datos de la configuración
					// inicial
					c.setWidth(DEFAULT);
					c.setResizable(true);
					mic.text(hideText());

				}

			}

		});

		return mic;
	}

	private String showText() {
		return SHOW + " " + title;
	}

	private String hideText() {
		return HIDE + " " + title;
	}

}

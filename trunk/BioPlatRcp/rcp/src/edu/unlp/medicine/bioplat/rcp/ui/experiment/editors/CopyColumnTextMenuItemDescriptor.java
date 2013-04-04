package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;

import com.google.common.annotations.Beta;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.BiomarkerEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

@Beta
public class CopyColumnTextMenuItemDescriptor implements MenuItemDescriptor {

	private ExperimentEditor0 editor;
	private BiomarkerEditor beditor;
	private String name;
	private Accesor accesor;

	// public CopyColumnTextMenuItemDescriptor(ExperimentEditor0
	// experimentEditor0, String name, Accesor accesor) {
	// this.editor = experimentEditor0;
	// this.name = name;
	// this.accesor = accesor;
	// }

	public CopyColumnTextMenuItemDescriptor(BiomarkerEditor biomarkerEditor, String name, Accesor accesor) {
		this.name = name;
		this.accesor = accesor;
		this.beditor = biomarkerEditor;
	}

	@Override
	public MenuContribution createOn(Menu menu, TableViewerColumn column) {
		final MenuItemContribution mic = MenuItemContribution.create(menu);
		mic.text(name);
		mic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Clipboard clipboard = new Clipboard(PlatformUIUtils.findDisplay());
				// tomo los seleccionados o los enfocados si no hay de los
				// primeros
				List<?> tableItems = beditor.tableref().selectedElements();
				if (tableItems.isEmpty())
					tableItems = beditor.tableref().focusedElements();
				if (tableItems.isEmpty())
					// TODO hacer que se copien todos ? por ahora no hace nada
					return;

				StringBuilder sb = new StringBuilder();
				for (Object item : tableItems)
					sb.append(accesor.get(item)).append(itemSeparator());
				// FIXME remove the las separator..
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { sb.toString() }, new Transfer[] { textTransfer });

			}

			protected String itemSeparator() {
				return "\t";
			}
		});
		return mic;
	}
}

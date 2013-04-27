package edu.unlp.medicine.bioplat.rcp.ui.entities.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;

import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.CopyTextMenuItemProvider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * 
 * Crea una acción de menú que permite copiar datos al clipboard del sistema
 * 
 * @author diego martínez
 * 
 */
public class CopyIntegerColumnMenuItemDescriptor implements MenuItemDescriptor {

	private CopyTextMenuItemProvider provider;
	private String name;
	private Accesor accesor;

	// public CopyColumnTextMenuItemDescriptor(ExperimentEditor0
	// experimentEditor0, String name, Accesor accesor) {
	// this.editor = experimentEditor0;
	// this.name = name;
	// this.accesor = accesor;
	// }

	public CopyIntegerColumnMenuItemDescriptor(CopyTextMenuItemProvider provider) {
		this.name = provider.name();
		this.accesor = provider.accesor();
		this.provider = provider;
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
				List<?> tableItems = provider.elements();

				if (tableItems.isEmpty())
					// TODO hacer que se copien todos ? por ahora no hace nada
					return;

				StringBuilder sb = new StringBuilder();
				List<Integer> genes = new ArrayList<Integer>();
				for (Object item : tableItems)
					genes.add(Integer.valueOf(accesor.get(item).toString()));
				
				Collections.sort(genes); 
						
				for (Integer id : genes) {
					sb.append(id).append(itemSeparator());	
				}
				String result = sb.toString().substring(0, sb.length()-1);
				
				// FIXME remove the las separator..
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { result }, new Transfer[] { textTransfer });

			}

		});
		return mic;
	}
	

	protected String itemSeparator() {
		return "\n";
	}
}

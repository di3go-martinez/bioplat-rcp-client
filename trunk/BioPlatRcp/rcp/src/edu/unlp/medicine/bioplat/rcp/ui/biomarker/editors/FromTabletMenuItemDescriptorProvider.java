package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.CopyTextMenuItemProvider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

public class FromTabletMenuItemDescriptorProvider implements CopyTextMenuItemProvider {

	private String name;
	private Accesor accesor;
	private TableReferenceProvider tableRp;
	private boolean all = false;

	public FromTabletMenuItemDescriptorProvider(TableReferenceProvider tableRp, String name, Accesor accesor) {
		this.name = name;
		this.accesor = accesor;
		this.tableRp = tableRp;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Accesor accesor() {
		return accesor;
	}

	@Override
	public List<?> elements() {
		if (all)
			return tableRp.tableReference().elements();

		List<?> tableItems = tableRp.tableReference().selectedElements();
		if (tableItems.isEmpty())
			tableItems = tableRp.tableReference().focusedElements();
		return tableItems;

	}

	public FromTabletMenuItemDescriptorProvider includeAll(boolean all) {
		this.all = all;
		return this;
	}

}

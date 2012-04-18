package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveSamplesCommand;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

public class RemoveSampleColumnDescriptor implements MenuItemDescriptor {

	private AbstractExperiment experiment;

	public RemoveSampleColumnDescriptor(AbstractExperiment model) {
		this.experiment = model;
	}

	@Override
	public MenuContribution createOn(Menu menu, final TableViewerColumn column) {
		final MenuItemContribution mic = MenuItemContribution.create(menu);
		mic.text("Remove");
		mic.image(PlatformUIUtils.findImage("removeItem.gif"));
		mic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String columnId = column.getColumn().getText();
				if (MessageDialog.openConfirm(PlatformUIUtils.findShell(), "", "¿Confirma el borrado de " + columnId + "?")) {
					// Borro del modelo
					new RemoveSamplesCommand(experiment, Arrays.asList(columnId)).execute();

					// "Borro" de la vista; SWT no proveé un mecanismo para
					// borrar columnas
					column.getColumn().setWidth(0);
					column.getColumn().setResizable(false);

					// reOrderColumns();

					// saco este listener porque la columna ya no está más
					mic.removeSelectionListener(this);
				}
			}

		});
		return mic;
	}

	// private void reOrderColumns() {
	// TODO Auto-generated method stub
	// fixeo el order de las columnas mandando la borrada al
	// final... con el cambio a CustomCellData es muy probable
	// que no se necesite cambiar el orden....
	// TableColumn deletedTableColumn = column.getColumn();
	// Table t = deletedTableColumn.getParent();
	// int columnOrder[] = t.getColumnOrder();
	// int deletedColumnIndex = t.indexOf(deletedTableColumn);
	// for (int i = deletedColumnIndex; i < columnOrder.length -
	// 1; i++)
	// columnOrder[i] = columnOrder[i + 1];
	// columnOrder[columnOrder.length - 1] = deletedColumnIndex;
	// t.setColumnOrder(columnOrder);

	// }

}

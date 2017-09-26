package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import static edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils.*;
import static org.eclipse.jface.dialogs.MessageDialog.openConfirm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

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
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;

public class RemoveSampleColumnDescriptor implements MenuItemDescriptor {

	private AbstractExperiment experiment;

	public RemoveSampleColumnDescriptor(AbstractExperiment model) {
		this.experiment = model;
	}

	@Override
	public MenuContribution createOn(Menu menu, final TableViewerColumn column) {
		final MenuItemContribution mic = MenuItemContribution.create(menu);
		mic.text("Remove");
		mic.image(findImage("removeItem.gif"));
		mic.addSelectionListener(createRemoveSampleSelectionListener(column, mic));
		experiment.addPropertyChangeListener(Experiment.SAMPLE_REMOVED, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Sample s = (Sample) evt.getOldValue();
				if (s.getName().equals(sampleName(column)))
					removeUIColumn(column);
			}

		});
		return mic;
	}

	private String sampleName(final TableViewerColumn column) {
		return column.getColumn().getText();
	}

	private SelectionAdapter createRemoveSampleSelectionListener(final TableViewerColumn column,
			final MenuItemContribution mic) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String sampleId = sampleName(column);
				if (remove(sampleId)) {
					doRemove(column, sampleId);

					// saco este listener porque la columna ya no está más
					mic.removeSelectionListener(this);
				}
			}

			private boolean remove(String sampleId) {
				return openConfirm(PlatformUIUtils.findShell(), "",
						"Do you really want to delete the sample " + sampleId + "?");
			}

		};
	}

	private void doRemove(final TableViewerColumn column, String sampleId) {
		new RemoveSamplesCommand(experiment, Arrays.asList(sampleId)).execute();
		removeUIColumn(column);
	}

	// "Borro" de la vista; SWT no proveé un mecanismo para
	// borrar columnas
	private void removeUIColumn(final TableViewerColumn column) {
		column.getColumn().setWidth(0);
		column.getColumn().setResizable(false);
	}

}

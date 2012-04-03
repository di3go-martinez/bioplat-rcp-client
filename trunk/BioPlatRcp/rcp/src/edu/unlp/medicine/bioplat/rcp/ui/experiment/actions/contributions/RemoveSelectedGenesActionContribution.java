package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToRemoveCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveSelectedGenesActionContribution extends AbstractActionContribution<Experiment> {

	private AbstractEditorPart editor;

	public RemoveSelectedGenesActionContribution() {
		PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {

			@Override
			public void windowOpened(IWorkbenchWindow window) {
				windowActivated(window);
			}

			@Override
			public void windowDeactivated(IWorkbenchWindow window) {

			}

			@Override
			public void windowClosed(IWorkbenchWindow window) {
				// PlatformUI.getWorkbench().removeWindowListener(this);
			}

			@Override
			public void windowActivated(IWorkbenchWindow window) {
				activeEditor = editor(window);

			}

			private IEditorPart editor(IWorkbenchWindow window) {
				final IEditorPart activeEditor = window.getActivePage().getActiveEditor();
				return activeEditor;
			}
		});
	}

	private IEditorPart activeEditor;

	@Override
	public void run() {

		try {
			final Holder<ISelection> sh = Holder.create();
			PlatformUIUtils.findDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					sh.hold(activeEditor.getSite().getSelectionProvider().getSelection());
				}
			});

			MultipleSelection ms = (MultipleSelection) sh.value();
			final List<Gene> selectedGenes = ms.get(Constants.SELECTED_GENES).toList();
			executeOn(selectedGenes);
		} catch (NullPointerException e) {
			// FIXME FIXME hay veces que tira nullpointerexception porque no se
			// registró la
			// selection por ahora se ignora ya que en la mayoría de las veces
			// anda y no se determinó la causa exacta todavía...
			// FIXME FIXME SE DECIDIO TAPAR LA EXCEPCIÓN PARA QUE SE CREA QUE NO
			// FUE TOMADO EL CLICK Y SE VUELVA A PROBAR YA QUE LA PRÓXIMA VEZ
			// ANDARÁ
			e.printStackTrace();
		}
	}

	protected void executeOn(final List<Gene> selectedGenes) {
		new RemoveGenesGivingTheListOfGenesToRemoveCommand(model(), selectedGenes).execute();
	}
}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.ActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageViewPart;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

public class ChangeZerosToAction extends Action implements ActionContribution {

	private ModelProvider modelProvider;

	public ChangeZerosToAction() {
		setText("Cambiar 0's...");
		// setToolTipText("Hace un promedio en función de un gen, donde la expresión génica en un sample es 0");
	}

	@Override
	public void modelProvider(ModelProvider modelProvider) {
		this.modelProvider = modelProvider;
	}

	@Override
	public IAction action() {
		return this;
	}

	@Override
	public void run() {

		// TODO está bien que se limpie antes de ejecutar esta accion SIEMPRE?
		MessageManager.INSTANCE.clear();

		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				try {
					Double.parseDouble(newText);
					return null;
				} catch (Exception e) {
					return newText + " no es un valor válido";
				}
			}
		};
		InputDialog id = new InputDialog(Display.getCurrent().getActiveShell(), "Ingrese un valor", "Cambiar 0s por:", "0", validator);
		if (id.open() != Dialog.OK)
			return;

		Double newValue = Double.parseDouble(id.getValue());

		AbstractExperiment e = (AbstractExperiment) modelProvider.model();
		for (Sample s : e.getSamples())
			for (Gene g : s.getGenes()) {
				Double expr = s.getExpressionLevelForAGene(g);
				if (expr == null || expr == getValueToSearch()) {
					s.setExpressionLevelForAGene(g, newValue);
					addModification(s, g);
				}
			}

		if (!MessageManager.INSTANCE.getMessages().isEmpty())
			PlatformUIUtils.openView(MessageViewPart.id());
	}

	private int getValueToSearch() {
		return 0;
	}

	private void addModification(Sample s, Gene g) {
		MessageManager.INSTANCE.add(new Message("Se modific\u00f3 el gen " + g + " en la muestra " + s));
	}
}

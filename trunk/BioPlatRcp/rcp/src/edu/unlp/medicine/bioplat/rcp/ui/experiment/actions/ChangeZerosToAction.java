package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.ActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

//TODO generalizar de cualquier valor a cualquier otro o por operación por ejemplo un valor que se calcule
public class ChangeZerosToAction extends AbstractActionContribution<AbstractExperiment> implements ActionContribution {

	public ChangeZerosToAction() {

		// setToolTipText("Hace un promedio en función de un gen, donde la expresión génica en una muestra es 0");
	}

	@Override
	public void run() {

		final IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				try {
					Double.parseDouble(newText);
					return null;
				} catch (Exception e) {
					return "'" + newText + "' no es un valor válido";
				}
			}
		};

		final Holder<Double> newValue = new Holder<Double>();
		final Holder<Boolean> accepted = new Holder<Boolean>(true);

		// FIXME hacer transparente el pedido de ejecución al ui-thread
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				InputDialog id = new InputDialog(PlatformUIUtils.findShell(), "Ingrese un valor", "Cambiar " + getValueToSearch() + "s por:", String.valueOf(getValueToSearch()), validator);
				if (id.open() != Dialog.OK)
					accepted.hold(false);
				else
					newValue.hold(Double.parseDouble(id.getValue()));

			}
		});

		if (!accepted.value())
			return;

		AbstractExperiment e = model();
		for (Sample s : e.getSamples())
			for (Gene g : s.getGenes()) {
				Double expr = s.getExpressionLevelForAGene(g);
				if (expr == null || expr == getValueToSearch()) {
					// FIXME no funciona asincrónicamente... si ssi tiene que
					// estar con el ui-fucking-thread...
					s.setExpressionLevelForAGene(g, newValue.value());
					addModification(s, g);
				}
			}

	}

	protected double getValueToSearch() {
		return 0.0;
	}

	private void addModification(Sample s, Gene g) {
		MessageManager.INSTANCE.add(Message.info("Se modific\u00f3 el gen " + g + " en la muestra " + s));
	}

}

package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.ActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.wizards.ChangeValueForOtherWizard;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

//TODO generalizar de cualquier valor a cualquier otro o por operación por ejemplo un valor que se calcule
public class ChangeZerosToAction extends AbstractActionContribution<AbstractExperiment> implements ActionContribution<AbstractExperiment> {

	
	
	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				new ChangeValueForOtherWizard(model()).blockOnOpen().open();
			}
		});

	}
	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public ChangeZerosToAction() {
//
//		// setToolTipText("Hace un promedio en función de un gen, donde la expresión génica en una muestra es 0");
//	}
//
//	@Override
//	public void run() {
//
//		final IInputValidator validator = new IInputValidator() {
//
//			@Override
//			public String isValid(String newText) {
//				try {
//					Double.parseDouble(newText);
//					return null;
//				} catch (Exception e) {
//					return "'" + newText + "' is not a valida value";
//				}
//			}
//		};
//
//		final Holder<Double> newValue = new Holder<Double>();
//		final Holder<Boolean> accepted = new Holder<Boolean>(true);
//
//		// FIXME hacer transparente el pedido de ejecución al ui-thread
//		Display.getDefault().syncExec(new Runnable() {
//			@Override
//			public void run() {
//				
//				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(PlatformUIUtils.findShell());
//				
//				
//				
//				InputDialog id = new InputDialog(PlatformUIUtils.findShell(), "Change zeros by other value", "Change " + getValueToSearch() + "s by:", String.valueOf(getValueToSearch()), validator);
//
//				
//				
//				if (id.open() != Dialog.OK)
//					accepted.hold(false);
//				else
//					newValue.hold(Double.parseDouble(id.getValue()));
//
//			}
//		});
//
//		if (!accepted.value())
//			return;
//
//		AbstractExperiment e = model();
//		for (Sample s : e.getSamples())
//			for (Gene g : e.getGenes()) {
//				Double expr = e.getExpressionLevelForAGene(s, g);
//				if (expr == null || expr == getValueToSearch()) {
//					e.setExpressionLevelForAGene(s, g, newValue.value());
//					addModification(s, g, newValue.value());
//				}
//			}
//
//	}
//
//	// TODO Generalizar
//	protected double getValueToSearch() {
//		return 0.0;
//	}
//
//	private void addModification(Sample s, Gene g, Double newValue) {
//		MessageManager.INSTANCE.add(Message.info("The expression data of the gene " + g + " in the sample" + s + " has changed. Old value: " + this.getValueToSearch() + ". New value: " + newValue));
//	}

}

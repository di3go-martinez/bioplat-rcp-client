package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;

/**
 * Acción adicional que agrega a los experimentos un atributo clínico (nombre y
 * respectivos valores para cada sample)
 * 
 * @author diego martínez
 * 
 */
public class AddClinicalAttributteAC extends AbstractActionContribution<AbstractExperiment> {

	private static final Random RANDOM = new Random();
	private String newClinicalAttributeName;
	private String[] newClinicalValues;

	public AddClinicalAttributteAC() {

	}

	@Override
	public void run() {
		String newAttrName = findAttributeName();
		Map<Sample, String> newAtrrValues = findAttributeValues();
		addAttribute(newAttrName, newAtrrValues);
		
	}

	private String findAttributeName() {

		final Display display = PlatformUIUtils.findDisplay();

		final Holder<String> holder = Holder.create();
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				final Clipboard clipboard = new Clipboard(display);
				holder.hold(clipboard.getContents(TextTransfer.getInstance()).toString());
				MessageManager.INSTANCE.add(Message.info("Text read from clipboard"));
			}
		});

		if (isValidFormat(holder.value())) {
			build(holder.value());
			return newClinicalAttributeName;
		}

		throw new RuntimeException("The text in the clipboard do not have the expected format. The text must have the attribute name and the " + model().getSampleCount()  + " values for each sample, separated by tab (as in the original file you have imported)");
		

	}

	private void build(String value) {
		String[] parsedvalue = StringUtils.split(value, "\t");
		newClinicalAttributeName = parsedvalue[0];
		newClinicalValues = new String[parsedvalue.length - 1];
		System.arraycopy(parsedvalue, 1, newClinicalValues, 0, parsedvalue.length - 1);
	}

	private boolean isValidFormat(String value) {
		String[] parsedvalue = StringUtils.split(value, "\t");
		return parsedvalue.length == model().getSampleCount() + 1; // +1 por el
																	// nombre
																	// del
																	// atributo
	}

	private void addAttribute(String newAttrName, Map<Sample, String> newAtrrValues) {
		// Asume que estan bien ordenados en la grilla los elementos
		final AbstractExperiment experiment = model();
		for (Map.Entry<Sample, String> entry : newAtrrValues.entrySet()) {
			experiment.setAttribute(entry.getKey().getName(), newAttrName, entry.getValue());
		}

	}

	/**
	 * result.get(0) ==> nombre del atributo result.get(1..N) ==> valor para el
	 * sample
	 * 
	 * 
	 * @return
	 */
	private Map<Sample, String> findAttributeValues() {
		Map<Sample, String> result = Maps.newHashMap();

		int i = 0;
		AbstractExperiment experiment = model();
		final ArrayList<Sample> samples = experiment.getSamples();
		for (String newClinicalValue : newClinicalValues) {
			result.put(samples.get(i++), newClinicalValue);
		}
		return result;
	}
}

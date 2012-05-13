package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardPageUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

public class GeneralPSOConfigurarion extends WizardPageDescriptor {

	public static final String PROCESS_NAME = "PROCESS_NAME";
	public static final String NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING = "NUMBER OF GENES TO DURING TRAINING";
	public static final String MINIMUM_NUMBER_OF_GENES = "MINIMUM NUMBER OF GENES";
	public static final String NUMBER_OF_ROUNDS = "NUMBER_OF_ROUNDS";
	public static final String NUMBER_OF_PARTICLES = "NUMBER_OF_PARTICLES";

	// TODO analizar si vale la pena, e implementar mejor en todo caso
	public static List<String> allAvailableParams() {

		return Lists.transform(Arrays.asList(GeneralPSOConfigurarion.class.getFields()), new Function<Field, String>() {

			@Override
			public String apply(Field input) {
				try {
					return input.get(null).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "";
			}
		});

	}

	public GeneralPSOConfigurarion() {
		super("PSO Configuration");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite container = Widgets.createDefaultContainer(parent, 2);

		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_PARTICLES), "Number of Particles");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_ROUNDS), "Number of Rounds");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(MINIMUM_NUMBER_OF_GENES), "Number of Genes");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING), "Genes To Keep");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(PROCESS_NAME), "Process Name");

		return container;
	}

}
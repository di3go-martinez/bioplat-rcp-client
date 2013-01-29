package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch;

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

public class GeneralBlindSearchConfiguration extends WizardPageDescriptor {

	public static final String NUMBER_OF_GENES_TO_REMOVE_FROM = "NUMBER_OF_GENES_TO_REMOVE_FROM";
	public static final String NUMBER_OF_GENES_TO_REMOVE_TO = "NUMBER_OF_GENES_TO_REMOVE_TO";
	public static final String NUMBER_OF_RESULTS_TO_SHOW = "NUMBER_OF_RESULTS_TO_SHOW";

	// TODO analizar si vale la pena, e implementar mejor en todo caso
	public static List<String> allAvailableParams() {

		return Lists.transform(Arrays.asList(GeneralBlindSearchConfiguration.class.getFields()), new Function<Field, String>() {

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

	public GeneralBlindSearchConfiguration() {
		super("PSO Configuration");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite container = Widgets.createDefaultContainer(parent, 2);
		wizardPage.setTitle("Configure the blind search");
		wizardPage.setDescription("You can use this algorithm for look in the whole space solution the biomarker optimizing the validation method you will select");
		
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_GENES_TO_REMOVE_FROM), "Number of genes to remove from");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_GENES_TO_REMOVE_TO), "Number of genes to remove to");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_RESULTS_TO_SHOW), "Number of better to show" );
		
		return container;
	}

}

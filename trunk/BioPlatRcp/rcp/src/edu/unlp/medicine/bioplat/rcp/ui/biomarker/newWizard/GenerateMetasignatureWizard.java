package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.utils.monitor.Monitor;

public class GenerateMetasignatureWizard extends AbstractWizard {

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();

		descriptors.add(createProvidersPage());
		descriptors.add(createFilterPage());
		descriptors.add(createAlgorithmPage());
		// descriptors.add(createValidationConfiguration());

		return descriptors;
	}

	private void createTextHolderWithLabel(Composite c, String label) {
		new Label(c, SWT.NONE).setText(label);
		new Text(c, SWT.BORDER);
	}

	private WizardPageDescriptor createProvidersPage() {
		return new WizardPageDescriptor("Providers") {

			@Override
			public Composite create(Composite parent, DataBindingContext dbc, WizardModel wmodel) {

				Composite container = new Composite(parent, SWT.NONE);

				Group g1 = new Group(container, SWT.NONE);
				g1.setText("GenSigDB signatures (online access)");

				createTextHolderWithLabel(g1, "Publication keyword");
				createTextHolderWithLabel(g1, "Limit time for processing (in minutes)");
				FileText.create(g1);

				Group g2 = new Group(container, SWT.NONE);
				g2.setText("GenSigDB Signatures, XML generated using the GenSigDb online provider");
				FileText.create(g2);

				Group g3 = new Group(container, SWT.NONE);
				g3.setText("MSigDB Provider, from downloaded file");
				FileText.create(g3);

				// Configuro los layouts
				final GridDataFactory gddef = GridDataFactory.fillDefaults().grab(true, false);
				final GridLayoutFactory gldef = GridLayoutFactory.fillDefaults().numColumns(2);
				for (Control c : container.getChildren())
					if (c instanceof Composite) {
						((Composite) c).setLayoutData(gddef.create());
						((Composite) c).setLayout(gldef.create());
					}

				return container;
			}

		};
	}

	private WizardPageDescriptor createFilterPage() {

		return new WizardPageDescriptor("Filter") {

			@Override
			public Composite create(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite c = new Composite(parent, SWT.NONE);
				new Label(c, SWT.NONE).setText("Organismo:");
				ComboViewer collapseStrategyCombo = new ComboViewer(c, SWT.BORDER | SWT.READ_ONLY);
				collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
				collapseStrategyCombo.setInput(//
						Arrays.asList("Human", "Mouse", "ALL"));

				// IObservableValue widgetObservable =
				// ViewersObservables.observeSingleSelection(collapseStrategyCombo);
				// dbc.bindValue(widgetObservable, wmodel.get(ORGANISM), new
				// UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Organismo")),
				// null);
				return c;
			}

		};
	}

	private WizardPageDescriptor createAlgorithmPage() {
		return new WizardPageDescriptor("Algorithms") {

			@Override
			public Composite create(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite c = new Composite(parent, SWT.NONE);
				new Label(c, SWT.NONE).setText("Algoritmo:");
				ComboViewer collapseStrategyCombo = new ComboViewer(c, SWT.BORDER | SWT.READ_ONLY);
				collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
				collapseStrategyCombo.setInput(//
						Arrays.asList("ALIX (At least in X signatures)", "UNION", "IES (In every Signature)", "SMS (Smart Selector)", "CONT (Considering Ontrology)", "BASOS (Based on significance value)"));

				return c;
			}
		};
	}

	private WizardPageDescriptor createValidationConfiguration() {
		return null;
	}

	@Override
	protected String getTaskName() {
		return "Generando metasignature";
	}

	@Override
	protected Object backgroundProcess(Monitor m) throws Exception {
		Thread.sleep(5000);
		return new Date();
	}

	@Override
	protected void doInUI(Object result) {
		System.out.println(result);
	}

}

package org.bioplat.classifiers.newWizards.evaluation;

import  static org.bioplat.classifiers.newWizards.evaluation.EvaluateClassifierWizard.SAMPLE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.gene.Gene;

public class EvaluateClassifierPageDescriptor extends WizardPageDescriptor {

	

	public EvaluateClassifierPageDescriptor() {
		super("Classifier Evaluation");
	}

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		final Composite container = Widgets.createDefaultContainer(parent);

		final Label info = new Label(container, SWT.WRAP);
		info.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		Button fromFileButton = new Button(container, SWT.FLAT);
		info.moveBelow(fromFileButton);
		fromFileButton.setText("Select Sample to Evaluate");
		fromFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(PlatformUIUtils.findShell());

				String samplefilename = fd.open();
				if (samplefilename == null)
					return;

				info.setText("Proccesing " + samplefilename);

				wmodel.set(SAMPLE, processFile(samplefilename));

				info.setText("File " + samplefilename + " successfully processed!");
				container.update();
				
				
				EvaluateClassifierPageDescriptor.this.fireUpdateButtons(wizardPage);
			}

		});

		return container;
	}

	

	@Override
	public boolean isPageComplete(WizardModel model) {
		return model.value(SAMPLE)!=null;
	}

	/**
	 * @return sample
	 */
	private Map<Gene, Double> processFile(String samplefilename) {
		final Map<Gene, Double> sample = Maps.newHashMap();
		try {
			Stream<String> stream = Files.lines(Paths.get(samplefilename));
			stream.forEach(new Consumer<String>() {

				@Override
				public void accept(String line) {
					String[] values = line.split(" ");
					Gene gene = MetaPlat.getInstance().findGene(values[0].trim());
					Double expression = Double.valueOf(values[0].trim());
					sample.put(gene, expression);
				}
			});
			return sample;
		} catch (IOException e) {
			MessageManager.INSTANCE.add(Message.error("Could not read the file " + samplefilename));
			return null;
		}
		

	}
}

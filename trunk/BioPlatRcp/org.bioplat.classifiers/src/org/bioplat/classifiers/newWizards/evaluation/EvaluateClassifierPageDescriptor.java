package org.bioplat.classifiers.newWizards.evaluation;

import static org.bioplat.classifiers.newWizards.evaluation.EvaluateClassifierWizard.SAMPLE;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.classifiers.exceptions.InvalidExpression;
import edu.unlp.medicine.domainLogic.framework.exceptions.GeneNotFoundByIdException;
import edu.unlp.medicine.entity.gene.Gene;

//TODO renombrar a selectSampleFile
public class EvaluateClassifierPageDescriptor extends WizardPageDescriptor {

	public EvaluateClassifierPageDescriptor() {
		super("Classifier Evaluation");
	}

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc,
			final WizardModel wmodel) {
		final Composite container = Widgets.createDefaultContainer(parent);

		final Label status = new Label(container, SWT.WRAP);
		status.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		Button fromFileButton = new Button(container, SWT.FLAT);
		status.moveBelow(fromFileButton);
		fromFileButton.setText("Select Sample to Evaluate");
		fromFileButton.addSelectionListener(processingSampleListener(wizardPage, wmodel, container, status));

		GUIUtils.addiItalicText(container, "Sample file format: \n" + 
				"    <gene1> <expression1>\n" + 
				"    <gene2> <expression2>\n" + 
				"   ...\n" + 
				"Where:\n" + 
				"   - gene and expression are separeated by tab character\n" + 
				"   - gene can be the gene id, an alternative gene id or the gene name", 8);

		return container;
	}

	private SelectionAdapter processingSampleListener(final WizardPage wizardPage, final WizardModel wmodel,
			final Composite container, final Label status) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(new Shell());

				String samplefilename = fd.open();
				if (samplefilename == null)
					return;

				status.setText("Proccesing " + samplefilename);

				try {
					wmodel.set(SAMPLE, processFile(samplefilename));
					status.setText("Sample file " + samplefilename + " successfully processed!");
				} catch (GeneNotFoundByIdException gnf) {
					MessageManager.INSTANCE.add(Message.error(gnf.getMessage()));
					status.setText(
							"Sample file '" + samplefilename + "' contains not valid data.\n" + gnf.getMessage());
				} catch (InvalidExpression ie) {
					MessageManager.INSTANCE.add(Message.error(ie.getMessage()));
					status.setText("Sample file '" + samplefilename + "' contains not valid data.\n" + ie.getMessage());
				}

				container.update();

				EvaluateClassifierPageDescriptor.this.fireUpdateButtons(wizardPage);
			}

		};
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return model.value(SAMPLE) != null;
	}

	/**
	 * @return sample
	 */
	//TODO refactorizar que no se entiende nada...
	private Map<Gene, Double> processFile(String samplefilename) {
		final Map<Gene, Double> sample = Maps.newHashMap();
		try {
			Stream<String> stream = Files.lines(Paths.get(samplefilename));
			try {
				stream.forEach(new Consumer<String>() {

					@Override
					public void accept(String line) {
						String[] lineValues = line.split("\t");
						Gene gene = readGene(lineValues[0]);
						Double expression = readExpression(gene, lineValues[1]);
						if (expression.isNaN() || expression.isInfinite())
							throw new InvalidExpression(gene, expression);
						sample.put(gene, expression);
					}

					private Gene readGene(String gene) {
						return MetaPlat.getInstance().findGene(gene.trim());
					}

					private Double readExpression(Gene gene, String expression) {
						try {
							return Double.valueOf(expression.trim());
						} catch (NumberFormatException nfe) {
							throw new InvalidExpression(gene, expression);
						}
					}
				});
			} finally {
				stream.close();
			}
			return sample;

		} catch (IOException e) {
			MessageManager.INSTANCE.add(Message.error("Could not read the file " + samplefilename));
			return null;
		}

	}
}

package org.bioplat.classifiers.newWizards.evaluation;

import static org.bioplat.classifiers.newWizards.evaluation.EvaluateClassifierWizard.CLASSIFIER;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.collect.Sets;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.classifiers.Classifier;
import edu.unlp.medicine.domainLogic.framework.classifiers.ListClassifiers;
import edu.unlp.medicine.entity.gene.Gene;

public class SelectClassifierPageDescriptor extends WizardPageDescriptor {

	private String author;
	private TableReference ref;
	private WizardModel wizarmodel;
	private TableReference ref2;

	public SelectClassifierPageDescriptor(String name, String author) {
		super(name);
		this.author = author;
	}

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc,
			final WizardModel wmodel) {

		Composite container = Widgets.createDefaultContainer(parent);

		new Label(container, SWT.NONE).setText("Select a Classifier");
		ComboViewer viewer = new ComboViewer(container, SWT.READ_ONLY);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(labelProvider());

		viewer.setInput(findClassifiers());
		viewer.addSelectionChangedListener(setClassifierListener(wizardPage, wmodel));

		createGeneMatching(container, wmodel);

		return container;
	}

	private void createGeneMatching(Composite container, WizardModel wmodel) {
		this.wizarmodel = wmodel;
		new Label(container, SWT.BOLD).setText("Matching Genes");
		ref = TableBuilder.tableBuilder(container).hideSelectionColumn()
				.addColumn(ColumnBuilder.create().title("Genes").property("name")).build();
	}

	private ISelectionChangedListener setClassifierListener(final WizardPage wizardPage, final WizardModel wmodel) {
		return new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection s = (StructuredSelection) event.getSelection();
				Classifier classifier = (Classifier) s.getFirstElement();
				wmodel.set(CLASSIFIER, classifier);

				updateView(wizardPage);
			}


		};
	}

	private void updateView(final WizardPage wizardPage) {
		SelectClassifierPageDescriptor.this.fireUpdateButtons(wizardPage);
		
		//update the matching table
		Map<Gene, Double> sample = wizarmodel.value(EvaluateClassifierWizard.SAMPLE);
		Classifier selected = wizarmodel.value(CLASSIFIER);

		Set<Gene> common = Sets.intersection(sample.keySet(), selected.genes());
		ref.input(common.stream().collect(Collectors.toList()));
	}

	private LabelProvider labelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object classifier) {
				if (classifier instanceof Classifier)
					return ((Classifier) classifier).name();
				return super.getText(classifier);
			}
		};
	}

	private List<Classifier> findClassifiers() {
		return new ListClassifiers().list(this.author);
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return model.value(CLASSIFIER) != null;
	}
}

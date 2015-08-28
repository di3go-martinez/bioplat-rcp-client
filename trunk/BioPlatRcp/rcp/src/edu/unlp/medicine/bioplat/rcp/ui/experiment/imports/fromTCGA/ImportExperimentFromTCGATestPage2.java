package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;
import edu.unlp.medicine.r4j.values.R4JStringDataMatrix;

public class ImportExperimentFromTCGATestPage2 extends WizardPageDescriptor {

	private TableViewer tv;
	private WizardPage wPage;
	private WizardModel wModel;
	public static String STUDY = "STUDY";
	
	
	public ImportExperimentFromTCGATestPage2(WizardModel wmodel) {
		super("Import from TCGA page 1 of 4");
		
	}
	
	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, 
			final DataBindingContext dbc, final WizardModel wmodel) {
		
		this.wPage = wizardPage;
		this.wModel = wmodel;
		
		R4JStringDataMatrix matrix = TCGAApi.getInstance().get_studies();
		ScrolledComposite container = new ScrolledComposite(parent, SWT.V_SCROLL);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
		container.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		createTable(container, matrix);
		return container;
	}

	private TableViewer createTable(Composite container, R4JStringDataMatrix input ){
		tv = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tv.setUseHashlookup(true);

		final Table table = tv.getTable();
		
		TableViewerColumn nameColumn = new TableViewerColumn(tv, SWT.NONE);
		TableViewerColumn descriptionColumn = new TableViewerColumn(tv, SWT.NONE);
		
		nameColumn.getColumn().setWidth(300);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[0];
			}
		});
		
		descriptionColumn.getColumn().setWidth(300);
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[1];
			}
		});
		
		
//		table.getVerticalBar().setVisible(true);
		table.setSize(650, 600);
		table.setHeaderVisible(true);
		table.setLinesVisible(true); 
		
		tv.setContentProvider(ArrayContentProvider.getInstance());
		tv.setInput(matrixToArray(input));
		
		tv.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				wPage.setPageComplete(isPageComplete(wModel));
				wModel.set(STUDY, (String[])((StructuredSelection) tv.getSelection()).getFirstElement() );
				
			}
		});
		return tv;
	}
	
	private ArrayList<String[]> matrixToArray(R4JStringDataMatrix matrix){
		ArrayList<String[]> arrayReturn = new ArrayList<String[]>();
		for (String[] row : matrix.getData()){
			arrayReturn.add(row);
		}
		return arrayReturn;
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return !tv.getSelection().isEmpty();
	}
}

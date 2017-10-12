/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Map;
import java.util.Observer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.AbstractSelectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Juan
 * 
 */
public class BiomarkerExperimentsEditor extends AbstractEditorPart<Biomarker>
		implements TableReferenceProvider2 {

	private static Logger logger = LoggerFactory.getLogger(BiomarkerExperimentsEditor.class);
	
	private TableReference tr;
	private BiomarkerExperimentsHelper helper;
	private GuiMaker guiMaker;

	public BiomarkerExperimentsEditor(boolean updatableTitle) {
		super(updatableTitle);
	}

	@Override
	public void setTableReference(TableReference tr) {
		this.tr = tr;
	}

	@Override
	public TableReference tableReference() {
		return this.tr;
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		this.guiMaker = new BiomarkerExperimetnsGUIMaker(this.getHelper(), this);
		this.guiMaker.build(parent);

	}

	@Override
	protected Observer createModificationObserver() {
		return getHelper();
	}

	protected BiomarkerExperimentsHelper getHelper() {
		if (this.helper == null)
			this.helper = new BiomarkerExperimentsHelper(this,
					new Provider<List<Validation>>() {

						@Override
						public List<Validation> get() {
							return model().getValidations();
						}
					});
		return this.helper;
	}
	
	@Override
	protected Map<Object, IStructuredSelection> getAdditionalSelections() {
		final IStructuredSelection element = new StructuredSelection(tr.focusedElements());
		final IStructuredSelection element1 = new StructuredSelection(tr.selectedElements());
		return ImmutableMap.of((Object) Constants.VALIDATIONS, element, Constants.SELECTED_VALIDATIONS, element1);
	}
	
}

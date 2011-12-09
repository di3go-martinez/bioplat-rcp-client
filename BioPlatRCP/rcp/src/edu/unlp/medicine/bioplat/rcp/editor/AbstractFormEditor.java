package edu.unlp.medicine.bioplat.rcp.editor;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.input.AbstractEditorInput;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public abstract class AbstractFormEditor<T extends AbstractEntity> extends FormEditor implements ModelProvider {

	private List<EditorDescription> editors = Lists.newArrayList();

	public AbstractFormEditor() {

	}

	private void configureEditors() {
		this.editors = Lists.newLinkedList(createEditors());
	}

	/**
	 * Se puede hacer getEditorInput
	 * 
	 * @return
	 */
	protected abstract List<EditorDescription> createEditors();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		configureEditors();
	}

	@Override
	protected void addPages() {
		for (EditorDescription ed : editors)
			try {
				int index = addPage(ed.editor(), ed.createEditorInput());
				setPageText(index, ed.title());
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IProgressMonitor submonitor = new Submonitor(monitor);
		for (EditorDescription ed : editors)
			ed.editor().doSave(submonitor);
		monitor.done();
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public AbstractEditorInput<T> getEditorInput() {
		return (AbstractEditorInput<T>) super.getEditorInput();
	}

	@Override
	public T model() {
		return getEditorInput().model();
	}

}

package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class PlatformUIUtils {
	private PlatformUIUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param input
	 *            representaci�n de la entrada a abrir
	 * @param editorId
	 *            id del editor a abrir
	 * @return el editor
	 */
	public static IEditorPart openEditor(IEditorInput input, String editorId) {
		// IEditorDescriptor desc =
		// PlatformUI.getWorkbench().getEditorRegistry()
		// .getDefaultEditor();
		try {
			IEditorPart e = activePage().openEditor(input, editorId);
			return e;
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
	}

	public static IWorkbenchPage activePage() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		return page;
	}

	public static IViewPart openView(String viewId) {
		try {

			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Shell findShell() {
		Display d = findDisplay();
		return d.getActiveShell();
	}

	public static Display findDisplay() {
		Display d = Display.getCurrent();
		if (d == null)
			d = Display.getDefault();
		return d;
	}
}
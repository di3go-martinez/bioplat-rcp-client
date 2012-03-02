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

//TODO ejecutar en el contexto del ui-thread
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

	public static void openView(final String viewId) {
		openView(viewId, true);
	}

	public static Shell findShell() {
		final Holder<Shell> result = Holder.create();

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Display d = findDisplay();
				result.hold(d.getActiveShell());
			}
		});

		return result.value();
	}

	public static Display findDisplay() {
		Display d = Display.getCurrent();
		if (d == null)
			d = Display.getDefault();
		return d;
	}

	public static void openEditor(Object o, String id) {
		openEditor(EditorInputFactory.createDefaultEditorInput(o), id);

	}

	public static void openView(final String viewId, final boolean forceFocus) {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					IViewPart v = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
					if (forceFocus)
						v.setFocus();
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}

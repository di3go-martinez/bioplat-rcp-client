package edu.unlp.medicine.bioplat.rcp.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.eclipse.swt.graphics.Image;
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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

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
		openView(viewId, false);
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

	public static IEditorPart openEditor(Object o, String id) {
		return openEditor(EditorInputFactory.createDefaultEditorInput(o), id);

	}

	public static void openView(final String viewId, final boolean forceFocus) {
		// al abrir la vista se lleva el foco... no debería, por eso se le
		// devuelve al editor que estaba activo... si era una vista la que
		// estaba activa, qué pasa? uhmmm

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final IEditorPart editor = org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				try {
					IViewPart v = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
					// FIXME no va el forceFocus...
					if (forceFocus)
						v.setFocus();
					if (editor != null)
						editor.setFocus();

				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private static Cache<String, Image> imagesCache = CacheBuilder.newBuilder().build();

	public static Image findImage(final String imagename) {
		try {
			return imagesCache.get(imagename, new Callable<Image>() {

				@Override
				public Image call() throws Exception {
					return Activator.imageDescriptorFromPlugin(imagename).createImage();
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			// default.png existe...
			return findImage("default.png");
		}
	}
}

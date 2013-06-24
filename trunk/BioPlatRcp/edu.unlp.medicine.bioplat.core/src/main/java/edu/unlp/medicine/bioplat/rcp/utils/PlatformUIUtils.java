package edu.unlp.medicine.bioplat.rcp.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;

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

	public static <T extends IViewPart> T openView(final String viewId) {
		return openView(viewId, false);
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

	// TODO actualizar comentarios!
	public static <T extends IViewPart> T openView(final String viewId, final boolean forceFocus) {
		// al abrir la vista se lleva el foco... no debería, por eso se le
		// devuelve al editor que estaba activo... si era una vista la que
		// estaba activa, qué pasa? uhmmm

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				// final IEditorPart editor =
				// org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				try {
					// TODO Revisar mejro... no siempre debe abrirse la vista si
					// no se encuentra abierta.....
					IViewPart v = findView(viewId);
					if (v == null)
						v = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
					// FIXME no va el forceFocus...
					// if (forceFocus)
					// v.setFocus();
					// if (editor != null)
					// editor.setFocus();

				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		return findView(viewId);

	}

	/**
	 * 
	 * @param viewId
	 * @return retorna la vista o null si la misma no se encuentra o está
	 *         cerrada.
	 */
	public static <T extends IViewPart> T findView(final String viewId) {
		final Holder<T> holder = Holder.create();
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				holder.hold((T) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId));
			}
		});
		return holder.value();
	}

	private static Cache<String, Image> imagesCache = createImageCache();

	protected static Cache<String, Image> createImageCache() {
		return CacheBuilder.newBuilder().build();
	}

	/**
	 * Busca la imagen imagename. Las imagenes se cachean para agilizar futuros.
	 * Se crean utilizando un descriptor de imagen generado por el Activator del
	 * plugin usos
	 * 
	 * @param imagename
	 * @return
	 * @see Activator#imageDescriptorFromPlugin(String)
	 */
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

	public static <T> List<T> openedEditors(Class<T> modelClass) {
		List<T> editors = Lists.newArrayList();
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				for (IEditorReference editor : page.getEditorReferences()) {
					IEditorPart ed = editor.getEditor(false);

					if (ed instanceof ModelProvider) {
						ModelProvider<?> mp = (ModelProvider<?>) ed;
						if (modelClass.isAssignableFrom(mp.model().getClass()))
							editors.add((T) mp.model());
					}

				}
			}
		}
		return editors;
	}

	public static IWorkbench getWorkbench() {
		return activePage().getWorkbenchWindow().getWorkbench();
	}

	public static void openInformation(final String title, final String message) {
		findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openInformation(findShell(), title, message);
			}
		});
	}


	public static void openWarning(final String title, final String message) {
		findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openWarning(findShell(), title, message);
			}
		});
	}

	public static boolean openQuestion(final String title, final String message) {
		final BooleanHolder result = new BooleanHolder();
		findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				result.value = MessageDialog.openQuestion(findShell(), title, message); 
			}
		});
		return result.value;
	}
	
	public static void openError(final String title, final String message) {
		findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openError(findShell(), title, message);
			}
		});
	}
	
	
	public static void center(Shell shell) {
		Point p = centerPointFor(shell);
		shell.setLocation(p.x, p.y);
	}

	// ni idea si anda esto.. revisar
	public static void center(Shell relativee, Shell target) {
		Point p = centerPointFor(relativee);
		target.setLocation(p.x, p.y);
	}

	// FIXME no anda bien...
	public static Point centerPointFor(Shell shell) {
		Display display = shell.getDisplay();

		org.eclipse.swt.widgets.Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		return new Point(x, y);
	}

}

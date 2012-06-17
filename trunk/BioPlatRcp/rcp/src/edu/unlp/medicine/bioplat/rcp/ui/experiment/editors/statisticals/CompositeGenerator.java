package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * 
 * Generador del apartado que muestra los resultados para determinada aplicación
 * de un experimento. Puede mostrar una imágen, un resultado, etc. Da sorporte
 * para la copia al portapapeles con un botón @see createCopyTextButton
 * 
 * @author Diego Martínez
 * 
 * 
 * @see CompositeGenerator#fill(Composite)
 * @see #createCopyTextButton(Composite, String)
 */
abstract class CompositeGenerator {

	private FormToolkit toolkit;

	public CompositeGenerator(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}

	Composite generate(Composite parent) throws Exception {
		ScrolledForm result = toolkit.createScrolledForm(parent);
		result.setLayout(GridLayoutFactory.fillDefaults().create());

		Composite c = result.getBody();
		c.setLayout(GridLayoutFactory.fillDefaults().create());
		fill(c);

		return result;

	}

	protected FormToolkit toolkit() {
		return toolkit;
	}

	/**
	 * Crea los widgets que contendrá el composite
	 * 
	 * @param container
	 * @throws Exception
	 */
	protected abstract void fill(Composite container) throws Exception;

	/**
	 * Crea un botón cuya acción es copiar el string recibido como parámetro al
	 * clipboard del sistema
	 * 
	 * @param c
	 * @param textToBeCopied
	 */
	protected Button createCopyTextButton(Composite c, final String textToBeCopied) {
		final Clipboard cb = new Clipboard(PlatformUIUtils.findDisplay());

		Button copy = new Button(c, SWT.FLAT);
		copy.setText("Copy Script");
		copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String textData = textToBeCopied;
				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { textData }, new Transfer[] { textTransfer });
			}
		});
		return copy;
	}
}
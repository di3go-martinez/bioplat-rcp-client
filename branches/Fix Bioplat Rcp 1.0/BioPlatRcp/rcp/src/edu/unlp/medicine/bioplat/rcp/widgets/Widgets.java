package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.generic.AbstractEntity;

//TODO hacer un builder mejor...
public class Widgets {

	private Widgets() {
	}

	/**
	 * createLabel(container, label); createText(container, model, property);
	 * 
	 * @param container
	 * @param label
	 * @param model
	 * @param property
	 */
	public static Widget createTextWithLabel(Composite container, String label, AbstractEntity model, String property) {
		return createTextWithLabel(container, label, model, property, false);
	}

	public static Widget createTextWithLabel(Composite container, String label, AbstractEntity model, String property, boolean readonly) {
		createLabel(container, label);
		return createText(container, model, property, readonly);
	}

	public static void createText(Composite container, AbstractEntity model, String property) {
		createText(container, model, property, false);
	}

	public static Widget createText(Composite container, AbstractEntity model, String property, boolean readOnly) {
		return new CText(container, model, property).readOnly(readOnly);
	}

	/**
	 * Crea un composite, con grab a ambos lados y una columna + defaults
	 * 
	 * @param parent
	 * @return
	 * 
	 * @see GridDataFactory#fillDefaults()
	 * @see GridLayoutFactory#fillDefaults()
	 */
	public static Composite createDefaultContainer(Composite parent) {
		return createDefaultContainer(parent, 1);
	}

	// Todo hacer otra implementación usando FormToolkit??
	// private static FormToolkit toolkit = new
	// FormToolkit(PlatformUIUtils.findDisplay());

	public static Composite createDefaultContainer(Composite parent, int numColumns) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(numColumns).create());
		return container;
	}

	public static void createLabel(Composite container, String text) {
		new CLabel(container, SWT.NONE).setText(text);
	}

	public static void createPassword(Composite container, AbstractEntity model, String property) {
		createPassword(container, model, property, false);
	}

	public static void createPassword(Composite container, AbstractEntity model, String property, boolean readOnly) {
		new CText(container, model, property, SWT.PASSWORD).readOnly(readOnly);
	}

	public static void createMultiTextWithLabel(Composite container, String label, Biomarker model, String property) {
		createMultiTextWithLabel(container, label, model, property, false);
	}

	public static void createMultiTextWithLabel(Composite container, String label, Biomarker model, String property, boolean readonly) {
		createLabel(container, label);
		new CText(container, model, property, SWT.MULTI).readOnly(readonly);
	}

	public static void createTextListWithLabel(Composite parent, String label, Biomarker model, String property) {
		createLabel(parent, label);
		new CList(parent, model, property, SWT.BORDER);
	}

	public static void retarget(AbstractEntity newmodel, Widget... widgets) {
		for (Widget w : widgets)
			w.retarget(newmodel);
	}

}

package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class Widgets {
    // TODO acomodar... hacer un configurador mejor... como el ColumnBuilder,
    // TableBuilder, etc
    private static boolean readonly = false;;

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
    public static void createTextWithLabel(Composite container, String label, AbstractEntity model, String property) {
        createLabel(container, label);
        createText(container, model, property);
    }

    public static void createText(Composite container, AbstractEntity model, String property) {
        new CText(container, model, property).readOnly(readonly);
	}

	public static Composite createDefaultContainer(Composite parent) {
		Composite container = new Composite(parent, SWT.BORDER);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		container.setLayout(GridLayoutFactory.fillDefaults().create());
		return container;
	}

	public static void createLabel(Composite container, String text) {
        new CLabel(container, SWT.NONE).setText(text);
	}

    public static void createPassword(Composite container, AbstractEntity model, String property) {
        new CText(container, model, property, SWT.PASSWORD).readOnly(readonly);
	}

    @Deprecated
    public static void readonly() {
        readonly = true;
    }

    @Deprecated
    public static void noreadonly() {
        readonly = false;
    }

    public static void createMultiTextWithLabel(Composite container, String label, Biomarker model, String property) {
        createLabel(container, label);
        new CText(container, model, property, SWT.MULTI).readOnly(readonly);
    }

}

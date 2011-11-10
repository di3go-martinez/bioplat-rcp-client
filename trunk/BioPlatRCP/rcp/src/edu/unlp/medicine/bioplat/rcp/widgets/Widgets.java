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

    private Widgets() {}

    /**
     * createLabel(container, label); createText(container, model, property);
     * 
     * @param container
     * @param label
     * @param model
     * @param property
     */
    public static void createTextWithLabel(Composite container, String label, AbstractEntity model, String property) {
        createTextWithLabel(container, label, model, property, false);
    }

    public static void createTextWithLabel(Composite container, String label, AbstractEntity model, String property, boolean readonly) {
        createLabel(container, label);
        createText(container, model, property, readonly);
    }

    public static void createText(Composite container, AbstractEntity model, String property) {
        createText(container, model, property, false);
    }

    public static void createText(Composite container, AbstractEntity model, String property, boolean readOnly) {
        new CText(container, model, property).readOnly(readOnly);
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
}

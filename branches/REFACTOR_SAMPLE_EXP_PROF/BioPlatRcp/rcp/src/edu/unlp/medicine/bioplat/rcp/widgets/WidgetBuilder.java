package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.entity.generic.AbstractEntity;

public class WidgetBuilder {

    private Control control;

    private WidgetBuilder(Control c) {
        control = c;
    }

    public static WidgetBuilder createText(Composite c) {
        return new WidgetBuilder(new Text(c, SWT.BORDER));
    }

    public static WidgetBuilder createList(Composite parent, AbstractEntity model, String property) {
        List list = new List(parent, SWT.V_SCROLL | SWT.BORDER);
        DataBindingContextHolder.dataBindingGlobalContext().bindList(SWTObservables.observeItems(list), BeansObservables.observeList(model, property));
        return new WidgetBuilder(list);
    }

    public WidgetBuilder layoutData(Object data) {
        control.setLayoutData(data);
        return this;
    }

}

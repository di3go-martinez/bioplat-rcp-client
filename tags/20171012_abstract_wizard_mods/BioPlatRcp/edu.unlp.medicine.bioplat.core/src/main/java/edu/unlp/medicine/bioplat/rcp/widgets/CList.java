package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import edu.unlp.medicine.entity.generic.AbstractEntity;

public class CList {

    private List list;
    private Binding binding;

    public CList(Composite parent, AbstractEntity model, String property, int style) {
        list = new List(parent, style | SWT.V_SCROLL);
        binding = DataBindingContextHolder.dataBindingGlobalContext()
                .bindList(SWTObservables.observeItems(list), BeansObservables.observeList(model, property));
    }

}

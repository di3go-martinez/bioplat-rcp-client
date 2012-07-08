package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.swt.widgets.Widget;

@Deprecated
public class CustomWidget<T extends Widget> {
	private T widget;

	CustomWidget(T widget) {
		this.widget = widget;
	}

	/* package */T widget() {
		return widget;
	}
}

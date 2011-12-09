package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;

public class MessageViewPart extends ViewPart {

	private TableReference tr;

	public static String id() {
		return "edu.medicine.bioplat.rcp.message.view";
	}

	public MessageViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		Composite c = new Composite(parent, SWT.BORDER);
		TableBuilder tb = TableBuilder.create(c)//
				.hideSelectionColumn().hideTableLines()//
				.addColumn(ColumnBuilder.create().property("text")).input(MessageManager.INSTANCE.getMessages());

		tr = tb.build();

		GridLayoutFactory.fillDefaults().generateLayout(c);
	}

	@Override
	public void setFocus() {
		tr.refresh();
	}

}

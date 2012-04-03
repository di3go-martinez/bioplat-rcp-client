package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.AbstractDataTransformer;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;

/**
 * Vista de mensajes
 * 
 * @author diego
 * 
 */
public class MessageViewPart extends ViewPart {

	private TableReference tr;

	public static String id() {
		return "edu.medicine.bioplat.rcp.message.view";
	}

	public MessageViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		Action action = new Action("") {
			{
				setImageDescriptor(Activator.imageDescriptorFromPlugin("resources/icons/clear.png"));
			}

			@Override
			public void run() {
				MessageManager.INSTANCE.clear();
				tr.refresh();
			}
		};
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager dropDownMenu = actionBars.getMenuManager();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		dropDownMenu.add(action);
		toolBar.add(action);

		Composite c = new Composite(parent, SWT.BORDER);
		TableBuilder tb = TableBuilder.create(c)//
				.hideSelectionColumn().hideTableLines()//
				.addColumn(ColumnBuilder.create().width(20).resizable(false).labelprovider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return "";
					}

					@Override
					public Image getImage(Object element) {
						String msgtype = ((Message) element).getType().getIconName();
						return PlatformUI.getWorkbench().getSharedImages().getImage(msgtype);
					}
				})) //
				.addColumn(ColumnBuilder.create().property("createdAt").transformer(new AbstractDataTransformer<Date, String>() {
					private final DateFormat df = DateFormat.getInstance();

					@Override
					public String doTransform(Date fecha) {
						return df.format(fecha);
					}
				})) //
				.addColumn(ColumnBuilder.create().property("text").width(450)) //
				.input(MessageManager.INSTANCE.getMessages());

		tr = tb.build();

		GridLayoutFactory.fillDefaults().generateLayout(c);
	}

	@Override
	public void setFocus() {
		tr.refresh();
	}

}

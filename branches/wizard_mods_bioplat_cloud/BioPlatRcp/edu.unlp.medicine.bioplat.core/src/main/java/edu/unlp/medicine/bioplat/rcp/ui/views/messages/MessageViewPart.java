package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.AbstractDataTransformer;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * Vista de mensajes
 * 
 * @author diego martínez
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

		Action actionClean = new Action("Clear Messages", Activator.imageDescriptorFromPlugin("resources/icons/clear.png")) {

			@Override
			public void run() {
				MessageManager.INSTANCE.clear();
				refresh();
			}
		};
		
		Action actionExport = new Action("Export Messages", Activator.imageDescriptorFromPlugin("resources/icons/export2.png")) {

			@Override
			public void run() {
				saveDialog(tr);
			}
		};
		
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager dropDownMenu = actionBars.getMenuManager();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		dropDownMenu.add(actionClean);
		toolBar.add(actionClean);
		dropDownMenu.add(actionExport);
		toolBar.add(actionExport);
		
			

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
				.addColumn(ColumnBuilder.create().title("Created at").property("createdAt").transformer(new AbstractDataTransformer<Date, String>() {
					private final DateFormat df = DateFormat.getInstance();

					@Override
					public String doTransform(Date fecha) {
						return df.format(fecha);
					}
				})) //
				.addColumn(ColumnBuilder.create().title("Message").property("text").width(1000)) //
				.input(MessageManager.INSTANCE.getMessages()).contextualMenuBuilder(createMenuBuilder());

		tr = tb.build();

		//TODO tr.sort("createdAt", SWT.DOWN);

		GridLayoutFactory.fillDefaults().generateLayout(c);
	}

	private MenuBuilder createMenuBuilder() {
		return new MenuBuilder() {

			@Override
			public void build(Menu menu) {
				Image openImage = PlatformUIUtils.findImage("openSelection.gif");
				MenuItemContribution.create(menu).image(openImage).text("Open Selection").addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Message message = (Message) tr.focusedElements().get(0);
						openDialog(message);
					}

				});
				// Exportador
				Image exportImage = PlatformUIUtils.findImage("export2.png");
				MenuItemContribution.create(menu).image(exportImage).text("Export Messages").addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
							saveDialog(tr);
							
					   }
				});
			}
		};
	}

	@Override
	public void setFocus() {
		refresh();
	}

	public void refresh() {
		tr.refresh();
	}

	public void focusAtLastLine() {
		// tr.getTable().select(tr.getTable().getItemCount());

	}

	private void openDialog(Message message) {
		MessageDialog.open(message.getType().kindForDialog(), null, "BioPlat", message.getText(), SWT.NONE);
	}
	
	private void saveDialog(TableReference tr){
		FileDialog fd = new FileDialog(PlatformUIUtils.findShell(), SWT.SAVE);
		fd.setText("Export");
        fd.setFilterPath(System.getProperty("user.home"));
        fd.setFilterExtensions(new String[]{ "*.txt","*.log","*.*" });
        String selected = fd.open();
		if(selected != null && !selected.isEmpty()){
			File file = new File(selected); 
			try {
			FileWriter fileWriter = new FileWriter(file);
			for(TableItem ti : tr.getTable().getItems()){
				fileWriter.write(ti.getText(2) + "\t" + ti.getText(3) + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
			MessageDialog.open(MessageDialog.INFORMATION, null, "BioPlat", "Messages were exported successfully.", SWT.NONE);
			MessageManager.INSTANCE.add(Message.info("Messages were exported successfully."));
			} catch (IOException e) {
				MessageDialog.open(MessageDialog.ERROR, null, "BioPlat", "Error when trying to export.", SWT.NONE);
				MessageManager.INSTANCE.add(Message.error(e.getMessage()));				
			}	
		}
	}
	
}

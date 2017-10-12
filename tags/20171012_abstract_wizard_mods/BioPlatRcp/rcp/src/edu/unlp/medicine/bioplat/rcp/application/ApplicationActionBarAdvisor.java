package edu.unlp.medicine.bioplat.rcp.application;

import static org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private static final String NEW_MENU_ID = "new.menu";
	private static final String BIOPLAT_MENU_ID = "bioplat.menu";
	private IActionBarConfigurer myconf;
	private IAction exportAction;
	private IAction aboutAction;
	private IAction exitAction;
	private IAction saveAction;
	private IAction preferencesAction;
	private IAction importAction;
	private IAction introAction;
	private IContributionItem showViewMenuAction;
	private IAction newWizard;
	private IAction about;
	private IAction checkConnection;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		myconf = configurer;
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		aboutAction = doMake(ActionFactory.ABOUT, window);
		importAction = doMake(ActionFactory.IMPORT, window);
		exportAction = doMake(ActionFactory.EXPORT, window);
		exitAction = doMake(ActionFactory.QUIT, window);
		saveAction = doMake(ActionFactory.SAVE, window);
		preferencesAction = doMake(ActionFactory.PREFERENCES, window);

		newWizard = doMake(ActionFactory.NEW, window);
		newWizard.setText("Gene Signatures/Datasets");
		newWizard.setImageDescriptor(Activator.imageDescriptorFromPlugin("startButton.png"));

		introAction = doMake(ActionFactory.INTRO, window);

		showViewMenuAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

		checkConnection = new CheckConnectionAction();
		checkConnection.setText("Check Remote Server Connection");
		checkConnection.setImageDescriptor(Activator.imageDescriptorFromPlugin("connection_x16.png"));
		
		about = new AboutAction();
	}

	private IAction doMake(ActionFactory af, IWorkbenchWindow window) {
		IAction action = af.create(window);
		register(action);
		return action;
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {

		MenuManager fileMenu = new MenuManager("&Start", BIOPLAT_MENU_ID);

		// bioplat.menu/new.menu/additions
		// MenuManager newMenu = new MenuManager("Nuevo", NEW_MENU_ID);
		// newMenu.add(new Separator(MB_ADDITIONS));
		// fileMenu.add(newMenu); // newMenu es submenú de mainMenu

		fileMenu.add(newWizard);

//		fileMenu.add(checkConnection);
		
		// fileMenu.add(saveAction);
		fileMenu.add(new Separator());
		// fileMenu.add(importAction);
		// fileMenu.add(exportAction);
//		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		MenuManager windowMenu = new MenuManager("&Window", "ventana.menu");
		windowMenu.add(new GroupMarker(MB_ADDITIONS));
		windowMenu.add(new Separator());

		IMenuManager m = new MenuManager("Views");
		m.add(showViewMenuAction);
		windowMenu.add(m);

		windowMenu.add(preferencesAction);
		windowMenu.add(introAction);
		windowMenu.add(about);

		// MenuManager helpMenu = new MenuManager("&Ayuda", "ayuda.menu");
		// helpMenu.add(aboutAction);

		// top level additions
		menuBar.add(fileMenu);

		MenuManager connections = new MenuManager("&CheckConnections", "ventana.menuConeccion");
		connections.add(checkConnection);
		
		// Used to check connection between the client and the remote server
		menuBar.add(connections);
		
		// utilizado por el contributerClass de los editores; "Operations Menu"
		menuBar.add(new GroupMarker("entidad.additions"));
		// MenuManager mm = new MenuManager("ABM");
		// menuBar.add(mm);
		// mm.add(new Separator("abm.additions"));
		menuBar.add(windowMenu);
		// menuBar.add(helpMenu);

	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		ToolBarManager tbm = new ToolBarManager(SWT.FLAT);
		tbm.add(newWizard);
//		tbm.add(checkConnection);
		tbm.add(new GroupMarker("agroup"));
		tbm.add(new Separator(MB_ADDITIONS));
		coolBar.add(tbm);
		// tbm = new ToolBarManager(SWT.FLAT);
		// tbm.add(saveAction);
		// coolBar.add(tbm);
	}
}

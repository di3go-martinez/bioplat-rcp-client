package edu.unlp.medicine.bioplat.rcp.application;

import static org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
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
	}

	private IAction doMake(ActionFactory af, IWorkbenchWindow window) {
		IAction action = af.create(window);
		register(action);
		return action;
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager mainMenu = new MenuManager("&Bio Plat", BIOPLAT_MENU_ID);

		MenuManager newMenu = new MenuManager("Nuevo", NEW_MENU_ID);
		// bioplat.menu/new.menu/additions
		newMenu.add(new Separator(MB_ADDITIONS));
		mainMenu.add(newMenu);
		mainMenu.add(saveAction);

		mainMenu.add(new Separator());

		mainMenu.add(importAction);
		mainMenu.add(exportAction);

		mainMenu.add(new Separator());

		mainMenu.add(exitAction);

		MenuManager helpMenu = new MenuManager("&Ayuda", "ayuda.menu");
		helpMenu.add(aboutAction);

		// top level additions
		menuBar.add(mainMenu);
		// menuBar.add(helpMenu);
	}
}

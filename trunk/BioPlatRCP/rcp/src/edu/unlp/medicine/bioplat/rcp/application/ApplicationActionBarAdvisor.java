package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private static IActionBarConfigurer myconf;
    private IAction export;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(myconf = configurer);
    }

    @Override
    protected void makeActions(IWorkbenchWindow window) {
        export = ActionFactory.EXPORT.create(window);
        register(export);
    }

    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager mm = new MenuManager("BioPlat");
        menuBar.add(mm);
        menuBar.add(export);
    }

}

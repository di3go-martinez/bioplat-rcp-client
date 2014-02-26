package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static Logger logger = LoggerFactory.getLogger(ApplicationWorkbenchAdvisor.class);

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return BioPlatPerspective.id();
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	// TODO eventualmente manejar un error
	@Override
	public void eventLoopException(Throwable exception) {
		Message m = Message.error("Error inesperado", exception);
		MessageManager.INSTANCE.add(m);
		logger.error("Error inesperado", exception);
		super.eventLoopException(exception);
	}
}

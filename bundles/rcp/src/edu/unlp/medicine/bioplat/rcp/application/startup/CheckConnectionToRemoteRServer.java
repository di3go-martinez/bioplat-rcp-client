package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.bioplat.r4j.R4JClient.connections.R4JConnection;
import org.eclipse.ui.IStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

public class CheckConnectionToRemoteRServer implements IStartup {

	protected static final Logger logger = LoggerFactory.getLogger(CheckConnectionToRemoteRServer.class);
	public static String errorMessage = "Connection to Bioplat Server failed. \nAre you connected to internet? Please, check it and test the connection again using the CheckConnections Menu. \nIf you are not connected to Bioplat server you can not do any statistic analysis.";
	
	@Override
	public void earlyStartup() {
		try {
			R4JConnection conn = R4JConnection.getInstance();
			if (!conn.isConnected()){
				PlatformUIUtils.openWarning("Could not connect to remote server", errorMessage);
				MessageManager.INSTANCE.add(Message.warn(errorMessage));
			} else {
				logger.info("Connection checked.");
				MessageManager.INSTANCE.add(Message.info("You are succesfully connect to Bioplat Server"));
			}
		} catch (Exception e) {
			PlatformUIUtils.openWarning("Could not connect to remote server", errorMessage);
			MessageManager.INSTANCE.add(Message.warn(errorMessage));
		}
	}

}

package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.bioplat.r4j.R4JClient.connections.R4JConnection;
import org.eclipse.ui.IStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

public class CheckConnectionToRemoteRServer implements IStartup {

	protected static final Logger logger = LoggerFactory.getLogger(CheckConnectionToRemoteRServer.class);
	
	@Override
	public void earlyStartup() {
		try {
			R4JConnection conn = R4JConnection.getInstance();
			if (!conn.isConnected()){
				PlatformUIUtils.openWarning("Could not connect to remote server", "There may be a problem with the connection to the internet or the server is under maintenance. Please, check your connection again from the CheckConnections Menu");
			} else {
				logger.info("Connection checked.");
			}
		} catch (Exception e) {
			PlatformUIUtils.openWarning("Could not connect to remote server", "There may be a problem with the connection to the internet or the server is under maintenance. Please, check your connection again from the CheckConnections Menu");
		}
	}

}

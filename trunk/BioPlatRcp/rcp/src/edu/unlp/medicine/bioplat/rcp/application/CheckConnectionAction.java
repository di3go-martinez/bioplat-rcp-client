package edu.unlp.medicine.bioplat.rcp.application;

import org.bioplat.r4j.R4JClient.connections.R4JConnection;
import org.eclipse.jface.action.Action;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

public class CheckConnectionAction extends Action{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		super.run();
		R4JConnection conn = R4JConnection.getInstance();
		if (!conn.isConnected()){
			PlatformUIUtils.openWarning("Could not connect to remote server", "There may be a problem with the connection to the internet or the server is under maintenance. Please, check your connection again from the Check Connections Menu");
		} else {
			PlatformUIUtils.openInformation("Connection to remote server established", "The Bioplat client could successfuly connect to the remote server");
		}
	}

		

}

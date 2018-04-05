package org.bioplat.rcp.conditions;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;

import edu.unlp.medicine.domainLogic.framework.MetaPlat;

public class Conditions {

	
	public static ICondition initialized() {
		return new ICondition() {

			@Override
			public boolean test() throws Exception {
				MetaPlat.getInstance();
				return true;
			}

			@Override
			public void init(SWTBot bot) {
				// TODO Auto-generated method stub
			}

			@Override
			public String getFailureMessage() {
				return "No se pudo inicializar correctamente la app";
			}
		};
	}
}

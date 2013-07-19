package edu.unlp.medicine.bioplat.rcp.widgets.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Text;

import com.google.common.annotations.Beta;

@Beta
public class ModificationTextEvent {

	private ModifyEvent event;

	public ModificationTextEvent(ModifyEvent event) {
		this.event = event;
	}

	public String getNewText() {
		return ((Text) event.widget).getText();
	}

}

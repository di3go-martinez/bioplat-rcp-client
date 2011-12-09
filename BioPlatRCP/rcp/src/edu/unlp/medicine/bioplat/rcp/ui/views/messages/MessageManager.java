package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * ENUMERATIVO PARA MANEJO AUTOMÁTICO DE LA INSTANCIA, ETCS.
 * 
 * @author diego
 * 
 */

public enum MessageManager {
	INSTANCE;

	private List<Message> messages = Lists.newArrayList();

	public List<Message> getMessages() {
		return messages;
	}

	public void clear() {
		messages.clear();
	}

	public void add(Message msg) {
		messages.add(msg);
	}

}

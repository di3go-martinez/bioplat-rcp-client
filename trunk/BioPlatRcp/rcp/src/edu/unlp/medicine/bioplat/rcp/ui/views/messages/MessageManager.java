package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * Enumerativo para manejo automático de la instancia (sic).
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

	public MessageManager clear() {
		messages.clear();

		return this;
	}

	public MessageManager add(Message msg) {
		messages.add(msg);
		// openView();// abre la vista cuando se agrega un mensaje//TODO
		// analizar si siempre o cuando es un error...
		return this;
	}

	public boolean isThereAnyMessage() {
		return !getMessages().isEmpty();
	}

	public MessageManager openView() {
		// TODO revisar el forzado del setFocus
		PlatformUIUtils.openView(MessageViewPart.id(), true);
		return this;
	}
}

package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * Enumerativo para manejo automático de la instancia (sic).
 * 
 * @author diego martínez
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
		msg.checkRequiresView(this);
		refresh();
		return this;
	}

	private void refresh() {
		MessageViewPart view = PlatformUIUtils.findView(MessageViewPart.id());
		if (view != null)
			view.refresh();
	}

	public boolean isThereAnyMessage() {
		return !getMessages().isEmpty();
	}

	public MessageManager openView() {
		PlatformUIUtils.openView(MessageViewPart.id());
		return this;
	}

}

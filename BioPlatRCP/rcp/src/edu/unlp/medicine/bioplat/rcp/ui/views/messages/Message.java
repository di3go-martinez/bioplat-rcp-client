package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * Elementos a mostrar en la vista de mensajes
 * 
 * @author diego
 * 
 */
// FIXME sacar el extends cuando se pueda...
public class Message extends AbstractEntity {
	private String message;

	public Message(String message) {
		this.message = message;
	}

	public String getText() {
		return message;
	}
}

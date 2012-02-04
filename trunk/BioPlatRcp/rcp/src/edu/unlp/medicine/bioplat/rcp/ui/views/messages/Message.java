package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.util.Date;

import org.eclipse.ui.ISharedImages;

import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * Elementos a mostrar en la vista de mensajes
 * 
 * @see MessageManager#add(Message)
 * 
 * @author diego
 * 
 */
// FIXME sacar el extends cuando se pueda...
public class Message extends AbstractEntity implements Comparable<Message> {
	public static enum MessageType {

		INFO(ISharedImages.IMG_OBJS_INFO_TSK), WARN(ISharedImages.IMG_OBJS_WARN_TSK), ERROR(ISharedImages.IMG_OBJS_ERROR_TSK);

		private String image;

		private MessageType(String name) {
			image = name;
		}

		public String getIconName() {
			return image;
		}
	}

	private String message;
	private MessageType type;
	private Date createdAt;

	/**
	 * 
	 * @param message
	 * @deprecated usar Message#info
	 */
	@Deprecated
	public Message(String message) {
		this(message, MessageType.INFO);
	}

	private Message(String message, MessageType type) {
		this.message = message;
		this.type = type;
		this.createdAt = new Date();
	}

	public static Message info(String text) {
		return new Message(text, MessageType.INFO);
	}

	public static Message warn(String text) {
		return new Message(text, MessageType.WARN);
	}

	public static Message error(String text) {
		return new Message(text, MessageType.ERROR);
	}

	public static Message error(String text, Throwable exception) {
		String cause = exception.getCause().toString();
		if (cause != null)
			text += ". Causa: " + cause;
		return error(text);
	}

	// ///
	public String getText() {
		return message;
	}

	public MessageType getType() {
		return type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@Override
	public int compareTo(Message o) {
		return this.getType().compareTo(o.getType());
	}
}

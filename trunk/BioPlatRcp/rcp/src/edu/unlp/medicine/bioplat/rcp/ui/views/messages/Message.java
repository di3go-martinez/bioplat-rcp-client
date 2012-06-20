package edu.unlp.medicine.bioplat.rcp.ui.views.messages;

import java.util.Date;

import org.eclipse.ui.ISharedImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger logger = LoggerFactory.getLogger(Message.class);

	public static enum MessageType {

		INFO(ISharedImages.IMG_OBJS_INFO_TSK), WARN(ISharedImages.IMG_OBJS_WARN_TSK), ERROR(ISharedImages.IMG_OBJS_ERROR_TSK);

		private String image;

		private MessageType(String name) {
			image = name;
		}

		public String getIconName() {
			return image;
		}

		public void checkRequiresView(MessageManager messageManager) {
			if (this.equals(ERROR))
				messageManager.openView();

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
		logger.info("(" + type + ")" + message);
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
		logger.error(text, exception);
		Throwable cause = exception.getCause();
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

	public void checkRequiresView(MessageManager messageManager) {
		getType().checkRequiresView(messageManager);
	}
}

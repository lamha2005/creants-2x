package com.creants.creants_2x.socket.gate.wood;

import java.util.List;

import com.creants.creants_2x.socket.gate.IMessageWriter;
import com.creants.creants_2x.socket.gate.MessageHandler;

/**
 * @author LamHa
 *
 */
public class MessageWriter implements IMessageWriter {
	private MessageHandler messageHandler;

	public MessageWriter(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void writeMessage(User user, Message message) {
		messageHandler.send(user, message);
	}

	@Override
	public void writeMessage(List<User> users, Message message) {
		messageHandler.send(users, message);
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

}

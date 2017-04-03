package com.creants.creants_2x.core.event.handler;

import java.util.List;

import com.creants.creants_2x.core.api.ICoreAPI;
import com.creants.creants_2x.core.util.DefaultMessageFactory;
import com.creants.creants_2x.socket.gate.IChannelService;
import com.creants.creants_2x.socket.gate.IMessageWriter;
import com.creants.creants_2x.socket.gate.wood.Message;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * Lớp trừu tượng của một RequestHandler
 * 
 * @author LamHa
 *
 */
public abstract class AbstractRequestHandler implements IRequestHandler {
	protected IChannelService channelService;
	private IMessageWriter messageWriter;
	protected ICoreAPI coreApi;

	public AbstractRequestHandler() {
		initialize();
	}

	protected void writeMessage(User user, Message message) {
		messageWriter.writeMessage(user, message);
	}

	protected void writeMessage(List<User> users, Message message) {
		messageWriter.writeMessage(users, message);
	}

	protected void writeErrorMessage(User user, short serviceId, short errorCode, String errorMessage) {
		messageWriter.writeMessage(user, DefaultMessageFactory.createErrorMessage(serviceId, errorCode, errorMessage));
	}

	public void setMessageWriter(IMessageWriter messageWriter) {
		this.messageWriter = messageWriter;
	}

	public IChannelService getChannelService() {
		return channelService;
	}

	public void setCoreApi(ICoreAPI coreApi) {
		this.coreApi = coreApi;
	}

}

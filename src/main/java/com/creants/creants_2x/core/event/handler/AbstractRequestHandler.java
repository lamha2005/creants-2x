package com.creants.creants_2x.core.event.handler;

import java.util.List;

import com.creants.creants_2x.core.api.ICoreAPI;
import com.creants.creants_2x.core.util.DefaultMessageFactory;
import com.creants.creants_2x.socket.gate.IChannelService;
import com.creants.creants_2x.socket.gate.IMessageWriter;
import com.creants.creants_2x.socket.gate.entities.CASObject;
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


	protected void writeMessage(User user, CASObject message) {
		messageWriter.writeMessage(user, message);
	}


	protected void writeMessage(List<User> users, CASObject message) {
		messageWriter.writeMessage(users, message);
	}


	protected void writeErrorMessage(User user, String errorCmdId, short errorCode, String errorMessage) {
		messageWriter.writeMessage(user, DefaultMessageFactory.createErrorMessage(errorCmdId, errorCode, errorMessage));
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

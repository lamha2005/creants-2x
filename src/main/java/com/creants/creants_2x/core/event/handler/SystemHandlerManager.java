package com.creants.creants_2x.core.event.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.creants.creants_2x.socket.gate.IMessage;

/**
 * 
 * @author LamHa
 *
 */
public class SystemHandlerManager {
	private Map<Short, AbstractRequestHandler> systemHandler;

	// TODO review EventManager SFS
	public SystemHandlerManager() {
		systemHandler = new ConcurrentHashMap<Short, AbstractRequestHandler>();
		// systemHandler.put(SystemNetworkConstant.COMMAND_PING_PONG, new
		// PingPongRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_CONNECT, new
		// ConnectRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_DISCONNECT, new
		// DisconnectRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_LOGIN, new
		// LoginRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_LOGOUT, new
		// LogoutRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_JOIN_GAME, new
		// JoinGameRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_GET_GAME_LIST, new
		// GameListRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_JOIN_ROOM, new
		// JoinRoomRequestHandler());
		// systemHandler.put(SystemNetworkConstant.COMMAND_USER_LEAVE_ROOM, new
		// LeaveRoomRequestHandler());
	}

	public AbstractRequestHandler getHandler(Short commandId) {
		return systemHandler.get(commandId);
	}

	public void dispatchEvent(IMessage message) {
		// TODO validate
		AbstractRequestHandler requestHandler = systemHandler.get(message.getCommandId());
		requestHandler.perform(message.getUser(), message);
	}
}

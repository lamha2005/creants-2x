package com.creants.creants_2x.core.util;

import com.creants.creants_2x.core.event.SystemNetworkConstant;
import com.creants.creants_2x.socket.gate.wood.Message;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public class DefaultMessageFactory {
	private static final byte PROTOCOL_VERSION = 1;

	public static Message createMessage(short commandId) {
		Message message = new Message();
		message.setCommandId(commandId);
		message.setProtocolVersion(PROTOCOL_VERSION);
		return message;
	}

	/**
	 * Tạo message lỗi.<br>
	 * Khi nào sử dụng createErrorMessage?<br>
	 * Khi client cần bắt những lỗi chung chung để hiện dialog.<br>
	 * Đối với các lỗi logic trong game thì nên trả về mã code lỗi theo command
	 * mà client request để client xử lý theo logic.
	 * 
	 * @param code
	 *            mã code lỗi
	 * @param errorMessage
	 *            thông tin lỗi
	 */
	public static Message createErrorMessage(short serviceId, short code, String errorMessage) {
		Message message = new Message();
		message.setCommandId(SystemNetworkConstant.COMMAND_ERROR);
		message.setProtocolVersion(PROTOCOL_VERSION);

		message.putShort(SystemNetworkConstant.KEYR_COMMAND_ID, serviceId);
		message.putShort(SystemNetworkConstant.KEYR_ERROR, code);
		message.putString(SystemNetworkConstant.KEYS_MESSAGE, errorMessage);
		return message;
	}

	public static Message responseMessage(short commandId) {
		return createMessage(commandId);
	}

	/**
	 * Tạo message connect
	 * 
	 * @param sessionId
	 * @return
	 */
	public static Message createConnectMessage(long sessionId) {
		Message message = new Message();
		message.setSessionId(sessionId);
		message.setCommandId(SystemNetworkConstant.COMMAND_USER_CONNECT);
		message.setProtocolVersion(PROTOCOL_VERSION);
		return message;
	}

	/**
	 * Tạo message disconnect
	 * 
	 * @param userId
	 * @return
	 */
	public static Message createDisconnectMessage(User user) {
		Message message = new Message();
		message.setUser(user);
		message.setCommandId(SystemNetworkConstant.COMMAND_USER_DISCONNECT);
		message.setProtocolVersion(PROTOCOL_VERSION);
		return message;
	}

	/**
	 * Tạo message trong game
	 * 
	 * @return
	 */
	public static Message createMessageInGame() {
		Message message = new Message();
		message.setCommandId(SystemNetworkConstant.COMMAND_USER_DISCONNECT);
		message.setProtocolVersion(PROTOCOL_VERSION);
		return message;
	}

}

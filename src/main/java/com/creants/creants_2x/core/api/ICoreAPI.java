package com.creants.creants_2x.core.api;

import java.util.List;

import com.creants.creants_2x.socket.gate.IMessage;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public abstract interface ICoreAPI {

	/**
	 * User thực hiện logout
	 * 
	 * @param user
	 */
	abstract void logout(User user);

	/**
	 * Thực hiện login
	 * 
	 * @param user
	 */
	abstract void login(User user);

	/**
	 * Kích người chơi khỏi bàn
	 * 
	 * @param owner
	 *            chủ bàn
	 * @param kickedUser
	 *            user bị kick
	 * @param paramString
	 * @param paramInt
	 */
	abstract void kickUser(User owner, User kickedUser, String paramString, int paramInt);

	abstract void disconnectUser(User user);

	abstract User getUserById(int userId);

	abstract User getUserByName(String name);

	abstract void sendExtensionResponse(IMessage message, List<User> recipients);

	abstract void sendExtensionResponse(IMessage message, User recipient);
}

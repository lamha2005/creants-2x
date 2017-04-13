package com.creants.creants_2x.core.api;

import java.util.List;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHa
 *
 */
public abstract interface IQAntAPI {

	/**
	 * User thực hiện logout
	 * 
	 * @param user
	 */
	abstract void logout(QAntUser user);


	/**
	 * Thực hiện login
	 * 
	 * @param user
	 */
	abstract void login(QAntUser user);


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
	abstract void kickUser(QAntUser owner, QAntUser kickedUser, String paramString, int paramInt);


	abstract void disconnectUser(QAntUser user);


	abstract QAntUser getUserById(int userId);


	abstract QAntUser getUserByName(String name);


	abstract void sendExtensionResponse(IQAntObject message, List<QAntUser> recipients);


	abstract void sendExtensionResponse(IQAntObject message, QAntUser recipient);
}

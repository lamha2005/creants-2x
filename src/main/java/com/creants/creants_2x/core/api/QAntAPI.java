package com.creants.creants_2x.core.api;

import java.util.List;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHa
 *
 */
public class QAntAPI implements IQAntAPI {

	@Override
	public void logout(QAntUser user) {
	}


	public void removeUser(int userId) {
	}


	@Override
	public void login(QAntUser user) {
	}


	@Override
	public void kickUser(QAntUser Owner, QAntUser kickedUser, String paramString, int paramInt) {

	}


	@Override
	public void disconnectUser(QAntUser user) {
	}


	@Override
	public QAntUser getUserById(int userId) {
		return null;
	}


	@Override
	public QAntUser getUserByName(String name) {
		return null;
	}


	@Override
	public void sendExtensionResponse(IQAntObject message, List<QAntUser> recipients) {

	}


	@Override
	public void sendExtensionResponse(IQAntObject message, QAntUser recipient) {

	}

}
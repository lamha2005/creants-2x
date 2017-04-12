package com.creants.creants_2x.core.api;

import java.util.List;

import com.creants.creants_2x.socket.gate.entities.ICASObject;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public class CoreAPI implements ICoreAPI {

	@Override
	public void logout(User user) {
	}


	public void removeUser(int userId) {
	}


	@Override
	public void login(User user) {
	}


	@Override
	public void kickUser(User Owner, User kickedUser, String paramString, int paramInt) {

	}


	@Override
	public void disconnectUser(User user) {
	}


	@Override
	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void sendExtensionResponse(ICASObject message, List<User> recipients) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendExtensionResponse(ICASObject message, User recipient) {
		// TODO Auto-generated method stub

	}

}
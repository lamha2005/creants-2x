package com.creants.creants_2x.socket.gate;

import java.util.List;

import com.creants.creants_2x.socket.gate.entities.CASObject;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public interface IMessageWriter {
	/**
	 * P2P Send message đến user
	 * 
	 * @param user
	 * @param message
	 */
	public void writeMessage(User user, CASObject message);


	/**
	 * P2G Send message đến danh sách user
	 * 
	 * @param user
	 * @param message
	 */
	public void writeMessage(List<User> user, CASObject message);

}

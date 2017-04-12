package com.creants.creants_2x.core.event.handler;

import com.creants.creants_2x.socket.gate.entities.ICASObject;
import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public interface IRequestHandler {
	/**
	 * Init các service, attribute cho một handler cụ thể
	 */
	public void initialize();


	/**
	 * Thực thi message request
	 * 
	 * @param user
	 *            đối tượng lưu trữ thông tin của user request
	 * @param message
	 *            message user gửi lên server
	 */
	public void perform(User user, ICASObject message);
}

package com.creants.creants_2x.core.api.response;

import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.exception.QAntErrorData;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public interface IResponseApi {

	/**
	 * Thông báo tất cả user trong group có room mới vừa được tạo
	 * 
	 * @param room
	 */
	void notifyRoomAdded(Room room);


	void notifyRequestError(QAntErrorData error, QAntUser receiver, SystemRequest request);


	void notifyRequestError(QAntException exception, QAntUser receiver, SystemRequest p2);


	void notifyJoinRoomSuccess(QAntUser recipient, Room joinedRoom);
}

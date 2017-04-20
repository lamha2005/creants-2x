package com.creants.creants_2x.core.api.response;

import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.exception.QAntErrorData;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;

/**
 * @author LamHM
 *
 */
public class ResponseApi implements IResponseApi {

	public void notifyRoomAdded(Room room) {
	}


	@Override
	public void notifyRequestError(QAntErrorData error, QAntUser receiver, SystemRequest request) {

	}


	@Override
	public void notifyRequestError(QAntException exception, QAntUser receiver, SystemRequest p2) {

	}


	@Override
	public void notifyJoinRoomSuccess(QAntUser recipient, Room joinedRoom) {
		IQAntObject resObj = QAntObject.newInstance();
		IResponse response = (IResponse) new Response();
		response.setId((short) SystemRequest.JoinRoom.getId());
		response.setContent(resObj);
		response.setRecipients(recipient.getChannel());
		response.write();
	}

}

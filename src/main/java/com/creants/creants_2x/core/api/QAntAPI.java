package com.creants.creants_2x.core.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.IQAntEventParam;
import com.creants.creants_2x.core.QAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.QAntEventType;
import com.creants.creants_2x.core.api.response.IResponseApi;
import com.creants.creants_2x.core.api.response.ResponseApi;
import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.entities.Zone;
import com.creants.creants_2x.core.exception.QAntCreateRoomException;
import com.creants.creants_2x.core.exception.QAntErrorCode;
import com.creants.creants_2x.core.exception.QAntErrorData;
import com.creants.creants_2x.core.exception.QAntJoinRoomException;
import com.creants.creants_2x.core.setting.CreateRoomSettings;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.managers.IUserManager;

import io.netty.channel.Channel;

/**
 * @author LamHa
 *
 */
public class QAntAPI implements IQAntAPI {
	protected final QAntServer qant;
	protected final IUserManager globalUserManager;
	protected final IResponseApi responseAPI;


	public QAntAPI(QAntServer qant) {
		this.qant = qant;
		globalUserManager = this.qant.getUserManager();
		responseAPI = new ResponseApi();
	}


	@Override
	public void logout(QAntUser user) {
		// TODO Auto-generated method stub

	}


	@Override
	public QAntUser login(Channel channel, String token, IQAntObject param) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public QAntUser login(Channel channel, String token, IQAntObject param, boolean forceLogout) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void kickUser(QAntUser owner, QAntUser kickedUser, String paramString, int paramInt) {
		// TODO Auto-generated method stub

	}


	@Override
	public void disconnectUser(QAntUser user) {
		// TODO Auto-generated method stub

	}


	@Override
	public void disconnect(Channel channel) {
		// TODO Auto-generated method stub

	}


	@Override
	public QAntUser getUserById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public QAntUser getUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Room createRoom(Zone zone, CreateRoomSettings roomSetting, QAntUser owner) throws QAntCreateRoomException {
		return createRoom(zone, roomSetting, owner, false, null, true, true);
	}


	@Override
	public Room createRoom(Zone zone, CreateRoomSettings roomSetting, QAntUser owner, boolean joinIt, Room roomToLeave)
			throws QAntCreateRoomException {
		return createRoom(zone, roomSetting, owner, joinIt, roomToLeave, true, true);
	}


	@Override
	public Room createRoom(Zone zone, CreateRoomSettings roomSetting, QAntUser owner, boolean joinIt, Room roomToLeave,
			boolean fireClientEvent, boolean fireServerEvent) throws QAntCreateRoomException {
		Room theRoom = null;
		try {
			String groupId = roomSetting.getGroupId();
			if (groupId == null || groupId.length() == 0) {
				roomSetting.setGroupId("default");
			}

			theRoom = zone.createRoom(roomSetting, owner);
			if (owner != null) {
				owner.addCreatedRoom(theRoom);
				owner.updateLastRequestTime();
			}

			if (fireClientEvent) {
				responseAPI.notifyRoomAdded(theRoom);
			}

			if (fireServerEvent) {
				Map<IQAntEventParam, Object> eventParams = new HashMap<IQAntEventParam, Object>();
				eventParams.put(QAntEventParam.ZONE, zone);
				eventParams.put(QAntEventParam.ROOM, theRoom);
				qant.getEventManager().dispatchEvent(new QAntEvent(QAntEventType.ROOM_ADDED, eventParams));
			}

		} catch (QAntCreateRoomException err) {
			if (fireClientEvent) {
				responseAPI.notifyRequestError(err, owner, SystemRequest.CreateRoom);
			}

			throw new QAntCreateRoomException(
					String.format("Room creation error. %s, %s, %s", err.getMessage(), zone, owner));
		}

		if (theRoom != null && owner != null && joinIt) {
			try {
				joinRoom(owner, theRoom, theRoom.getPassword(), false, roomToLeave, true, true);
			} catch (QAntJoinRoomException e) {
				QAntTracer.warn(this.getClass(),
						"Unable to join the just created Room: " + theRoom + ", reason: " + e.getMessage());
			}
		}

		return theRoom;
	}


	@Override
	public void joinRoom(QAntUser user, Room room) throws QAntJoinRoomException {
		joinRoom(user, room, "", false, user.getLastJoinedRoom());
	}


	@Override
	public void joinRoom(QAntUser user, Room roomToJoin, String password, boolean asSpectator, Room roomToLeave)
			throws QAntJoinRoomException {

		joinRoom(user, roomToJoin, password, asSpectator, roomToLeave, true, true);
	}


	@Override
	public void joinRoom(QAntUser user, Room roomToJoin, String password, boolean asSpectator, Room roomToLeave,
			boolean fireClientEvent, boolean fireServerEvent) throws QAntJoinRoomException {

		try {
			if (user.isJoining()) {
				throw new RuntimeException("Join request discarded. User is already in a join transaction: " + user);
			}

			user.setJoining(true);
			if (roomToJoin == null) {
				throw new QAntJoinRoomException("Requested room doesn't exist",
						new QAntErrorData(QAntErrorCode.JOIN_BAD_ROOM));
			}
			if (!roomToJoin.isActive()) {
				String message = String.format("Room is currently locked, %s", roomToJoin);
				QAntErrorData errData = new QAntErrorData(QAntErrorCode.JOIN_ROOM_LOCKED);
				errData.addParameter(roomToJoin.getName());
				throw new QAntJoinRoomException(message, errData);
			}

			boolean doorIsOpen = true;
			if (roomToJoin.isPasswordProtected()) {
				doorIsOpen = roomToJoin.getPassword().equals(password);
			}

			if (!doorIsOpen) {
				QAntErrorData data = new QAntErrorData(QAntErrorCode.JOIN_BAD_PASSWORD);
				data.addParameter(roomToJoin.getName());
				throw new QAntJoinRoomException(String.format("Room password is wrong, %s", roomToJoin), data);
			}

			roomToJoin.addUser(user, asSpectator);
			user.updateLastRequestTime();
			if (fireClientEvent) {
				responseAPI.notifyJoinRoomSuccess(user, roomToJoin);
				// TODO Báo cho các user khác user này join room, kèm theo các
				// tham
				// số là gì, nên customize lại trong extension
				// responseAPI.notifyUserEnterRoom(user, roomToJoin);

				// TODO báo trong group đó có bao nhiêu người chơi
				// responseAPI.notifyUserCountChange(user.getZone(),
				// roomToJoin);
			}

			if (fireServerEvent) {
				Map<IQAntEventParam, Object> evtParams = new HashMap<IQAntEventParam, Object>();
				evtParams.put(QAntEventParam.ZONE, user.getZone());
				evtParams.put(QAntEventParam.ROOM, roomToJoin);
				evtParams.put(QAntEventParam.USER, user);
				qant.getEventManager().dispatchEvent(new QAntEvent(QAntEventType.USER_JOIN_ROOM, evtParams));
			}

			if (roomToLeave != null) {
				leaveRoom(user, roomToLeave);
			}
		} catch (QAntJoinRoomException err) {
			if (fireClientEvent) {
				responseAPI.notifyRequestError(err, user, SystemRequest.JoinRoom);
			}

			throw new QAntJoinRoomException(String.format("Join Error - %s", err.getMessage()));
		} finally {
			user.setJoining(false);
		}
		user.setJoining(false);
	}


	@Override
	public void leaveRoom(QAntUser user, Room room) {
		// TODO Auto-generated method stub

	}


	@Override
	public void leaveRoom(QAntUser user, Room room, boolean fireClientEvent, boolean fireServerEvent) {
		// TODO Auto-generated method stub

	}


	@Override
	public void removeRoom(Room room) {
		// TODO Auto-generated method stub

	}


	@Override
	public void removeRoom(Room room, boolean fireClientEvent, boolean fireServerEvent) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendPublicMessage(Room room, QAntUser user, String message, IQAntObject param) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendPrivateMessage(QAntUser sender, QAntUser receiver, String message, IQAntObject param) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendExtensionResponse(IQAntObject message, List<QAntUser> recipients) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendExtensionResponse(IQAntObject message, QAntUser recipient) {
		// TODO Auto-generated method stub

	}

}
package com.creants.creants_2x.core.controllers;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IRequest;
import com.smartfoxserver.v2.exceptions.SFSExtensionException;

/**
 * @author LamHM
 *
 */
public class ExtensionController extends AbstractController {
	public static final String KEY_EXT_CMD = "c";
	public static final String KEY_EXT_PARAMS = "p";
	public static final String KEY_ROOMID = "r";


	public ExtensionController() {
	}


	@Override
	public void processRequest(IRequest request) throws Exception {
		QAntTracer.debug(this.getClass(), request.toString());
		long t1 = System.nanoTime();
		QAntUser sender = QAntServer.getInstance().getUserManager().getUserByChannel(request.getSender());
		if (sender == null) {
			throw new SFSExtensionException("Extension Request refused. Sender is not a User: " + request.getSender());
		}
		IQAntObject reqObj = (IQAntObject) request.getContent();
		QAntTracer.debug(this.getClass(), reqObj.getDump());

		String cmd = reqObj.getUtfString("c");
		if (cmd == null || cmd.length() == 0) {
			QAntTracer.warn(this.getClass(), "Extension Request refused. Missing CMD. " + sender);
			return;
		}

		int roomId = -1;
		if (reqObj.containsKey("r")) {
			roomId = reqObj.getInt("r");
		}

		IQAntObject params = reqObj.getQAntObject("p");
		// Zone zone = sender.getZone();
		// ISFSExtension extension = null;
		// if (roomId > -1) {
		// Room room = zone.getRoomById(roomId);
		// if (room != null) {
		// if (!room.containsUser(sender)) {
		// throw new SFSExtensionException(
		// "User cannot invoke Room extension if he's not joined in that Room. "
		// + sender + ", "
		// + room);
		// }
		// extension = extensionManager.getRoomExtension(room);
		// }
		// } else {
		// extension = extensionManager.getZoneExtension(zone);
		// }
		//
		// if (extension == null) {
		// throw new SFSExtensionException(String.format("No extensions can be
		// invoked: %s, RoomId: %s",
		// zone.toString(), (roomId == -1) ? "None" : roomId));
		// }
		// sender.updateLastRequestTime();
		// try {
		// extension.handleClientRequest(cmd, sender, params);
		// } catch (Exception e) {
		// QAntTracer.error(this.getClass(),
		// "Error while handling client request in extension: ??? , Extension
		// Cmd:" + cmd);
		// }
		final long t2 = System.nanoTime();
		QAntTracer.debug(this.getClass(), "Extension call executed in: " + (t2 - t1) / 1000000.0);
	}

}

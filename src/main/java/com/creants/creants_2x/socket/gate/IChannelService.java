package com.creants.creants_2x.socket.gate;

import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public interface IChannelService {
	void disconnect(long sessionId);

	void disconnect(User user);
}

package com.creants.creants_2x.socket.io;

import com.creants.creants_2x.socket.data.TransportType;
import com.creants.creants_2x.socket.gate.IQAntUser;

/**
 * @author LamHa
 *
 */
public interface IRequest extends IEngineMessage {
	TransportType getTransportType();

	void setTransportType(final TransportType p0);

	IQAntUser getSender();

	void setSender(IQAntUser sender);

	long getTimeStamp();

	void setTimeStamp(long requestTime);

	boolean isTcp();

	boolean isUdp();
}

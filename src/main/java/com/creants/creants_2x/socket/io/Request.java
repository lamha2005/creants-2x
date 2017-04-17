package com.creants.creants_2x.socket.io;

import com.creants.creants_2x.socket.data.TransportType;
import com.creants.creants_2x.socket.gate.IQAntUser;

/**
 * @author LamHa
 *
 */
public class Request extends AbstractEngineMessage implements IRequest {
	private IQAntUser sender;
	private long timeStamp;

	public Request() {
		this.timeStamp = System.nanoTime();
	}

	@Override
	public IQAntUser getSender() {
		return this.sender;
	}

	@Override
	public void setSender(IQAntUser session) {
		this.sender = session;
	}

	@Override
	public long getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public boolean isTcp() {
		return true;
	}

	@Override
	public boolean isUdp() {
		return false;
	}

	@Override
	public String toString() {
		return String.format("[Req Type: %s, Sender: %s]", TransportType.TCP.name(), this.sender);
	}
}

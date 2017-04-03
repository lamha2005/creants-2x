package com.creants.creants_2x.socket.gate.entities;

/**
 * @author Lamhm
 *
 */
public class CASDataWrapper {
	private CASDataType typeId;
	private Object object;

	public CASDataWrapper(CASDataType typeId, Object object) {
		this.typeId = typeId;
		this.object = object;
	}

	public CASDataType getTypeId() {
		return this.typeId;
	}

	public Object getObject() {
		return this.object;
	}
}

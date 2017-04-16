package com.creants.creants_2x.socket.io;

/**
 * @author LamHa
 *
 */
public interface IEngineMessage {
	Object getId();

	void setId(Object id);

	Object getContent();

	void setContent(Object qAntObject);

	Object getAttribute(String attr);

	void setAttribute(String attrKey, Object attrObj);
}

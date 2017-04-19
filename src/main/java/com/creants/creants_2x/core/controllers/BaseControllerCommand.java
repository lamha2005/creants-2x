package com.creants.creants_2x.core.controllers;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.api.IQAntAPI;

/**
 * @author LamHM
 *
 */
public abstract class BaseControllerCommand implements IControllerCommand {
	protected final QAntServer qant;
	protected final IQAntAPI api;
	private short id;


	public BaseControllerCommand(SystemRequest request) {
		qant = QAntServer.getInstance();
		api = qant.getApiManager().getQAntApi();
		id = (short) request.getId();
	}
	
	public short getId() {
        return this.id;
    }

}

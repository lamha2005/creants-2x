package com.creants.creants_2x.core.controllers.system;

import com.creants.creants_2x.core.controllers.IControllerCommand;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntDataWrapper;
import com.creants.creants_2x.socket.io.IRequest;

/**
 * @author LamHM
 *
 */
public class Login implements IControllerCommand {
	private static final String TOKEN = "_token";


	@Override
	public void execute(IRequest request) throws Exception {
		IQAntObject param = request.getContent();
		QAntDataWrapper token = param.get(TOKEN);

	}


	@Override
	public boolean validate(IRequest request) throws Exception {
		IQAntObject param = request.getContent();
		if (param == null || !param.containsKey(TOKEN))
			return false;

		return true;
	}


	@Override
	public Object preProcess(IRequest request) throws Exception {
		return null;
	}

}

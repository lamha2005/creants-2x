package com.creants.creants_2x.core.controllers;

import com.creants.creants_2x.socket.io.IRequest;

/**
 * @author LamHM
 *
 */
public interface IControllerCommand {
	boolean validate(IRequest request) throws Exception;

	Object preProcess(IRequest request) throws Exception;

	void execute(IRequest request) throws Exception;
}

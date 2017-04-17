package com.creants.creants_2x.core.controllers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LamHa
 *
 */
public class DefaultControllerManager implements IControllerManager {
	protected String name;
	protected ConcurrentMap<Byte, IController> controllers;

	public DefaultControllerManager() {
		controllers = new ConcurrentHashMap<Byte, IController>();
	}

	@Override
	public void init(Object o) {
		this.startAllControllers();
	}

	@Override
	public void destroy(Object o) {
		shutDownAllControllers();
		controllers = null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void handleMessage(Object message) {
	}

	@Override
	public void addController(byte id, IController controller) {
		controllers.putIfAbsent(id, controller);
	}

	@Override
	public IController getControllerById(byte id) {
		return controllers.get(id);
	}

	@Override
	public void removeController(byte id) {
		controllers.remove(id);
	}

	private synchronized void shutDownAllControllers() {
		for (IController controller : controllers.values()) {
			controller.destroy(null);
		}
	}

	private synchronized void startAllControllers() {
		for (IController controller : controllers.values()) {
			controller.init(null);
		}
	}

}

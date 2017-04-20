package com.creants.creants_2x.core;

import java.util.concurrent.Executor;

import com.smartfoxserver.bitswarm.service.IService;

/**
 * @author LamHM
 *
 */
public interface IQAntEventManager extends IQAntEventDispatcher, IService {
	void setThreadPoolSize(int size);


	Executor getThreadPool();
}

package com.creants.creants_2x.core.service;

/**
 * @author LamHa
 *
 */
public interface IService {
	void init(Object p0);

	void destroy(Object p0);

	void handleMessage(Object p0);

	String getName();

	void setName(String name);
}

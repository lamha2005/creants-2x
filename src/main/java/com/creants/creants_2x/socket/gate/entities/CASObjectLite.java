package com.creants.creants_2x.socket.gate.entities;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LamHM
 *
 */
public class CASObjectLite {
	private Map<String, CASDataWrapper> dataHolder;


	public CASObjectLite() {
		dataHolder = new ConcurrentHashMap<String, CASDataWrapper>();
	}


	public static CASObjectLite newInstance() {
		return new CASObjectLite();
	}


	@SuppressWarnings("unchecked")
	public Collection<Boolean> getBoolArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Boolean>) o.getObject();
	}


	public Byte getByte(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Byte) o.getObject();
	}


	@SuppressWarnings("unchecked")
	public Collection<Double> getDoubleArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Double>) o.getObject();
	}


	public Float getFloat(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Float) o.getObject();
	}


	@SuppressWarnings("unchecked")
	public Collection<Float> getFloatArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Float>) o.getObject();
	}


	@SuppressWarnings("unchecked")
	public Collection<Integer> getIntArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Integer>) o.getObject();
	}


	public Long getLong(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Long) o.getObject();
	}


	public Short getShort(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Short) o.getObject();
	}


	@SuppressWarnings("unchecked")
	public Collection<Short> getShortArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Short>) o.getObject();
	}


	@SuppressWarnings("unchecked")
	public Collection<String> getUtfStringArray(String key) {
		CASDataWrapper o = dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<String>) o.getObject();
	}

}

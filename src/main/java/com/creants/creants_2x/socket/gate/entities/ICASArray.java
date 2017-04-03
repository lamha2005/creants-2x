package com.creants.creants_2x.socket.gate.entities;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Lamhm
 *
 */
public interface ICASArray {
	boolean contains(Object key);

	Iterator<CASDataWrapper> iterator();

	Object getElementAt(int key);

	CASDataWrapper get(int key);

	void removeElementAt(int key);

	int size();

	byte[] toBinary();

	String toJson();

	String getHexDump();

	String getDump();

	String getDump(boolean key);

	void addNull();

	void addBool(boolean key);

	void addByte(byte key);

	void addShort(short key);

	void addInt(int key);

	void addLong(long key);

	void addFloat(float key);

	void addDouble(double key);

	void addUtfString(String key);

	void addText(String key);

	void addBoolArray(Collection<Boolean> key);

	void addByteArray(byte[] key);

	void addShortArray(Collection<Short> key);

	void addIntArray(Collection<Integer> key);

	void addLongArray(Collection<Long> key);

	void addFloatArray(Collection<Float> key);

	void addDoubleArray(Collection<Double> key);

	void addUtfStringArray(Collection<String> key);

	void addCASArray(ICASArray key);

	void addCASObject(ICASObject key);

	void addClass(Object key);

	void add(CASDataWrapper key);

	boolean isNull(int key);

	Boolean getBool(int key);

	Byte getByte(int key);

	Integer getUnsignedByte(int key);

	Short getShort(int key);

	Integer getInt(int key);

	Long getLong(int key);

	Float getFloat(int key);

	Double getDouble(int key);

	String getUtfString(int key);

	String getText(int key);

	Collection<Boolean> getBoolArray(int key);

	byte[] getByteArray(int key);

	Collection<Integer> getUnsignedByteArray(int key);

	Collection<Short> getShortArray(int key);

	Collection<Integer> getIntArray(int key);

	Collection<Long> getLongArray(int key);

	Collection<Float> getFloatArray(int key);

	Collection<Double> getDoubleArray(int key);

	Collection<String> getUtfStringArray(int key);

	Object getClass(int key);

	ICASArray getCASArray(int key);

	ICASObject getCASObject(int key);
}

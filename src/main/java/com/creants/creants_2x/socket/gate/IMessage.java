package com.creants.creants_2x.socket.gate;

import java.util.List;

import com.creants.creants_2x.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public interface IMessage {
	/**
	 * Lấy 1 dãy byte theo 1 key duy nhất.<br>
	 * Chú ý khác với {@link #getBytes(key)} lấy byte array khi push bằng nhiều
	 * key giống nhau.
	 */
	byte[] getBlob(short key);

	/**
	 * Lấy mãng byte 2 chiều theo 1 key duy nhất.
	 * 
	 * @param key
	 * @return
	 */
	byte[][] getBlobs(short key);

	Byte getByte(short key);

	/**
	 * Lấy byte array khi push bằng nhiều key giống nhau.<br>
	 * Chú ý khác với {@link #getBlob(key)} lấy 1 dãy byte theo 1 key duy nhất
	 */
	byte[] getBytes(short key);

	/**
	 * Lấy byte array theo vị trí của Message.
	 * 
	 * @param index
	 * @return
	 */
	byte[] getBytesAt(int index);

	Double getDouble(short key);

	/**
	 * Lấy double array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	double[] getDoubles(short key);

	Float getFloat(short key);

	/**
	 * Lấy float array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	float[] getFloats(short key);

	Integer getInt(short key);

	/**
	 * Lấy int array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	int[] getInts(short key);

	Short getKeyAt(int index);

	/**
	 * Lấy danh sách key
	 * 
	 * @return
	 */
	List<Short> getKeyList();

	Long getLong(short key);

	/**
	 * Lấy long array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	long[] getLongs(short key);

	short getCommandId();

	Short getShort(short key);

	/**
	 * Lấy short array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	short[] getShorts(short key);

	String getString(short key);

	byte getProtocolVersion();

	long getSessionId();

	int getUserId();

	boolean isEncrypt();

	User getUser();

	/**
	 * Lấy String array được push bằng nhiều key giống nhau.<br>
	 * 
	 * @param key
	 * @return
	 */
	String[] getStrings(short key);

	void putBytes(short key, byte[] paramArrayOfByte);

	void putByte(short key, byte paramByte);

	void putDouble(short key, double paramDouble);

	void putFloat(short key, float paramFloat);

	void putInt(short key, int paramInt);

	void putLong(short key, long paramLong);

	void putShort(short key, short paramShort);

	void putString(short key, String paramString);

	void setCommandId(short commandId);

}

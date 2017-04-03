package com.creants.creants_2x.socket.gate.wood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.core.event.SystemNetworkConstant;
import com.creants.creants_2x.socket.gate.IMessage;
import com.creants.creants_2x.socket.gate.IMessageContentInterpreter;
import com.creants.creants_2x.socket.util.ConverterUtil;

/**
 * Message xử lý bên trong Core - Game Logic. <br>
 * Tất cả các value sẽ lưu theo thứ tự đọc được từ client, hoặc là được build từ
 * GameLogic.
 * 
 * @author LamHa
 *
 */
public class Message implements IMessage {
	private static IMessageContentInterpreter interpreter;
	private byte protocolVersion;
	private boolean isEncrypt;
	private short commandId;
	private int userId;
	private long sessionId;
	private User user;
	private List<MessageParameter> content;
	private Map<Short, List<Integer>> keyIndexMap;

	public Message() {
		content = new ArrayList<MessageParameter>();
		keyIndexMap = new HashMap<Short, List<Integer>>();
	}

	public static void setIntepreter(IMessageContentInterpreter intepreter) {
		interpreter = intepreter;
	}

	public static void addInToIntepreter(Class<?>... clazz) {
		interpreter.addInToInterpreter(clazz);
	}

	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(byte protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	@Override
	public byte[] getBlob(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return keyValueInfo.value;
		}

		return null;
	}

	@Override
	public byte[][] getBlobs(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		byte[][] results = new byte[keyIndexes.size()][];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null && info.value.length >= 1) {
				results[i] = info.value;
			}
		}

		return results;
	}

	@Override
	public Byte getByte(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Byte(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public byte[] getBytes(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		byte[] results = new byte[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null && info.value.length >= 1) {
				results[i] = info.value[0];
			}
		}

		return results;
	}

	@Override
	public byte[] getBytesAt(int index) {
		if (index < 0 || index >= content.size()) {
			return null;
		}

		MessageParameter keyValueInfo = content.get(index);
		if (keyValueInfo != null) {
			return keyValueInfo.value;
		}

		return null;
	}

	public List<MessageParameter> getContent() {
		return content;
	}

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public Double getDouble(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Double(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public double[] getDoubles(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		double[] results = new double[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2Double(info.value).doubleValue();
			}
		}

		return results;
	}

	@Override
	public Float getFloat(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Float(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public float[] getFloats(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		float[] results = new float[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2Float(info.value).floatValue();
			}
		}

		return results;
	}

	@Override
	public Integer getInt(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Integer(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public int[] getInts(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		int[] results = new int[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2Integer(info.value).intValue();
			}
		}

		return results;
	}

	@Override
	public Short getKeyAt(int index) {
		if (index < 0 || index >= content.size()) {
			return null;
		}

		return content.get(index).key;
	}

	@Override
	public List<Short> getKeyList() {
		if (content == null || content.size() == 0) {
			return null;
		}

		int size = content.size();
		List<Short> keys = new ArrayList<Short>(size);
		for (int i = 0; i < size; i++) {
			keys.add(content.get(i).key);
		}

		return keys;
	}

	private MessageParameter getKeyValueInfo(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		return content.get(keyIndexes.get(0));
	}

	@Override
	public Long getLong(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Long(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public long[] getLongs(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		long[] results = new long[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2Long(info.value).longValue();
			}
		}

		return results;
	}

	@Override
	public short getCommandId() {
		return commandId;
	}

	public long getSessionId() {
		return sessionId;
	}

	@Override
	public Short getShort(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2Short(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public short[] getShorts(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		short[] results = new short[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2Short(info.value).shortValue();
			}
		}

		return results;
	}

	@Override
	public String getString(short key) {
		MessageParameter keyValueInfo = getKeyValueInfo(key);
		if (keyValueInfo != null) {
			return ConverterUtil.convertBytes2String(keyValueInfo.value);
		}

		return null;
	}

	@Override
	public String[] getStrings(short key) {
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			return null;
		}

		String[] results = new String[keyIndexes.size()];
		MessageParameter info = null;
		for (int i = 0, length = results.length; i < length; i++) {
			info = content.get(keyIndexes.get(i));
			if (info != null) {
				results[i] = ConverterUtil.convertBytes2String(info.value);
			}
		}

		return results;
	}

	@Override
	public final void putBytes(short key, byte[] value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = value;
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putByte(short key, byte value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = new byte[] { value };
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putDouble(short key, double value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertDouble2Bytes(Double.valueOf(value));
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putFloat(short key, float value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertFloat2Bytes(Float.valueOf(value));
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putInt(short key, int value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertInteger2Bytes(Integer.valueOf(value));
		putKeyValueInfo(key, info);
	}

	private void putKeyValueInfo(short key, MessageParameter info) {
		if (info == null || info.key == null || info.value == null) {
			return;
		}

		content.add(info);
		int keyIndex = content.size() - 1;
		List<Integer> keyIndexes = keyIndexMap.get(key);
		if (keyIndexes == null || keyIndexes.size() == 0) {
			keyIndexes = new ArrayList<Integer>();
			keyIndexMap.put(key, keyIndexes);
		}

		keyIndexes.add(Integer.valueOf(keyIndex));
	}

	@Override
	public final void putLong(short key, long value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertLong2Bytes(Long.valueOf(value));
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putShort(short key, short value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertShort2Bytes(Short.valueOf(value));
		putKeyValueInfo(key, info);
	}

	@Override
	public final void putString(short key, String value) {
		MessageParameter info = new MessageParameter();
		info.key = key;
		info.value = ConverterUtil.convertString2Bytes(value);
		putKeyValueInfo(key, info);
	}

	public void setContent(List<MessageParameter> content) {
		this.content = content;
	}

	public void setUserId(int clientId) {
		this.userId = clientId;
	}

	public void setCommandId(short commandId) {
		this.commandId = commandId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		if (interpreter == null) {
			return trace();
		}

		StringBuffer string = new StringBuffer("\n[" + interpreter.interpretCommand(commandId) + "] \n");
		for (MessageParameter info : content) {
			Short key = info.key;
			string.append(" " + interpreter.interpretKey(key) + "[" + key + "] =");
			if (key == SystemNetworkConstant.KEYR_ACTION_IN_GAME) {
				string.append(" " + interpreter.interpetActionInGameValue(key, info.value) + "\n");
			} else {
				string.append(" " + interpreter.interpetValue(key, info.value) + "\n");
			}
		}
		return string.toString();
	}

	private String trace() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ClientId:" + userId + ",\n");
		sb.append(" SessionId:" + sessionId + ",\n");
		sb.append(" Protocol:" + protocolVersion + ",\n");
		sb.append(" Command Event:" + commandId + ",\n");
		if (content.size() > 0) {
			for (MessageParameter messageParameter : content) {
				sb.append(" " + String.valueOf(messageParameter.key) + ":" + Arrays.toString(messageParameter.value)
						+ ",\n");
			}
			sb.deleteCharAt(sb.length() - 2);
		}

		return "\n{ \n" + sb.toString() + "}";
	}

}

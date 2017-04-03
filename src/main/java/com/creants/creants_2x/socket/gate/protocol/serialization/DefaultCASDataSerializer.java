package com.creants.creants_2x.socket.gate.protocol.serialization;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.util.StringUtils;

import com.creants.creants_2x.socket.exception.CASCodecException;
import com.creants.creants_2x.socket.exception.CASRuntimeException;
import com.creants.creants_2x.socket.gate.entities.CASArray;
import com.creants.creants_2x.socket.gate.entities.CASDataType;
import com.creants.creants_2x.socket.gate.entities.CASDataWrapper;
import com.creants.creants_2x.socket.gate.entities.CASObject;
import com.creants.creants_2x.socket.gate.entities.ICASArray;
import com.creants.creants_2x.socket.gate.entities.ICASObject;

/**
 * @author LamHM
 *
 */
public class DefaultCASDataSerializer implements ICASDataSerializer {
	private static final String CLASS_MARKER_KEY = "$C";
	private static final String CLASS_FIELDS_KEY = "$F";
	private static final String FIELD_NAME_KEY = "N";
	private static final String FIELD_VALUE_KEY = "V";
	private static DefaultCASDataSerializer instance;
	private static int BUFFER_CHUNK_SIZE;

	static {
		DefaultCASDataSerializer.instance = new DefaultCASDataSerializer();
		DefaultCASDataSerializer.BUFFER_CHUNK_SIZE = 512;
	}

	public static DefaultCASDataSerializer getInstance() {
		return DefaultCASDataSerializer.instance;
	}

	private DefaultCASDataSerializer() {
	}

	public int getUnsignedByte(final byte b) {
		return 0xFF & b;
	}

	@Override
	public String array2json(final List<Object> array) {
		// return JSONArray.fromObject((Object) array).toString();
		return null;
	}

	@Override
	public ICASArray binary2array(final byte[] data) {
		if (data.length < 3) {
			throw new IllegalStateException(
					"Can't decode an CASArray. Byte data is insufficient. Size: " + data.length + " bytes");
		}
		final ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return this.decodeCASArray(buffer);
	}

	private ICASArray decodeCASArray(final ByteBuffer buffer) {
		final ICASArray casArray = CASArray.newInstance();
		final byte headerBuffer = buffer.get();
		if (headerBuffer != CASDataType.CAS_ARRAY.getTypeID()) {
			throw new IllegalStateException(
					"Invalid CASDataType. Expected: " + CASDataType.CAS_ARRAY.getTypeID() + ", found: " + headerBuffer);
		}
		final short size = buffer.getShort();
		if (size < 0) {
			throw new IllegalStateException("Can't decode CASArray. Size is negative = " + size);
		}
		try {
			for (int i = 0; i < size; ++i) {
				final CASDataWrapper decodedObject = this.decodeObject(buffer);
				if (decodedObject == null) {
					throw new IllegalStateException("Could not decode CASArray item at index: " + i);
				}
				casArray.add(decodedObject);
			}
		} catch (Exception codecError) {
			throw new IllegalArgumentException(codecError.getMessage());
		}
		return casArray;
	}

	@Override
	public ICASObject binary2object(final byte[] data) {
		if (data.length < 3) {
			throw new IllegalStateException(
					"Can't decode an CASObject. Byte data is insufficient. Size: " + data.length + " bytes");
		}
		final ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return this.decodeCASObject(buffer);
	}

	private ICASObject decodeCASObject(final ByteBuffer buffer) {
		final ICASObject casObject = CASObject.newInstance();
		final byte headerBuffer = buffer.get();
		if (headerBuffer != CASDataType.CAS_OBJECT.getTypeID()) {
			throw new IllegalStateException("Invalid CASDataType. Expected: " + CASDataType.CAS_OBJECT.getTypeID()
					+ ", found: " + headerBuffer);
		}
		final short size = buffer.getShort();
		if (size < 0) {
			throw new IllegalStateException("Can't decode CASObject. Size is negative = " + size);
		}
		try {
			for (int i = 0; i < size; ++i) {
				final short keySize = buffer.getShort();
				if (keySize < 0 || keySize > 255) {
					throw new IllegalStateException("Invalid CASObject key length. Found = " + keySize);
				}
				final byte[] keyData = new byte[keySize];
				buffer.get(keyData, 0, keyData.length);
				final String key = new String(keyData);
				final CASDataWrapper decodedObject = this.decodeObject(buffer);
				if (decodedObject == null) {
					throw new IllegalStateException("Could not decode value for key: " + keyData);
				}
				casObject.put(key, decodedObject);
			}
		} catch (Exception codecError) {
			throw new IllegalArgumentException(codecError.getMessage());
		}
		return casObject;
	}

	@Override
	public ICASArray json2array(String jsonStr) {
		if (jsonStr.length() < 2) {
			throw new IllegalStateException(
					"Can't decode CASObject. JSON String is too short. Len: " + jsonStr.length());
		}
		// final JSONArray jsa = JSONArray.fromObject((Object) jsonStr);
		// return this.decodeCASArray(jsa);
		return null;
	}

	// private ICASArray decodeCASArray(final JSONArray jsa) {
	// final ICASArray casArray = (ICASArray) CASArrayLite.newInstance();
	// for (final Object value : jsa) {
	// final CASDataWrapper decodedObject = this.decodeJsonObject(value);
	// if (decodedObject == null) {
	// throw new IllegalStateException("(json2sfarray) Could not decode value
	// for object: " + value);
	// }
	// casArray.add(decodedObject);
	// }
	// return casArray;
	// }

	@Override
	public ICASObject json2object(final String jsonStr) {
		if (jsonStr.length() < 2) {
			throw new IllegalStateException(
					"Can't decode CASObject. JSON String is too short. Len: " + jsonStr.length());
		}
		// final JSONObject jso = JSONObject.fromObject((Object) jsonStr);
		// return this.decodeCASObject(jso);
		return null;
	}

	// private ICASObject decodeCASObject(final JSONObject jso) {
	// final ICASObject CASObject = CASObjectLite.newInstance();
	// for (final Object key : jso.keySet()) {
	// final Object value = jso.get(key);
	// final CASDataWrapper decodedObject = this.decodeJsonObject(value);
	// if (decodedObject == null) {
	// throw new IllegalStateException("(json2CASobj) Could not decode value for
	// key: " + key);
	// }
	// CASObject.put((String) key, decodedObject);
	// }
	// return CASObject;
	// }

	private CASDataWrapper decodeJsonObject(final Object o) {
		if (o instanceof Integer) {
			return new CASDataWrapper(CASDataType.INT, o);
		}
		if (o instanceof Long) {
			return new CASDataWrapper(CASDataType.LONG, o);
		}
		if (o instanceof Double) {
			return new CASDataWrapper(CASDataType.DOUBLE, o);
		}
		if (o instanceof Boolean) {
			return new CASDataWrapper(CASDataType.BOOL, o);
		}
		if (o instanceof String) {
			final String value = (String) o;
			CASDataType type = CASDataType.UTF_STRING;
			if (value.length() > 32767) {
				type = CASDataType.TEXT;
			}
			return new CASDataWrapper(type, o);
		}

		// if (o instanceof JSONObject) {
		// final JSONObject jso = (JSONObject) o;
		// if (jso.isNullObject()) {
		// return new CASDataWrapper(CASDataType.NULL, null);
		// }
		// return new CASDataWrapper(CASDataType.CAS_OBJECT,
		// this.decodeCASObject(jso));
		// } else {
		// if (o instanceof JSONArray) {
		// return new CASDataWrapper(CASDataType.CAS_ARRAY,
		// this.decodeCASArray((JSONArray) o));
		// }
		// throw new IllegalArgumentException(
		// String.format("Unrecognized DataType while converting JSONObject 2
		// CASObject. Object: %s, Type: %s",
		// o, (o == null) ? "null" : o.getClass()));
		// }
		return null;
	}

	@Override
	public CASObject resultSet2object(final ResultSet rset) throws SQLException {
		final ResultSetMetaData metaData = rset.getMetaData();
		final CASObject CASo = new CASObject();
		if (rset.isBeforeFirst()) {
			rset.next();
		}
		for (int col = 1; col <= metaData.getColumnCount(); ++col) {
			final String colName = metaData.getColumnLabel(col);
			final int type = metaData.getColumnType(col);
			final Object rawDataObj = rset.getObject(col);
			if (rawDataObj != null) {
				if (type == 0) {
					CASo.putNull(colName);
				} else if (type == 16) {
					CASo.putBool(colName, rset.getBoolean(col));
				} else if (type == 91) {
					CASo.putLong(colName, rset.getDate(col).getTime());
				} else if (type == 6 || type == 3 || type == 8 || type == 7) {
					CASo.putDouble(colName, rset.getDouble(col));
				} else if (type == 4 || type == -6 || type == 5) {
					CASo.putInt(colName, rset.getInt(col));
				} else if (type == -1 || type == 12 || type == 1) {
					CASo.putUtfString(colName, rset.getString(col));
				} else if (type == -9 || type == -16 || type == -15) {
					CASo.putUtfString(colName, rset.getNString(col));
				} else if (type == 93) {
					CASo.putLong(colName, rset.getTimestamp(col).getTime());
				} else if (type == -5) {
					CASo.putLong(colName, rset.getLong(col));
				} else if (type == -4) {
					final byte[] binData = this.getBlobData(colName, rset.getBinaryStream(col));
					if (binData != null) {
						CASo.putByteArray(colName, binData);
					}
				} else if (type == 2004) {
					final Blob blob = rset.getBlob(col);
					CASo.putByteArray(colName, blob.getBytes(0L, (int) blob.length()));
				} else {
					// this.logger.info("Skipping Unsupported SQL TYPE: " + type
					// + ", Column:" + colName);
				}
			}
		}
		return CASo;
	}

	private byte[] getBlobData(final String colName, final InputStream stream) {
		final BufferedInputStream bis = new BufferedInputStream(stream);
		byte[] bytes = null;
		try {
			bytes = new byte[bis.available()];
			bis.read(bytes);
		} catch (IOException ex) {
			// this.logger.warn("CASObject serialize error. Failed reading BLOB
			// data for column: " + colName);
			return bytes;
		} finally {
			// IOUtils.closeQuietly((InputStream) bis);
		}
		// IOUtils.closeQuietly((InputStream) bis);
		return bytes;
	}

	@Override
	public CASArray resultSet2array(final ResultSet rset) throws SQLException {
		final CASArray CASa = new CASArray();
		while (rset.next()) {
			CASa.addCASObject(this.resultSet2object(rset));
		}
		return CASa;
	}

	@Override
	public byte[] object2binary(final ICASObject object) {
		final ByteBuffer buffer = ByteBuffer.allocate(DefaultCASDataSerializer.BUFFER_CHUNK_SIZE);
		buffer.put((byte) CASDataType.CAS_OBJECT.getTypeID());
		buffer.putShort((short) object.size());
		return this.obj2bin(object, buffer);
	}

	private byte[] obj2bin(final ICASObject object, ByteBuffer buffer) {
		final Set<String> keys = object.getKeys();
		for (final String key : keys) {
			final CASDataWrapper wrapper = object.get(key);
			final Object dataObj = wrapper.getObject();
			buffer = this.encodeCASObjectKey(buffer, key);
			buffer = this.encodeObject(buffer, wrapper.getTypeId(), dataObj);
		}
		final int pos = buffer.position();
		final byte[] result = new byte[pos];
		buffer.flip();
		buffer.get(result, 0, pos);
		return result;
	}

	@Override
	public byte[] array2binary(final ICASArray array) {
		final ByteBuffer buffer = ByteBuffer.allocate(DefaultCASDataSerializer.BUFFER_CHUNK_SIZE);
		buffer.put((byte) CASDataType.CAS_ARRAY.getTypeID());
		buffer.putShort((short) array.size());
		return this.arr2bin(array, buffer);
	}

	private byte[] arr2bin(final ICASArray array, ByteBuffer buffer) {
		// for (final CASDataWrapper wrapper : array) {
		// final Object dataObj = wrapper.getObject();
		// buffer = this.encodeObject(buffer, wrapper.getTypeId(), dataObj);
		// }
		// final int pos = buffer.position();
		// final byte[] result = new byte[pos];
		// buffer.flip();
		// buffer.get(result, 0, pos);
		return null;
	}

	@Override
	public String object2json(final Map<String, Object> map) {
		// return JSONObject.fromObject((Object) map).toString();
		return null;
	}

	public void flattenObject(Map<String, Object> map, CASObject casObj) {
		// for (Map.Entry<String, CASDataWrapper> entry : casObj) {
		// final String key = entry.getKey();
		// final CASDataWrapper value = entry.getValue();
		// if (value.getTypeId() == CASDataType.CAS_OBJECT) {
		// final Map<String, Object> newMap = new HashMap<String, Object>();
		// map.put(key, newMap);
		// this.flattenObject(newMap, (CASObject) value.getObject());
		// } else if (value.getTypeId() == CASDataType.CAS_ARRAY) {
		// final List<Object> newList = new ArrayList<Object>();
		// map.put(key, newList);
		// this.flattenArray(newList, (CASArray) value.getObject());
		// } else {
		// map.put(key, value.getObject());
		// }
		// }
	}

	public void flattenArray(final List<Object> array, CASArray casArray) {
		// for (CASDataWrapper value : casArray) {
		// if (value.getTypeId() == CASDataType.CAS_OBJECT) {
		// final Map<String, Object> newMap = new HashMap<String, Object>();
		// array.add(newMap);
		// this.flattenObject(newMap, (CASObject) value.getObject());
		// } else if (value.getTypeId() == CASDataType.CAS_ARRAY) {
		// final List<Object> newList = new ArrayList<Object>();
		// array.add(newList);
		// this.flattenArray(newList, (CASArray) value.getObject());
		// } else {
		// array.add(value.getObject());
		// }
		// }
	}

	private CASDataWrapper decodeObject(final ByteBuffer buffer) throws CASCodecException {
		CASDataWrapper decodedObject = null;
		final byte headerByte = buffer.get();
		if (headerByte == CASDataType.NULL.getTypeID()) {
			decodedObject = this.binDecode_NULL(buffer);
		} else if (headerByte == CASDataType.BOOL.getTypeID()) {
			decodedObject = this.binDecode_BOOL(buffer);
		} else if (headerByte == CASDataType.BOOL_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_BOOL_ARRAY(buffer);
		} else if (headerByte == CASDataType.BYTE.getTypeID()) {
			decodedObject = this.binDecode_BYTE(buffer);
		} else if (headerByte == CASDataType.BYTE_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_BYTE_ARRAY(buffer);
		} else if (headerByte == CASDataType.SHORT.getTypeID()) {
			decodedObject = this.binDecode_SHORT(buffer);
		} else if (headerByte == CASDataType.SHORT_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_SHORT_ARRAY(buffer);
		} else if (headerByte == CASDataType.INT.getTypeID()) {
			decodedObject = this.binDecode_INT(buffer);
		} else if (headerByte == CASDataType.INT_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_INT_ARRAY(buffer);
		} else if (headerByte == CASDataType.LONG.getTypeID()) {
			decodedObject = this.binDecode_LONG(buffer);
		} else if (headerByte == CASDataType.LONG_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_LONG_ARRAY(buffer);
		} else if (headerByte == CASDataType.FLOAT.getTypeID()) {
			decodedObject = this.binDecode_FLOAT(buffer);
		} else if (headerByte == CASDataType.FLOAT_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_FLOAT_ARRAY(buffer);
		} else if (headerByte == CASDataType.DOUBLE.getTypeID()) {
			decodedObject = this.binDecode_DOUBLE(buffer);
		} else if (headerByte == CASDataType.DOUBLE_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_DOUBLE_ARRAY(buffer);
		} else if (headerByte == CASDataType.UTF_STRING.getTypeID()) {
			decodedObject = this.binDecode_UTF_STRING(buffer);
		} else if (headerByte == CASDataType.TEXT.getTypeID()) {
			decodedObject = this.binDecode_TEXT(buffer);
		} else if (headerByte == CASDataType.UTF_STRING_ARRAY.getTypeID()) {
			decodedObject = this.binDecode_UTF_STRING_ARRAY(buffer);
		} else if (headerByte == CASDataType.CAS_ARRAY.getTypeID()) {
			buffer.position(buffer.position() - 1);
			decodedObject = new CASDataWrapper(CASDataType.CAS_ARRAY, this.decodeCASArray(buffer));
		} else {
			if (headerByte != CASDataType.CAS_OBJECT.getTypeID()) {
				throw new CASCodecException("Unknow CASDataType ID: " + headerByte);
			}
			buffer.position(buffer.position() - 1);
			final ICASObject CASObj = this.decodeCASObject(buffer);
			CASDataType type = CASDataType.CAS_OBJECT;
			Object finalCASObj = CASObj;
			if (CASObj.containsKey("$C") && CASObj.containsKey("$F")) {
				type = CASDataType.CLASS;
				finalCASObj = this.CAS2pojo(CASObj);
			}
			decodedObject = new CASDataWrapper(type, finalCASObj);
		}

		return decodedObject;
	}

	private ByteBuffer encodeObject(ByteBuffer buffer, final CASDataType typeId, final Object object) {
		switch (typeId) {
		case NULL: {
			buffer = this.binEncode_NULL(buffer);
			break;
		}
		case BOOL: {
			buffer = this.binEncode_BOOL(buffer, (Boolean) object);
			break;
		}
		case BYTE: {
			buffer = this.binEncode_BYTE(buffer, (Byte) object);
			break;
		}
		case SHORT: {
			buffer = this.binEncode_SHORT(buffer, (Short) object);
			break;
		}
		case INT: {
			buffer = this.binEncode_INT(buffer, (Integer) object);
			break;
		}
		case LONG: {
			buffer = this.binEncode_LONG(buffer, (Long) object);
			break;
		}
		case FLOAT: {
			buffer = this.binEncode_FLOAT(buffer, (Float) object);
			break;
		}
		case DOUBLE: {
			buffer = this.binEncode_DOUBLE(buffer, (Double) object);
			break;
		}
		case UTF_STRING: {
			buffer = this.binEncode_UTF_STRING(buffer, (String) object);
			break;
		}
		case TEXT: {
			buffer = this.binEncode_TEXT(buffer, (String) object);
			break;
		}
		case BOOL_ARRAY: {
			buffer = this.binEncode_BOOL_ARRAY(buffer, (Collection<Boolean>) object);
			break;
		}
		case BYTE_ARRAY: {
			buffer = this.binEncode_BYTE_ARRAY(buffer, (byte[]) object);
			break;
		}
		case SHORT_ARRAY: {
			buffer = this.binEncode_SHORT_ARRAY(buffer, (Collection<Short>) object);
			break;
		}
		case INT_ARRAY: {
			buffer = this.binEncode_INT_ARRAY(buffer, (Collection<Integer>) object);
			break;
		}
		case LONG_ARRAY: {
			buffer = this.binEncode_LONG_ARRAY(buffer, (Collection<Long>) object);
			break;
		}
		case FLOAT_ARRAY: {
			buffer = this.binEncode_FLOAT_ARRAY(buffer, (Collection<Float>) object);
			break;
		}
		case DOUBLE_ARRAY: {
			buffer = this.binEncode_DOUBLE_ARRAY(buffer, (Collection<Double>) object);
			break;
		}
		case UTF_STRING_ARRAY: {
			buffer = this.binEncode_UTF_STRING_ARRAY(buffer, (Collection<String>) object);
			break;
		}
		case CAS_ARRAY: {
			buffer = this.addData(buffer, this.array2binary((ICASArray) object));
			break;
		}
		case CAS_OBJECT: {
			buffer = this.addData(buffer, this.object2binary((ICASObject) object));
			break;
		}
		case CLASS: {
			buffer = this.addData(buffer, this.object2binary(this.pojo2CAS(object)));
			break;
		}
		default: {
			throw new IllegalArgumentException("Unrecognized type in CASObject serialization: " + typeId);
		}
		}
		return buffer;
	}

	private CASDataWrapper binDecode_NULL(final ByteBuffer buffer) {
		return new CASDataWrapper(CASDataType.NULL, null);
	}

	private CASDataWrapper binDecode_BOOL(final ByteBuffer buffer) throws CASCodecException {
		final byte boolByte = buffer.get();
		Boolean bool = null;
		if (boolByte == 0) {
			bool = new Boolean(false);
		} else {
			if (boolByte != 1) {
				throw new CASCodecException("Error decoding Bool type. Illegal value: " + bool);
			}
			bool = new Boolean(true);
		}
		return new CASDataWrapper(CASDataType.BOOL, bool);
	}

	private CASDataWrapper binDecode_BYTE(final ByteBuffer buffer) {
		final byte boolByte = buffer.get();
		return new CASDataWrapper(CASDataType.BYTE, boolByte);
	}

	private CASDataWrapper binDecode_SHORT(final ByteBuffer buffer) {
		final short shortValue = buffer.getShort();
		return new CASDataWrapper(CASDataType.SHORT, shortValue);
	}

	private CASDataWrapper binDecode_INT(final ByteBuffer buffer) {
		final int intValue = buffer.getInt();
		return new CASDataWrapper(CASDataType.INT, intValue);
	}

	private CASDataWrapper binDecode_LONG(final ByteBuffer buffer) {
		final long longValue = buffer.getLong();
		return new CASDataWrapper(CASDataType.LONG, longValue);
	}

	private CASDataWrapper binDecode_FLOAT(final ByteBuffer buffer) {
		final float floatValue = buffer.getFloat();
		return new CASDataWrapper(CASDataType.FLOAT, floatValue);
	}

	private CASDataWrapper binDecode_DOUBLE(final ByteBuffer buffer) {
		final double doubleValue = buffer.getDouble();
		return new CASDataWrapper(CASDataType.DOUBLE, doubleValue);
	}

	private CASDataWrapper binDecode_UTF_STRING(final ByteBuffer buffer) throws CASCodecException {
		final short strLen = buffer.getShort();
		if (strLen < 0) {
			throw new CASCodecException("Error decoding UtfString. Negative size: " + strLen);
		}
		final byte[] strData = new byte[strLen];
		buffer.get(strData, 0, strLen);
		final String decodedString = new String(strData);
		return new CASDataWrapper(CASDataType.UTF_STRING, decodedString);
	}

	private CASDataWrapper binDecode_TEXT(final ByteBuffer buffer) throws CASCodecException {
		final int strLen = buffer.getInt();
		if (strLen < 0) {
			throw new CASCodecException("Error decoding UtfString. Negative size: " + strLen);
		}
		final byte[] strData = new byte[strLen];
		buffer.get(strData, 0, strLen);
		final String decodedString = new String(strData);
		return new CASDataWrapper(CASDataType.TEXT, decodedString);
	}

	private CASDataWrapper binDecode_BOOL_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Boolean> array = new ArrayList<Boolean>();
		for (int j = 0; j < arraySize; ++j) {
			final byte boolData = buffer.get();
			if (boolData == 0) {
				array.add(false);
			} else {
				if (boolData != 1) {
					throw new CASCodecException("Error decoding BoolArray. Invalid bool value: " + boolData);
				}
				array.add(true);
			}
		}
		return new CASDataWrapper(CASDataType.BOOL_ARRAY, array);
	}

	private CASDataWrapper binDecode_BYTE_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final int arraySize = buffer.getInt();
		if (arraySize < 0) {
			throw new CASCodecException("Error decoding typed array size. Negative size: " + arraySize);
		}
		final byte[] byteData = new byte[arraySize];
		buffer.get(byteData, 0, arraySize);
		return new CASDataWrapper(CASDataType.BYTE_ARRAY, byteData);
	}

	private CASDataWrapper binDecode_SHORT_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Short> array = new ArrayList<Short>();
		for (int j = 0; j < arraySize; ++j) {
			final short shortValue = buffer.getShort();
			array.add(shortValue);
		}
		return new CASDataWrapper(CASDataType.SHORT_ARRAY, array);
	}

	private CASDataWrapper binDecode_INT_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Integer> array = new ArrayList<Integer>();
		for (int j = 0; j < arraySize; ++j) {
			final int intValue = buffer.getInt();
			array.add(intValue);
		}
		return new CASDataWrapper(CASDataType.INT_ARRAY, array);
	}

	private CASDataWrapper binDecode_LONG_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Long> array = new ArrayList<Long>();
		for (int j = 0; j < arraySize; ++j) {
			final long longValue = buffer.getLong();
			array.add(longValue);
		}
		return new CASDataWrapper(CASDataType.LONG_ARRAY, array);
	}

	private CASDataWrapper binDecode_FLOAT_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Float> array = new ArrayList<Float>();
		for (int j = 0; j < arraySize; ++j) {
			final float floatValue = buffer.getFloat();
			array.add(floatValue);
		}
		return new CASDataWrapper(CASDataType.FLOAT_ARRAY, array);
	}

	private CASDataWrapper binDecode_DOUBLE_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<Double> array = new ArrayList<Double>();
		for (int j = 0; j < arraySize; ++j) {
			final double doubleValue = buffer.getDouble();
			array.add(doubleValue);
		}
		return new CASDataWrapper(CASDataType.DOUBLE_ARRAY, array);
	}

	private CASDataWrapper binDecode_UTF_STRING_ARRAY(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = this.getTypeArraySize(buffer);
		final List<String> array = new ArrayList<String>();
		for (int j = 0; j < arraySize; ++j) {
			final short strLen = buffer.getShort();
			if (strLen < 0) {
				throw new CASCodecException(
						"Error decoding UtfStringArray element. Element has negative size: " + strLen);
			}
			final byte[] strData = new byte[strLen];
			buffer.get(strData, 0, strLen);
			array.add(new String(strData));
		}
		return new CASDataWrapper(CASDataType.UTF_STRING_ARRAY, array);
	}

	private short getTypeArraySize(final ByteBuffer buffer) throws CASCodecException {
		final short arraySize = buffer.getShort();
		if (arraySize < 0) {
			throw new CASCodecException("Error decoding typed array size. Negative size: " + arraySize);
		}
		return arraySize;
	}

	private ByteBuffer binEncode_NULL(final ByteBuffer buffer) {
		return this.addData(buffer, new byte[1]);
	}

	private ByteBuffer binEncode_BOOL(final ByteBuffer buffer, final Boolean value) {
		if (value == null)
			return null;
		
		final byte[] data = { (byte) CASDataType.BOOL.getTypeID(), (byte) (value.booleanValue() ? 1 : 0) };
		return this.addData(buffer, data);
	}

	private ByteBuffer binEncode_BYTE(final ByteBuffer buffer, final Byte value) {
		final byte[] data = { (byte) CASDataType.BYTE.getTypeID(), value };
		return this.addData(buffer, data);
	}

	private ByteBuffer binEncode_SHORT(final ByteBuffer buffer, final Short value) {
		final ByteBuffer buf = ByteBuffer.allocate(3);
		buf.put((byte) CASDataType.SHORT.getTypeID());
		buf.putShort(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_INT(final ByteBuffer buffer, final Integer value) {
		final ByteBuffer buf = ByteBuffer.allocate(5);
		buf.put((byte) CASDataType.INT.getTypeID());
		buf.putInt(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_LONG(final ByteBuffer buffer, final Long value) {
		final ByteBuffer buf = ByteBuffer.allocate(9);
		buf.put((byte) CASDataType.LONG.getTypeID());
		buf.putLong(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_FLOAT(final ByteBuffer buffer, final Float value) {
		final ByteBuffer buf = ByteBuffer.allocate(5);
		buf.put((byte) CASDataType.FLOAT.getTypeID());
		buf.putFloat(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_DOUBLE(final ByteBuffer buffer, final Double value) {
		final ByteBuffer buf = ByteBuffer.allocate(9);
		buf.put((byte) CASDataType.DOUBLE.getTypeID());
		buf.putDouble(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_UTF_STRING(final ByteBuffer buffer, final String value) {
		final byte[] stringBytes = value.getBytes();
		final ByteBuffer buf = ByteBuffer.allocate(3 + stringBytes.length);
		buf.put((byte) CASDataType.UTF_STRING.getTypeID());
		buf.putShort((short) stringBytes.length);
		buf.put(stringBytes);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_TEXT(final ByteBuffer buffer, final String value) {
		final byte[] stringBytes = value.getBytes();
		final ByteBuffer buf = ByteBuffer.allocate(5 + stringBytes.length);
		buf.put((byte) CASDataType.TEXT.getTypeID());
		buf.putInt(stringBytes.length);
		buf.put(stringBytes);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_BOOL_ARRAY(final ByteBuffer buffer, final Collection<Boolean> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + value.size());
		buf.put((byte) CASDataType.BOOL_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final boolean b : value) {
			buf.put((byte) (b ? 1 : 0));
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_BYTE_ARRAY(final ByteBuffer buffer, final byte[] value) {
		final ByteBuffer buf = ByteBuffer.allocate(5 + value.length);
		buf.put((byte) CASDataType.BYTE_ARRAY.getTypeID());
		buf.putInt(value.length);
		buf.put(value);
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_SHORT_ARRAY(final ByteBuffer buffer, final Collection<Short> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + 2 * value.size());
		buf.put((byte) CASDataType.SHORT_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final short item : value) {
			buf.putShort(item);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_INT_ARRAY(final ByteBuffer buffer, final Collection<Integer> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + 4 * value.size());
		buf.put((byte) CASDataType.INT_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final int item : value) {
			buf.putInt(item);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_LONG_ARRAY(final ByteBuffer buffer, final Collection<Long> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + 8 * value.size());
		buf.put((byte) CASDataType.LONG_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final long item : value) {
			buf.putLong(item);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_FLOAT_ARRAY(final ByteBuffer buffer, final Collection<Float> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + 4 * value.size());
		buf.put((byte) CASDataType.FLOAT_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final float item : value) {
			buf.putFloat(item);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_DOUBLE_ARRAY(final ByteBuffer buffer, final Collection<Double> value) {
		final ByteBuffer buf = ByteBuffer.allocate(3 + 8 * value.size());
		buf.put((byte) CASDataType.DOUBLE_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		for (final double item : value) {
			buf.putDouble(item);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer binEncode_UTF_STRING_ARRAY(final ByteBuffer buffer, final Collection<String> value) {
		int stringDataLen = 0;
		final byte[][] binStrings = new byte[value.size()][];
		int count = 0;
		for (final String item : value) {
			final byte[] binStr = item.getBytes();
			binStrings[count++] = binStr;
			stringDataLen += 2 + binStr.length;
		}
		final ByteBuffer buf = ByteBuffer.allocate(3 + stringDataLen);
		buf.put((byte) CASDataType.UTF_STRING_ARRAY.getTypeID());
		buf.putShort((short) value.size());
		byte[][] array;
		for (int length = (array = binStrings).length, i = 0; i < length; ++i) {
			final byte[] binItem = array[i];
			buf.putShort((short) binItem.length);
			buf.put(binItem);
		}
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer encodeCASObjectKey(final ByteBuffer buffer, final String value) {
		final ByteBuffer buf = ByteBuffer.allocate(2 + value.length());
		buf.putShort((short) value.length());
		buf.put(value.getBytes());
		return this.addData(buffer, buf.array());
	}

	private ByteBuffer addData(ByteBuffer buffer, final byte[] newData) {
		if (buffer.remaining() < newData.length) {
			int newSize = DefaultCASDataSerializer.BUFFER_CHUNK_SIZE;
			if (newSize < newData.length) {
				newSize = newData.length;
			}
			final ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + newSize);
			buffer.flip();
			newBuffer.put(buffer);
			buffer = newBuffer;
		}
		buffer.put(newData);
		return buffer;
	}

	@Override
	public ICASObject pojo2CAS(final Object pojo) {
		final ICASObject CASObj = CASObject.newInstance();
		try {
			this.convertPojo(pojo, CASObj);
		} catch (Exception e) {
			throw new CASRuntimeException(e);
		}
		return CASObj;
	}

	private void convertPojo(final Object pojo, final ICASObject CASObj) throws Exception {
		final Class<?> pojoClazz = pojo.getClass();
		final String classFullName = pojoClazz.getCanonicalName();
		if (classFullName == null) {
			throw new IllegalArgumentException("Anonymous classes cannot be serialized!");
		}

		final ICASArray fieldList = CASArray.newInstance();
		CASObj.putUtfString("$C", classFullName);
		CASObj.putCASArray("$F", fieldList);
		Field[] declaredFields;
		for (int length = (declaredFields = pojoClazz.getDeclaredFields()).length, i = 0; i < length; ++i) {
			final Field field = declaredFields[i];
			try {
				final int modifiers = field.getModifiers();
				if (!Modifier.isTransient(modifiers)) {
					if (!Modifier.isStatic(modifiers)) {
						final String fieldName = field.getName();
						Object fieldValue = null;
						if (Modifier.isPublic(modifiers)) {
							fieldValue = field.get(pojo);
						} else {
							fieldValue = this.readValueFromGetter(fieldName, field.getType().getSimpleName(), pojo);
						}
						final ICASObject fieldDescriptor = CASObject.newInstance();
						fieldDescriptor.putUtfString("N", fieldName);
						fieldDescriptor.put("V", this.wrapPojoField(fieldValue));
						fieldList.addCASObject(fieldDescriptor);
					}
				}
			} catch (NoSuchMethodException err) {
				// this.logger.info("-- No public getter -- Serializer skipping
				// private field: " + field.getName()
				// + ", from class: " + pojoClazz);
				err.printStackTrace();
			}
		}
	}

	private Object readValueFromGetter(final String fieldName, final String type, final Object pojo) throws Exception {
		Object value = null;
		final boolean isBool = type.equalsIgnoreCase("boolean");
		final String getterName = isBool ? ("is" + StringUtils.capitalize(fieldName))
				: ("get" + StringUtils.capitalize(fieldName));
		final Method getterMethod = pojo.getClass().getMethod(getterName, (Class<?>[]) new Class[0]);
		value = getterMethod.invoke(pojo, new Object[0]);
		return value;
	}

	private CASDataWrapper wrapPojoField(final Object value) {
		if (value == null) {
			return new CASDataWrapper(CASDataType.NULL, null);
		}
		CASDataWrapper wrapper = null;
		if (value instanceof Boolean) {
			wrapper = new CASDataWrapper(CASDataType.BOOL, value);
		} else if (value instanceof Byte) {
			wrapper = new CASDataWrapper(CASDataType.BYTE, value);
		} else if (value instanceof Short) {
			wrapper = new CASDataWrapper(CASDataType.SHORT, value);
		} else if (value instanceof Integer) {
			wrapper = new CASDataWrapper(CASDataType.INT, value);
		} else if (value instanceof Long) {
			wrapper = new CASDataWrapper(CASDataType.LONG, value);
		} else if (value instanceof Float) {
			wrapper = new CASDataWrapper(CASDataType.FLOAT, value);
		} else if (value instanceof Double) {
			wrapper = new CASDataWrapper(CASDataType.DOUBLE, value);
		} else if (value instanceof String) {
			wrapper = new CASDataWrapper(CASDataType.UTF_STRING, value);
		} else if (value.getClass().isArray()) {
			wrapper = new CASDataWrapper(CASDataType.CAS_ARRAY, this.unrollArray((Object[]) value));
		} else if (value instanceof Collection) {
			wrapper = new CASDataWrapper(CASDataType.CAS_ARRAY, this.unrollCollection((Collection) value));
		} else if (value instanceof Map) {
			// wrapper = new CASDataWrapper(CASDataType.CAS_OBJECT,
			// this.unrollMap((Map) value));
		}
		// else if (value instanceof SerializableCASType) {
		// wrapper = new CASDataWrapper(CASDataType.CAS_OBJECT,
		// this.pojo2CAS(value));
		// }
		return wrapper;
	}

	private ICASArray unrollArray(final Object[] arr) {
		final ICASArray array = CASArray.newInstance();
		for (final Object item : arr) {
			array.add(this.wrapPojoField(item));
		}
		return array;
	}

	private ICASArray unrollCollection(final Collection collection) {
		final ICASArray array = CASArray.newInstance();
		for (final Object item : collection) {
			array.add(this.wrapPojoField(item));
		}
		return array;
	}

	@Override
	public Object CAS2pojo(final ICASObject CASObj) {
		Object pojo = null;
		if (!CASObj.containsKey("$C") && !CASObj.containsKey("$F")) {
			throw new CASRuntimeException("The CASObject passed does not represent any serialized class.");
		}
		try {
			final String className = CASObj.getUtfString("$C");
			final Class<?> theClass = Class.forName(className);
			pojo = theClass.newInstance();
			// if (!(pojo instanceof SerializableCASType)) {
			// throw new IllegalStateException("Cannot deserialize object: " +
			// pojo + ", type: " + className
			// + " -- It doesn't implement the SerializableCASType interface");
			// }
			this.convertCASObject(CASObj.getCASArray("$F"), pojo);
		} catch (Exception e) {
			throw new CASRuntimeException(e);
		}
		return pojo;
	}

	private void convertCASObject(final ICASArray fieldList, final Object pojo) throws Exception {
		for (int j = 0; j < fieldList.size(); ++j) {
			final ICASObject fieldDescriptor = fieldList.getCASObject(j);
			final String fieldName = fieldDescriptor.getUtfString("N");
			final Object fieldValue = this.unwrapPojoField(fieldDescriptor.get("V"));
			this.setObjectField(pojo, fieldName, fieldValue);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setObjectField(final Object pojo, final String fieldName, Object fieldValue) throws Exception {
		final Class pojoClass = pojo.getClass();
		final Field field = pojoClass.getDeclaredField(fieldName);
		final int fieldModifier = field.getModifiers();
		if (Modifier.isTransient(fieldModifier)) {
			return;
		}
		final boolean isArray = field.getType().isArray();
		if (isArray) {
			if (!(fieldValue instanceof Collection)) {
				// TODO runtime exception
				// throw new CASRuntimeException("Problem during CASObject =>
				// POJO conversion. Found array field in POJO: "
				// + fieldName + ", but data is not a Collection!");
			}
			final Collection collection = (Collection) fieldValue;
			fieldValue = collection.toArray();
			final int arraySize = collection.size();
			final Object typedArray = Array.newInstance(field.getType().getComponentType(), arraySize);
			System.arraycopy(fieldValue, 0, typedArray, 0, arraySize);
			fieldValue = typedArray;
		} else if (fieldValue instanceof Collection) {
			final Collection collection = (Collection) fieldValue;
			final String fieldClass = field.getType().getSimpleName();
			if (fieldClass.equals("ArrayList") || fieldClass.equals("List")) {
				fieldValue = new ArrayList(collection);
			}
			if (fieldClass.equals("CopyOnWriteArrayList")) {
				fieldValue = new CopyOnWriteArrayList(collection);
			} else if (fieldClass.equals("LinkedList")) {
				fieldValue = new LinkedList(collection);
			} else if (fieldClass.equals("Vector")) {
				fieldValue = new Vector(collection);
			} else if (fieldClass.equals("Set") || fieldClass.equals("HashSet")) {
				fieldValue = new HashSet(collection);
			} else if (fieldClass.equals("LinkedHashSet")) {
				fieldValue = new LinkedHashSet(collection);
			} else if (fieldClass.equals("TreeSet")) {
				fieldValue = new TreeSet(collection);
			} else if (fieldClass.equals("CopyOnWriteArraySet")) {
				fieldValue = new CopyOnWriteArraySet(collection);
			} else if (fieldClass.equals("Queue") || fieldClass.equals("PriorityQueue")) {
				fieldValue = new PriorityQueue(collection);
			} else if (fieldClass.equals("BlockingQueue") || fieldClass.equals("LinkedBlockingQueue")) {
				fieldValue = new LinkedBlockingQueue(collection);
			} else if (fieldClass.equals("PriorityBlockingQueue")) {
				fieldValue = new PriorityBlockingQueue(collection);
			} else if (fieldClass.equals("ConcurrentLinkedQueue")) {
				fieldValue = new ConcurrentLinkedQueue(collection);
			} else if (fieldClass.equals("DelayQueue")) {
				fieldValue = new DelayQueue(collection);
			} else if (fieldClass.equals("Deque") || fieldClass.equals("ArrayDeque")) {
				fieldValue = new ArrayDeque(collection);
			} else if (fieldClass.equals("LinkedBlockingDeque")) {
				fieldValue = new LinkedBlockingDeque(collection);
			}
		}
		if (Modifier.isPublic(fieldModifier)) {
			field.set(pojo, fieldValue);
		} else {
			this.writeValueFromSetter(field, pojo, fieldValue);
		}
	}

	private void writeValueFromSetter(final Field field, final Object pojo, final Object fieldValue) throws Exception {
		final String setterName = "set" + StringUtils.capitalize(field.getName());
		try {
			final Method setterMethod = pojo.getClass().getMethod(setterName, field.getType());
			setterMethod.invoke(pojo, fieldValue);
		} catch (NoSuchMethodException e) {
			// this.logger.info("-- No public setter -- Serializer skipping
			// private field: " + field.getName()
			// + ", from class: " + pojo.getClass().getName());
		}
	}

	private Object unwrapPojoField(final CASDataWrapper wrapper) {
		Object obj = null;
		final CASDataType type = wrapper.getTypeId();
		if (type.getTypeID() <= CASDataType.UTF_STRING.getTypeID()) {
			obj = wrapper.getObject();
		} else if (type == CASDataType.CAS_ARRAY) {
			obj = this.rebuildArray((ICASArray) wrapper.getObject());
		} else if (type == CASDataType.CAS_OBJECT) {
			obj = this.rebuildMap((ICASObject) wrapper.getObject());
		} else if (type == CASDataType.CLASS) {
			obj = wrapper.getObject();
		}
		return obj;
	}

	private Object rebuildArray(final ICASArray CASArray) {
		final Collection<Object> collection = new ArrayList<Object>();
		final Iterator<CASDataWrapper> iter = CASArray.iterator();
		while (iter.hasNext()) {
			final Object item = this.unwrapPojoField(iter.next());
			collection.add(item);
		}
		return collection;
	}

	private Object rebuildMap(final ICASObject CASObj) {
		final Map<String, Object> map = new HashMap<String, Object>();
		for (final String key : CASObj.getKeys()) {
			final CASDataWrapper wrapper = CASObj.get(key);
			map.put(key, this.unwrapPojoField(wrapper));
		}
		return map;
	}
}

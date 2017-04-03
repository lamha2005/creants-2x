// 
// Decompiled by Procyon v0.5.30
// 

package com.creants.creants_2x.socket.gate.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.creants.creants_2x.socket.gate.protocol.serialization.DefaultCASDataSerializer;
import com.creants.creants_2x.socket.gate.protocol.serialization.DefaultObjectDumpFormatter;
import com.creants.creants_2x.socket.gate.protocol.serialization.ICASDataSerializer;
import com.creants.creants_2x.socket.util.ByteUtils;

/**
 * @author LamHM
 *
 */
public class CASObject implements ICASObject {
	private Map<String, CASDataWrapper> dataHolder;
	private ICASDataSerializer serializer;

	public static CASObject newFromObject(final Object o) {
		return (CASObject) DefaultCASDataSerializer.getInstance().pojo2CAS(o);
	}

	public static CASObject newFromBinaryData(final byte[] bytes) {
		return (CASObject) DefaultCASDataSerializer.getInstance().binary2object(bytes);
	}

	public static ICASObject newFromJsonData(final String jsonStr) {
		return DefaultCASDataSerializer.getInstance().json2object(jsonStr);
	}

	public static CASObject newFromResultSet(final ResultSet rset) throws SQLException {
		return DefaultCASDataSerializer.getInstance().resultSet2object(rset);
	}

	public static CASObject newInstance() {
		return new CASObject();
	}

	public CASObject() {
		this.dataHolder = new ConcurrentHashMap<String, CASDataWrapper>();
		this.serializer = DefaultCASDataSerializer.getInstance();
	}

	@Override
	public Iterator<Map.Entry<String, CASDataWrapper>> iterator() {
		return this.dataHolder.entrySet().iterator();
	}

	@Override
	public boolean containsKey(final String key) {
		return this.dataHolder.containsKey(key);
	}

	@Override
	public boolean removeElement(final String key) {
		return this.dataHolder.remove(key) != null;
	}

	@Override
	public int size() {
		return this.dataHolder.size();
	}

	@Override
	public byte[] toBinary() {
		return this.serializer.object2binary(this);
	}

	@Override
	public String toJson() {
		return this.serializer.object2json(this.flatten());
	}

	@Override
	public String getDump() {
		if (this.size() == 0) {
			return "[ Empty CASObject ]";
		}
		return DefaultObjectDumpFormatter.prettyPrintDump(this.dump());
	}

	@Override
	public String getDump(final boolean noFormat) {
		if (!noFormat) {
			return this.dump();
		}
		return this.getDump();
	}

	private String dump() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		for (final String key : this.getKeys()) {
			final CASDataWrapper wrapper = this.get(key);
			buffer.append("(").append(wrapper.getTypeId().name().toLowerCase()).append(") ").append(key).append(": ");
			if (wrapper.getTypeId() == CASDataType.CAS_OBJECT) {
				buffer.append(((CASObject) wrapper.getObject()).getDump(false));
			} else if (wrapper.getTypeId() == CASDataType.CAS_ARRAY) {
				buffer.append(((CASArray) wrapper.getObject()).getDump(false));
			} else if (wrapper.getTypeId() == CASDataType.BYTE_ARRAY) {
				buffer.append(DefaultObjectDumpFormatter.prettyPrintByteArray((byte[]) wrapper.getObject()));
			} else if (wrapper.getTypeId() == CASDataType.CLASS) {
				buffer.append(wrapper.getObject().getClass().getName());
			} else {
				buffer.append(wrapper.getObject());
			}
			buffer.append(';');
		}
		buffer.append('}');
		return buffer.toString();
	}

	@Override
	public String getHexDump() {
		return ByteUtils.fullHexDump(this.toBinary());
	}

	@Override
	public boolean isNull(final String key) {
		final CASDataWrapper wrapper = this.dataHolder.get(key);
		return wrapper != null && wrapper.getTypeId() == CASDataType.NULL;
	}

	@Override
	public CASDataWrapper get(final String key) {
		return this.dataHolder.get(key);
	}

	@Override
	public Boolean getBool(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Boolean) o.getObject();
	}

	@Override
	public Collection<Boolean> getBoolArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Boolean>) o.getObject();
	}

	@Override
	public Byte getByte(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Byte) o.getObject();
	}

	@Override
	public byte[] getByteArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (byte[]) o.getObject();
	}

	@Override
	public Double getDouble(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Double) o.getObject();
	}

	@Override
	public Collection<Double> getDoubleArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Double>) o.getObject();
	}

	@Override
	public Float getFloat(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Float) o.getObject();
	}

	@Override
	public Collection<Float> getFloatArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Float>) o.getObject();
	}

	@Override
	public Integer getInt(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Integer) o.getObject();
	}

	@Override
	public Collection<Integer> getIntArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Integer>) o.getObject();
	}

	@Override
	public Set<String> getKeys() {
		return this.dataHolder.keySet();
	}

	@Override
	public Long getLong(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Long) o.getObject();
	}

	@Override
	public Collection<Long> getLongArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Long>) o.getObject();
	}

	@Override
	public ICASArray getCASArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (ICASArray) o.getObject();
	}

	@Override
	public ICASObject getCASObject(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (ICASObject) o.getObject();
	}

	@Override
	public Short getShort(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Short) o.getObject();
	}

	@Override
	public Collection<Short> getShortArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<Short>) o.getObject();
	}

	@Override
	public Integer getUnsignedByte(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return DefaultCASDataSerializer.getInstance().getUnsignedByte((byte) o.getObject());
	}

	@Override
	public Collection<Integer> getUnsignedByteArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		final DefaultCASDataSerializer serializer = DefaultCASDataSerializer.getInstance();
		final Collection<Integer> intCollection = new ArrayList<Integer>();
		byte[] array;
		for (int length = (array = (byte[]) o.getObject()).length, i = 0; i < length; ++i) {
			final byte b = array[i];
			intCollection.add(serializer.getUnsignedByte(b));
		}
		return intCollection;
	}

	@Override
	public String getUtfString(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (String) o.getObject();
	}

	@Override
	public String getText(final String key) {
		return this.getUtfString(key);
	}

	@Override
	public Collection<String> getUtfStringArray(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return (Collection<String>) o.getObject();
	}

	@Override
	public Object getClass(final String key) {
		final CASDataWrapper o = this.dataHolder.get(key);
		if (o == null) {
			return null;
		}
		return o.getObject();
	}

	@Override
	public void putBool(final String key, final boolean value) {
		this.putObj(key, value, CASDataType.BOOL);
	}

	@Override
	public void putBoolArray(final String key, final Collection<Boolean> value) {
		this.putObj(key, value, CASDataType.BOOL_ARRAY);
	}

	@Override
	public void putByte(final String key, final byte value) {
		this.putObj(key, value, CASDataType.BYTE);
	}

	@Override
	public void putByteArray(final String key, final byte[] value) {
		this.putObj(key, value, CASDataType.BYTE_ARRAY);
	}

	@Override
	public void putDouble(final String key, final double value) {
		this.putObj(key, value, CASDataType.DOUBLE);
	}

	@Override
	public void putDoubleArray(final String key, final Collection<Double> value) {
		this.putObj(key, value, CASDataType.DOUBLE_ARRAY);
	}

	@Override
	public void putFloat(final String key, final float value) {
		this.putObj(key, value, CASDataType.FLOAT);
	}

	@Override
	public void putFloatArray(final String key, final Collection<Float> value) {
		this.putObj(key, value, CASDataType.FLOAT_ARRAY);
	}

	@Override
	public void putInt(final String key, final int value) {
		this.putObj(key, value, CASDataType.INT);
	}

	@Override
	public void putIntArray(final String key, final Collection<Integer> value) {
		this.putObj(key, value, CASDataType.INT_ARRAY);
	}

	@Override
	public void putLong(final String key, final long value) {
		this.putObj(key, value, CASDataType.LONG);
	}

	@Override
	public void putLongArray(final String key, final Collection<Long> value) {
		this.putObj(key, value, CASDataType.LONG_ARRAY);
	}

	@Override
	public void putNull(final String key) {
		this.dataHolder.put(key, new CASDataWrapper(CASDataType.NULL, null));
	}

	@Override
	public void putCASArray(final String key, final ICASArray value) {
		this.putObj(key, value, CASDataType.CAS_ARRAY);
	}

	@Override
	public void putCASObject(final String key, final ICASObject value) {
		this.putObj(key, value, CASDataType.CAS_OBJECT);
	}

	@Override
	public void putShort(final String key, final short value) {
		this.putObj(key, value, CASDataType.SHORT);
	}

	@Override
	public void putShortArray(final String key, final Collection<Short> value) {
		this.putObj(key, value, CASDataType.SHORT_ARRAY);
	}

	@Override
	public void putUtfString(final String key, final String value) {
		this.putObj(key, value, CASDataType.UTF_STRING);
	}

	@Override
	public void putText(final String key, final String value) {
		this.putObj(key, value, CASDataType.TEXT);
	}

	@Override
	public void putUtfStringArray(final String key, final Collection<String> value) {
		this.putObj(key, value, CASDataType.UTF_STRING_ARRAY);
	}

	@Override
	public void put(final String key, final CASDataWrapper wrappedObject) {
		this.putObj(key, wrappedObject, null);
	}

	@Override
	public void putClass(final String key, final Object o) {
		this.putObj(key, o, CASDataType.CLASS);
	}

	@Override
	public String toString() {
		return "[CASObject, size: " + this.size() + "]";
	}

	private void putObj(final String key, final Object value, final CASDataType typeId) {
		if (key == null) {
			throw new IllegalArgumentException("CASObject requires a non-null key for a 'put' operation!");
		}
		if (key.length() > 255) {
			throw new IllegalArgumentException("CASObject keys must be less than 255 characters!");
		}
		if (value == null) {
			throw new IllegalArgumentException(
					"CASObject requires a non-null value! If you need to add a null use the putNull() method.");
		}
		if (value instanceof CASDataWrapper) {
			this.dataHolder.put(key, (CASDataWrapper) value);
		} else {
			this.dataHolder.put(key, new CASDataWrapper(typeId, value));
		}
	}

	private Map<String, Object> flatten() {
		final Map<String, Object> map = new HashMap<String, Object>();
		DefaultCASDataSerializer.getInstance().flattenObject(map, this);
		return map;
	}
}

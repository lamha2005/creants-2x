// 
// Decompiled by Procyon v0.5.30
// 

package com.creants.creants_2x.socket.gate.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.creants.creants_2x.socket.gate.protocol.serialization.DefaultCASDataSerializer;
import com.creants.creants_2x.socket.gate.protocol.serialization.DefaultObjectDumpFormatter;
import com.creants.creants_2x.socket.gate.protocol.serialization.ICASDataSerializer;
import com.creants.creants_2x.socket.util.ByteUtils;

/**
 * @author LamHM
 *
 */
public class CASArray implements ICASArray {
	private ICASDataSerializer serializer;
	private List<CASDataWrapper> dataHolder;

	public CASArray() {
		this.dataHolder = new ArrayList<CASDataWrapper>();
		this.serializer = DefaultCASDataSerializer.getInstance();
	}

	public static CASArray newFromBinaryData(byte[] bytes) {
		return (CASArray) DefaultCASDataSerializer.getInstance().binary2array(bytes);
	}

	public static CASArray newFromResultSet(ResultSet rset) throws SQLException {
		return DefaultCASDataSerializer.getInstance().resultSet2array(rset);
	}

	public static CASArray newFromJsonData(String jsonStr) {
		return (CASArray) DefaultCASDataSerializer.getInstance().json2array(jsonStr);
	}

	public static CASArray newInstance() {
		return new CASArray();
	}

	@Override
	public String getDump() {
		if (this.size() == 0) {
			return "[ Empty CASArray ]";
		}
		return DefaultObjectDumpFormatter.prettyPrintDump(this.dump());
	}

	@Override
	public String getDump(boolean noFormat) {
		if (!noFormat) {
			return this.dump();
		}
		return this.getDump();
	}

	private String dump() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		Object objDump = null;
		for (CASDataWrapper wrappedObject : this.dataHolder) {
			if (wrappedObject.getTypeId() == CASDataType.CAS_OBJECT) {
				objDump = ((ICASObject) wrappedObject.getObject()).getDump(false);
			} else if (wrappedObject.getTypeId() == CASDataType.CAS_ARRAY) {
				objDump = ((ICASArray) wrappedObject.getObject()).getDump(false);
			} else if (wrappedObject.getTypeId() == CASDataType.BYTE_ARRAY) {
				objDump = DefaultObjectDumpFormatter.prettyPrintByteArray((byte[]) wrappedObject.getObject());
			} else if (wrappedObject.getTypeId() == CASDataType.CLASS) {
				objDump = wrappedObject.getObject().getClass().getName();
			} else {
				objDump = wrappedObject.getObject();
			}
			sb.append(" (").append(wrappedObject.getTypeId().name().toLowerCase()).append(") ").append(objDump)
					.append(';');
		}
		if (this.size() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		sb.append('}');
		return sb.toString();
	}

	@Override
	public String getHexDump() {
		return ByteUtils.fullHexDump(this.toBinary());
	}

	@Override
	public byte[] toBinary() {
		return this.serializer.array2binary(this);
	}

	@Override
	public String toJson() {
		return DefaultCASDataSerializer.getInstance().array2json(this.flatten());
	}

	@Override
	public boolean isNull(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return wrapper != null && wrapper.getTypeId() == CASDataType.NULL;
	}

	@Override
	public CASDataWrapper get(int index) {
		return this.dataHolder.get(index);
	}

	@Override
	public Boolean getBool(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Boolean) wrapper.getObject()) : null;
	}

	@Override
	public Byte getByte(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Byte) wrapper.getObject()) : null;
	}

	@Override
	public Integer getUnsignedByte(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? DefaultCASDataSerializer.getInstance().getUnsignedByte((byte) wrapper.getObject())
				: null;
	}

	@Override
	public Short getShort(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Short) wrapper.getObject()) : null;
	}

	@Override
	public Integer getInt(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Integer) wrapper.getObject()) : null;
	}

	@Override
	public Long getLong(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Long) wrapper.getObject()) : null;
	}

	@Override
	public Float getFloat(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Float) wrapper.getObject()) : null;
	}

	@Override
	public Double getDouble(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((Double) wrapper.getObject()) : null;
	}

	@Override
	public String getUtfString(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((String) wrapper.getObject()) : null;
	}

	@Override
	public String getText(int index) {
		return this.getUtfString(index);
	}

	@Override
	public Collection<Boolean> getBoolArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Boolean>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public byte[] getByteArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (byte[]) ((wrapper != null) ? wrapper.getObject() : null);
	}

	@Override
	public Collection<Integer> getUnsignedByteArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		if (wrapper == null) {
			return null;
		}
		DefaultCASDataSerializer serializer = DefaultCASDataSerializer.getInstance();
		Collection<Integer> intCollection = new ArrayList<Integer>();
		byte[] array;
		for (int length = (array = (byte[]) wrapper.getObject()).length, i = 0; i < length; ++i) {
			byte b = array[i];
			intCollection.add(serializer.getUnsignedByte(b));
		}
		return intCollection;
	}

	@Override
	public Collection<Short> getShortArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Short>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public Collection<Integer> getIntArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Integer>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public Collection<Long> getLongArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Long>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public Collection<Float> getFloatArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Float>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public Collection<Double> getDoubleArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<Double>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public Collection<String> getUtfStringArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (Collection<String>) ((wrapper != null) ? ((Collection) wrapper.getObject()) : null);
	}

	@Override
	public ICASArray getCASArray(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((ICASArray) wrapper.getObject()) : null;
	}

	@Override
	public ICASObject getCASObject(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? ((ICASObject) wrapper.getObject()) : null;
	}

	@Override
	public Object getClass(int index) {
		CASDataWrapper wrapper = this.dataHolder.get(index);
		return (wrapper != null) ? wrapper.getObject() : null;
	}

	@Override
	public void addBool(boolean value) {
		this.addObject(value, CASDataType.BOOL);
	}

	@Override
	public void addBoolArray(Collection<Boolean> value) {
		this.addObject(value, CASDataType.BOOL_ARRAY);
	}

	@Override
	public void addByte(byte value) {
		this.addObject(value, CASDataType.BYTE);
	}

	@Override
	public void addByteArray(byte[] value) {
		this.addObject(value, CASDataType.BYTE_ARRAY);
	}

	@Override
	public void addDouble(double value) {
		this.addObject(value, CASDataType.DOUBLE);
	}

	@Override
	public void addDoubleArray(Collection<Double> value) {
		this.addObject(value, CASDataType.DOUBLE_ARRAY);
	}

	@Override
	public void addFloat(float value) {
		this.addObject(value, CASDataType.FLOAT);
	}

	@Override
	public void addFloatArray(Collection<Float> value) {
		this.addObject(value, CASDataType.FLOAT_ARRAY);
	}

	@Override
	public void addInt(int value) {
		this.addObject(value, CASDataType.INT);
	}

	@Override
	public void addIntArray(Collection<Integer> value) {
		this.addObject(value, CASDataType.INT_ARRAY);
	}

	@Override
	public void addLong(long value) {
		this.addObject(value, CASDataType.LONG);
	}

	@Override
	public void addLongArray(Collection<Long> value) {
		this.addObject(value, CASDataType.LONG_ARRAY);
	}

	@Override
	public void addNull() {
		this.addObject(null, CASDataType.NULL);
	}

	@Override
	public void addCASArray(ICASArray value) {
		this.addObject(value, CASDataType.CAS_ARRAY);
	}

	@Override
	public void addCASObject(ICASObject value) {
		this.addObject(value, CASDataType.CAS_OBJECT);
	}

	@Override
	public void addShort(short value) {
		this.addObject(value, CASDataType.SHORT);
	}

	@Override
	public void addShortArray(Collection<Short> value) {
		this.addObject(value, CASDataType.SHORT_ARRAY);
	}

	@Override
	public void addUtfString(String value) {
		this.addObject(value, CASDataType.UTF_STRING);
	}

	@Override
	public void addText(String value) {
		this.addObject(value, CASDataType.TEXT);
	}

	@Override
	public void addUtfStringArray(Collection<String> value) {
		this.addObject(value, CASDataType.UTF_STRING_ARRAY);
	}

	@Override
	public void addClass(Object o) {
		this.addObject(o, CASDataType.CLASS);
	}

	@Override
	public void add(CASDataWrapper wrappedObject) {
		this.dataHolder.add(wrappedObject);
	}

	@Override
	public boolean contains(Object obj) {
		if (obj instanceof ICASArray || obj instanceof ICASObject) {
			throw new UnsupportedOperationException("ICASArray and ICASObject are not supported by this method.");
		}
		boolean found = false;
		Iterator<CASDataWrapper> iter = this.dataHolder.iterator();
		while (iter.hasNext()) {
			Object item = iter.next().getObject();
			if (item.equals(obj)) {
				found = true;
				break;
			}
		}
		return found;
	}

	@Override
	public Object getElementAt(int index) {
		Object item = null;
		CASDataWrapper wrapper = this.dataHolder.get(index);
		if (wrapper != null) {
		}
		item = wrapper.getObject();
		return item;
	}

	@Override
	public Iterator<CASDataWrapper> iterator() {
		return this.dataHolder.iterator();
	}

	@Override
	public void removeElementAt(int index) {
		this.dataHolder.remove(index);
	}

	@Override
	public int size() {
		return this.dataHolder.size();
	}

	@Override
	public String toString() {
		return "[CASArray, size: " + this.size() + "]";
	}

	private void addObject(Object value, CASDataType typeId) {
		this.dataHolder.add(new CASDataWrapper(typeId, value));
	}

	private List<Object> flatten() {
		List<Object> list = new ArrayList<Object>();
		DefaultCASDataSerializer.getInstance().flattenArray(list, this);
		return list;
	}
}

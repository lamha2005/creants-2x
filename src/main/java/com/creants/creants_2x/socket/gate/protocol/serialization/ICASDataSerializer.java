package com.creants.creants_2x.socket.gate.protocol.serialization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.socket.gate.entities.CASArray;
import com.creants.creants_2x.socket.gate.entities.CASObject;
import com.creants.creants_2x.socket.gate.entities.ICASArray;
import com.creants.creants_2x.socket.gate.entities.ICASObject;

/**
 * @author Lamhm
 *
 */
public interface ICASDataSerializer {
	byte[] object2binary(final ICASObject object);

	byte[] array2binary(final ICASArray array);

	ICASObject binary2object(final byte[] byteArray);

	ICASArray binary2array(final byte[] byteArray);

	String object2json(final Map<String, Object> map);

	String array2json(final List<Object> list);

	ICASObject json2object(final String jsonString);

	ICASArray json2array(final String jsonString);

	ICASObject pojo2CAS(final Object pojoObject);

	Object CAS2pojo(final ICASObject casObj);

	CASObject resultSet2object(final ResultSet resultSet) throws SQLException;

	CASArray resultSet2array(final ResultSet resultSet) throws SQLException;
}

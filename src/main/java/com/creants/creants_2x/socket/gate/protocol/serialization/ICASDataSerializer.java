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
	byte[] object2binary(ICASObject object);


	byte[] array2binary(ICASArray array);


	ICASObject binary2object(byte[] byteArray);


	ICASArray binary2array(byte[] byteArray);


	String object2json(Map<String, Object> map);


	String array2json(List<Object> list);


	ICASObject json2object(String jsonString);


	ICASArray json2array(String jsonString);


	CASObject resultSet2object(ResultSet resultSet) throws SQLException;


	CASArray resultSet2array(ResultSet resultSet) throws SQLException;
}

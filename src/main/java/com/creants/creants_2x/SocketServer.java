package com.creants.creants_2x;

import com.creants.creants_2x.socket.gate.entities.CASArray;
import com.creants.creants_2x.socket.gate.entities.CASObject;
import com.creants.creants_2x.socket.gate.entities.ICASArray;
import com.creants.creants_2x.socket.gate.entities.ICASObject;

/**
 * @author LamHM
 *
 */
public class SocketServer {
	public static void main(String[] args) {
		ICASObject obj = CASObject.newInstance();
		obj.putUtfString("username", "lamha");
		obj.putUtfString("pass", "1234");

		ICASArray casArray = CASArray.newInstance();
		ICASObject obj1 = CASObject.newInstance();
		obj1.putInt("user_id", 1);
		casArray.addCASObject(obj1);
		obj.putCASArray("list_user", casArray);
		byte[] binary = obj.toBinary();

		CASObject.newFromBinaryData(binary);
		String utfString = obj.getUtfString("username");
		System.out.println(utfString);
	}
}

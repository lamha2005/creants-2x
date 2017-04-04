package com.creants.creants_2x;

import java.util.Arrays;

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
		casArray.addCASObject(obj);
		obj.putCASArray("list_user", casArray);
		
		byte[] bytes = obj.toBinary();
		System.out.println(Arrays.toString(bytes));
		CASObject data = CASObject.newFromBinaryData(bytes);      
		String utfString = data.getUtfString("username");
		System.out.println(utfString);
	}
}

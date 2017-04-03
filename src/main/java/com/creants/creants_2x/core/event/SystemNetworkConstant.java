package com.creants.creants_2x.core.event;

/**
 * Class định nghĩa các Event của hệ thống. Các Event của hệ thống sẽ được xử lý
 * bởi
 * 
 * @author LamHa
 *
 */
public class SystemNetworkConstant {
	public static final byte PROTOCOL_VERSION = 1;

	/************************ SERVICE ID *********************/
	public static final short COMMAND_ERROR = 0x00;
	public static final short COMMAND_USER_CONNECT = 0x01;
	public static final short COMMAND_USER_DISCONNECT = 0x02;
	public static final short COMMAND_USER_SEND_INFO = 0x03;
	public static final short COMMAND_USER_JOIN_GAME = 0x04;
	public static final short COMMAND_USER_LOGIN = 0x05;
	public static final short COMMAND_USER_LOGOUT = 0x06;
	public static final short COMMAND_GET_GAME_LIST = 0x07;
	public static final short COMMAND_USER_JOIN_ROOM = 0x08;
	public static final short COMMAND_USER_LEAVE_ROOM = 0x09;
	public static final short COMMAND_USER_CREATE_ROOM = 0x0A;

	public static final short COMMAND_PING_PONG = 0x10;
	public static final short COMMAND_MONEY_CHANGE = 0x11;
	public static final short COMMAND_UNKNOW = 0xFF;

	/************************ KEY ****************************/
	public static final short KEYR_ERROR = 0x00;

	public static final short KEYS_USERNAME = 0x01;
	public static final short KEYS_PASSWORD = 0x02;
	public static final short KEYL_MONEY = 0x03;
	public static final short KEYS_AVATAR = 0x04;
	public static final short KEYS_LANGUAGE = 0x05;
	public static final short KEYS_GAME_NAME = 0x06;
	public static final short KEYB_GAME_ID = 0x07;
	public static final short KEYS_FULL_NAME = 0x08;
	public static final short KEYS_DEVICE_NAME = 0x09;
	public static final short KEYS_DEVICE_OS = 0x0A;
	public static final short KEYS_OS_VERSION = 0x0B;
	public static final short KEYBL_IS_JAIBREAK = 0x0C;
	public static final short KEYS_GAME_VERSION = 0x0E;
	public static final short KEYS_DEVICE_UNIQUE_ID = 0x0F;
	public static final short KEYS_PHONE_NUMBER = 0x10;
	public static final short KEYR_SCREEN_WIDTH = 0x11;
	public static final short KEYR_SCREEN_HEIGHT = 0x12;
	public static final short KEYBL_IS_FIRST_LOGIN = 0x13;
	public static final short KEYS_MESSAGE = 0x14;
	public static final short KEYS_JSON_DATA = 0x15;
	public static final short KEYB_STATUS = 0x16;
	public static final short KEYI_USER_ID = 0x17;
	public static final short KEYR_ACTION_IN_GAME = 0x18;

	public static final short KEYI_ROOM_ID = 0x19;
	public static final short KEYS_ROOM_NAME = 0x1A;
	public static final short KEYS_TOKEN = 0x1B;
	public static final short KEYR_COMMAND_ID = 0x1C;
	public static final short KEYB_GAME_STATE = 0x1E;

}

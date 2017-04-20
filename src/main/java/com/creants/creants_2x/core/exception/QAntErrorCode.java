package com.creants.creants_2x.core.exception;

/**
 * @author LamHM
 *
 */
public enum QAntErrorCode implements IErrorCode {
	HANDSHAKE_API_OBSOLETE("HANDSHAKE_API_OBSOLETE", 0),
	LOGIN_BAD_ZONENAME("LOGIN_BAD_ZONENAME", 1),
	LOGIN_BAD_USERNAME("LOGIN_BAD_USERNAME", 2),
	LOGIN_BAD_PASSWORD("LOGIN_BAD_PASSWORD", 3),
	LOGIN_BANNED_USER("LOGIN_BANNED_USER", 4),
	LOGIN_ZONE_FULL("LOGIN_ZONE_FULL", 5),
	LOGIN_ALREADY_LOGGED("LOGIN_ALREADY_LOGGED", 6),
	LOGIN_SERVER_FULL("LOGIN_SERVER_FULL", 7),
	LOGIN_INACTIVE_ZONE("LOGIN_INACTIVE_ZONE", 8),
	LOGIN_NAME_CONTAINS_BAD_WORDS("LOGIN_NAME_CONTAINS_BAD_WORDS", 9),
	LOGIN_GUEST_NOT_ALLOWED("LOGIN_GUEST_NOT_ALLOWED", 10),
	LOGIN_BANNED_IP("LOGIN_BANNED_IP", 11),
	ROOM_DUPLICATE_NAME("ROOM_DUPLICATE_NAME", 12),
	CREATE_ROOM_BAD_GROUP("CREATE_ROOM_BAD_GROUP", 13),
	ROOM_NAME_BAD_SIZE("ROOM_NAME_BAD_SIZE", 14),
	ROOM_NAME_CONTAINS_BADWORDS("ROOM_NAME_CONTAINS_BADWORDS", 15),
	CREATE_ROOM_ZONE_FULL("CREATE_ROOM_ZONE_FULL", 16),
	CREATE_ROOM_EXCEED_USER_LIMIT("CREATE_ROOM_EXCEED_USER_LIMIT", 17),
	CREATE_ROOM_WRONG_PARAMETER("CREATE_ROOM_WRONG_PARAMETER", 18),
	JOIN_ALREADY_JOINED("JOIN_ALREADY_JOINED", 19),
	JOIN_ROOM_FULL("JOIN_ROOM_FULL", 20),
	JOIN_BAD_PASSWORD("JOIN_BAD_PASSWORD", 21),
	JOIN_BAD_ROOM("JOIN_BAD_ROOM", 22),
	JOIN_ROOM_LOCKED("JOIN_ROOM_LOCKED", 23),
	SUBSCRIBE_GROUP_ALREADY_SUBSCRIBED("SUBSCRIBE_GROUP_ALREADY_SUBSCRIBED", 24),
	SUBSCRIBE_GROUP_NOT_FOUND("SUBSCRIBE_GROUP_NOT_FOUND", 25),
	UNSUBSCRIBE_GROUP_NOT_SUBSCRIBED("UNSUBSCRIBE_GROUP_NOT_SUBSCRIBED", 26),
	UNSUBSCRIBE_GROUP_NOT_FOUND("UNSUBSCRIBE_GROUP_NOT_FOUND", 27),
	GENERIC_ERROR("GENERIC_ERROR", 28),
	ROOM_NAME_CHANGE_PERMISSION_ERR("ROOM_NAME_CHANGE_PERMISSION_ERR", 29),
	ROOM_PASS_CHANGE_PERMISSION_ERR("ROOM_PASS_CHANGE_PERMISSION_ERR", 30),
	ROOM_CAPACITY_CHANGE_PERMISSION_ERR("ROOM_CAPACITY_CHANGE_PERMISSION_ERR", 31),
	SWITCH_NO_PLAYER_SLOTS_AVAILABLE("SWITCH_NO_PLAYER_SLOTS_AVAILABLE", 32),
	SWITCH_NO_SPECTATOR_SLOTS_AVAILABLE("SWITCH_NO_SPECTATOR_SLOTS_AVAILABLE", 33),
	SWITCH_NOT_A_GAME_ROOM("SWITCH_NOT_A_GAME_ROOM", 34),
	SWITCH_NOT_JOINED_IN_ROOM("SWITCH_NOT_JOINED_IN_ROOM", 35),
	BUDDY_LIST_LOAD_FAILURE("BUDDY_LIST_LOAD_FAILURE", 36),
	BUDDY_LIST_FULL("BUDDY_LIST_FULL", 37),
	BUDDY_BLOCK_FAILURE("BUDDY_BLOCK_FAILURE", 38),
	BUDDY_TOO_MANY_VARIABLES("BUDDY_TOO_MANY_VARIABLES", 39),
	JOIN_GAME_ACCESS_DENIED("JOIN_GAME_ACCESS_DENIED", 40),
	JOIN_GAME_NOT_FOUND("JOIN_GAME_NOT_FOUND", 41),
	INVITATION_NOT_VALID("INVITATION_NOT_VALID", 42);

	private short id;


	private QAntErrorCode(String s, int id) {
		this.id = (short) id;
	}


	@Override
	public short getId() {
		return this.id;
	}
}

package com.creants.creants_2x.socket.gate.wood;

import java.util.Iterator;

import com.creants.creants_2x.socket.gate.IQAntUser;

import io.netty.channel.Channel;

/**
 * @author LamHa
 *
 */
public class QAntUser implements IQAntUser {
	private long sessionId;
	private int userId;
	private long createTime;
	private String userName;
	private String name;
	private long money;
	private String avatar;
	private byte language;
	private boolean isJoiningARoom = false;
	private long loginMoney;
	private long loginTime;
	private byte currentGameId = -1;
	private Channel channel;


	public QAntUser() {
		userId = -1;
	}


	@Override
	public <V> V getAttribute(Object key, Class<V> clazz) {
		return null;
	}


	@Override
	public Iterator<Object> getAttributeKeys() {
		return null;
	}


	public boolean isNPC() {
		return false;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public long getCreatedTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public Channel getChannel() {
		return channel;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}


	@Override
	public byte getDeviceType() {
		return 0;
	}


	@Override
	public long getSessionId() {
		return sessionId;
	}


	public String getClientIp() {
		return null;
	}


	public void setClientIp(String clientIp) {
	}


	@Override
	public String getLocale() {
		return null;
	}


	@Override
	public String getPlatformInformation() {
		return null;
	}


	@Override
	public byte getProtocolVersion() {
		return 0;
	}


	@Override
	public String getScreenSize() {
		return null;
	}


	@Override
	public int getUserId() {
		return userId;
	}


	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	@Override
	public String getUserName() {
		return userName;
	}


	@Override
	public String getVersion() {
		return null;
	}


	@Override
	public void initialize(String version, long sessionId, long clientId, byte deviceType, long createTime) {

	}


	@Override
	public void removeAttribute(Object key) {

	}


	@Override
	public void setAttribute(Object key, Object value) {

	}


	@Override
	public void setVersion(String version) {

	}


	public boolean isConnected() {
		return false;
	}


	public long getMoney() {
		return money;
	}


	public void setMoney(long money) {
		this.money = money;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public byte getLanguage() {
		return language;
	}


	public void setLanguage(byte language) {
		this.language = language;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public synchronized boolean isJoining() {
		return isJoiningARoom;
	}


	public synchronized void setJoining(boolean flag) {
		isJoiningARoom = flag;
	}


	public long getLastRequestTime() {
		return 0;
	}


	public synchronized void updateLastRequestTime() {
		setLastRequestTime(System.currentTimeMillis());
	}


	public void setLastRequestTime(long lastRequestTime) {
		// TODO set lastrequestTime cua 1 seesion user
	}


	public void setLoginInfo(QAntUser user) {
		userId = user.getUserId();
		userName = user.getUserName();
		avatar = user.getAvatar();
		language = user.getLanguage();
		money = user.getMoney();
		loginTime = System.currentTimeMillis();
	}


	public long getLoginMoney() {
		return loginMoney;
	}


	public void setLoginMoney(long loginMoney) {
		this.loginMoney = loginMoney;
	}


	public long getLoginTime() {
		return loginTime;
	}


	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}


	public long getTimeOnline() {
		return (System.currentTimeMillis() - getLoginTime()) / 1000;
	}


	@Override
	public byte getCurrentGameId() {
		return currentGameId;
	}


	@Override
	public void setCurrentGameId(byte currentGameId) {
		this.currentGameId = currentGameId;
	}


	@Override
	public String toString() {
		return String.format("[sessionId: %d, userId: %s, username: %s, currentGame: %s, money: %d]", sessionId, userId,
				userName, currentGameId, money);
	}

}

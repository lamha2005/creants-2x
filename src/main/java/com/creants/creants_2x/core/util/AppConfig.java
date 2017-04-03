package com.creants.creants_2x.core.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LamHa
 *
 */
public class AppConfig {
	private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);
	private static String socketIp;
	private static int socketPort;
	private static String websocketIp;
	private static int websocketPort;

	// Couchbase
	public static long couchBaseOpTimeout;
	public static String cacheHosts;
	public static String bucketName;
	public static String cachePassword;
	
	public static String graphApi;

	public static void init(String configPath) {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(configPath)) {
			prop.load(input);

		} catch (Exception e) {
			LOG.error("Can't load config: ", e);
			return;
		}

		socketIp = prop.getProperty("socket.ip", "localhost");
		socketPort = Integer.parseInt(prop.getProperty("socket.port", "8888"));
		websocketIp = prop.getProperty("websocket.ip", "localhost");
		websocketPort = Integer.parseInt(prop.getProperty("websocket.port", "1235"));

		// couchbase
		couchBaseOpTimeout = Long.parseLong(prop.getProperty("couchbase.opTimeout", "1000"));
		cacheHosts = prop.getProperty("couchbase.cacheHosts");
		bucketName = prop.getProperty("couchbase.bucketName");
		cachePassword = prop.getProperty("couchbase.pass");

		graphApi = prop.getProperty("graph.api","http://localhost:8686/api/");

	}

	public static String getSocketIp() {
		return socketIp;
	}

	public static int getSocketPort() {
		return socketPort;
	}

	public static String getWebsocketIp() {
		return websocketIp;
	}

	public static int getWebsocketPort() {
		return websocketPort;
	}

}

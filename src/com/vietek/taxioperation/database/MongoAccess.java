package com.vietek.taxioperation.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.vietek.taxioperation.util.ConfigUtil;

public class MongoAccess {

	private static MongoClient mongoClient = null;
	public static MongoClient getMongoClient() {
		if (mongoClient == null) {
			String host =  ConfigUtil.getConfigUtil().getPropValues("MONGO_SERVER");
			int port =  Integer.valueOf(ConfigUtil.getConfigUtil().getPropValues("MONGO_PORT"));
			mongoClient = new MongoClient(host, port);
		}
		return mongoClient;
	}

	public static MongoDatabase getDB(String dbName) {
		MongoClient mongoClient = MongoAccess.getMongoClient();
		return mongoClient.getDatabase(dbName);
	}

	public static MongoClient getNewMongoClient() {
		MongoClient mongoClient;
		String host =  ConfigUtil.getConfigUtil().getPropValues("MONGO_SERVER");
		int port =  Integer.valueOf(ConfigUtil.getConfigUtil().getPropValues("MONGO_PORT"));
		mongoClient = new MongoClient(host, port);
		return mongoClient;
	}
	
	public static MongoDatabase getNewDB(String dbName) {
		MongoClient mongoClient = MongoAccess.getNewMongoClient();
		return mongoClient.getDatabase(dbName);
	}
}

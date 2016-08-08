package com.vietek.trackingOnline.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class DBMongo {

	public DB db;
	public static MongoClient mongoClient = CreateMongo();

	
	public DBMongo() {

	}
	public static MongoClient CreateMongo() {
		MongoClientURI uri = new MongoClientURI("mongodb://user:pass@host:port/db");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());

		return client;
	}

	public DB getDb() {
		return db;
	}
	public void setDb(DB db) {
		this.db = db;
	}
	
}

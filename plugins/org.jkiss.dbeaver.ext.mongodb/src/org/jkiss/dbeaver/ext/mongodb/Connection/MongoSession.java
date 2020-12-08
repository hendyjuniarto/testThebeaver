package org.jkiss.dbeaver.ext.mongodb.Connection;


import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.model.exec.DBCSession;


public interface MongoSession extends DBCSession, MongoClient {
  @NotNull
  NonJDBCDataSource getDataSource();


  NonJDBCExecutionContext getExecutionContext();

  MongoDatabase getOriginal() throws MongoException;

  MongoDatabase getDatabase(String var1);

  ClientSession startSession();

  ClientSession startSession(ClientSessionOptions var1);

  void close();

  MongoIterable<String> listDatabaseNames();

  MongoIterable<String> listDatabaseNames(ClientSession var1);

  ListDatabasesIterable<Document> listDatabases();

  ListDatabasesIterable<Document> listDatabases(ClientSession var1);
}

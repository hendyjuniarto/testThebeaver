package org.jkiss.dbeaver.ext.mongodb.Connection;

import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public interface MongoMetaData extends MongoDatabase {
  String getName();

  MongoCollection<Document> getCollection(String var1);

  Document runCommand(Bson var1);

  void drop();

  MongoIterable<String> listCollectionNames();

  ListCollectionsIterable<Document> listCollections();

  void createView(String var1, String var2, List<? extends Bson> var3);
}

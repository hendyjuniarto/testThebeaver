package org.jkiss.dbeaver.ext.mongodb.Model;

import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCExecutionContext;
import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCRemoteInstance;

public class MongoExecutionContext extends NonJDBCExecutionContext {
  public MongoExecutionContext(NonJDBCRemoteInstance instance,
      String purpose) {
    super(instance, purpose);
  }
}

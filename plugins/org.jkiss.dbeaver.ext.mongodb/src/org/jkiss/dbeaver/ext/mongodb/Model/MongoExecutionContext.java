package org.jkiss.dbeaver.ext.mongodb.Model;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCExecutionContext;
import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCRemoteInstance;
import org.jkiss.utils.CommonUtils;

public class MongoExecutionContext extends NonJDBCExecutionContext {
  private static final Log log = Log.getLog(MongoExecutionContext.class);

  private String activeDatabaseName;
  public MongoExecutionContext(NonJDBCRemoteInstance instance,
      String purpose) {
    super(instance, purpose);
  }

  @NotNull
  @Override
  public MongoDataSource getDataSource() {
    return (MongoDataSource) super.getDataSource();
  }

}

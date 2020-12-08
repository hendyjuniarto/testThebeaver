package org.jkiss.dbeaver.ext.mongodb.Connection;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.model.exec.DBCExecutionPurpose;
import org.jkiss.dbeaver.model.impl.AbstractSession;
import org.jkiss.dbeaver.model.runtime.DBRBlockingObject;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;

import java.sql.Connection;
import java.sql.SQLException;

public class MongoConnectionImpl extends AbstractSession implements MongoSession,
    DBRBlockingObject {

  @NotNull
  final NonJDBCExecutionContext context;

  public MongoConnectionImpl(NonJDBCExecutionContext context, DBRProgressMonitor monitor, DBCExecutionPurpose purpose, String taskTitle) {
    super(monitor, purpose, taskTitle);
    this.context = context;
  }
  @Override
  public MongoDatabase getOriginal() throws MongoException
  {
    return context.getConnection(getProgressMonitor());
  }

}

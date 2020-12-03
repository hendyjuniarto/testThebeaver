package org.jkiss.dbeaver.ext.mongodb.Connection;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBPExclusiveResource;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCExecutionContext;
import org.jkiss.dbeaver.model.impl.SimpleExclusiveLock;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCExecutionContext;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSInstance;
import org.jkiss.dbeaver.model.struct.DBSObject;

import java.util.ArrayList;
import java.util.List;

public class NonJDBCRemoteInstance implements DBSInstance {
  private static final Log log = Log.getLog(NonJDBCRemoteInstance.class);
  @NotNull
  protected final NonJDBCDataSource dataSource;
  @Nullable
  protected NonJDBCExecutionContext executionContext;
  @Nullable
  protected NonJDBCExecutionContext metaContext;
  @NotNull
  private final List<NonJDBCExecutionContext> allContexts = new ArrayList();
  private final DBPExclusiveResource exclusiveLock = new SimpleExclusiveLock();

  protected NonJDBCRemoteInstance(@NotNull DBRProgressMonitor monitor,
      @NotNull NonJDBCDataSource dataSource,
      boolean initContext) throws DBException {
    this.dataSource = dataSource;
    if (initContext) {
      initializeMainContext(monitor);
    }
  }
  protected void initializeMainContext(@NotNull DBRProgressMonitor monitor) throws
      DBCException {
    if (executionContext == null) {
      this.executionContext = dataSource.createExecutionContext(this, getMainContextName());
      this.executionContext.connect(monitor, null, null, null, true);
    }
  }
  public NonJDBCExecutionContext initializeMetaContext(@NotNull DBRProgressMonitor monitor) throws DBException {
    if (this.metaContext != null) {
      return this.metaContext;
    } else if (!this.dataSource.getContainer().getDriver().isEmbedded() && this.dataSource.getContainer().getPreferenceStore().getBoolean("database.meta.separate.connection")) {
      synchronized(this.allContexts) {
        this.metaContext = this.dataSource.createExecutionContext(this, this.getMetadataContextName());
        this.metaContext.connect(monitor, true, (Integer)null, (NonJDBCExecutionContext)null, true);
        return this.metaContext;
      }
    } else {
      return this.executionContext;
    }
  }

  @Override
  public DBCExecutionContext getDefaultContext(DBRProgressMonitor dbrProgressMonitor, boolean b) {
    return null;
  }

  @Override
  public DBCExecutionContext[] getAllContexts() {
    return new DBCExecutionContext[0];
  }

  @Override
  public DBCExecutionContext openIsolatedContext(DBRProgressMonitor dbrProgressMonitor,
      String s,
      DBCExecutionContext dbcExecutionContext) throws DBException {
    return null;
  }

  @Override
  public void shutdown(DBRProgressMonitor dbrProgressMonitor) {

  }

  @Override
  public DBPExclusiveResource getExclusiveLock() {
    return this.exclusiveLock;
  }

  @Override
  public DBSObject getParentObject() {
    return null;
  }

  @Override
  public NonJDBCDataSource getDataSource() {
    return this.dataSource;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public boolean isPersisted() {
    return false;
  }

  @NotNull
  protected String getMainContextName() {
    return NonJDBCExecutionContext.TYPE_MAIN;
  }
  @NotNull
  protected String getMetadataContextName() {
    return "Metadata";
  }

}

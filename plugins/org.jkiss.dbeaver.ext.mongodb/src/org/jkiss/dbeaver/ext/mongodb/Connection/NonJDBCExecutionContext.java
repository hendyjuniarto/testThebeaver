package org.jkiss.dbeaver.ext.mongodb.Connection;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.eclipse.core.runtime.IAdaptable;
import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBPTransactionIsolation;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCExecutionPurpose;
import org.jkiss.dbeaver.model.exec.DBCSavepoint;
import org.jkiss.dbeaver.model.exec.DBCSession;
import org.jkiss.dbeaver.model.exec.DBCTransactionManager;
import org.jkiss.dbeaver.model.exec.DBExecUtils;
import org.jkiss.dbeaver.model.impl.AbstractExecutionContext;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;



public class NonJDBCExecutionContext extends AbstractExecutionContext<NonJDBCDataSource> implements
    DBCTransactionManager, IAdaptable {
  public static final String TYPE_MAIN = "Main";
  public static final String TYPE_METADATA = "Metadata";
  private static final Log log = Log.getLog(NonJDBCExecutionContext.class);
  static final int TXN_INFO_READ_TIMEOUT = 5000;
  @NotNull
  private volatile NonJDBCRemoteInstance instance;
  private volatile MongoDatabase connection;
  private volatile Integer transactionIsolationLevel;

  public NonJDBCExecutionContext(@NotNull NonJDBCRemoteInstance instance, String purpose) {
    super(instance.getDataSource(), purpose);
    this.instance = instance;
  }

  @Override
  public <T> T getAdapter(Class<T> aClass) {
    return null;
  }

  @Override
  public NonJDBCRemoteInstance getOwnerInstance() {
    return this.instance;
  }
  protected void setOwnerInstance (@NotNull NonJDBCRemoteInstance instance){
    this.instance = instance;
  }
  @NotNull
  private MongoDatabase getConnection(){
    return this.connection;
  }

  public void connect(DBRProgressMonitor monitor)throws DBCException{
    this.connect(monitor,(Boolean)null, (Integer)null, (NonJDBCExecutionContext)null, true);
  }

  protected void connect(@NotNull DBRProgressMonitor monitor, Boolean autoCommit, @Nullable Integer txnLevel, NonJDBCExecutionContext initFrom, boolean addContext)
      throws DBCException {
    if(this.connection !=null && addContext){
      log.error("Reopening not-closed connection");
      this.close();
    }
    NonJDBCRemoteInstance currentInstance = this.instance;
    DBExecUtils.startContextInitiation(dataSource.getContainer());
    Object exclusiveLock = currentInstance.getExclusiveLock().acquireExclusiveLock();

    try{
      this.connection = ((NonJDBCDataSource)this.dataSource).openConnection(monitor, this, this.purpose);
      log.info("connection" + this.connection);
      monitor.subTask("Set connection defaults");
      if (this.connection == null) {
        throw new DBCException("Null connection returned");
      }
      try {
        ((NonJDBCDataSource)this.dataSource).initializeContextState(monitor, this, initFrom);
      } catch (DBException var39) {
        log.warn("Error while initializing context state", var39);
      }
    }
    finally {
      DBExecUtils.finishContextInitiation(((NonJDBCDataSource)this.dataSource).getContainer());
      currentInstance.getExclusiveLock().releaseExclusiveLock(exclusiveLock);
    }
  }
//  protected void disconnect() {
//    synchronized(this) {
//      if (this.connection != null && !((NonJDBCDataSource)this.dataSource).closeConnection(this.connection, this.purpose)) {
//        log.debug("Connection close timeout");
//      }
//      this.connection = null;
//    }
//
//    super.closeContext();
//  }

  @Override
  public boolean isConnected() {
    return false;
  }

  @Override
  public MongoSession openSession(@NotNull DBRProgressMonitor monitor, @NotNull DBCExecutionPurpose purpose, @NotNull String taskTitle) {
    return ((NonJDBCDataSource)this.dataSource).createConnection(monitor, this, purpose, taskTitle);
  }

  @NotNull
  public MongoDatabase getConnection(DBRProgressMonitor monitor) throws MongoException {
    if (connection == null) {
      try {
        connect(monitor);
      } catch (DBCException e) {
        if (e.getCause() instanceof MongoException) {
          throw (MongoException) e.getCause();
        } else {
          throw new MongoException(e.getMessage());
        }
      }
    }
    return connection;
  }

  @Override
  public void checkContextAlive(DBRProgressMonitor dbrProgressMonitor) throws DBException {

  }

  @Override
  public InvalidateResult invalidateContext(DBRProgressMonitor dbrProgressMonitor, boolean b)
      throws DBException {
    return null;
  }

  @Override
  public void close() {

  }

  @Override
  public DBPTransactionIsolation getTransactionIsolation() throws DBCException {
    return null;
  }

  @Override
  public void setTransactionIsolation(DBRProgressMonitor dbrProgressMonitor,
      DBPTransactionIsolation dbpTransactionIsolation) throws DBCException {

  }

  @Override
  public boolean isAutoCommit() throws DBCException {
    return false;
  }

  @Override
  public void setAutoCommit(DBRProgressMonitor dbrProgressMonitor, boolean b) throws DBCException {

  }

  @Override
  public boolean supportsSavepoints() {
    return false;
  }

  @Override
  public DBCSavepoint setSavepoint(DBRProgressMonitor dbrProgressMonitor, String s)
      throws DBCException {
    return null;
  }

  @Override
  public void releaseSavepoint(DBRProgressMonitor dbrProgressMonitor, DBCSavepoint dbcSavepoint)
      throws DBCException {

  }

  @Override
  public void commit(DBCSession dbcSession) throws DBCException {

  }

  @Override
  public void rollback(DBCSession dbcSession, DBCSavepoint dbcSavepoint) throws DBCException {

  }

  @Override
  public boolean isSupportsTransactions() {
    return false;
  }
}

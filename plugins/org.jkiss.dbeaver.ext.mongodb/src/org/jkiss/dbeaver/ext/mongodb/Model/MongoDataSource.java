package org.jkiss.dbeaver.ext.mongodb.Model;

import com.mongodb.MongoClient;
import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ModelPreferences;
import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCDataSource;
import org.jkiss.dbeaver.ext.mongodb.Connection.NonJDBCExecutionContext;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBPDataSourceInfo;
import org.jkiss.dbeaver.model.data.DBDDataFormatterProfile;
import org.jkiss.dbeaver.model.data.DBDValueHandler;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCExecutionContext;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.sql.SQLDialect;
import org.jkiss.dbeaver.model.struct.DBSInstance;
import org.jkiss.dbeaver.model.struct.DBSObject;

import java.util.Collection;

public class MongoDataSource extends NonJDBCDataSource {
  private static final Log log = Log.getLog(MongoDataSource.class);
  private String activeDatabaseName;
  private volatile boolean hasStatistics;

  public MongoDataSource(DBRProgressMonitor monitor,
      DBPDataSourceContainer container) throws DBException {
    super(monitor, container);
    hasStatistics =
        !container.getPreferenceStore().getBoolean(ModelPreferences.READ_EXPENSIVE_STATISTICS);
  }

  protected MongoDataSource(DBRProgressMonitor monitor,
      DBPDataSourceContainer container, boolean initContext)
      throws DBException {
    super(monitor, container, initContext);
  }

  @Override
  public Collection<? extends DBSObject> getChildren(DBRProgressMonitor dbrProgressMonitor)
      throws DBException {
    return null;
  }

  @Override
  public DBSObject getChild(DBRProgressMonitor dbrProgressMonitor, String s) throws DBException {
    return null;
  }

  @Override
  public Class<? extends DBSObject> getPrimaryChildType(DBRProgressMonitor dbrProgressMonitor)
      throws DBException {
    return null;
  }

  @Override
  protected MongoClient openConnection(@NotNull DBRProgressMonitor monitor, @Nullable
      NonJDBCExecutionContext context, @NotNull String purpose) throws DBCException {
    try {
      return super.openConnection(monitor, context, purpose);
    } catch (DBCException e) {
      log.info("error : " + e);
      throw e;
    }
  }

//  protected void initializeContextState(@NotNull DBRProgressMonitor monitor, @NotNull NonJDBCExecutionContext context, NonJDBCExecutionContext initFrom) throws DBException {
//    super.initializeContextState(monitor, context, initFrom);
//    if (initFrom != null) {
//      MongoExecutionContext metaContext = (MongoExecutionContext)initFrom;
//      ((MongoExecutionContext)context).initDefaultsFrom(monitor, metaContext);
//    } else {
//      ((MongoExecutionContext)context).refreshDefaults(monitor, true);
//    }
//
//  }
  @Override
  public void cacheStructure(DBRProgressMonitor dbrProgressMonitor, int i) throws DBException {

  }

  @Override
  public DBSObject getParentObject() {
    return null;
  }

  @Override
  public DBPDataSource getDataSource() {
    return null;
  }

  @Override
  public DBPDataSourceInfo getInfo() {
    return null;
  }

  @Override
  public Object getDataSourceFeature(String s) {
    return null;
  }

  @Override
  public SQLDialect getSQLDialect() {
    return null;
  }

  @Override
  public void initialize(DBRProgressMonitor dbrProgressMonitor) throws DBException {

  }

  @Override
  public ErrorType discoverErrorType(Throwable throwable) {
    return null;
  }

  @Override
  public ErrorPosition[] getErrorPosition(DBRProgressMonitor dbrProgressMonitor,
      DBCExecutionContext dbcExecutionContext,
      String s,
      Throwable throwable) {
    return new ErrorPosition[0];
  }

  @Override
  public DBSObject refreshObject(DBRProgressMonitor dbrProgressMonitor) throws DBException {
    return null;
  }

  @Override
  public DBDDataFormatterProfile getDataFormatterProfile() {
    return null;
  }

  @Override
  public void setDataFormatterProfile(DBDDataFormatterProfile dbdDataFormatterProfile) {

  }

  @Override
  public DBDValueHandler getDefaultValueHandler() {
    return null;
  }

  @Override
  public DBSInstance getDefaultInstance() {
    return null;
  }

  @Override
  public Collection<? extends DBSInstance> getAvailableInstances() {
    return null;
  }

  @Override
  public void shutdown(DBRProgressMonitor dbrProgressMonitor) {

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
}

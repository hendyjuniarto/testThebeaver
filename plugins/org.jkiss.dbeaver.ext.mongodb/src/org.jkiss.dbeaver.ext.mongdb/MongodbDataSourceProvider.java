package org.jkiss.dbeaver.ext.elasticsearch;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ext.generic.GenericDataSourceProvider;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.app.DBPPlatform;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSourceProvider;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;

public class MongodbDataSourceProvider extends JDBCDataSourceProvider {

  private static final Log log = Log.getLog(MongodbDataSourceProvider.class);

  public MongodbDataSourceProvider()
  {
  }

  @Override
  public void init(@NotNull DBPPlatform platform) {

  }

  @Override
  public long getFeatures() {
    return 0;
  }

  @Override
  public DBPDataSource openDataSource(DBRProgressMonitor dbrProgressMonitor,
      DBPDataSourceContainer dbpDataSourceContainer) throws DBException {
    return null;
  }

  @Override
  public String getConnectionURL(DBPDriver dbpDriver,
      DBPConnectionConfiguration dbpConnectionConfiguration) {
    return null;
  }
}
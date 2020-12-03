package org.jkiss.dbeaver.ext.mongodb.Connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ModelPreferences;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBPDataSourceInfo;
import org.jkiss.dbeaver.model.DBPErrorAssistant;
import org.jkiss.dbeaver.model.DBPRefreshableObject;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.data.DBDPreferences;
import org.jkiss.dbeaver.model.exec.DBCConnectException;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.runtime.DBRRunnableWithProgress;
import org.jkiss.dbeaver.model.struct.DBSInstanceContainer;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.model.struct.DBSObjectContainer;
import org.jkiss.dbeaver.utils.RuntimeUtils;
import org.jkiss.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class NonJDBCDataSource
    implements DBPDataSource, DBPErrorAssistant, DBPRefreshableObject,
    DBDPreferences, DBSObject, DBSObjectContainer, DBSInstanceContainer {
  private static final Log log = Log.getLog(NonJDBCDataSource.class);
  @NotNull
  private final DBPDataSourceContainer container;
  @NotNull
  protected volatile DBPDataSourceInfo dataSourceInfo;
  private NonJDBCRemoteInstance defaultRemoteInstance;
  private int databaseMajorVersion;
  private int databaseMinorVersion;


  protected NonJDBCDataSource(@NotNull DBRProgressMonitor monitor,
      @NotNull DBPDataSourceContainer container) throws DBException {
    this(monitor, container, true);
  }

  protected NonJDBCDataSource(@NotNull DBRProgressMonitor monitor,
      @NotNull DBPDataSourceContainer container,
      boolean initContext) throws DBException {
    this.dataSourceInfo = new NonJDBCDataSourceInfo(container);
    this.container = container;
    if (initContext) {
      this.initializeRemoteInstance(monitor);
    }
  }

  protected void initializeRemoteInstance(@NotNull DBRProgressMonitor monitor) throws DBException {
    this.defaultRemoteInstance = new NonJDBCRemoteInstance(monitor, this, true);
  }

  protected MongoClient openConnection(@NotNull DBRProgressMonitor monitor, @Nullable
      NonJDBCExecutionContext context, @NotNull String purpose) throws DBCException {

    DBPDriver driver = getContainer().getDriver();
    if (!CommonUtils.isEmpty(driver.getDriverClassName())) {
      try {
        driver.loadDriver(monitor);
        Class.forName(driver.getDriverClassName(), true, driver.getClassLoader());
      } catch (Exception e) {
        throw new DBCException("Driver class '" + driver.getDriverClassName() + "' not found", e);
      }
    }
    DBPConnectionConfiguration connectionInfo = new DBPConnectionConfiguration(container.getActualConnectionConfiguration());
    MongoClientOptions options = getAllConnectionProperties(connectionInfo);

    try{

      final String url = getConnectionURL(connectionInfo);
      monitor.subTask("Connecting " + purpose);
      MongoClient[] connection = new MongoClient[1];
      Exception[] error = new Exception[1];
      int openTimeout = getContainer().getPreferenceStore().getInt(ModelPreferences.CONNECTION_OPEN_TIMEOUT);
      List<MongoCredential> credentials = new ArrayList<MongoCredential>();
      credentials.add(MongoCredential.createCredential
          (connectionInfo.getUserName(),connectionInfo.getDatabaseName(),connectionInfo.getUserPassword().toCharArray()));
//      try{
//
//      }catch (Exception e){
//        throw new DBCException("Authentication error", e);
//      }
      DBRRunnableWithProgress connectTask = monitor1 ->{
        ServerAddress serverAddress = new ServerAddress(connectionInfo.getHostName(),
            Integer.parseInt(connectionInfo.getHostPort()));
        try {
          connection[0] = new MongoClient(serverAddress, credentials, options);
        }catch (Exception e){
          log.debug("error:" + e);
        }
      };
      boolean openTaskFinished;
        if (openTimeout <= 0) {
          openTaskFinished = true;
          connectTask.run(monitor);
        } else {
          openTaskFinished = RuntimeUtils.runTask(connectTask, "Opening database connection", (long)(openTimeout + 2000));
        }

      if (error[0] != null) {
        throw error[0];
      } else if (!openTaskFinished) {
        throw new DBCException("Connection has timed out");
      } else if (connection[0] == null) {
        throw new DBCException("Null connection returned");
      } else {
        return connection[0];
      }
  } catch (MongoException e) {
      throw new DBCConnectException(e.getMessage(), e, this);
    } catch (DBCException e) {
      throw e;
    } catch (Throwable e) {
      throw new DBCConnectException("Unexpected driver error occurred while connecting to the database", e);
    }

  }
  protected void initializeContextState(@NotNull DBRProgressMonitor monitor, @NotNull NonJDBCExecutionContext context, NonJDBCExecutionContext initFrom) throws DBException {
  }

  public boolean closeConnection(MongoClient connection, String purpose){
      if(connection!=null){
        return RuntimeUtils.runTask((monitor) -> {
          try {
            connection.close();
          } catch (Throwable var4) {
            log.debug("Error closing connection", var4);
          }

        }, "Close NonJDBC connection (" + purpose + ")",
            (long)this.getContainer().getPreferenceStore().getInt("connection.close.timeout"));

      }else {
        log.debug("Null connection parameter");
        return true;
      }
  }

  protected MongoClientOptions getAllConnectionProperties(DBPConnectionConfiguration connectionInfo) throws MongoException {

    Properties properties =  fillConnectionProperties(connectionInfo);

    MongoClientOptions options = MongoClientOptions.builder()
//        .readPreference(ReadPreference.valueOf(properties.getProperty("readReference")))
//        .maxConnectionIdleTime(Integer.parseInt(properties.getProperty("maxConnectionIdleTime")))
//        .connectionsPerHost(Integer.parseInt(properties.getProperty("connectionsPerHost")))
//        .connectTimeout(Integer.parseInt(properties.getProperty("connectTimeout")))
//        .socketTimeout(Integer.parseInt(properties.getProperty("socketTimeout")))
        .readPreference(ReadPreference.nearest())
        .maxConnectionIdleTime(60000)
        .connectionsPerHost(20)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .build();

    return options;
  }

  protected Properties fillConnectionProperties(DBPConnectionConfiguration connectionInfo) {
    Properties connectProps = new Properties();
    {
      final Map<String, Object> driverProperties = container.getDriver().getConnectionProperties();
      for (Map.Entry<String, Object> prop : driverProperties.entrySet()) {
        connectProps.setProperty(prop.getKey(), CommonUtils.toString(prop.getValue()));
      }
    }
    for (Map.Entry<String, String> prop : connectionInfo.getProperties().entrySet()) {
      connectProps.setProperty(CommonUtils.toString(prop.getKey()),
          CommonUtils.toString(prop.getValue()));
    }
    return connectProps;
  }
  protected String getConnectionURL(DBPConnectionConfiguration connectionInfo) {
    String url = connectionInfo.getUrl();
    if (CommonUtils.isEmpty(url)) {
      // It can be empty in some cases (e.g. when we create connections from command line command)
      url = getContainer().getDriver().getDataSourceProvider().getConnectionURL(getContainer().getDriver(), connectionInfo);
    }
    return url;
  }

  protected NonJDBCExecutionContext createExecutionContext(NonJDBCRemoteInstance instance, String type) {
    return new NonJDBCExecutionContext(instance, type);
  }

  @NotNull
  public DBPDataSourceContainer getContainer() {
    return this.container;
  }

  @NotNull
  public DBPDataSourceInfo getInfo() {
    return this.dataSourceInfo;
  }

}

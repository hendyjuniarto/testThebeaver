package org.jkiss.dbeaver.ext.mongodb.Connection;

import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBPTransactionIsolation;
import org.jkiss.dbeaver.model.impl.AbstractDataSourceInfo;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSourceInfo;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCTransactionIsolation;
import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.List;

public class NonJDBCDataSourceInfo extends AbstractDataSourceInfo {
  private boolean readOnly;
  private String databaseProductName;
  private String databaseProductVersion;
  private String driverName;
  private String driverVersion;
  private Version databaseVersion;
  private String procedureTerm;
  private String catalogTerm;
  private boolean supportsTransactions;
  private List<DBPTransactionIsolation> supportedIsolations;
  private boolean supportsReferences = true;
  private boolean supportsIndexes = true;
  private boolean supportsStoredCode = true;
  private boolean supportsBatchUpdates = false;
  private boolean supportsScroll;

  public NonJDBCDataSourceInfo(DBPDataSourceContainer container){
    this.readOnly = false;
    this.databaseProductName = "?";
    this.databaseProductVersion = "?";
    this.driverName = container.getDriver().getName();
    this.driverVersion = "?";
    this.databaseVersion = new Version(0, 0, 0);
    this.supportsBatchUpdates = false;
    this.supportsTransactions = false;
    this.supportedIsolations = new ArrayList();
    this.supportedIsolations.add(0, JDBCTransactionIsolation.NONE);
    this.supportsScroll = true;
  }
  @Override
  public String getDatabaseProductName() {
    return null;
  }

  @Override
  public String getDatabaseProductVersion() {
    return null;
  }

  @Override
  public Version getDatabaseVersion() {
    return null;
  }

  @Override
  public String getDriverName() {
    return null;
  }

  @Override
  public String getDriverVersion() {
    return null;
  }

  @Override
  public String getSchemaTerm() {
    return null;
  }

  @Override
  public String getProcedureTerm() {
    return null;
  }

  @Override
  public String getCatalogTerm() {
    return null;
  }
}

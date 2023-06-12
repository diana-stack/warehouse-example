package pl.com.warehousefull.warehouse.models.config;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "config_origin_db")
public class ConfigOriginDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @Column(name = "db_name")
    private String dbName;

    @Size(max = 255)
    @Column(name = "db_url")
    private String dbUrl;

    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 32)
    @Column(name = "server_name", length = 32)
    private String serverName;

    @Column(name = "tables_to_sync")
    private String tablesToSync;
    @Column(name = "config_warehouse_db_id")
    private int configWarehouseDbId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getConfigWarehouseDbId() {
        return configWarehouseDbId;
    }

    public void setConfigWarehouseDbId(int configWarehouseDbId) {
        this.configWarehouseDbId = configWarehouseDbId;
    }

    public String getTablesToSync() {
        return tablesToSync;
    }

    public void setTablesToSync(String tablesToSync) {
        this.tablesToSync = tablesToSync;
    }
}
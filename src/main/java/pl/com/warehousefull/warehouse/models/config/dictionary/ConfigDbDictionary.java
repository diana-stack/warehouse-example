package pl.com.warehousefull.warehouse.models.config.dictionary;

import javax.persistence.*;

@Entity
@Table(name = "config_db_dictionary")
public class ConfigDbDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "origin_db_name")
    private String originDbName;

    @Column(name = "origin_db_url")
    private String originDbUrl;

    @Column(name = "origin_user_name")
    private String originUserName;

    @Column(name = "origin_password")
    private String originPassword;

    @Column(name = "origin_server_name")
    private String originServerName;

    @Column(name = "origin_tables_to_sync")
    private String originTablesToSync;

    @Column(name = "warehouse_db_name")
    private String warehouseDbName;

    @Column(name = "warehouse_db_url")
    private String warehouseDbUrl;

    @Column(name = "warehouse_user_name")
    private String warehouseUserName;

    @Column(name = "warehouse_password")
    private String warehousePassword;


    public ConfigDbDictionary(Integer id, String originDbName, String originDbUrl, String originUserName, String originPassword, String originServerName, String originTablesToSync, String warehouseDbName, String warehouseDbUrl, String warehouseUserName, String warehousePassword) {
        this.id = id;
        this.originDbName = originDbName;
        this.originDbUrl = originDbUrl;
        this.originUserName = originUserName;
        this.originPassword = originPassword;
        this.originServerName = originServerName;
        this.originTablesToSync = originTablesToSync;
        this.warehouseDbName = warehouseDbName;
        this.warehouseDbUrl = warehouseDbUrl;
        this.warehouseUserName = warehouseUserName;
        this.warehousePassword = warehousePassword;
    }

    public ConfigDbDictionary() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginDbName() {
        return originDbName;
    }

    public void setOriginDbName(String originDbName) {
        this.originDbName = originDbName;
    }

    public String getOriginDbUrl() {
        return originDbUrl;
    }

    public void setOriginDbUrl(String originDbUrl) {
        this.originDbUrl = originDbUrl;
    }

    public String getOriginUserName() {
        return originUserName;
    }

    public void setOriginUserName(String originUserName) {
        this.originUserName = originUserName;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getOriginServerName() {
        return originServerName;
    }

    public void setOriginServerName(String originServerName) {
        this.originServerName = originServerName;
    }

    public String getOriginTablesToSync() {
        return originTablesToSync;
    }

    public void setOriginTablesToSync(String originTablesToSync) {
        this.originTablesToSync = originTablesToSync;
    }

    public String getWarehouseDbName() {
        return warehouseDbName;
    }

    public void setWarehouseDbName(String warehouseDbName) {
        this.warehouseDbName = warehouseDbName;
    }

    public String getWarehouseDbUrl() {
        return warehouseDbUrl;
    }

    public void setWarehouseDbUrl(String warehouseDbUrl) {
        this.warehouseDbUrl = warehouseDbUrl;
    }

    public String getWarehouseUserName() {
        return warehouseUserName;
    }

    public void setWarehouseUserName(String warehouseUserName) {
        this.warehouseUserName = warehouseUserName;
    }

    public String getWarehousePassword() {
        return warehousePassword;
    }

    public void setWarehousePassword(String warehousePassword) {
        this.warehousePassword = warehousePassword;
    }
}
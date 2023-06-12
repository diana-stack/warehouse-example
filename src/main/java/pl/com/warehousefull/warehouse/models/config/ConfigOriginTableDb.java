package pl.com.warehousefull.warehouse.models.config;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "config_origin_table_db")
public class ConfigOriginTableDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "config_origin_db_id")
    private Integer configOriginDbId;

    @Size(max = 255)
    @Column(name = "table_name")
    private String tableName;

    @Size(max = 255)
    @Column(name = "create_view_name")
    private String createViewName;

    @Size(max = 255)
    @Column(name = "update_view_name")
    private String updateViewName;

    @Column(name = "fields_to_sync")
    private String fieldsToSync;

    @Column(name = "table_type")
    private String tableType;

    @Column(name = "entry_type")
    private String entryType;

    @Column(name = "field_to_count")
    private String fieldToCount;

    @Column(name = "config_warehouse_table_db_unid")
    private Integer configWarehouseTableDbUnid;

    @Column(name = "fields_to_sync_types")
    private String fieldsToSyncTypes;

    public ConfigOriginTableDb() {
    }

    public ConfigOriginTableDb(Integer id, Integer configOriginDbId, String tableName, String createViewName, String updateViewName, String fieldsToSync, String tableType, String entryType, String fieldToCount, Integer configWarehouseTableDbUnid, String fieldsToSyncTypes) {
        this.id = id;
        this.configOriginDbId = configOriginDbId;
        this.tableName = tableName;
        this.createViewName = createViewName;
        this.updateViewName = updateViewName;
        this.fieldsToSync = fieldsToSync;
        this.tableType = tableType;
        this.entryType = entryType;
        this.fieldToCount = fieldToCount;
        this.configWarehouseTableDbUnid = configWarehouseTableDbUnid;
        this.fieldsToSyncTypes = fieldsToSyncTypes;
    }

    public Integer getConfigWarehouseTableDbUnid() {
        return configWarehouseTableDbUnid;
    }

    public void setConfigWarehouseTableDbUnid(Integer configWarehouseTableDbUnid) {
        this.configWarehouseTableDbUnid = configWarehouseTableDbUnid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldToCount() {
        return fieldToCount;
    }

    public void setFieldToCount(String fieldToCount) {
        this.fieldToCount = fieldToCount;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Integer getConfigOriginDbId() {
        return configOriginDbId;
    }

    public void setConfigOriginDbId(Integer configOriginDbId) {
        this.configOriginDbId = configOriginDbId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreateViewName() {
        return createViewName;
    }

    public void setCreateViewName(String createViewName) {
        this.createViewName = createViewName;
    }

    public String getUpdateViewName() {
        return updateViewName;
    }

    public void setUpdateViewName(String updateViewName) {
        this.updateViewName = updateViewName;
    }

    public String getFieldsToSync() {
        return fieldsToSync;
    }

    public void setFieldsToSync(String fieldsToSync) {
        this.fieldsToSync = fieldsToSync;
    }

    public String getFieldsToSyncTypes() {
        return fieldsToSyncTypes;
    }

    public void setFieldsToSyncTypes(String fieldsToSyncTypes) {
        this.fieldsToSyncTypes = fieldsToSyncTypes;
    }
}
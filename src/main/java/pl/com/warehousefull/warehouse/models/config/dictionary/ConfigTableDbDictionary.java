package pl.com.warehousefull.warehouse.models.config.dictionary;

import javax.persistence.*;

@Entity
@Table(name = "config_table_db_dictionary")
public class ConfigTableDbDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "origin_table_name")
    private String originTableName;

    @Column(name = "origin_create_view_name")
    private String originCreateViewName;

    @Column(name = "origin_update_view_name")
    private String originUpdateViewName;

    @Column(name = "origin_table_type")
    private String originTableType;

    @Column(name = "origin_field_to_count")
    private String originFieldToCount;

    @Column(name = "origin_fields_to_sync")
    private String originFieldsToSync;

    @Column(name = "warehouse_table_name")
    private String warehouseTableName;

    @Column(name = "warehouse_fields_to_sync")
    private String warehouseFieldsToSync;

    @Column(name = "warehouse_parent_unid_field")
    private String warehouseParentUnidField;

    @Column(name = "fields_to_sync_types")
    private String fieldsToSyncTypes;

    public ConfigTableDbDictionary(Integer id, String originTableName, String originCreateViewName, String originUpdateViewName, String originTableType, String originFieldToCount, String originFieldsToSync, String warehouseTableName, String warehouseFieldsToSync, String warehouseParentUnidField, String fieldsToSyncTypes) {
        this.id = id;
        this.originTableName = originTableName;
        this.originCreateViewName = originCreateViewName;
        this.originUpdateViewName = originUpdateViewName;
        this.originTableType = originTableType;
        this.originFieldToCount = originFieldToCount;
        this.originFieldsToSync = originFieldsToSync;
        this.warehouseTableName = warehouseTableName;
        this.warehouseFieldsToSync = warehouseFieldsToSync;
        this.warehouseParentUnidField = warehouseParentUnidField;
        this.fieldsToSyncTypes = fieldsToSyncTypes;
    }

    public ConfigTableDbDictionary() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginTableName() {
        return originTableName;
    }

    public void setOriginTableName(String originTableName) {
        this.originTableName = originTableName;
    }

    public String getOriginCreateViewName() {
        return originCreateViewName;
    }

    public void setOriginCreateViewName(String originCreateViewName) {
        this.originCreateViewName = originCreateViewName;
    }

    public String getOriginUpdateViewName() {
        return originUpdateViewName;
    }

    public void setOriginUpdateViewName(String originUpdateViewName) {
        this.originUpdateViewName = originUpdateViewName;
    }

    public String getOriginTableType() {
        return originTableType;
    }

    public void setOriginTableType(String originTableType) {
        this.originTableType = originTableType;
    }

    public String getOriginFieldToCount() {
        return originFieldToCount;
    }

    public void setOriginFieldToCount(String originFieldToCount) {
        this.originFieldToCount = originFieldToCount;
    }

    public String getOriginFieldsToSync() {
        return originFieldsToSync;
    }

    public void setOriginFieldsToSync(String originFieldsToSync) {
        this.originFieldsToSync = originFieldsToSync;
    }

    public String getWarehouseTableName() {
        return warehouseTableName;
    }

    public void setWarehouseTableName(String warehouseTableName) {
        this.warehouseTableName = warehouseTableName;
    }

    public String getWarehouseFieldsToSync() {
        return warehouseFieldsToSync;
    }

    public void setWarehouseFieldsToSync(String warehouseFieldsToSync) {
        this.warehouseFieldsToSync = warehouseFieldsToSync;
    }

    public String getWarehouseParentUnidField() {
        return warehouseParentUnidField;
    }

    public void setWarehouseParentUnidField(String warehouseParentUnidField) {
        this.warehouseParentUnidField = warehouseParentUnidField;
    }

    public String getFieldsToSyncTypes() {
        return fieldsToSyncTypes;
    }

    public void setFieldsToSyncTypes(String fieldsToSyncTypes) {
        this.fieldsToSyncTypes = fieldsToSyncTypes;
    }
}
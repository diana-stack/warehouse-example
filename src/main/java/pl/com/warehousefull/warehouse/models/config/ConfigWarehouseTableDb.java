package pl.com.warehousefull.warehouse.models.config;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "config_warehouse_table_db")
public class ConfigWarehouseTableDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "config_warehouse_db_id")
    private Integer configWarehouseDbId;

    @Size(max = 255)
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "fields_to_sync")
    private String fieldsToSync;

    @Column(name = "parent_unid_field")
    private String parentUnidField;

    public String getParentUnidField() {
        return parentUnidField;
    }

    public void setParentUnidField(String parentUnidField) {
        this.parentUnidField = parentUnidField;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConfigWarehouseDbId() {
        return configWarehouseDbId;
    }

    public void setConfigWarehouseDbId(Integer configWarehouseDbId) {
        this.configWarehouseDbId = configWarehouseDbId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldsToSync() {
        return fieldsToSync;
    }

    public void setFieldsToSync(String fieldsToSync) {
        this.fieldsToSync = fieldsToSync;
    }

}
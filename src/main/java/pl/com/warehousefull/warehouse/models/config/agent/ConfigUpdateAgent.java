package pl.com.warehousefull.warehouse.models.config.agent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config_update_agent")
public class ConfigUpdateAgent {
    @Id
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "config_origin_db_ids")
    private String configOriginDbIds;
    @Column(name = "warehouse_db_name")
    private String warehouseDbName;

    public ConfigUpdateAgent() {
    }

    public ConfigUpdateAgent(String startTime, String configOriginDbIds, String warehouseDbName) {
        this.startTime = startTime;
        this.configOriginDbIds = configOriginDbIds;
        this.warehouseDbName = warehouseDbName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getConfigOriginDbIds() {
        return configOriginDbIds;
    }

    public void setConfigOriginDbIds(String configOriginDbIds) {
        this.configOriginDbIds = configOriginDbIds;
    }

    public String getWarehouseDbName() {
        return warehouseDbName;
    }

    public void setWarehouseDbName(String warehouseDbName) {
        this.warehouseDbName = warehouseDbName;
    }
}
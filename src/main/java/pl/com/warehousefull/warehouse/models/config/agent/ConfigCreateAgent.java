package pl.com.warehousefull.warehouse.models.config.agent;

import javax.persistence.*;

@Entity
@Table(name = "config_create_agent")
public class ConfigCreateAgent {
    @Id
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "config_origin_db_ids")
    private String configOriginDbIds;
    @Column(name = "warehouse_db_name")
    private String warehouseDbName;

    public ConfigCreateAgent() {
    }

    public ConfigCreateAgent(String startTime, String configOriginDbIds, String warehouseDbName) {
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
package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;

import java.util.Optional;

public interface ConfigCreateAgentDbRepository extends JpaRepository<ConfigCreateAgent, String> {
    Optional<ConfigCreateAgent> findByStartTime(String time);
    Optional<ConfigCreateAgent> findByWarehouseDbName(String warehouseDbName);
}
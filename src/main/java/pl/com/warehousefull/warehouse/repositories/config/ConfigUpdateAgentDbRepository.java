package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;

import java.util.Optional;

public interface ConfigUpdateAgentDbRepository extends JpaRepository<ConfigUpdateAgent, String> {
    Optional<ConfigUpdateAgent> findByStartTime(String time);
    Optional<ConfigUpdateAgent> findByWarehouseDbName(String warehouseDbName);
}
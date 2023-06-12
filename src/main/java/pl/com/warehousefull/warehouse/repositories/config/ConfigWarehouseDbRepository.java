package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;

import java.util.Optional;

public interface ConfigWarehouseDbRepository extends JpaRepository<ConfigWarehouseDb, Integer> {
    Optional<ConfigWarehouseDb> findById(Integer id);
}
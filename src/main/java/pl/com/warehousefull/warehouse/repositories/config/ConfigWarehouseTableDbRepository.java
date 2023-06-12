package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;

import java.util.Optional;

public interface ConfigWarehouseTableDbRepository extends JpaRepository<ConfigWarehouseTableDb, Integer> {
    Optional<ConfigWarehouseTableDb> findById(Integer id);
}
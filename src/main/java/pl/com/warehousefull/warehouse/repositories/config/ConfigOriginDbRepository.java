package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;

import java.util.Optional;

public interface ConfigOriginDbRepository extends JpaRepository<ConfigOriginDb, Integer> {
    Optional<ConfigOriginDb> findById(Integer id);
}
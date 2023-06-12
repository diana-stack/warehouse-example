package pl.com.warehousefull.warehouse.repositories.config;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;

import java.util.List;
import java.util.Optional;

public interface ConfigOriginTableDbRepository extends JpaRepository<ConfigOriginTableDb, Integer> {
    Optional<ConfigOriginTableDb> findById(Integer id);

    List<ConfigOriginTableDb> findAllByConfigOriginDbId(Integer id);

}
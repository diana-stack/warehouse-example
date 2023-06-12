package pl.com.warehousefull.warehouse.repositories.config.dictionary;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigDbDictionary;

import java.util.Optional;

public interface ConfigDbDictionaryRepository extends JpaRepository<ConfigDbDictionary, String> {
    Optional<ConfigDbDictionary> findById(Integer id);
}
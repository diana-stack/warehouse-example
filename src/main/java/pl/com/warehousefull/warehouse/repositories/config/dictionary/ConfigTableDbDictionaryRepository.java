package pl.com.warehousefull.warehouse.repositories.config.dictionary;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigTableDbDictionary;

import java.util.Optional;

public interface ConfigTableDbDictionaryRepository extends JpaRepository<ConfigTableDbDictionary, String> {
    Optional<ConfigTableDbDictionary> findById(Integer id);
}
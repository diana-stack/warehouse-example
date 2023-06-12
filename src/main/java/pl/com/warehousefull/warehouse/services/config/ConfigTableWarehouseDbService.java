package pl.com.warehousefull.warehouse.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigTableDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseTableDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigTableDbDictionaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigTableWarehouseDbService {
    @Autowired
    private ConfigWarehouseTableDbRepository configWarehouseTableDbRepository;
    @Autowired
    private ConfigTableDbDictionaryRepository configTableDbDictionaryRepository;

    public void create(@RequestBody ConfigWarehouseTableDb configWarehouseTableDb){
        configWarehouseTableDbRepository.save(configWarehouseTableDb);
    }

    public void delete(@RequestBody ConfigWarehouseTableDb configWarehouseTableDb){
        configWarehouseTableDbRepository.delete(configWarehouseTableDb);
    }

    public void update(int id,ConfigWarehouseTableDb configWarehouseTableDbNew){
        ConfigWarehouseTableDb configWarehouseTableDb = configWarehouseTableDbRepository.findById(id).get();
        configWarehouseTableDb.setConfigWarehouseDbId(configWarehouseTableDbNew.getConfigWarehouseDbId());
        configWarehouseTableDb.setTableName(configWarehouseTableDbNew.getTableName());
        configWarehouseTableDb.setFieldsToSync(configWarehouseTableDbNew.getFieldsToSync());
        configWarehouseTableDb.setParentUnidField(configWarehouseTableDbNew.getParentUnidField());
        configWarehouseTableDbRepository.save(configWarehouseTableDb);
    }

    public List<ConfigWarehouseTableDb> getAll(){
        return configWarehouseTableDbRepository.findAll();
    }

    public Optional<ConfigWarehouseTableDb> getById(Integer id) {
        return configWarehouseTableDbRepository.findById(id);
    }

    public Optional<ConfigWarehouseTableDb> createNewEntryWithTemplate(Integer dictionaryId){
        ConfigTableDbDictionary configTableDbDictionary = configTableDbDictionaryRepository.findById(dictionaryId).get();
        ConfigWarehouseTableDb configWarehouseTableDb = new ConfigWarehouseTableDb();
        configWarehouseTableDb.setTableName(configTableDbDictionary.getWarehouseTableName());
        configWarehouseTableDb.setParentUnidField(configTableDbDictionary.getWarehouseParentUnidField());
        configWarehouseTableDb.setFieldsToSync(configTableDbDictionary.getWarehouseFieldsToSync());
        return Optional.of(configWarehouseTableDb);
    }

    public Optional<ConfigWarehouseTableDb> setEntryValuesByTemplate(Integer dictionaryId, ConfigWarehouseTableDb configWarehouseTableDb){
        ConfigTableDbDictionary configTableDbDictionary = configTableDbDictionaryRepository.findById(dictionaryId).get();
        configWarehouseTableDb.setTableName(configTableDbDictionary.getWarehouseTableName());
        configWarehouseTableDb.setParentUnidField(configTableDbDictionary.getWarehouseParentUnidField());
        configWarehouseTableDb.setFieldsToSync(configTableDbDictionary.getWarehouseFieldsToSync());
        return Optional.of(configWarehouseTableDb);
    }


}

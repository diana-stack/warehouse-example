package pl.com.warehousefull.warehouse.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigDbDictionaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigWarehouseDbService {
    @Autowired
    private ConfigWarehouseDbRepository configWarehouseDbRepository;

    @Autowired
    private ConfigDbDictionaryRepository configDbDictionaryRepository;

    public void save(@RequestBody ConfigWarehouseDb configWarehouseDb){
        configWarehouseDbRepository.save(configWarehouseDb);
    }

    public void delete(@RequestBody ConfigWarehouseDb configWarehouseDb){
        configWarehouseDbRepository.delete(configWarehouseDb);
    }

    public void update(int id, ConfigWarehouseDb configWarehouseDbNew){
        ConfigWarehouseDb configWarehouseDbOld = configWarehouseDbRepository.findById(id).get();
        configWarehouseDbOld.setDbName(configWarehouseDbNew.getDbName());
        configWarehouseDbOld.setDbUrl(configWarehouseDbNew.getDbUrl());
        configWarehouseDbOld.setPassword(configWarehouseDbNew.getPassword());
        configWarehouseDbOld.setUserName(configWarehouseDbNew.getUserName());
        configWarehouseDbRepository.save(configWarehouseDbOld);
    }

    public List<ConfigWarehouseDb> getAll(){
        return configWarehouseDbRepository.findAll();
    }

    public Optional<ConfigWarehouseDb> getById(Integer id) {
        return configWarehouseDbRepository.findById(id);
    }

    public Optional<ConfigWarehouseDb> createNewEntry(){
        Optional<ConfigWarehouseDb> configWarehouseDb = Optional.of(new ConfigWarehouseDb());
        return configWarehouseDb;
    }

    public Optional<ConfigWarehouseDb> createNewEntryWithTemplate(Integer dictionaryId){
        ConfigDbDictionary configDbDictionary = configDbDictionaryRepository.findById(dictionaryId).get();
        ConfigWarehouseDb configWarehouseDb = new ConfigWarehouseDb();
        configWarehouseDb.setDbName(configDbDictionary.getWarehouseDbName());
        configWarehouseDb.setDbUrl(configDbDictionary.getWarehouseDbUrl());
        configWarehouseDb.setUserName(configDbDictionary.getWarehouseUserName());
        configWarehouseDb.setPassword(configDbDictionary.getWarehousePassword());
        return Optional.of(configWarehouseDb);
    }

    public Optional<ConfigWarehouseDb> setEntryValuesByTemplate(Integer dictionaryId, ConfigWarehouseDb configWarehouseDb){
        ConfigDbDictionary configDbDictionary = configDbDictionaryRepository.findById(dictionaryId).get();
        configWarehouseDb.setDbName(configDbDictionary.getWarehouseDbName());
        configWarehouseDb.setDbUrl(configDbDictionary.getWarehouseDbUrl());
        configWarehouseDb.setUserName(configDbDictionary.getWarehouseUserName());
        configWarehouseDb.setPassword(configDbDictionary.getWarehousePassword());
        return Optional.of(configWarehouseDb);
    }

}

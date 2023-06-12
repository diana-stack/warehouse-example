package pl.com.warehousefull.warehouse.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigDbDictionaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigOriginDbService {
    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;

    @Autowired
    private ConfigDbDictionaryRepository configDbDictionaryRepository;

    public void save(@RequestBody ConfigOriginDb configOriginDb){
        configOriginDbRepository.save(configOriginDb);
    }

    public void delete(@RequestBody ConfigOriginDb configOriginDb){
        configOriginDbRepository.delete(configOriginDb);
    }

    public void update(int id,ConfigOriginDb configOriginDbNew){
        ConfigOriginDb configOriginDbOld = configOriginDbRepository.findById(id).get();
        configOriginDbOld.setConfigWarehouseDbId(configOriginDbNew.getConfigWarehouseDbId());
        configOriginDbOld.setDbName(configOriginDbNew.getDbName());
        configOriginDbOld.setDbUrl(configOriginDbNew.getDbUrl());
        configOriginDbOld.setPassword(configOriginDbNew.getPassword());
        configOriginDbOld.setServerName(configOriginDbNew.getServerName());
        configOriginDbOld.setTablesToSync(configOriginDbNew.getTablesToSync());
        configOriginDbOld.setUserName(configOriginDbNew.getUserName());
        configOriginDbRepository.save(configOriginDbOld);
    }


    public List<ConfigOriginDb> getAll(){
        return configOriginDbRepository.findAll();
    }

    public Optional<ConfigOriginDb> getById(Integer id) {
        return configOriginDbRepository.findById(id);
    }

    public Optional<ConfigOriginDb> createNewEntry(){
        Optional<ConfigOriginDb> configOriginDb = Optional.of(new ConfigOriginDb());
        return configOriginDb;
    }

    public Optional<ConfigOriginDb> createNewEntryWithTemplate(Integer dictionaryId){
        ConfigDbDictionary configDbDictionary = configDbDictionaryRepository.findById(dictionaryId).get();
        ConfigOriginDb configOriginDb = new ConfigOriginDb();
        configOriginDb.setDbName(configDbDictionary.getOriginDbName());
        configOriginDb.setDbUrl(configDbDictionary.getOriginDbUrl());
        configOriginDb.setUserName(configDbDictionary.getOriginUserName());
        configOriginDb.setPassword(configDbDictionary.getOriginPassword());
        configOriginDb.setServerName(configDbDictionary.getOriginServerName());
        configOriginDb.setTablesToSync(configDbDictionary.getOriginTablesToSync());
        return Optional.of(configOriginDb);
    }

    public Optional<ConfigOriginDb> setEntryValuesByTemplate(Integer dictionaryId, ConfigOriginDb configOriginDb){
        ConfigDbDictionary configDbDictionary = configDbDictionaryRepository.findById(dictionaryId).get();
        configOriginDb.setDbName(configDbDictionary.getOriginDbName());
        configOriginDb.setDbUrl(configDbDictionary.getOriginDbUrl());
        configOriginDb.setUserName(configDbDictionary.getOriginUserName());
        configOriginDb.setPassword(configDbDictionary.getOriginPassword());
        configOriginDb.setServerName(configDbDictionary.getOriginServerName());
        configOriginDb.setTablesToSync(configDbDictionary.getOriginTablesToSync());
        return Optional.of(configOriginDb);
    }
}

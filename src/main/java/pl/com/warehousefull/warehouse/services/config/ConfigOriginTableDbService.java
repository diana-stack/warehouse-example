package pl.com.warehousefull.warehouse.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigTableDbDictionary;
import pl.com.warehousefull.warehouse.parser.domino.DominoDocumentFieldsToSyncMapParser;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginTableDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigTableDbDictionaryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigOriginTableDbService {
    @Autowired
    private ConfigOriginTableDbRepository configOriginTableDbRepository;
    @Autowired
    private DominoDocumentFieldsToSyncMapParser dominoDocumentFieldsToSyncMapParser;
    @Autowired
    private ConfigTableDbDictionaryRepository configTableDbDictionaryRepository;

    public void save(@RequestBody ConfigOriginTableDb configOriginTableDb){
        configOriginTableDbRepository.save(configOriginTableDb);
    }

    public void delete(@RequestBody ConfigOriginTableDb configOriginTableDb){
        configOriginTableDbRepository.delete(configOriginTableDb);
    }

    public void update(int id, ConfigOriginTableDb configOriginTableDbNew){
        ConfigOriginTableDb configOriginTableDbOld = configOriginTableDbRepository.findById(id).get();
        configOriginTableDbOld.setConfigWarehouseTableDbUnid(configOriginTableDbNew.getConfigWarehouseTableDbUnid());
        configOriginTableDbOld.setTableName(configOriginTableDbNew.getTableName());
        configOriginTableDbOld.setTableType(configOriginTableDbNew.getTableType());
        configOriginTableDbOld.setEntryType(configOriginTableDbNew.getEntryType());
        configOriginTableDbOld.setCreateViewName(configOriginTableDbNew.getCreateViewName());
        configOriginTableDbOld.setUpdateViewName(configOriginTableDbNew.getUpdateViewName());
        configOriginTableDbOld.setFieldToCount(configOriginTableDbNew.getFieldToCount());
        configOriginTableDbOld.setFieldsToSync(configOriginTableDbNew.getFieldsToSync());
        configOriginTableDbOld.setFieldsToSyncTypes(configOriginTableDbNew.getFieldsToSyncTypes());
        configOriginTableDbRepository.save(configOriginTableDbOld);
    }

    public List<ConfigOriginTableDb> getAll(){
        List<ConfigOriginTableDb> configOriginTableDbList = new ArrayList<>();
        for (ConfigOriginTableDb configOriginTableDb : configOriginTableDbRepository.findAll()) {
            String fieldsNamesWithHTML = configOriginTableDb.getFieldsToSync();
            String fieldsNamesView = dominoDocumentFieldsToSyncMapParser.getFieldsToParseString(fieldsNamesWithHTML);
            configOriginTableDb.setFieldsToSync(fieldsNamesView);
            configOriginTableDbList.add(configOriginTableDb);
        }
        return configOriginTableDbList;
    }

    public Optional<ConfigOriginTableDb> getById(Integer id) {
        return configOriginTableDbRepository.findById(id);
    }

    public Optional<ConfigOriginTableDb> createNewEntry(){
        Optional<ConfigOriginTableDb> configOriginTableDb = Optional.of(new ConfigOriginTableDb());
        return configOriginTableDb;
    }

    public Optional<ConfigOriginTableDb> createNewEntryWithTemplate(Integer dictionaryId){
        ConfigTableDbDictionary configTableDbDictionary = configTableDbDictionaryRepository.findById(dictionaryId).get();
        ConfigOriginTableDb configOriginTableDb = new ConfigOriginTableDb();
        configOriginTableDb.setTableName(configTableDbDictionary.getOriginTableName());
        configOriginTableDb.setCreateViewName(configTableDbDictionary.getOriginCreateViewName());
        configOriginTableDb.setFieldsToSync(configTableDbDictionary.getOriginFieldsToSync());
        configOriginTableDb.setFieldsToSyncTypes(configTableDbDictionary.getFieldsToSyncTypes());
        configOriginTableDb.setTableType(configTableDbDictionary.getOriginTableType());
        configOriginTableDb.setEntryType(configTableDbDictionary.getWarehouseTableName());
        configOriginTableDb.setFieldToCount(configTableDbDictionary.getOriginFieldToCount());
        configOriginTableDb.setUpdateViewName(configTableDbDictionary.getOriginUpdateViewName());
        return Optional.of(configOriginTableDb);
    }

    public Optional<ConfigOriginTableDb> setEntryValuesByTemplate(Integer dictionaryId, ConfigOriginTableDb configOriginTableDb){
        ConfigTableDbDictionary configTableDbDictionary = configTableDbDictionaryRepository.findById(dictionaryId).get();
        configOriginTableDb.setTableName(configTableDbDictionary.getOriginTableName());
        configOriginTableDb.setCreateViewName(configTableDbDictionary.getOriginCreateViewName());
        configOriginTableDb.setFieldsToSync(configTableDbDictionary.getOriginFieldsToSync());
        configOriginTableDb.setFieldsToSyncTypes(configTableDbDictionary.getFieldsToSyncTypes());
        configOriginTableDb.setTableType(configTableDbDictionary.getOriginTableType());
        configOriginTableDb.setEntryType(configTableDbDictionary.getWarehouseTableName());
        configOriginTableDb.setFieldToCount(configTableDbDictionary.getOriginFieldToCount());
        configOriginTableDb.setUpdateViewName(configTableDbDictionary.getOriginUpdateViewName());
        return Optional.of(configOriginTableDb);
    }

}

package pl.com.warehousefull.warehouse.services.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigTableDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigTableDbDictionaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigTableDbDictionaryService {
    @Autowired
    private ConfigTableDbDictionaryRepository configTableDbDictionaryRepository;

    public void create(@RequestBody ConfigTableDbDictionary processedEntry){
        configTableDbDictionaryRepository.save(processedEntry);
    }

    public void delete(@RequestBody ConfigTableDbDictionary processedEntry){
        configTableDbDictionaryRepository.delete(processedEntry);
    }

    public void update(@RequestBody ConfigTableDbDictionary configTableDbDictionaryNew){
        ConfigTableDbDictionary configTableDbDictionaryOld = configTableDbDictionaryRepository.findById(configTableDbDictionaryNew.getId()).get();
        configTableDbDictionaryOld.setOriginTableName(configTableDbDictionaryNew.getOriginTableName());
        configTableDbDictionaryOld.setOriginTableType(configTableDbDictionaryNew.getOriginTableType());
        configTableDbDictionaryOld.setOriginCreateViewName(configTableDbDictionaryNew.getOriginCreateViewName());
        configTableDbDictionaryOld.setOriginUpdateViewName(configTableDbDictionaryNew.getOriginUpdateViewName());
        configTableDbDictionaryOld.setOriginFieldsToSync(configTableDbDictionaryNew.getOriginFieldsToSync());
        configTableDbDictionaryOld.setOriginFieldToCount(configTableDbDictionaryNew.getOriginFieldToCount());
        configTableDbDictionaryOld.setWarehouseTableName(configTableDbDictionaryNew.getWarehouseTableName());
        configTableDbDictionaryOld.setWarehouseFieldsToSync(configTableDbDictionaryNew.getWarehouseFieldsToSync());
        configTableDbDictionaryOld.setWarehouseParentUnidField(configTableDbDictionaryNew.getWarehouseParentUnidField());
        configTableDbDictionaryOld.setFieldsToSyncTypes(configTableDbDictionaryNew.getFieldsToSyncTypes());
        configTableDbDictionaryRepository.save(configTableDbDictionaryOld);
    }

    public List<ConfigTableDbDictionary> getAll(){
        return configTableDbDictionaryRepository.findAll();
    }

    public Optional<ConfigTableDbDictionary> getById(Integer id) {
        return configTableDbDictionaryRepository.findById(id);
    }

    public Optional<ConfigTableDbDictionary> createNewEntry(){
        Optional<ConfigTableDbDictionary> configTableDbDictionary = Optional.of(new ConfigTableDbDictionary());
        return configTableDbDictionary;
    }

    public void save(@RequestBody ConfigTableDbDictionary configTableDbDictionary){
        configTableDbDictionaryRepository.save(configTableDbDictionary);
    }


}

package pl.com.warehousefull.warehouse.services.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigDbDictionaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigDbDictionaryService {
    @Autowired
    private ConfigDbDictionaryRepository configDbDictionaryRepository;

    public void create(@RequestBody ConfigDbDictionary processedEntry){
        configDbDictionaryRepository.save(processedEntry);
    }

    public void delete(@RequestBody ConfigDbDictionary processedEntry){
        configDbDictionaryRepository.delete(processedEntry);
    }

    public void update(@RequestBody ConfigDbDictionary configDbDictionaryNew){
        ConfigDbDictionary configDbDictionaryOld = configDbDictionaryRepository.findById(configDbDictionaryNew.getId()).get();
        configDbDictionaryOld.setWarehouseDbName(configDbDictionaryNew.getWarehouseDbName());
        configDbDictionaryOld.setWarehouseDbUrl(configDbDictionaryNew.getWarehouseDbUrl());
        configDbDictionaryOld.setWarehouseUserName(configDbDictionaryNew.getWarehouseUserName());
        configDbDictionaryOld.setWarehousePassword(configDbDictionaryNew.getWarehousePassword());
        configDbDictionaryOld.setOriginDbName(configDbDictionaryNew.getOriginDbName());
        configDbDictionaryOld.setOriginDbUrl(configDbDictionaryNew.getOriginDbUrl());
        configDbDictionaryOld.setOriginPassword(configDbDictionaryNew.getOriginPassword());
        configDbDictionaryOld.setOriginServerName(configDbDictionaryNew.getOriginServerName());
        configDbDictionaryOld.setOriginUserName(configDbDictionaryNew.getOriginUserName());
        configDbDictionaryOld.setOriginTablesToSync(configDbDictionaryNew.getOriginTablesToSync());
        configDbDictionaryRepository.save(configDbDictionaryOld);
    }

    public List<ConfigDbDictionary> getAll(){
        return configDbDictionaryRepository.findAll();
    }

    public Optional<ConfigDbDictionary> getById(Integer id) {
        return configDbDictionaryRepository.findById(id);
    }

    public Optional<ConfigDbDictionary> createNewEntry(){
        Optional<ConfigDbDictionary> configDbDictionary = Optional.of(new ConfigDbDictionary());
        return configDbDictionary;
    }

    public void save(@RequestBody ConfigDbDictionary configDbDictionary){
        configDbDictionaryRepository.save(configDbDictionary);
    }


}

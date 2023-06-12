package pl.com.warehousefull.warehouse.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.services.config.ConfigOriginDbService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config/config-origin-db")
public class ConfigOriginDbController {

    @Autowired
    private ConfigOriginDbService configOriginDbService;

    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigOriginDb>> getAll() {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigOriginDb> all = configOriginDbService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigOriginDb> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigOriginDb> processedEntry = configOriginDbService.getById(id);

        return processedEntry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigOriginDb> create() {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigOriginDb> configOriginDb = configOriginDbService.createNewEntry();

        return configOriginDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/template/create")
    public ResponseEntity<ConfigOriginDb> createNewEntryWithTemplate(@RequestParam Integer dictionaryId) {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY BY TEMPLATE");
        Optional<ConfigOriginDb> configOriginDb = configOriginDbService.createNewEntryWithTemplate(dictionaryId);
        return configOriginDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/template/use")
    public ResponseEntity<ConfigOriginDb> setEntryValueByTemplate(@RequestParam Integer dictionaryId, @RequestBody ConfigOriginDb configOriginDb) {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY BY TEMPLATE");
        Optional<ConfigOriginDb> configOriginDbNew = configOriginDbService.setEntryValuesByTemplate(dictionaryId, configOriginDb);
        return configOriginDbNew.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigOriginDb configOriginDb) throws IOException {
        configOriginDbService.save(configOriginDb);
        return ResponseEntity.ok("Config document created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigOriginDb configOriginDb) throws IOException {
        if(!configOriginDbRepository.existsById(configOriginDb.getId())){
            return ResponseEntity.notFound().build();
        }

        configOriginDbService.delete(configOriginDb);
        return ResponseEntity.ok("Config document deleted successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam Integer id,@RequestBody ConfigOriginDb configOriginDbUpdate) throws IOException {
        if(!configOriginDbRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        configOriginDbService.update(id, configOriginDbUpdate);
        return ResponseEntity.ok("Config document update successfully!");
    }

}

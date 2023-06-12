package pl.com.warehousefull.warehouse.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;
import pl.com.warehousefull.warehouse.services.config.ConfigWarehouseDbService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config/config-warehouse-db")
public class ConfigWarehouseDbController {

    @Autowired
    private ConfigWarehouseDbService configWarehouseDbService;

    @Autowired
    private ConfigWarehouseDbRepository configWarehouseDbRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigWarehouseDb>> getAll() {
        Logger.getLogger(ConfigWarehouseDbController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigWarehouseDb> all = configWarehouseDbService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigWarehouseDb> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigWarehouseDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigWarehouseDb> plan = configWarehouseDbService.getById(id);
        return plan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigWarehouseDb> create() {
        Logger.getLogger(ConfigWarehouseDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigWarehouseDb> configWarehouseDb = configWarehouseDbService.createNewEntry();
        return configWarehouseDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/template/create")
    public ResponseEntity<ConfigWarehouseDb> createNewEntryWithTemplate(@RequestParam Integer dictionaryId) {
        Logger.getLogger(ConfigWarehouseDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY BY TEMPLATE");
        Optional<ConfigWarehouseDb> configWarehouseDb = configWarehouseDbService.createNewEntryWithTemplate(dictionaryId);
        return configWarehouseDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/template/use")
    public ResponseEntity<ConfigWarehouseDb> setEntryValueByTemplate(@RequestParam Integer dictionaryId, @RequestBody ConfigWarehouseDb configWarehouseDb) {
        Logger.getLogger(ConfigWarehouseDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR SET ENTRY VALUES BY TEMPLATE");
        Optional<ConfigWarehouseDb> configWarehouseDbNew = configWarehouseDbService.setEntryValuesByTemplate(dictionaryId, configWarehouseDb);
        return configWarehouseDbNew.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigWarehouseDb configWarehouseDb) throws IOException {
        configWarehouseDbService.save(configWarehouseDb);
        return ResponseEntity.ok("Config document created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigWarehouseDb configWarehouseDb) throws IOException {
        configWarehouseDbService.delete(configWarehouseDb);
        return ResponseEntity.ok("Config document deleted successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam Integer id,@RequestBody ConfigWarehouseDb configWarehouseDb) throws IOException {
        if(!configWarehouseDbRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        configWarehouseDbService.update(id, configWarehouseDb);
        return ResponseEntity.ok("Config document update successfully!");
    }

}

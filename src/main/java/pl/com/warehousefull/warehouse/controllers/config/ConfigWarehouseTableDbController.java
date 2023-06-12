package pl.com.warehousefull.warehouse.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseTableDbRepository;
import pl.com.warehousefull.warehouse.services.config.ConfigTableWarehouseDbService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config/config-warehouse-table-db")
public class ConfigWarehouseTableDbController {

    @Autowired
    private ConfigTableWarehouseDbService configTableWarehouseDbService;
    @Autowired
    private ConfigWarehouseTableDbRepository configWarehouseTableDbRepository;


    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigWarehouseTableDb>> getAll() {
        Logger.getLogger(ConfigWarehouseTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigWarehouseTableDb> all = configTableWarehouseDbService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigWarehouseTableDb> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigWarehouseTableDb> plan = configTableWarehouseDbService.getById(id);
        return plan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ConfigWarehouseTableDb configWarehouseTableDb) throws IOException {
        configTableWarehouseDbService.create(configWarehouseTableDb);
        return ResponseEntity.ok("Config document created successfully!");
    }

    @GetMapping("/template/create")
    public ResponseEntity<ConfigWarehouseTableDb> createNewEntryWithTemplate(@RequestParam Integer dictionaryId) {
        Logger.getLogger(ConfigWarehouseTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY BY TEMPLATE");
        Optional<ConfigWarehouseTableDb> configWarehouseTableDb = configTableWarehouseDbService.createNewEntryWithTemplate(dictionaryId);
        return configWarehouseTableDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
    @GetMapping("/template/use")
    public ResponseEntity<ConfigWarehouseTableDb> setEntryValueByTemplate(@RequestParam Integer dictionaryId) {
        Logger.getLogger(ConfigWarehouseTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR SET ENTRY VALUES BY TEMPLATE");
        Optional<ConfigWarehouseTableDb> configWarehouseTableDb = configTableWarehouseDbService.createNewEntryWithTemplate(dictionaryId);
        return configWarehouseTableDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigWarehouseTableDb configWarehouseTableDb) throws IOException {
        configTableWarehouseDbService.delete(configWarehouseTableDb);
        return ResponseEntity.ok("Config document delete successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam Integer id,@RequestBody ConfigWarehouseTableDb configWarehouseTableDb) throws IOException {
        if(!configWarehouseTableDbRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        configTableWarehouseDbService.update(id, configWarehouseTableDb);
        return ResponseEntity.ok("Config document update successfully!");
    }

}

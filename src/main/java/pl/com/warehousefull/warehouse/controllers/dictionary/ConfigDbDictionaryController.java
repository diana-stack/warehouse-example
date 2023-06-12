package pl.com.warehousefull.warehouse.controllers.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigDbDictionaryRepository;
import pl.com.warehousefull.warehouse.services.dictionary.ConfigDbDictionaryService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/db-dictionary")
public class ConfigDbDictionaryController {

    @Autowired
    private ConfigDbDictionaryService configDbDictionaryService;
    @Autowired
    private ConfigDbDictionaryRepository configDbDictionaryRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigDbDictionary>> getAll() {
        Logger.getLogger(ConfigDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigDbDictionary> all = configDbDictionaryService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigDbDictionary> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigDbDictionary> processedEntry = configDbDictionaryService.getById(id);

        return processedEntry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigDbDictionary> create() {
        Logger.getLogger(ConfigDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigDbDictionary> ConfigDbDictionary = configDbDictionaryService.createNewEntry();
        return ConfigDbDictionary.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigDbDictionary configDbDictionary) throws IOException {
        configDbDictionaryService.save(configDbDictionary);
        return ResponseEntity.ok("Config document save successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigDbDictionary processedEntry) throws IOException {
        configDbDictionaryService.delete(processedEntry);
        return ResponseEntity.ok("Dictionary document deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ConfigDbDictionary processedEntry) throws IOException {
        if(!configDbDictionaryRepository.findById(Integer.valueOf(processedEntry.getId())).isPresent()){
            return ResponseEntity.notFound().build();
        }
        configDbDictionaryService.update(processedEntry);
        return ResponseEntity.ok("Agent document update successfully!");
    }
}

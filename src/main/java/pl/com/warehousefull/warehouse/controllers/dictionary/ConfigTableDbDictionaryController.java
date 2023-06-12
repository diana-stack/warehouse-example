package pl.com.warehousefull.warehouse.controllers.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.dictionary.ConfigTableDbDictionary;
import pl.com.warehousefull.warehouse.repositories.config.dictionary.ConfigTableDbDictionaryRepository;
import pl.com.warehousefull.warehouse.services.dictionary.ConfigTableDbDictionaryService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/table-db-dictionary")
public class ConfigTableDbDictionaryController {

    @Autowired
    private ConfigTableDbDictionaryService configTableDbDictionaryService;
    @Autowired
    private ConfigTableDbDictionaryRepository configTableDbDictionaryRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigTableDbDictionary>> getAll() {
        Logger.getLogger(ConfigTableDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigTableDbDictionary> all = configTableDbDictionaryService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigTableDbDictionary> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigTableDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigTableDbDictionary> processedEntry = configTableDbDictionaryService.getById(id);

        return processedEntry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigTableDbDictionary> create() {
        Logger.getLogger(ConfigTableDbDictionaryController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigTableDbDictionary> ConfigTableDbDictionary = configTableDbDictionaryService.createNewEntry();
        return ConfigTableDbDictionary.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigTableDbDictionary configTableDbDictionary) throws IOException {
        configTableDbDictionaryService.save(configTableDbDictionary);
        return ResponseEntity.ok("Config document save successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigTableDbDictionary processedEntry) throws IOException {
        configTableDbDictionaryService.delete(processedEntry);
        return ResponseEntity.ok("Dictionary document deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ConfigTableDbDictionary processedEntry) throws IOException {
        if(!configTableDbDictionaryRepository.findById(Integer.valueOf(processedEntry.getId())).isPresent()){
            return ResponseEntity.notFound().build();
        }
        configTableDbDictionaryService.update(processedEntry);
        return ResponseEntity.ok("Agent document update successfully!");
    }
}

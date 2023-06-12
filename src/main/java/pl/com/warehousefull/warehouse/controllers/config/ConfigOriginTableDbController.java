package pl.com.warehousefull.warehouse.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.parser.domino.DominoDocumentFieldsToSyncMapParser;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginTableDbRepository;
import pl.com.warehousefull.warehouse.services.config.ConfigOriginTableDbService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config/config-origin-table-db")
public class ConfigOriginTableDbController {

    @Autowired
    private ConfigOriginTableDbService configOriginTableDbService;

    @Autowired
    private  ConfigOriginTableDbRepository configOriginTableDbRepository;
    @Autowired
    private DominoDocumentFieldsToSyncMapParser dominoDocumentFieldsToSyncMapParser;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigOriginTableDb>> getAll() {
        Logger.getLogger(ConfigOriginTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigOriginTableDb> all = configOriginTableDbService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigOriginTableDb> getOne(@RequestParam final Integer id) {
        Logger.getLogger(ConfigOriginDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC " + id);
        final Optional<ConfigOriginTableDb> plan = configOriginTableDbService.getById(id);
        return plan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigOriginTableDb configOriginTableDb) throws IOException {
        configOriginTableDbService.save(configOriginTableDb);
        return ResponseEntity.ok("Config document created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigOriginTableDb processedEntry) throws IOException {
        configOriginTableDbService.delete(processedEntry);
        return ResponseEntity.ok("Config document delete successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam Integer id,@RequestBody ConfigOriginTableDb configOriginDbUpdate) throws IOException {
        if(!configOriginTableDbRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        configOriginTableDbService.update(id, configOriginDbUpdate);
        return ResponseEntity.ok("Config document update successfully!");
    }

    @GetMapping("/create")
    public ResponseEntity<String> create() throws IOException {
        configOriginTableDbService.createNewEntry();
        return ResponseEntity.ok("Config document created successfully!");
    }

    @GetMapping("/template/create")
    public ResponseEntity<ConfigOriginTableDb> createNewEntryWithTemplate(@RequestParam Integer dictionaryId) {
        Logger.getLogger(ConfigOriginTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY BY TEMPLATE");
        Optional<ConfigOriginTableDb> configOriginTableDb = configOriginTableDbService.createNewEntryWithTemplate(dictionaryId);
        return configOriginTableDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/template/use")
    public ResponseEntity<ConfigOriginTableDb> setEntryValueByTemplate(@RequestParam Integer dictionaryId, @RequestBody ConfigOriginTableDb configOriginTableDb) {
        Logger.getLogger(ConfigOriginTableDbController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR SET ENTRY VALUES BY TEMPLATE");
        Optional<ConfigOriginTableDb> configOriginTableDbNew = configOriginTableDbService.setEntryValuesByTemplate(dictionaryId, configOriginTableDb);
        return configOriginTableDbNew.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/change")
    public ResponseEntity<String> changeValues(){
        List<ConfigOriginTableDb> configOriginTableDbList = configOriginTableDbRepository.findAll();
        for(ConfigOriginTableDb configOriginTableDb : configOriginTableDbList){
            String fieldsHTML = configOriginTableDb.getFieldsToSync();
            String htmlStringName = "";
            String htmlStringType = "";
            String[] htmlToParseList = fieldsHTML.split("</option>");
            for (String htmlToParseString : htmlToParseList) {
                htmlStringType += dominoDocumentFieldsToSyncMapParser.getFieldType(htmlToParseString) + ",";
                htmlStringName += dominoDocumentFieldsToSyncMapParser.getFieldName(htmlToParseString) + ",";
            }
            configOriginTableDb.setFieldsToSync(htmlStringName.substring(0,htmlStringName.length()-1));
            configOriginTableDb.setFieldsToSyncTypes(htmlStringType.substring(0,htmlStringType.length()-1));
            configOriginTableDbRepository.save(configOriginTableDb);
        }
        return ResponseEntity.ok("Done!");
    }

}

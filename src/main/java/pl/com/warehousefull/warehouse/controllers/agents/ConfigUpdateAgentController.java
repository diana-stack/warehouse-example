package pl.com.warehousefull.warehouse.controllers.agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.agents.ConfigUpdateAgentService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config-update-agent")
public class ConfigUpdateAgentController {

    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;
    @Autowired
    private ConfigUpdateAgentService configUpdateAgentService;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigUpdateAgent>> getAll() {
        Logger.getLogger(ConfigUpdateAgentController.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigUpdateAgent> all = configUpdateAgentService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigUpdateAgent> getOne(@RequestParam String startTime) {
        Logger.getLogger(ConfigUpdateAgentController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC");
        final Optional<ConfigUpdateAgent> processedEntry = configUpdateAgentService.getById(startTime);

        return processedEntry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigUpdateAgent> create() {
        Logger.getLogger(ConfigUpdateAgentController.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigUpdateAgent> configCreateAgent = configUpdateAgentService.createNewEntry();

        return configCreateAgent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigUpdateAgent configUpdateAgent) throws IOException {
        configUpdateAgentService.save(configUpdateAgent);
        return ResponseEntity.ok("Agent document created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigUpdateAgent configUpdateAgent) throws IOException {
        if(!configUpdateAgentDbRepository.findById(configUpdateAgent.getStartTime()).isPresent()){
            return ResponseEntity.notFound().build();
        }

        configUpdateAgentService.delete(configUpdateAgent);
        return ResponseEntity.ok("Agent document deleted successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ConfigUpdateAgent configUpdateAgent) throws IOException {
        if(!configUpdateAgentDbRepository.findById(configUpdateAgent.getStartTime()).isPresent()){
            return ResponseEntity.notFound().build();
        }
        configUpdateAgentService.update(configUpdateAgent);
        return ResponseEntity.ok("Agent document update successfully!");
    }

}

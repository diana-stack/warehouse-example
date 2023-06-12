package pl.com.warehousefull.warehouse.controllers.agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.agents.ConfigCreateAgentService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/config-create-agent")
public class ConfigCreateAgentController {

    @Autowired
    private ConfigCreateAgentService configCreateAgentService;
    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<ConfigCreateAgent>> getAll() {
        Logger.getLogger(ConfigCreateAgentDbRepository.class.getName()).log(Level.INFO, "PROCESSING GET ALL REQUEST");
        final List<ConfigCreateAgent> all = configCreateAgentService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/get/one")
    public ResponseEntity<ConfigCreateAgent> getOne(@RequestParam String startTime) {
        Logger.getLogger(ConfigCreateAgentDbRepository.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR DOC");
        final Optional<ConfigCreateAgent> processedEntry = configCreateAgentService.getById(startTime);

        return processedEntry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/create")
    public ResponseEntity<ConfigCreateAgent> create() {
        Logger.getLogger(ConfigCreateAgentDbRepository.class.getName()).log(Level.INFO, "PROCESSING GET REQUEST FOR CREATE NEW ENTRY");
        Optional<ConfigCreateAgent> configCreateAgent = configCreateAgentService.createNewEntry();

        return configCreateAgent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConfigCreateAgent configCreateAgent) throws IOException {
        configCreateAgentService.save(configCreateAgent);
        return ResponseEntity.ok("Agent document created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody ConfigCreateAgent configCreateAgent) throws IOException {
        if(!configCreateAgentDbRepository.findById(configCreateAgent.getStartTime()).isPresent()){
            return ResponseEntity.notFound().build();
        }

        configCreateAgentService.delete(configCreateAgent);
        return ResponseEntity.ok("Agent document deleted successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ConfigCreateAgent configCreateAgent) throws IOException {
        if(!configCreateAgentDbRepository.findById(configCreateAgent.getStartTime()).isPresent()){
            return ResponseEntity.notFound().build();
        }

        configCreateAgentService.update(configCreateAgent);
        return ResponseEntity.ok("Agent document update successfully!");
    }

}

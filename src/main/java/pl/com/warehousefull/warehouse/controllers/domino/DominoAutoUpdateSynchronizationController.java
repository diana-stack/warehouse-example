package pl.com.warehousefull.warehouse.controllers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.domino.DominoUpdateSynchronizationService;


@RestController
@RequestMapping("/domino")
public class DominoAutoUpdateSynchronizationController {

    @Autowired
    private DominoUpdateSynchronizationService dominoUpdateSynchronizationService;
    @Autowired
    private DateTimeHelper dateTimeHelper;
    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;

    @GetMapping("/auto/update")
    public ResponseEntity<String> autoUpdate() throws Exception {
        ConfigUpdateAgent updateAgentDb = configUpdateAgentDbRepository.findByWarehouseDbName("WH_DOMINO").get();
        for(String configOriginDbId :updateAgentDb.getConfigOriginDbIds().split(",")){
            dominoUpdateSynchronizationService.autoUpdateSync(Integer.valueOf(configOriginDbId));
        }
        return ResponseEntity.ok("Update sync for Domino end successfully");
    }
}

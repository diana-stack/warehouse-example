package pl.com.warehousefull.warehouse.controllers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.domino.DominoCreateSynchronizationService;

@RestController
@RequestMapping("/domino")
public class DominoAutoCreateSynchronizationController {

    @Autowired
    private DominoCreateSynchronizationService dominoCreateSynchronizationService;
    @Autowired
    private DateTimeHelper dateTimeHelper;
    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;

    @GetMapping("/auto/create")
    public ResponseEntity<String> autoCreate() throws Exception {
        ConfigCreateAgent createAgentDb = configCreateAgentDbRepository.findByWarehouseDbName("WH_DOMINO").get();
        for(String configOriginDbId :createAgentDb.getConfigOriginDbIds().split(",")){
            dominoCreateSynchronizationService.autoCreateSync(Integer.valueOf(configOriginDbId));
        }
        return ResponseEntity.ok("Create sync for Domino end successfully");
    }
}

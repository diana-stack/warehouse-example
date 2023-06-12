package pl.com.warehousefull.warehouse.controllers.enova;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.mssql.CreateTableSynchronizationService;
import pl.com.warehousefull.warehouse.services.mssql.UpdateTableSynchronizationService;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/enova")
public class EnovaAutoSynchronizationController {

    private static final String SYNC_TYPE = "ENOVA";
    @Autowired
    private CreateTableSynchronizationService createTableSynchronizationService;
    @Autowired
    private UpdateTableSynchronizationService updateTableSynchronizationService;
    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;
    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;
    @GetMapping("/auto/create")
    public ResponseEntity<String> autoCreate() throws IOException, SQLException, DecoderException {
        ConfigCreateAgent createAgentDb = configCreateAgentDbRepository.findByWarehouseDbName("WH_ENOVA").get();
        if(createAgentDb.getConfigOriginDbIds().contains(",")){
            for(String configOriginDbId : createAgentDb.getConfigOriginDbIds().split(",")){
                createTableSynchronizationService.createSynchronization(Integer.valueOf(configOriginDbId),SYNC_TYPE);
            }
        } else if(!createAgentDb.getConfigOriginDbIds().isEmpty()){
            createTableSynchronizationService.createSynchronization(Integer.valueOf(createAgentDb.getConfigOriginDbIds()),SYNC_TYPE);
        }

        return ResponseEntity.ok("Create sync for Enova end successfully");
    }

    @GetMapping("/auto/update")
    public ResponseEntity<String> autoUpdate() throws IOException, SQLException, DecoderException {
        ConfigUpdateAgent updateAgentDb = configUpdateAgentDbRepository.findByWarehouseDbName("WH_ENOVA").get();
        if(updateAgentDb.getConfigOriginDbIds().contains(",")){
            for(String configOriginDbId : updateAgentDb.getConfigOriginDbIds().split(",")){
                updateTableSynchronizationService.updateSynchronization(Integer.valueOf(configOriginDbId),SYNC_TYPE);
            }
        } else if(!updateAgentDb.getConfigOriginDbIds().isEmpty()){
            updateTableSynchronizationService.updateSynchronization(Integer.valueOf(updateAgentDb.getConfigOriginDbIds()),SYNC_TYPE);
        }

        return ResponseEntity.ok("Update sync for Enova end successfully");
    }
}

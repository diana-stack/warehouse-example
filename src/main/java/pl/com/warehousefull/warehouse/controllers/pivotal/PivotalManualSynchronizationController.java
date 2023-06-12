package pl.com.warehousefull.warehouse.controllers.pivotal;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;
import pl.com.warehousefull.warehouse.services.mssql.CreateTableSynchronizationService;
import pl.com.warehousefull.warehouse.services.mssql.UpdateTableSynchronizationService;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/pivotal")
public class PivotalManualSynchronizationController {

    private static final String SYNC_TYPE = "PIVOTAL";
    @Autowired
    private CreateTableSynchronizationService createTableSynchronizationService;
    @Autowired
    private UpdateTableSynchronizationService updateTableSynchronizationService;
    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;
    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;

    @GetMapping("/create")
    public ResponseEntity<String> create(@RequestParam(name = "id") int configOriginDbId) throws IOException, SQLException, DecoderException {
        createTableSynchronizationService.createSynchronization(configOriginDbId,SYNC_TYPE);
        return ResponseEntity.ok("Create sync for Pivotal end successfully");
    }

    @GetMapping("/update")
    public ResponseEntity<String> update(@RequestParam(name = "id") int configOriginDbId) throws IOException, SQLException, DecoderException {
        updateTableSynchronizationService.updateSynchronization(configOriginDbId,SYNC_TYPE);
        return ResponseEntity.ok("Update sync for Pivotal end successfully");
    }

}

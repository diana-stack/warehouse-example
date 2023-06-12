package pl.com.warehousefull.warehouse.services.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.warehousefull.warehouse.helpers.domino.ConnectionHelper;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.helpers.domino.DominoDataByRestHelper;
import pl.com.warehousefull.warehouse.helpers.domino.SynchronizationHistoryHelper;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginTableDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseTableDbRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DominoUpdateSynchronizationService {

    private static final String HISTORY_TABLE_NAME = "synchronization_update_history";
    private static final String SYNC_TYPE = "update";

    @Autowired
    private DominoDataByRestHelper dominoDataByRestHelper;
    @Autowired
    private  ConfigOriginTableDbRepository configOriginTableDbRepository;
    @Autowired
    private  ConfigWarehouseTableDbRepository configWarehouseTableDbRepository;
    @Autowired
    private  DocumentUpdateSynchronizationService documentUpdateSynchronizationService;
    @Autowired
    private  ConfigOriginDbRepository configOriginDbRepository;
    @Autowired
    private DateTimeHelper dateTimeHelper;
    @Autowired
    private SynchronizationHistoryHelper synchronizationHistoryHelper;
    @Autowired
    private ConfigWarehouseDbRepository configWarehouseDbRepository;
    @Autowired
    private ConnectionHelper connectionHelper;

    public void autoUpdateSync(int configOriginDbId) throws Exception {
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginDbId).get();
        List<ConfigOriginTableDb> allConfigDocsForDB = configOriginTableDbRepository.findAllByConfigOriginDbId(configOriginDbId);
        for (ConfigOriginTableDb configOriginTableDb : allConfigDocsForDB){
            getAllParamsAndStartSync(configOriginTableDb, configOriginDb);
        }
    }

    public void manualUpdateSync(int configOriginTableDbRepositoryId) throws Exception {
        ConfigOriginTableDb configOriginTableDb = configOriginTableDbRepository.findById(configOriginTableDbRepositoryId).get();
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginTableDb.getConfigOriginDbId()).get();
        getAllParamsAndStartSync(configOriginTableDb, configOriginDb);
    }

    private void getAllParamsAndStartSync(ConfigOriginTableDb configOriginTableDb, ConfigOriginDb configOriginDb) throws Exception {
        ConfigWarehouseTableDb configWarehouseTableDb = configWarehouseTableDbRepository.findById(configOriginTableDb.getConfigWarehouseTableDbUnid()).get();
        ConfigWarehouseDb processedDb = configWarehouseDbRepository.findById(configWarehouseTableDb.getConfigWarehouseDbId()).get();

        System.out.println("Start update: " + configOriginTableDb.getEntryType() + "    At: " + LocalDateTime.now());
        String json = dominoDataByRestHelper.getJsonByRest(configOriginDb, configOriginTableDb, configOriginTableDb.getTableType(), SYNC_TYPE, configOriginTableDb.getUpdateViewName(), configWarehouseTableDb);
        verifyJsonAndStartSync(json, configOriginTableDb, configWarehouseTableDb, processedDb);
        System.out.println("End update: " + configOriginTableDb.getEntryType() + "    At: " + LocalDateTime.now());
    }

    private void verifyJsonAndStartSync(String json, ConfigOriginTableDb configOriginTableDb, ConfigWarehouseTableDb configWarehouseTableDb, ConfigWarehouseDb processedDb) throws Exception {
        if(!isJsonEmptyOrNotCorrect(json) && configOriginTableDb.getTableType().equals("response")){
            documentUpdateSynchronizationService.updateResponseDocument(configOriginTableDb, configWarehouseTableDb,json, dateTimeHelper.getActualDateTime(), HISTORY_TABLE_NAME);
        }
        if(!isJsonEmptyOrNotCorrect(json) && configOriginTableDb.getTableType().equals("main")){
            documentUpdateSynchronizationService.updateMainDocument(configOriginTableDb, configWarehouseTableDb, json, dateTimeHelper.getActualDateTime(), HISTORY_TABLE_NAME);
        }
        if(isJsonEmptyOrNotCorrect(json)){
            synchronizationHistoryHelper.addHistoryEntry(HISTORY_TABLE_NAME,connectionHelper.createWarehouseConnection(processedDb),configOriginTableDb,0,0,dateTimeHelper.getActualDateTime(),dateTimeHelper.getActualDateTime());
        }
    }

    private boolean isJsonEmptyOrNotCorrect(String json){
        if(json != null && json.contains("<fieldToCountName><split>")){
            return false;
        } else {
            return true;
        }
    }
}

package pl.com.warehousefull.warehouse.services.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.helpers.domino.DominoDataByRestHelper;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginTableDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseTableDbRepository;

import java.util.List;

@Service
public class DominoCreateSynchronizationService {

    private static final String HISTORY_TABLE_NAME = "synchronization_history";
    private static final String SYNC_TYPE = "create";
    @Autowired
    DominoDataByRestHelper dominoDataByRestHelper;
    @Autowired
    private DateTimeHelper dateTimeHelper;
    @Autowired
    private  ConfigOriginTableDbRepository configOriginTableDbRepository;
    @Autowired
    private  ConfigWarehouseTableDbRepository configWarehouseTableDbRepository;
    @Autowired
    private  DocumentCreateSynchronizationService documentCreateSynchronizationService;
    @Autowired
    private  ConfigOriginDbRepository configOriginDbRepository;

    public void autoCreateSync(int configOriginDbId) throws Exception {
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginDbId).get();
        List<ConfigOriginTableDb> allConfigDocsForDB = configOriginTableDbRepository.findAllByConfigOriginDbId(configOriginDbId);
        for (ConfigOriginTableDb configOriginTableDb : allConfigDocsForDB){
            startCreateSync(configOriginTableDb, configOriginDb);
        }
    }

    public void manualCreateSync(int configOriginTableDbId) throws Exception {
        ConfigOriginTableDb configOriginTableDb = configOriginTableDbRepository.findById(configOriginTableDbId).get();
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginTableDb.getConfigOriginDbId()).get();
        startCreateSync(configOriginTableDb, configOriginDb);
    }

    private void startCreateSync(ConfigOriginTableDb configOriginTableDb, ConfigOriginDb configOriginDb) throws Exception {
        System.out.println("Start create: " + configOriginTableDb.getEntryType() + dateTimeHelper.getActualDateTime());
        ConfigWarehouseTableDb configWarehouseTableDb = configWarehouseTableDbRepository.findById(configOriginTableDb.getConfigWarehouseTableDbUnid()).get();
        String json = dominoDataByRestHelper.getJsonByRest(configOriginDb, configOriginTableDb, configOriginTableDb.getTableType(), SYNC_TYPE, configOriginTableDb.getCreateViewName(), configWarehouseTableDb);
        if(json != null){
            documentCreateSynchronizationService.createDocument(configOriginTableDb, configWarehouseTableDb, json, dateTimeHelper.getActualDateTime(), HISTORY_TABLE_NAME);
        }
        System.out.println("End create for: " + configOriginTableDb.getEntryType() + "    At: " + dateTimeHelper.getActualDateTime());
    }

}

package pl.com.warehousefull.warehouse.services.mssql;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLSynchronizationHistoryHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLConnectionHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLUpdateEntryHelper;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;

import java.io.IOException;
import java.sql.*;

@Service
public class UpdateTableSynchronizationService {
    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;
    @Autowired
    private ConfigWarehouseDbRepository configWarehouseDbRepository;
    @Autowired
    private MsSQLSynchronizationHistoryHelper synchronizationHistoryEnovaHelper;
    @Autowired
    private MsSQLConnectionHelper msSQLConnectionHelper;
    @Autowired
    private MsSQLUpdateEntryHelper msSQLUpdateEntryHelper;
    @Autowired
    private DateTimeHelper dateTimeHelper;

    public void updateSynchronization(int configOriginDbId,String syncType) throws SQLException, IOException, DecoderException {
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginDbId).get();
        ConfigWarehouseDb configWarehouseDb = configWarehouseDbRepository.findById(configOriginDb.getConfigWarehouseDbId()).get();

        try (Connection sourceConnection = msSQLConnectionHelper.createOriginConnection(configOriginDb);
             Connection targetConnection = msSQLConnectionHelper.createWarehouseConnection(configWarehouseDb)) {
            if(syncType.equals("PIVOTAL")){
                startSync("pivotal_synchronization_history",sourceConnection, targetConnection, configOriginDb);
            } else if(syncType.equals("ENOVA")){
                startSync("enova_synchronization_history",sourceConnection,targetConnection,configOriginDb);
            }
        }
    }
    private void startSync(String historyTableName, Connection sourceConnection, Connection targetConnection, ConfigOriginDb configOriginDb) throws DecoderException, SQLException, IOException {

        if(configOriginDb.getTablesToSync().contains(",")){
            String[] firstPartTab = configOriginDb.getTablesToSync().split(",");
            for(String tableName : firstPartTab) {
                runsSynchronization(historyTableName,tableName,sourceConnection,targetConnection);
            }
        } else {
            String tableName = configOriginDb.getTablesToSync();
            runsSynchronization(historyTableName,tableName,sourceConnection,targetConnection);
        }

    }

    private void runsSynchronization(String historyTableName, String tableName, Connection sourceConnection, Connection targetConnection) throws DecoderException, SQLException, IOException {
        String startDateTime = dateTimeHelper.getActualDateTime();
        System.out.println("Copying data for table " + tableName);
        msSQLUpdateEntryHelper.updateSynchronization(sourceConnection,targetConnection,tableName);
        int entrySynced = synchronizationHistoryEnovaHelper.getCountToSync(tableName, targetConnection.createStatement());
        synchronizationHistoryEnovaHelper.addHistoryEntry(historyTableName, targetConnection.createStatement(), startDateTime, dateTimeHelper.getActualDateTime(), tableName, entrySynced,sourceConnection.createStatement());

    }

}

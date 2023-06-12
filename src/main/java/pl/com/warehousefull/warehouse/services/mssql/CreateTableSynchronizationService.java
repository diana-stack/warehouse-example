package pl.com.warehousefull.warehouse.services.mssql;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLSynchronizationHistoryHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLConnectionHelper;
import pl.com.warehousefull.warehouse.helpers.mssql.MsSQLCreateEntryHelper;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;

import java.io.IOException;
import java.sql.*;

@Service
public class CreateTableSynchronizationService {
    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;
    @Autowired
    private ConfigWarehouseDbRepository configWarehouseDbRepository;
    @Autowired
    private MsSQLSynchronizationHistoryHelper synchronizationHistoryEnovaHelper;
    @Autowired
    private MsSQLConnectionHelper msSQLConnectionHelper;
    @Autowired
    private MsSQLCreateEntryHelper msSQLCreateEntryHelper;
    @Autowired
    private DateTimeHelper dateTimeHelper;

    public void createSynchronization(int configOriginDbId,String syncType) throws SQLException, IOException, DecoderException {
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(configOriginDbId).get();
        ConfigWarehouseDb configWarehouseDb = configWarehouseDbRepository.findById(configOriginDb.getConfigWarehouseDbId()).get();

        try (Connection sourceConnection = msSQLConnectionHelper.createOriginConnection(configOriginDb);
             Connection targetConnection = msSQLConnectionHelper.createWarehouseConnection(configWarehouseDb)) {
            if(syncType.equals("PIVOTAL")){
                startSyncPivotal(sourceConnection, targetConnection);
            } else if(syncType.equals("ENOVA")){
                startSyncEnova(sourceConnection,targetConnection,configOriginDb);
            }
        }
    }

    private void startSyncPivotal(Connection sourceConnection, Connection targetConnection) throws SQLException, DecoderException, IOException {
        DatabaseMetaData sourceMetaData = sourceConnection.getMetaData();
        ResultSet sourceTables = sourceMetaData.getTables(null, null, null, new String[]{"TABLE"});
        while (sourceTables.next()) {
            String tableName = sourceTables.getString("TABLE_NAME");
            runsSynchronization("pivotal_synchronization_history",tableName,targetConnection,targetConnection);

        }
    }

    private void startSyncEnova(Connection sourceConnection, Connection targetConnection, ConfigOriginDb configOriginDb) throws DecoderException, SQLException, IOException {
        if(configOriginDb.getTablesToSync().contains(",")){
            String[] firstPartTab = configOriginDb.getTablesToSync().split(",");
            for(String tableName : firstPartTab) {
                runsSynchronization("enova_synchronization_history",tableName,targetConnection,targetConnection);
            }
        } else {
            String tableName = configOriginDb.getTablesToSync();
            runsSynchronization("enova_synchronization_history",tableName,targetConnection,targetConnection);
        }

    }

    private void runsSynchronization(String historyTableName, String tableName, Connection sourceConnection, Connection targetConnection) throws DecoderException, SQLException, IOException {
        String startDateTime = dateTimeHelper.getActualDateTime();
        System.out.println("Copying data for table " + tableName);
        msSQLCreateEntryHelper.createSynchronization(sourceConnection, targetConnection, tableName);
        int entrySynced = synchronizationHistoryEnovaHelper.getCountToSync(tableName, targetConnection.createStatement());
        synchronizationHistoryEnovaHelper.addHistoryEntry(historyTableName, targetConnection.createStatement(), startDateTime, dateTimeHelper.getActualDateTime(), tableName, entrySynced,sourceConnection.createStatement());

    }
}

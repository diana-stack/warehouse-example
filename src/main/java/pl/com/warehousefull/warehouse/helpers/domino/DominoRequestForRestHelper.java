
package pl.com.warehousefull.warehouse.helpers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.parser.domino.DominoDocumentFieldsToSyncMapParser;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginTableDbRepository;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class DominoRequestForRestHelper {

    @Autowired
    private DominoDocumentFieldsToSyncMapParser fieldsToSyncMapParser;

    @Autowired
    private ConfigOriginTableDbRepository configOriginTableDbRepository;

    @Autowired
    private  ConfigWarehouseDbRepository configWarehouseDbRepository;

    @Autowired
    ConnectionHelper connectionHelper;

    public String createBodyForRequest(ConfigWarehouseTableDb warehouseProcessedTableEntry, ConfigOriginTableDb configOriginTableDb, String syncType, String viewName) throws SQLException {
        StringBuilder bodyBuilder = new StringBuilder();
        String originFieldsToSyncList = fieldsToSyncMapParser.getFieldsToParseString(configOriginTableDb.getFieldsToSync());
        String lastSyncDate = "";
        if (syncType.equals("update")){
            lastSyncDate = getLastUpdateDateTime(configOriginTableDb, warehouseProcessedTableEntry);
        }
        if(configOriginTableDb.getTableType().equals("response")) {
            bodyBuilder.append(configOriginTableDb.getEntryType() + ";"
                    + viewName + ";"
                    + configOriginTableDb.getTableName() + ";"
                    + configOriginTableDb.getFieldToCount() + ";"
                    + originFieldsToSyncList + ";"
                    + lastSyncDate + ";"
                    + syncType);
        } else if(configOriginTableDb.getTableType().equals("main")){
            bodyBuilder.append(configOriginTableDb.getEntryType() + ";"
                    + viewName + ";"
                    + configOriginTableDb.getTableName() + ";"
                    + originFieldsToSyncList + ";"
                    + lastSyncDate + ";"
                    + syncType);
        }
        return bodyBuilder.toString();
    }

    private String getLastUpdateDateTime(ConfigOriginTableDb configOriginTableDb,  ConfigWarehouseTableDb warehouseProcessedTableEntry) throws SQLException {
        ConfigWarehouseDb processedDb = configWarehouseDbRepository.findById(warehouseProcessedTableEntry.getConfigWarehouseDbId()).get();
        Statement stmt = connectionHelper.createWarehouseConnection(processedDb);
        String SQL = "USE WH_DOMINO \n" + "SELECT * FROM synchronization_history\n WHERE table_name like '" + configOriginTableDb.getEntryType() + "'";
        ResultSet rs = stmt.executeQuery(SQL);
        if(rs.next()){
            return rs.getString("synchronization_end");
        } else {
            return null;
        }
    }
}

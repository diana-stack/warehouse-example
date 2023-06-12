package pl.com.warehousefull.warehouse.services.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.warehousefull.warehouse.helpers.domino.ConnectionHelper;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.helpers.domino.SynchronizationHistoryHelper;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;
import pl.com.warehousefull.warehouse.parser.domino.DominoDocumentFieldsToSyncSQLParser;
import pl.com.warehousefull.warehouse.parser.domino.JsonParser;
import pl.com.warehousefull.warehouse.repositories.config.ConfigWarehouseDbRepository;

import java.sql.Statement;
import java.util.Map;

@Service
public class DocumentCreateSynchronizationService {
    @Autowired
    ConnectionHelper connectionHelper;
    @Autowired
    private JsonParser jsonParser;

    @Autowired
    private DateTimeHelper dateTimeHelper;

    @Autowired
    private final ConfigWarehouseDbRepository configWarehouseDbRepository;

    @Autowired
    private DominoDocumentFieldsToSyncSQLParser fieldsToSyncSQLParser;

    @Autowired
    private SynchronizationHistoryHelper synchronizationHistoryHelper;

    public DocumentCreateSynchronizationService(ConfigWarehouseDbRepository configWarehouseDbRepository) {
        this.configWarehouseDbRepository = configWarehouseDbRepository;
    }

    public void createDocument(ConfigOriginTableDb originProcessedTableEntry, ConfigWarehouseTableDb warehouseProcessedTableEntry, String json, String startSync, String historyTableName) throws Exception {
        String deleteSQL = "USE WH_DOMINO \n" + "DELETE FROM " + warehouseProcessedTableEntry.getTableName();
        String SQL = "USE WH_DOMINO \n" + "INSERT INTO " + warehouseProcessedTableEntry.getTableName();

        ConfigWarehouseDb processedDb = configWarehouseDbRepository.findById(1).get();
        Statement stmt = connectionHelper.createWarehouseConnection(processedDb);
        stmt.executeUpdate(deleteSQL);
        String[] jsonList = jsonParser.splitJson(json);
        int i = 0;
        for (String jsonForOne :jsonList) {
            Map<String,String> jsonMap = jsonParser.getMapFromJsonByGSON(jsonForOne);
            String sql = fieldsToSyncSQLParser.getCreateStringSQL(originProcessedTableEntry, warehouseProcessedTableEntry, jsonMap);
           try{
                stmt.executeUpdate(SQL + sql);
               i++;
            } catch (Exception e){
               System.out.println(jsonForOne);
                System.out.println(SQL + sql);
            }
        }
        synchronizationHistoryHelper.addHistoryEntry(historyTableName, stmt, originProcessedTableEntry, jsonList.length, i, startSync, dateTimeHelper.getActualDateTime());
        stmt.close();
    }
}

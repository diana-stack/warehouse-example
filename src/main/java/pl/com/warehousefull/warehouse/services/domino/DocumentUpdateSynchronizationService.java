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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentUpdateSynchronizationService {

    @Autowired
    ConnectionHelper connectionHelper;
    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private  ConfigWarehouseDbRepository configWarehouseDbRepository;
    @Autowired
    private DominoDocumentFieldsToSyncSQLParser fieldsToSyncSQLParser;
    @Autowired
    private SynchronizationHistoryHelper synchronizationHistoryHelper;
    @Autowired
    private DateTimeHelper dateTimeHelper;


    public void updateMainDocument(ConfigOriginTableDb originProcessedTableEntry, ConfigWarehouseTableDb warehouseProcessedTableEntry, String json, String startSync, String historyTableName) throws Exception {
            String SQL = "";
            String sql = "";
            ConfigWarehouseDb processedDb = configWarehouseDbRepository.findById(warehouseProcessedTableEntry.getConfigWarehouseDbId()).get();
            Statement stmt = connectionHelper.createWarehouseConnection(processedDb);
            String[] jsonList = jsonParser.splitJson(json);
            int i = 0;
            for (String jsonForOne :jsonList) {
                Map<String,String> jsonMap = jsonParser.getMapFromJsonByGSON(jsonForOne);
                if (isDocumentPresent(jsonForOne,jsonMap, stmt, warehouseProcessedTableEntry)){
                    SQL = "USE WH_DOMINO \n" + "UPDATE " + warehouseProcessedTableEntry.getTableName() + "\n";
                    sql = fieldsToSyncSQLParser.getUpdateStringSQL(originProcessedTableEntry, warehouseProcessedTableEntry, jsonMap);
                   try{
                       stmt.executeUpdate(SQL + sql);
                   } catch (Exception e) {
                       System.out.println(jsonForOne);
                       System.out.println(SQL + sql);
                   }
                } else {
                    SQL = "USE WH_DOMINO \n" + "INSERT INTO " + warehouseProcessedTableEntry.getTableName();
                    sql = fieldsToSyncSQLParser.getCreateStringSQL(originProcessedTableEntry, warehouseProcessedTableEntry, jsonMap);
                    try{
                        stmt.executeUpdate(SQL + sql);
                    } catch (Exception e) {
                        System.out.println(jsonForOne);
                        System.out.println(SQL + sql);
                    }
                }
                i++;
            }
            synchronizationHistoryHelper.addHistoryEntry(historyTableName,stmt,originProcessedTableEntry, jsonList.length, i, startSync,dateTimeHelper.getActualDateTime());
            stmt.close();
    }

    public void updateResponseDocument(ConfigOriginTableDb originProcessedTableEntry, ConfigWarehouseTableDb warehouseProcessedTableEntry, String json, String startSync, String historyTableName) throws Exception {
        String SQL = "USE WH_DOMINO \n" + "INSERT INTO " + warehouseProcessedTableEntry.getTableName();
        String sql = "";
        ConfigWarehouseDb processedDb = configWarehouseDbRepository.findById(warehouseProcessedTableEntry.getConfigWarehouseDbId()).get();
        Statement stmt = connectionHelper.createWarehouseConnection(processedDb);
        Connection con = connectionHelper.createWarehouseConnectionTest(processedDb);
        deleteResponseEntry(con,warehouseProcessedTableEntry, json);
            String[] jsonList = jsonParser.splitResponseJson(json);
            int i = 0;
            for (String jsonForOne :jsonList) {
                Map<String,String> jsonMap = jsonParser.getMapFromJsonByGSON(jsonForOne);
                sql = fieldsToSyncSQLParser.getCreateStringSQL(originProcessedTableEntry, warehouseProcessedTableEntry, jsonMap);
                stmt.executeUpdate(SQL + sql);
                i++;
            }
        synchronizationHistoryHelper.addHistoryEntry(historyTableName,stmt,originProcessedTableEntry, jsonList.length, i, startSync, dateTimeHelper.getActualDateTime());
        stmt.close();
    }

    public void deleteResponseEntry(Connection connection, ConfigWarehouseTableDb warehouseProcessedTableEntry, String json) throws SQLException {
        String[] ids = jsonParser.getListToClean(json);
        if(ids != null){
            List<String> smallIdLists = getPartialToDeleteLists(ids);
            PreparedStatement statement = null;
            for (String smallIdList : smallIdLists) {
                if(smallIdList.charAt(smallIdList.length()-1) == ','){
                    smallIdList = smallIdList.substring(0,smallIdList.length()-1);
                }
                String sql = "USE WH_DOMINO \nDELETE FROM " +  warehouseProcessedTableEntry.getTableName() + " WHERE " + warehouseProcessedTableEntry.getParentUnidField() + " IN (" + smallIdList + ")" ;
                statement = connection.prepareStatement(sql);
                try{
                    statement.executeUpdate();
                } catch (Exception e){
                    System.out.println("Delete error in: " + warehouseProcessedTableEntry.getTableName());
                }
            }
            statement.close();
        }
    }

    private List<String> getPartialToDeleteLists(String[] ids ) {
        List<String> smallIdLists = new ArrayList<>();
        String currentIdList = "";
        for (int i = 0; i < ids.length; i++) {
            currentIdList += ids[i];
            if (i < ids.length - 1) {
                currentIdList += ",";
            }
            if (currentIdList.length() > 20000) {
                smallIdLists.add(currentIdList);
                currentIdList = "";
            }
        }
        if (!currentIdList.isEmpty()) {
            smallIdLists.add(currentIdList);
        }
        return smallIdLists;
    }

    private boolean isDocumentPresent(String jsonForOne, Map<String,String> jsonMap, Statement stmt, ConfigWarehouseTableDb warehouseProcessedTableEntry) throws SQLException {
        String SQL = "USE WH_DOMINO \n" + "SELECT * FROM " + warehouseProcessedTableEntry.getTableName() + "\n WHERE unid = '" + jsonMap.get("unid") + "'";
        ResultSet rs = stmt.executeQuery(SQL);
        if(rs.next()){
            return true;
        } else {
            return false;
        }
    }
}

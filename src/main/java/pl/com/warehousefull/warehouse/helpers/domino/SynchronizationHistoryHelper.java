package pl.com.warehousefull.warehouse.helpers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;

import java.sql.*;


@Component
public class SynchronizationHistoryHelper {
    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;

    public void addHistoryEntry(String historyTableName, Statement stmt, ConfigOriginTableDb originProcessedTableEntry, int docsToSyncCount, int docsSyncedCount, String startDateTime, String endDateTime) throws SQLException {
        ConfigOriginDb configOriginDb = configOriginDbRepository.findById(originProcessedTableEntry.getConfigOriginDbId()).get();
        if(!isEntryPresent(stmt,originProcessedTableEntry,historyTableName)){
            String SQL = "USE WH_DOMINO \n" + "INSERT INTO " + historyTableName + "\n"
                    + "(table_name,database_name,synchronization_start,synchronization_end,docs_to_synchronize,docs_synchronized)"
                    + "VALUES('" + originProcessedTableEntry.getEntryType() + "','" + configOriginDb.getDbName() + "','" + startDateTime + "','" + endDateTime + "'," + docsToSyncCount + "," + docsSyncedCount + ")";
           try{
               stmt.execute(SQL);
           } catch(Exception e){
               System.out.println(SQL);
           }
        } else {
            String SQL = "USE WH_DOMINO \n" + "UPDATE " + historyTableName + "\n"
                    + "SET synchronization_start = '" + startDateTime + "', synchronization_end = '" + endDateTime + "', docs_to_synchronize = " + docsToSyncCount + ", docs_synchronized = " + docsSyncedCount
                    + "\nWHERE table_name like '" + originProcessedTableEntry.getEntryType() + "'";
            try{
                stmt.execute(SQL);
            } catch(Exception e){
                System.out.println(SQL);
            }
        }
    }
    public boolean isEntryPresent(Statement stmt, ConfigOriginTableDb originProcessedTableEntry, String historyTableName) throws SQLException {
        String SQL = "USE WH_DOMINO \n" + "SELECT * FROM " + historyTableName + "\n WHERE table_name like '" + originProcessedTableEntry.getEntryType() + "'";
        ResultSet rs = stmt.executeQuery(SQL);
        if(rs.next()){
            return true;
        } else {
            return false;
        }
    }
}

package pl.com.warehousefull.warehouse.helpers.mssql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.repositories.config.ConfigOriginDbRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class MsSQLSynchronizationHistoryHelper {
    @Autowired
    private ConfigOriginDbRepository configOriginDbRepository;

    public void addHistoryEntry(String historyTableName, Statement targetStmt, String startDateTime, String endDateTime, String tableToSync, int entrySyncedCount,Statement sourceStmt) throws SQLException {
        if(!isEntryPresent(targetStmt,tableToSync,historyTableName)){
            String SQL = "INSERT INTO " + historyTableName + "\n"
                    + "(table_name,startDateTime,endDateTime,entryToSync,entrySynced)"
                    + "VALUES('" + tableToSync + "','"
                    + startDateTime + "','"
                    + endDateTime + "',"
                    + getCountToSync(tableToSync,sourceStmt) + ","
                    + entrySyncedCount + ")";
           try{
               targetStmt.execute(SQL);
           } catch(Exception e){
               System.out.println(SQL);
           }
        } else {
            String SQL =  "UPDATE " + historyTableName + "\n"
                    + "SET startDateTime = '" + startDateTime + "', endDateTime = '" + endDateTime + "', entryToSync = " + getCountToSync(tableToSync,sourceStmt) + ", entrySynced = " + entrySyncedCount
                    + "\nWHERE table_name like '" + tableToSync + "'";
            try{
                targetStmt.execute(SQL);
            } catch(Exception e){
                System.out.println(SQL);
            }
        }
    }
    public boolean isEntryPresent(Statement stmt, String tableToSync, String historyTableName) throws SQLException {
        String SQL =  "SELECT * FROM " + historyTableName + "\n WHERE table_name like '" + tableToSync + "'";
        try{
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println(SQL);
            return false;
        }

    }

    public int getCountToSync(String tableName, Statement stmt) throws SQLException {
        String SQL =  "SELECT COUNT(*) AS count FROM " + tableName;
        ResultSet rs = stmt.executeQuery(SQL);
        if(rs.next()){
            return rs.getInt("count");
        } else {
            return 0;
        }
    }
}


package pl.com.warehousefull.warehouse.helpers.mssql;

import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;


@Component
public class MsSQLCreateEntryHelper {
    public void createSynchronization(Connection sourceConn, Connection targetConn, String tableName) throws SQLException, DecoderException, IOException {
        Statement sourceStmt = sourceConn.createStatement();
        ResultSet sourceData = sourceStmt.executeQuery("SELECT * FROM " + tableName);
        Statement targetStmt = targetConn.createStatement();

        targetStmt.executeUpdate("DELETE FROM " + tableName);

        ResultSetMetaData metaData = sourceData.getMetaData();
        int columnCount = metaData.getColumnCount();

        PreparedStatement targetStmtInsert = getInsertStatement(targetConn,columnCount,tableName,metaData);

        int batchSize = 1000;
        int count = 0;
        while (sourceData.next()){
            createInsertStatement(targetStmtInsert,sourceData,columnCount,metaData);
            targetStmtInsert.addBatch();

            if(++count % batchSize == 0){
                targetStmtInsert.executeBatch();
            }
        }
        targetStmtInsert.executeBatch();

    }

    public PreparedStatement getInsertStatement(Connection targetConn, int columnCount, String tableName, ResultSetMetaData metaData) throws SQLException {
        StringBuilder insertSQLBuilder = new StringBuilder();
        insertSQLBuilder.append("INSERT INTO " + tableName + " (");
        createInsertSQL(insertSQLBuilder, columnCount, metaData);
        createValuesSQL(insertSQLBuilder, columnCount,metaData);
        return targetConn.prepareStatement(insertSQLBuilder.toString());
    }

    private void createInsertSQL(StringBuilder insertSQLBuilder, int columnCount, ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            if(metaData.getColumnTypeName(i).equals("timestamp")){
                continue;
            } else {
                insertSQLBuilder.append("\"" + columnName + "\"");
                insertSQLBuilder.append(",");
            }
        }
        if (insertSQLBuilder.charAt(insertSQLBuilder.length() - 1) == ',') {
            insertSQLBuilder.setLength(insertSQLBuilder.length() - 1);
        }
    }

    private void createValuesSQL(StringBuilder insertSQLBuilder, int columnCount, ResultSetMetaData metaData) throws SQLException {
        insertSQLBuilder.append(") VALUES (");
        for (int i = 1; i <= columnCount; i++) {
            if(metaData.getColumnTypeName(i).equals("timestamp")){
                continue;
            } else {
                insertSQLBuilder.append("?");
            }
            if (i < columnCount && !metaData.getColumnTypeName(i).equals("timestamp")) {
                insertSQLBuilder.append(",");
            }
        }
        if (insertSQLBuilder.charAt(insertSQLBuilder.length() - 1) == ',') {
            insertSQLBuilder.setLength(insertSQLBuilder.length() - 1);
        }
        insertSQLBuilder.append(")");
    }

    public void createInsertStatement(PreparedStatement targetStmtInsert, ResultSet sourceData, int columnCount, ResultSetMetaData metaData) throws SQLException {
        int j = 1;
        for (int i = 1; i <= columnCount; i++) {
            int columnType = metaData.getColumnType(i);
            Object value = sourceData.getObject(i);
            if(metaData.getColumnTypeName(i).equals("timestamp")){
                continue;
            } else {
                createObjects(value,columnType,targetStmtInsert,sourceData,i,j);
                j++;
            }
        }
//        targetStmtInsert.executeUpdate();
    }

    private void createObjects(Object value, int columnType,PreparedStatement targetStmtInsert,ResultSet sourceData,int i, int j) throws SQLException {
        if (value == null && columnType != Types.LONGVARBINARY) {
            targetStmtInsert.setNull(j, columnType);
        } else if (columnType == Types.LONGVARBINARY) {
            if (value != null) {
                byte[] byteValue = sourceData.getBytes(i);
                targetStmtInsert.setObject(j, byteValue);
            } else {
                targetStmtInsert.setNull(j, columnType);
            }
        } else if (value.toString().isEmpty()) {
            targetStmtInsert.setNull(j, columnType);
        } else {
            targetStmtInsert.setObject(j, value);
        }
    }

}


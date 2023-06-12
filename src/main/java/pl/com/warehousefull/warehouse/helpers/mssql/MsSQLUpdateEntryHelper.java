
package pl.com.warehousefull.warehouse.helpers.mssql;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;


@Component
public class MsSQLUpdateEntryHelper {

    @Autowired
    private MsSQLCreateEntryHelper msSQLCreateEntryHelper;

    public void updateSynchronization(Connection sourceConn, Connection targetConn, String tableName) throws SQLException, DecoderException, IOException {
        Statement sourceStmt = sourceConn.createStatement();
        ResultSet sourceData = sourceStmt.executeQuery("SELECT * FROM " + tableName);

        ResultSetMetaData metaData = sourceData.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] primaryKeyColumnNames = getPrimaryKeys(tableName,targetConn);

        StringBuilder updateSQLBuilder = createUpdateSQL(columnCount,metaData,tableName,primaryKeyColumnNames);
        PreparedStatement targetStmtUpdate = targetConn.prepareStatement(updateSQLBuilder.toString());
        PreparedStatement targetStmtInsert = msSQLCreateEntryHelper.getInsertStatement(targetConn,columnCount,tableName,metaData);
        int batchSize = 1000;
        int updateCount = 0;
        int insertCount = 0;
        while (sourceData.next()){
            if(isDocumentExists(primaryKeyColumnNames,targetConn,tableName,sourceData)){
//                System.out.println("Document exist");
                runUpdateStatement(targetStmtUpdate,columnCount,tableName,metaData,sourceData,primaryKeyColumnNames);
                targetStmtUpdate.addBatch();
                if(++updateCount % batchSize == 0){
                    targetStmtUpdate.executeBatch();
                }
            } else {
//                System.out.println("Document not exist");
                msSQLCreateEntryHelper.createInsertStatement(targetStmtInsert,sourceData,columnCount,metaData);
                targetStmtInsert.addBatch();
                if(++insertCount % batchSize == 0){
                    targetStmtInsert.executeBatch();
                }
            }
        }
        targetStmtUpdate.executeBatch();
        targetStmtInsert.executeBatch();
    }

    public void runUpdateStatement(PreparedStatement targetStmtUpdate, int columnCount, String tableName, ResultSetMetaData metaData,ResultSet sourceData,String[] primaryKeyColumnNames) throws SQLException {

        int j = 1;
        for (int i = 1; i <= columnCount; i++) {
            int columnType = metaData.getColumnType(i);
            Object value = sourceData.getObject(i);
            if(metaData.getColumnTypeName(i).equals("timestamp")){
                continue;
            } else {
                createUpdateStatement(value,columnType,targetStmtUpdate,sourceData,i,j);
                j++;
            }
            if (primaryKeyColumnNames != null) {
                setPKValues(j,primaryKeyColumnNames,sourceData,targetStmtUpdate);
            }
        }
    }

    private StringBuilder createUpdateSQL(int columnCount, ResultSetMetaData metaData, String tableName,String[] primaryKeyColumnNames) throws SQLException {
        StringBuilder updateSQLBuilder = new StringBuilder();
        updateSQLBuilder.append("UPDATE " + tableName + " SET ");
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            int columnType = metaData.getColumnType(i);
            if(metaData.getColumnTypeName(i).equals("timestamp")){
                continue;
            } else {
                updateSQLBuilder.append("\"" + columnName + "\"" + " = ?\n");
                updateSQLBuilder.append(",");
            }
        }
        if (updateSQLBuilder.charAt(updateSQLBuilder.length() - 1) == ',') {
            updateSQLBuilder.setLength(updateSQLBuilder.length() - 1);
        }

        if (primaryKeyColumnNames != null) {
            setPKNames(tableName,updateSQLBuilder,primaryKeyColumnNames);
        }

        return updateSQLBuilder;
    }

    private void createUpdateStatement(Object value, int columnType,PreparedStatement targetStmtInsert,ResultSet sourceData,int i,int j) throws SQLException {
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


    public String[] getPrimaryKeys(String tableName, Connection sourceConnection) throws SQLException {
        StringBuilder primaryKeysBuilder = new StringBuilder();
        DatabaseMetaData metaData = sourceConnection.getMetaData();
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        while(primaryKeys.next()){
            primaryKeysBuilder.append(primaryKeys.getString("COLUMN_NAME") + ",");
        }
        if(primaryKeysBuilder.toString().isEmpty()){
            return null;
        } else {
            if (primaryKeysBuilder.charAt(primaryKeysBuilder.length() - 1) == ',') {
                primaryKeysBuilder.setLength(primaryKeysBuilder.length() - 1);
            }
            return primaryKeysBuilder.toString().split(",");
        }
    }

    public boolean isDocumentExists(String[] primaryKeyColumnNames,Connection targetConnection, String tableName, ResultSet sourceData) throws SQLException {
        if(primaryKeyColumnNames != null){
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM ");
            queryBuilder.append(tableName);

            setPKNames(tableName,queryBuilder,primaryKeyColumnNames);
            PreparedStatement statement = targetConnection.prepareStatement(queryBuilder.toString());
            int i = 1;
            setPKValues(i,primaryKeyColumnNames,sourceData,statement);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            } else {
                return false;
            }
        } else {
            PreparedStatement targetPreparedStatement = targetConnection.prepareStatement("DELETE FROM " + tableName);
            targetPreparedStatement.executeUpdate();
            return false;
        }
    }

    private void setPKNames(String tableName, StringBuilder queryBuilder, String[] primaryKeyColumnNames){
        queryBuilder.append(" ").append("WHERE ");
        int i = 0;
        for (String primaryKey : primaryKeyColumnNames) {
            queryBuilder.append(primaryKey).append(" = ?");
            if (i < primaryKeyColumnNames.length - 1) {
                queryBuilder.append(" AND ");
            }
            i++;
        }
    }

    private void setPKValues(int i, String[] primaryKeyColumnNames, ResultSet sourceData, PreparedStatement statement) throws SQLException {
        for (String primaryKey : primaryKeyColumnNames) {
            Object primaryKeyValue = sourceData.getObject(primaryKey);
            if (primaryKeyValue != null && primaryKeyValue.getClass().isArray() && primaryKeyValue.getClass().getComponentType() == Byte.TYPE) {
                byte[] byteValue = (byte[]) primaryKeyValue;
                statement.setBytes(i, byteValue);
            } else {
                statement.setObject(i, primaryKeyValue);
            }
            i++;
        }
    }
}


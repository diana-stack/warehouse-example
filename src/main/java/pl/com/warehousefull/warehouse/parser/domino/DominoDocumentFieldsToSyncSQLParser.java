
package pl.com.warehousefull.warehouse.parser.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;

import java.util.*;

@Component
public class DominoDocumentFieldsToSyncSQLParser {

    @Autowired
    private DateParser dateParser;
    @Autowired
    private DominoDocumentFieldsToSyncMapParser dominoDocumentFieldsToSyncMapParser;


    public String getCreateStringSQL(ConfigOriginTableDb originProcessedTableEntry, ConfigWarehouseTableDb warehouseProcessedTableEntry, Map<String,String> jsonMap) throws Exception {
        String tableType = originProcessedTableEntry.getTableType();

        Map<String,String> originFieldsToSyncMap = getOriginFieldsToSyncMap(originProcessedTableEntry);

        List<String> originFieldsToSyncList = Arrays.asList(originProcessedTableEntry.getFieldsToSync().split(","));
        String[] warehouseFieldsToSyncList = warehouseProcessedTableEntry.getFieldsToSync().split(",");
        List<String> originValueList = getListOriginValueFromMap(jsonMap, originFieldsToSyncMap, originFieldsToSyncList, tableType);

        String firstPartSQL = getSQLNamesOfFieldsToCreate(warehouseFieldsToSyncList);
        String secondPartSQL = getSQLValuesToCreate(originValueList);
        String SQL = firstPartSQL + "\n" + secondPartSQL;

        return SQL;
    }

    public String getUpdateStringSQL(ConfigOriginTableDb originProcessedTableEntry, ConfigWarehouseTableDb warehouseProcessedTableEntry, Map<String,String> jsonMap) throws Exception {
        String tableType = originProcessedTableEntry.getTableType();

        Map<String,String> originFieldsToSyncMap = getOriginFieldsToSyncMap(originProcessedTableEntry);
        List<String> originFieldsToSyncList = Arrays.asList(originProcessedTableEntry.getFieldsToSync().split(","));
        List<String> originValueList = getListOriginValueFromMap(jsonMap, originFieldsToSyncMap,  originFieldsToSyncList, tableType);
        String[] warehouseFieldsToSyncList = warehouseProcessedTableEntry.getFieldsToSync().split(",");
        String SQL = createSQLScriptToUpdate(originValueList, warehouseFieldsToSyncList, tableType);

        return SQL;
    }

    private String createSQLScriptToUpdate(List<String> originValueList,  String[] warehouseFieldsToSyncList, String tableType){
        StringBuilder updateSQLBuilder = new StringBuilder();
        String unid = "";
        int i = 0;
        for (String warehouseFieldName : warehouseFieldsToSyncList) {
            if(warehouseFieldName.equals("unid") && tableType.equals("main")){
                unid = originValueList.get(i);
            }
            updateSQLBuilder.append(warehouseFieldName + " = " + originValueList.get(i) + ",");
            i++;
        }
        String updateSql = updateSQLBuilder.toString().substring(0, updateSQLBuilder.toString().length()-1);
        return "SET " + updateSql + "\n WHERE unid = " + unid;
    }

    private String getSQLNamesOfFieldsToCreate(String[] warehouseFieldsToSyncList){
        StringBuilder createSQLBuilder = new StringBuilder();
        for (String warehouseFieldName : warehouseFieldsToSyncList) {
            createSQLBuilder.append(warehouseFieldName +  ",");
        }
        String createSQL = createSQLBuilder.toString().substring(0, createSQLBuilder.toString().length()-1);
        return "(" + createSQL + ")";
    }

    private String getSQLValuesToCreate(List<String> originValuesList) {
        StringBuilder createValuesSQLBuilder = new StringBuilder();
        for (String originValue : originValuesList) {
            createValuesSQLBuilder.append(originValue +  ",");
        }
        String createSQL = createValuesSQLBuilder.toString().substring(0, createValuesSQLBuilder.toString().length()-1);
        return "VALUES(" + createSQL + ")";
    }

    private List<String> getListOriginValueFromMap(Map<String,String> mapFromJson, Map<String,String> originFieldsToSyncMap, List<String> originFieldsToSyncList, String tableType) throws Exception {
        List<String> valueList = new ArrayList<>();
        int i = 0;
        for (String warehouseFieldToSync : originFieldsToSyncList){
            for (Map.Entry<String, String> originFieldToSync : originFieldsToSyncMap.entrySet()) {
                String key = originFieldToSync.getKey();
                String type = originFieldToSync.getValue();

                String value = mapFromJson.get(warehouseFieldToSync);

                if(tableType.equals("response")){
                   if(isProcessedFieldUnid(warehouseFieldToSync,key)){
                        UUID uuid = UUID.randomUUID();
                        valueList.add("'" + uuid.toString().toUpperCase().replaceAll("-", "") + "'");
                    }
                }
                if(tableType.equals("main")){
                   if(isProcessedFieldUnid(warehouseFieldToSync,key)){
                        valueList.add("'" + value + "'");
                    }
                }

                if(type.equals("String") && warehouseFieldToSync.equals(key) && !warehouseFieldToSync.equals("unid")){
                    if(value == null || value.isEmpty()) {
                        valueList.add(null);
                    } else {
                        if(value.substring(value.length()-1).equals("\"") && !value.contains("\'")){
                            value = value.substring(0,value.length()-1);
                            valueList.add("'" + value + "'");
                        } else if(value.contains("\'")){
                            valueList.add("'" + value.replaceAll("\'","\'\'") + "'");
                        } else if(value.contains("≤")){
                            valueList.add("'" + value.replaceAll("≤","<=") + "'");
                        } else {
                            valueList.add("'" + value + "'");
                        }
                    }
                }

                if(type.equals("Integer") && key.equals(warehouseFieldToSync)){
                    if(value == null || value.isEmpty()) {
                        valueList.add(null);
                    } else {
                        value = value.replaceAll(" ","");
                        if(value.contains("\"")){
                            value = value.replace("\"", "");
                        } else if(value.contains(",")) {
                            valueList.add(value.replaceAll(",", "."));
                        } else {
                            valueList.add(value);
                        }
                    }
                }

                if(type.equals("DateTime") && key.equals(warehouseFieldToSync)){
                    if(value == null || value.isEmpty()){
                        valueList.add(null);
                    } else {
                        String dateTime = dateParser.formatDateTime(value);
                        if(dateTime!=null) {
                            if (Integer.valueOf(dateTime.substring(6, 10)) > 1900) {
                                valueList.add("convert(datetime, '" + dateParser.formatDateTime(value) + "', 104)");
                            } else {
                                valueList.add(null);
                            }
                        } else {
                            valueList.add(null);
                        }
                    }
                }

                if(type.equals("Date") && key.equals(warehouseFieldToSync)){
                    if(value == null || value.isEmpty()){
                        valueList.add(null);
                    } else {
                        String date = dateParser.formatDate(value);
                        if(date == null) {
                            valueList.add(null);
                        } else {
                            if (Integer.valueOf(date.substring(6, 10)) > 1900) {
                                valueList.add("convert(date, '" + date + "', 104)");
                            } else {
                                valueList.add(null);
                            }
                        }
                    }
                }
                i++;
            }
        }

        return valueList;
    }

    private Map<String,String> getOriginFieldsToSyncMap(ConfigOriginTableDb processedTableEntry){
        Map<String,String> map = dominoDocumentFieldsToSyncMapParser.getFieldsToParseMap(processedTableEntry);
        return map;
    }

    private boolean isProcessedFieldUnid(String warehouseFieldToSync, String key){
        if(warehouseFieldToSync.equals(key) && warehouseFieldToSync.equals("unid")){
            return true;
        }
        return false;
    }
}

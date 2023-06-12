
package pl.com.warehousefull.warehouse.parser.domino;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;

import java.util.HashMap;
import java.util.Map;

@Component
public class DominoDocumentFieldsToSyncMapParser {

    public Map<String, String> getFieldsToParseMap(ConfigOriginTableDb processedEntry) {
       Map<String, String> fieldsMap = new HashMap<>();
       String[] fieldsNames = processedEntry.getFieldsToSync().split(",");
       String[] fieldsTypes = processedEntry.getFieldsToSyncTypes().split(",");
       for(int i=0; i < fieldsNames.length; i++){
           fieldsMap.put(fieldsNames[i],fieldsTypes[i]);
       }
        return fieldsMap;
    }

    public String getFieldsToParseString(String htmlToParse) {
        String fieldsString = "";
        String[] htmlToParseList = htmlToParse.split("</option>");
        for (String htmlToParseString : htmlToParseList) {
            String htmlStringName = getFieldName(htmlToParseString);
            fieldsString += htmlStringName + ",";
        }
        return fieldsString.substring(0,fieldsString.length()-1);
    }

    public String getFieldName(String htmlToParseString) {
        int startIndexName = htmlToParseString.indexOf(">");
        String htmlStringName = htmlToParseString.substring(startIndexName + 1);
        return htmlStringName;
    }
    public String getFieldType(String htmlToParseString){
        int startIndexType = htmlToParseString.indexOf("type");
        int endIndexType = 0;
        String htmlStringType = "";
        if(htmlToParseString.contains("value")){
            endIndexType = htmlToParseString.indexOf("value");
            htmlStringType = htmlToParseString.substring(startIndexType + 6, endIndexType - 2);
        } else {
            endIndexType = htmlToParseString.indexOf(">");
            htmlStringType = htmlToParseString.substring(startIndexType + 6, endIndexType - 1);
        }

        return htmlStringType;
    }
}

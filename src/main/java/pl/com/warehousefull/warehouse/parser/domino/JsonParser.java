
package pl.com.warehousefull.warehouse.parser.domino;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class JsonParser {

    public String[] splitJson(String jsonAllDocs){
        String[] jsonList = jsonAllDocs.split("<fieldToCountName><split>");
        return jsonList;
    }

    public String[] splitResponseJson(String jsonAllDocs){
        jsonAllDocs = jsonAllDocs.substring(0,jsonAllDocs.indexOf("<parent_unids>"));
        String[] jsonList = jsonAllDocs.split("<fieldToCountName><split>");
        return jsonList;
    }

    public Map<String, String> getMapFromJsonByGSON(String jsonOneDoc){
        Gson gson = new Gson();
        try{
            TypeToken<Map<String, String>> token = new TypeToken<Map<String, String>>(){};
            return gson.fromJson(jsonOneDoc, token.getType());
        } catch (Exception e){
            System.out.println(jsonOneDoc);
            return null;
        }

    }

    public String[] getListToClean(String json) {
        String parentUnids = null;
        try {
            parentUnids = json.substring(json.indexOf("<parent_unids>") + 14, json.indexOf("</parent_unids>"));
            return parentUnids.split(",");
        } catch (Exception e) {
            System.out.println(json);
            return null;
        }
    }

    public String getListToCleanTest(String json){
        String parentUnids = json.substring(json.indexOf("<parent_unids>")+14,json.indexOf("</parent_unids>")-1);
        return parentUnids;
    }
}

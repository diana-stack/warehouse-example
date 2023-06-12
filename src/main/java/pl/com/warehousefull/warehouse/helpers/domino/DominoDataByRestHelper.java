
package pl.com.warehousefull.warehouse.helpers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginTableDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseTableDb;

import java.io.*;
import java.net.*;
import java.sql.SQLException;


@Component
public class DominoDataByRestHelper {

    @Autowired
    DominoRequestForRestHelper dominoRequestForRestHelper;

    public String getJsonByRest(ConfigOriginDb configOriginDb, ConfigOriginTableDb configOriginTableDb, String requestType, String syncType, String viewName, ConfigWarehouseTableDb warehouseProcessedTableEntry) throws SQLException, IOException {
        String url = "http://" + configOriginDb.getDbUrl() + "/" + configOriginDb.getDbName() + "/rest.xsp/" + requestType;
        String json = sendRequestWithAuthenticator(url, configOriginTableDb, configOriginDb, syncType, viewName, warehouseProcessedTableEntry);
        return json;
    }

    public String sendRequestWithAuthenticator(String url,ConfigOriginTableDb configOriginTableDb, ConfigOriginDb configOriginDb,String syncType, String viewName, ConfigWarehouseTableDb warehouseProcessedTableEntry) throws IOException, SQLException {
        setAuthenticator(configOriginDb);

        HttpURLConnection connection = null;
        try {
            connection = createConnection(url, configOriginTableDb, syncType, viewName, warehouseProcessedTableEntry);
            BufferedReader br = null;
            if (100 <= connection.getResponseCode() && connection.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    private HttpURLConnection createConnection(String urlString,ConfigOriginTableDb configOriginTableDb, String syncType, String viewName,  ConfigWarehouseTableDb warehouseProcessedTableEntry) throws MalformedURLException, IOException, ProtocolException, SQLException {
        URL url = new URL(String.format(urlString));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(dominoRequestForRestHelper.createBodyForRequest(warehouseProcessedTableEntry, configOriginTableDb, syncType, viewName));
        osw.flush();
        osw.close();
        os.close();
        connection.connect();
        return connection;
    }



    private void setAuthenticator(ConfigOriginDb configOriginDb) {
        Authenticator.setDefault(new BasicAuthenticator(configOriginDb.getUserName(), configOriginDb.getPassword()));
    }

    private final class BasicAuthenticator extends Authenticator {
        String userName;
        String password;

        public BasicAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password.toCharArray());
        }
    }
}

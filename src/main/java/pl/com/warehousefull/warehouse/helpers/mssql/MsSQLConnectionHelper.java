package pl.com.warehousefull.warehouse.helpers.mssql;

import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigOriginDb;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class MsSQLConnectionHelper {


    public Connection createWarehouseConnection(ConfigWarehouseDb configWarehouseDb){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(configWarehouseDb.getDbUrl(), configWarehouseDb.getUserName(), configWarehouseDb.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection createOriginConnection(ConfigOriginDb configOriginDb){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(configOriginDb.getDbUrl(), configOriginDb.getUserName(), configOriginDb.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


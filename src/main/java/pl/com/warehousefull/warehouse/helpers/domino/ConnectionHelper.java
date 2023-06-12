package pl.com.warehousefull.warehouse.helpers.domino;

import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.models.config.ConfigWarehouseDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ConnectionHelper {
    public Statement createWarehouseConnection(ConfigWarehouseDb processedDb){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(processedDb.getDbUrl(), processedDb.getUserName(), processedDb.getPassword());
            Statement stmt = connection.createStatement();
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection createWarehouseConnectionTest(ConfigWarehouseDb processedDb){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(processedDb.getDbUrl(), processedDb.getUserName(), processedDb.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


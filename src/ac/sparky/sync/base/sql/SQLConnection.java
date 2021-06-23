package ac.sparky.sync.base.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private String host, username, password, database;
    private int port;
    private Connection connection;

    public SQLConnection(String host, int port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public boolean connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?autoReconnect=true", username, password);
            return true;
        } catch (SQLException exception) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

}

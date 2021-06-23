package ac.sparky.sync.base.sql;

import ac.sparky.sync.bungee.MainBungee;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SQLPool {

    private SQLConnection sqlConnection;
    private ScheduledExecutorService pool;

    public SQLPool(SQLConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
        this.pool = Executors.newScheduledThreadPool(1);
        try {
            PreparedStatement statement = this.sqlConnection.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS sparky_sync (PlayerName VARCHAR(100))"
            );
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void startChecker() { /* this checker is only for bungee plugin */
        this.pool.scheduleAtFixedRate(() -> {
            Set<String> currentPlayers = new HashSet<>();
            try {
                PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(
                        "SELECT * FROM sparky_sync"
                );
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    currentPlayers.add(resultSet.getString("PlayerName"));
                }
            } catch (SQLException exception) {
            }
            String banCommand = MainBungee.getInstance().getConfig().getString("sync.ban-command");
            currentPlayers.forEach(all -> {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(
                        ProxyServer.getInstance().getConsole(),
                        banCommand.replace("%player%", all)
                );
                try {
                    PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(
                            "DELETE FROM sparky_sync WHERE PlayerName='" + all + "'"
                    );
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException exception) {
                }
            });
        }, 1L, 1L, TimeUnit.SECONDS);
    }

    public void injectPlayer(String name) { /* and this injector method for spigot */
        this.pool.execute(() -> {
            try {
                PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(
                        "INSERT INTO sparky_sync (PlayerName) VALUES (" + name + ")"
                );
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException exception) {
            }
        });
    }
}

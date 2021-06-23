package ac.sparky.sync.base;

import ac.sparky.sync.base.impl.SparkyKeyPair;
import ac.sparky.sync.base.impl.Synchronizable;
import ac.sparky.sync.base.impl.sync.PluginSync;
import ac.sparky.sync.base.impl.sync.SqlSync;
import ac.sparky.sync.base.sql.SQLConnection;
import ac.sparky.sync.base.sql.SQLPool;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.base.type.SyncType;
import ac.sparky.sync.bungee.MainBungee;
import ac.sparky.sync.spigot.MainSpigot;

public class SparkyBungee {
    private static SparkyBungee instance;
    private Synchronizable synchronizable;
    private final SyncType syncType;
    private PluginType pluginType;
    private SparkyKeyPair keyPair;
    private SQLConnection connection;
    private SQLPool pool;
    private String host, name, pass, db;
    private int port;

    public SparkyBungee(PluginType pluginType) {
        this.syncType = SyncType.PLUGIN;
        this.keyPair = new SparkyKeyPair(pluginType);
        if(pluginType == PluginType.SPIGOT) {
            this.synchronizable = new PluginSync();
        }
    }

    public SparkyBungee(PluginType pluginType, String host, int port, String name, String pass, String db) {
        this.syncType = SyncType.SQL;
        this.host = host;
        this.port = port;
        this.name = name;
        this.pass = pass;
        this.db = db;
        if(!(connection = new SQLConnection(host, port, name, pass, db)).connect()) {
            if(pluginType == PluginType.BUNGEE) {
                MainBungee.getInstance().getLogger().severe("Cannot connect to mysql database");
            } else {
                MainSpigot.getInstance().getLogger().severe("Cannot connect to mysql database");
            }
        }
        this.synchronizable = new SqlSync();
        this.pool = new SQLPool(connection);
        this.pool.startChecker();
    }

    public static SparkyBungee getInstance() {
        return instance;
    }

    public static boolean init(PluginType type) {
        if(type == PluginType.SPIGOT) {
            String typeString = MainSpigot.getInstance().getConfig().getString("sync.type");
            if(!typeString.equals("PLUGIN") && ! typeString.equals("SQL")) {
                MainSpigot.getInstance().getLogger().severe("Type is invalid");
                return false;
            }
            if(typeString.equals("PLUGIN")) {
                instance = new SparkyBungee(type);
            }
            if(typeString.equals("SQL")) {
                String host = MainSpigot.getInstance().getConfig().getString("sql.host");
                int port = MainSpigot.getInstance().getConfig().getInt("sql.port");
                String db = MainSpigot.getInstance().getConfig().getString("sql.database");
                String name = MainSpigot.getInstance().getConfig().getString("sql.authentication.username");
                String pass = MainSpigot.getInstance().getConfig().getString("sql.authentication.password");
                instance = new SparkyBungee(type, host, port, name, pass, db);
            }
        } else if(type == PluginType.BUNGEE) {
            String typeString = MainBungee.getInstance().getConfig().getString("sync.type");
            if(!typeString.equals("PLUGIN") && ! typeString.equals("SQL")) {
                MainBungee.getInstance().getLogger().severe("Type is invalid");
                return false;
            }
            if(typeString.equals("PLUGIN")) {
                instance = new SparkyBungee(type);
            }
            if(typeString.equals("SQL")) {
                String host = MainBungee.getInstance().getConfig().getString("sql.host");
                int port = MainBungee.getInstance().getConfig().getInt("sql.port");
                String db = MainBungee.getInstance().getConfig().getString("sql.database");
                String name = MainBungee.getInstance().getConfig().getString("sql.authentication.username");
                String pass = MainBungee.getInstance().getConfig().getString("sql.authentication.password");
                instance = new SparkyBungee(type, host, port, name, pass, db);
            }
        } else {
            return false;
        }
        return true;
    }

    public Synchronizable getSynchronizable() {
        return synchronizable;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public SparkyKeyPair getKeyPair() {
        return keyPair;
    }

    public SQLPool getSqlPool() {
        return pool;
    }
}

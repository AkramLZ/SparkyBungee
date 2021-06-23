package ac.sparky.sync.base;

import ac.sparky.sync.base.impl.SparkyKeyPair;
import ac.sparky.sync.base.impl.Synchronizable;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.base.type.SyncType;
import ac.sparky.sync.spigot.MainSpigot;

public class SparkyBungee {

    private static SparkyBungee instance;
    private Synchronizable synchronizable;
    private SyncType syncType;
    private PluginType pluginType;
    private SparkyKeyPair keyPair;
    private String host, name, pass, db;
    private int port;

    public SparkyBungee(PluginType pluginType) {
        this.syncType = SyncType.PLUGIN;
        this.keyPair = new SparkyKeyPair(pluginType);
    }

    public SparkyBungee(PluginType pluginType, String host, int port, String name, String pass, String db) {
        this.syncType = SyncType.SQL;
        this.host = host;
        this.port = port;
        this.name = name;
        this.pass = pass;
        this.db = db;
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
                String encode = MainSpigot.getInstance().getConfig().getString("sync.messaging.encode");
                String key = MainSpigot.getInstance().getConfig().getString("sync.messaging.key");
                instance = new SparkyBungee(type);
            }
        } else if(type == PluginType.BUNGEE) {

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
}

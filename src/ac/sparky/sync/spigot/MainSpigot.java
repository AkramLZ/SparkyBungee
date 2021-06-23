package ac.sparky.sync.spigot;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.spigot.listeners.BanListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSpigot extends JavaPlugin {

    private static MainSpigot instance;

    @Override
    public void onEnable() {
        instance = this;
        SparkyBungee.init(PluginType.SPIGOT);
        getServer().getPluginManager().registerEvents(new BanListener(), this);
    }

    public static MainSpigot getInstance() {
        return instance;
    }
}

package ac.sparky.sync.bungee;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.bungee.listeners.PluginMessageListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class MainBungee extends Plugin {

    private static MainBungee instance;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;
        SparkyBungee.init(PluginType.BUNGEE);
        ProxyServer.getInstance().registerChannel("sparky:ban");
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessageListener());
    }

    public static MainBungee getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }
}

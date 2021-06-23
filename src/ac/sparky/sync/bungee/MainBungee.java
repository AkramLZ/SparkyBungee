package ac.sparky.sync.bungee;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.bungee.listeners.PluginMessageListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;

public class MainBungee extends Plugin {

    private static MainBungee instance;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;
        if(!saveDefaultConfig()) {
            getLogger().severe("Failed to load configuration file.");
            return;
        }
        SparkyBungee.init(PluginType.BUNGEE);
        getProxy().registerChannel("sparky:ban");
        getProxy().getPluginManager().registerListener(this, new PluginMessageListener());
    }

    public static MainBungee getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }

    boolean saveDefaultConfig() {
        try {
            File file = new File(getDataFolder() + "/config.yml");
            if(!file.exists()) {
                Files.copy(getResourceAsStream("config.yml"), file.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

}

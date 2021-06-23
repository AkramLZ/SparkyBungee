package ac.sparky.sync.spigot.listeners;

import ac.sparky.api.events.SparkyPunishEvent;
import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.spigot.MainSpigot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BanListener implements Listener {

    @EventHandler
    public void onBan(SparkyPunishEvent event) {
        if(event.isCancelled())
            return;
        event.setCancelled(true);
        event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&',
                MainSpigot.getInstance().getConfig().getString("sync.kick-message")));
        SparkyBungee.getInstance().getSynchronizable().execute(event.getPlayer().getName());
    }

}
package ac.sparky.sync.bungee.listeners;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.bungee.MainBungee;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        String channel = event.getTag();
        if(event.getSender() instanceof ProxiedPlayer)
            return;
        if(event.isCancelled() || !channel.equals("sparky:ban"))
            return;
        event.setCancelled(true);
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(event.getData());
        String playerName = dataInput.readUTF();
        String publicKey = dataInput.readUTF();
        String privateKey = dataInput.readUTF();
        String publicHex = SparkyBungee.getInstance().getKeyPair().getHexString(
                SparkyBungee.getInstance().getKeyPair().getKeyPair().getPublic().getEncoded());
        String privateHex = SparkyBungee.getInstance().getKeyPair().getHexString(
                SparkyBungee.getInstance().getKeyPair().getKeyPair().getPrivate().getEncoded());
        if(!publicHex.equals(publicKey) || !privateHex.equals(privateKey)) {
            return;
        }
        String banCommand = MainBungee.getInstance().getConfig().getString("sync.ban-command").replace("%player%", playerName);
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), banCommand);
    }
}

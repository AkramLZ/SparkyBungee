package ac.sparky.sync.base.impl.sync;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.base.impl.Synchronizable;
import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.spigot.MainSpigot;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginSync extends Synchronizable {
    @Override
    public void execute(String playerName) {
        if(SparkyBungee.getInstance().getPluginType() == PluginType.SPIGOT) {
            Player player = Bukkit.getPlayerExact(playerName);
            ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
            String publicHex = SparkyBungee.getInstance().getKeyPair().getHexString(
                    SparkyBungee.getInstance().getKeyPair().getKeyPair().getPublic().getEncoded());
            String privateHex = SparkyBungee.getInstance().getKeyPair().getHexString(
                    SparkyBungee.getInstance().getKeyPair().getKeyPair().getPrivate().getEncoded());
            dataOutput.writeUTF(playerName);
            dataOutput.writeUTF(publicHex);
            dataOutput.writeUTF(privateHex);
            player.sendPluginMessage(MainSpigot.getInstance(), "sparky:ban", dataOutput.toByteArray());
        }
    }
}

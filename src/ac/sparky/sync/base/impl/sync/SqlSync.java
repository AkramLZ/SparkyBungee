package ac.sparky.sync.base.impl.sync;

import ac.sparky.sync.base.SparkyBungee;
import ac.sparky.sync.base.impl.Synchronizable;

public class SqlSync extends Synchronizable {
    @Override
    public void execute(String playerName) {
        SparkyBungee.getInstance().getSqlPool().injectPlayer(playerName);
    }
}

package cn.nukkit.inventory;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockVector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class FakeBlockMenu extends Position implements InventoryHolder {

    private final Inventory inventory;

    public FakeBlockMenu(Inventory inventory, BlockVector3 pos, Level level) {
        super(pos.x, pos.y, pos.z, level);
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockVector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AnvilInventory extends ContainerInventory {

    public AnvilInventory(BlockVector3 position, Level level) {
        super(null, InventoryType.get(InventoryType.ANVIL));
        this.holder = new FakeBlockMenu(this, position, level);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu) this.holder;
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);

        for (int i = 0; i < 2; ++i) {
            this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), this.getItem(i));
            this.clear(i);
        }
    }
}

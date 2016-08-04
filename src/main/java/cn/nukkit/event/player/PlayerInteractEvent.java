package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PlayerInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public static final int LEFT_CLICK_BLOCK = 0;
    public static final int RIGHT_CLICK_BLOCK = 1;
    public static final int LEFT_CLICK_AIR = 2;
    public static final int RIGHT_CLICK_AIR = 3;
    public static final int PHYSICAL = 4;

    protected final Block blockTouched;

    protected final Vector3 touchVector;

    protected final int blockFace;

    protected final Item item;

    protected final int action;

    public PlayerInteractEvent(Player player, Item item, Vector3 pos, Block block, int face) {
        this(player, item, pos, block, face, RIGHT_CLICK_BLOCK);
    }

    public PlayerInteractEvent(Player player, Item item, Vector3 pos, Block block, int face, int action) {
        if (block != null) {
            this.blockTouched = block;
            this.touchVector = new Vector3(0, 0, 0);
        } else {
            this.touchVector = pos;
            this.blockTouched = Block.get(Block.AIR, 0, new BlockVector3(0, 0, 0), player.level);
        }

        this.player = player;
        this.item = item;
        this.blockFace = face;
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public Item getItem() {
        return item;
    }

    public Block getBlock() {
        return blockTouched;
    }

    public Vector3 getTouchVector() {
        return touchVector;
    }

    public int getFace() {
        return blockFace;
    }
}

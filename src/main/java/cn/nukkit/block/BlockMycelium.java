package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

/**
 * Created by Pub4Game on 03.01.2016.
 */
public class BlockMycelium extends BlockSolid {

    public BlockMycelium() {
        this(0);
    }

    public BlockMycelium(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Mycelium";
    }

    @Override
    public int getId() {
        return MYCELIUM;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{
                {Item.DIRT, 0, 1}
        };
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            //TODO: light levels
            NukkitRandom random = new NukkitRandom();
            int x = random.nextRange(this.x - 1, this.x + 1);
            int y = random.nextRange(this.y - 1, this.y + 1);
            int z = random.nextRange(this.z - 1, this.z + 1);
            Block block = this.level.getBlock(new BlockVector3(x, y, z));
            if (block.getId() == Block.DIRT) {
                if (block.getSide(1) instanceof BlockTransparent) {
                    BlockSpreadEvent ev = new BlockSpreadEvent(block, this, new BlockMycelium());
                    Server.getInstance().getPluginManager().callEvent(ev);
                    if (!ev.isCancelled()) {
                        this.level.setBlock(block, ev.getNewState());
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRASS_BLOCK_COLOR;
    }
}

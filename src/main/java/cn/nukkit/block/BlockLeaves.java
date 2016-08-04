package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockLeaves extends BlockTransparent {
    public static final int OAK = 0;
    public static final int SPRUCE = 1;
    public static final int BRICH = 2;
    public static final int JUNGLE = 3;
    public static final int ACACIA = 4;
    public static final int DARK_OAK = 5;

    public BlockLeaves() {
        this(0);
    }

    public BlockLeaves(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return LEAVES;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Oak Leaves",
                "Spruce Leaves",
                "Birch Leaves",
                "Jungle Leaves"
        };
        return names[this.meta & 0x03];
    }

    @Override
    public int getBurnChance() {
        return 30;
    }

    @Override
    public int getBurnAbility() {
        return 60;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        this.meta |= 0x04;
        this.level.setBlock(this, this, true);
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isShears()) {
            return new int[][]{
                    {Item.LEAVES, this.meta & 0x03, 1}
            };
        } else {
            if ((int) ((Math.random()) * 200) == 0 && (this.meta & 0x03) == OAK) {
                return new int[][]{
                        {Item.APPLE, 0, 1}
                };
            }
            if ((int) ((Math.random()) * 20) == 0) {
                return new int[][]{
                        {Item.SAPLING, this.meta & 0x03, 1}
                };
            }
        }
        return new int[0][0];
    }

    //todo:LeavesOnUpdate


    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}

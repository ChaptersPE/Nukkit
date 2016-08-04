package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.redstone.Redstone;
import cn.nukkit.utils.BlockColor;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneWire extends BlockFlowable {

    public BlockRedstoneWire() {
        this(0);
    }

    public BlockRedstoneWire(int meta) {
        super(meta);
        this.powerLevel = meta;
    }

    @Override
    public String getName() {
        return "Redstone Wire";
    }

    @Override
    public int getId() {
        return REDSTONE_WIRE;
    }

    @Override
    public void setPowerLevel(int redstonePower) {
        if (redstonePower > 15) redstonePower = 15;
        else if (redstonePower < 0) redstonePower = 0;
        this.powerLevel = redstonePower;
        this.meta = redstonePower;
    }

    @Override
    public int getNeighborPowerLevel() {
        int power = 0;
        int tempLevel;
        tempLevel = this.getSide(SIDE_DOWN).getPowerLevel();
        power = tempLevel > power ? tempLevel : power;
        tempLevel = this.getSide(SIDE_UP).getPowerLevel();
        power = tempLevel > power ? tempLevel : power;
        Block block;
        for (int side : new int[]{Vector3.SIDE_NORTH, Vector3.SIDE_SOUTH, Vector3.SIDE_WEST, Vector3.SIDE_EAST}) {
            block = this.getSide(side);
            tempLevel = block.getPowerLevel();
            power = tempLevel > power ? tempLevel : power;
            if (!(block instanceof BlockSolid)) {
                Block blockDown;
                blockDown = block.getSide(SIDE_DOWN);
                if (blockDown instanceof BlockRedstoneWire) {
                    tempLevel = blockDown.getPowerLevel();
                    power = tempLevel > power ? tempLevel : power;
                }
            }
        }
        Block topBlock = this.getSide(SIDE_UP);
        if (!(topBlock instanceof BlockSolid)) {
            for (int side : new int[]{Vector3.SIDE_NORTH, Vector3.SIDE_SOUTH, Vector3.SIDE_WEST, Vector3.SIDE_EAST}) {
                block = topBlock.getSide(side);
                if (block instanceof BlockRedstoneWire) {
                    tempLevel = block.getPowerLevel();
                    power = tempLevel > power ? tempLevel : power;
                }
            }
        }
        return power;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(Vector3.SIDE_DOWN).isTransparent()) {
                this.level.useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        if (this.getSide(Vector3.SIDE_DOWN).isTransparent()) {
            return false;
        } else {
            this.setPowerLevel(this.getNeighborPowerLevel() - 1);
            block.level.setBlock(block, this, true, true);
            Redstone.active(this);
            return true;
        }
    }

    @Override
    public boolean onBreak(Item item) {
        int level = this.getPowerLevel();
        this.level.setBlock(this, new BlockAir(), true, false);
        Redstone.deactive(this, level);
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{
                {Item.REDSTONE, 0, 1}
        };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}

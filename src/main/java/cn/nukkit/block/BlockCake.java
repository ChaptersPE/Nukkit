package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

/**
 * @author Nukkit Project Team
 */
public class BlockCake extends BlockTransparent {

    public BlockCake(int meta) {
        super(meta);
    }

    public BlockCake() {
        this(0);
    }

    @Override
    public String getName() {
        return "Cake Block";
    }

    @Override
    public int getId() {
        return CAKE_BLOCK;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                x + (1 + getDamage() * 2) / 16,
                y,
                z + 0.0625,
                x - 0.0625 + 1,
                y + 0.5,
                z - 0.0625 + 1
        );
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        if (getSide(0).getId() != Block.AIR) {
            this.level.setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (getSide(0).getId() == Block.AIR) {
                this.level.setBlock(this, new BlockAir(), true);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[0][0];
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null && player.getFoodData().getLevel() < player.getFoodData().getMaxLevel()) {
            if (meta <= 0x06) meta++;
            if (meta >= 0x06) {
                this.level.setBlock(this, new BlockAir(), true);
            } else {
                Food.getByRelative(this).eatenBy(player);
                this.level.setBlock(this, this, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}

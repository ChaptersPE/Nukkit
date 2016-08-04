package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.redstone.Redstone;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneTorch extends BlockTorch {

    public BlockRedstoneTorch() {
        this(0);
    }

    public BlockRedstoneTorch(int meta) {
        super(meta);
        this.setPowerSource(true);
        this.setPowerLevel(Redstone.POWER_STRONGEST);
    }

    @Override
    public String getName() {
        return "Redstone Torch";
    }

    @Override
    public int getId() {
        return REDSTONE_TORCH;
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        Block below = this.getSide(0);

        if (!target.isTransparent() && face != 0) {
            int[] faces = new int[]{
                    0, //0, nerver used
                    5, //1
                    4, //2
                    3, //3
                    2, //4
                    1, //5
            };
            this.meta = faces[face];
            this.level.setBlock(block, this, true, true);
            Redstone.active(this);

            return true;
        } else if (!below.isTransparent() || below instanceof BlockFence || below.getId() == COBBLE_WALL) {
            this.meta = 0;
            this.level.setBlock(block, this, true, true);
            Redstone.active(this);

            return true;
        }
        return false;
    }


    @Override
    public boolean onBreak(Item item) {
        int level = this.getPowerLevel();
        this.level.setBlock(this, new BlockAir(), true, true);
        Redstone.deactive(this, level);
        return true;
    }

}

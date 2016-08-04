package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.object.ObjectTallGrass;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockGrass extends BlockDirt {

    public BlockGrass() {
        this(0);
    }

    public BlockGrass(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return GRASS;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0x0F) {
            item.count--;
            ObjectTallGrass.growGrass(this.level, this, new NukkitRandom(), 15, 10);
            return true;
        } else if (item.isHoe()) {
            item.useOn(this);
            this.level.setBlock(this, new BlockFarmland());
            return true;
        } else if (item.isShovel()) {
            item.useOn(this);
            this.level.setBlock(this, new BlockGrassPath());
            return true;
        }

        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (this.getSide(1).getLightLevel() < 4) {
                BlockSpreadEvent ev = new BlockSpreadEvent(this, this, new BlockDirt());
                Server.getInstance().getPluginManager().callEvent(ev);
            } else if (this.getSide(1).getLightLevel() >= 9) {
                for (int l = 0; l < 4; ++l) {
//                    NukkitRandom random = new NukkitRandom();
//                    int x = random.nextRange((int) this.x - 1, (int) this.x + 1);
//                    int y = random.nextRange((int) this.y - 2, (int) this.y + 2);
//                    int z = random.nextRange((int) this.z - 1, (int) this.z + 1);
                    Block blocks = this.level.getBlock(this);
                    if (blocks.getId() == Block.DIRT && blocks.getDamage() == 0x0F && blocks.getSide(1).getLightLevel() >= 4 && blocks.z <= 2) {
                        BlockSpreadEvent ev = new BlockSpreadEvent(blocks, this, new BlockGrass());
                        Server.getInstance().getPluginManager().callEvent(ev);
                        if (!ev.isCancelled()) {
                            this.level.setBlock(blocks, ev.getNewState());
                        }
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

package cn.nukkit.block;


import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;

import java.util.Iterator;
import java.util.Map;

public class BlockBrewingStand extends BlockSolid {

    public BlockBrewingStand() {
        this(0);
    }

    public BlockBrewingStand(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Brewing Stand";
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
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getId() {
        return BREWING_STAND_BLOCK;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        if (!block.getSide(SIDE_DOWN).isTransparent()) {
            this.level.setBlock(block, this, true, true);

            CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<>("Items"))
                    .putString("id", BlockEntity.BREWING_STAND)
                    .putInt("x", this.x)
                    .putInt("y", this.y)
                    .putInt("z", this.z);

            if (item.hasCustomName()) {
                nbt.putString("CustomName", item.getCustomName());
            }

            if (item.hasCustomBlockData()) {
                Map<String, Tag> customData = item.getCustomBlockData().getTags();
                Iterator iter = customData.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry tag = (Map.Entry) iter.next();
                    nbt.put((String) tag.getKey(), (Tag) tag.getValue());
                }
            }

            new BlockEntityBrewingStand(this.level.getChunk(this.x >> 4, this.z >> 4), nbt);

            return true;
        }
        return false;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity t = this.level.getBlockEntity(this);
            BlockEntityBrewingStand brewing = null;
            if (t instanceof BlockEntityBrewingStand) {
                brewing = (BlockEntityBrewingStand) t;
            } else {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.BREWING_STAND)
                        .putInt("x", this.x)
                        .putInt("y", this.y)
                        .putInt("z", this.z);
                brewing = new BlockEntityBrewingStand(this.level.getChunk(this.x >> 4, this.z >> 4), nbt);
            }

            if (brewing.namedTag.contains("Lock") && brewing.namedTag.get("Lock") instanceof StringTag) {
                if (!brewing.namedTag.getString("Lock").equals(item.getCustomName())) {
                    return false;
                }
            }

            player.addWindow(brewing.getInventory());
        }

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                    {Item.BREWING_STAND, 0, 1}
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}

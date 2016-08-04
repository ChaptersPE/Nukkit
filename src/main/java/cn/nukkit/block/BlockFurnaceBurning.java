package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;

import java.util.Iterator;
import java.util.Map;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockFurnaceBurning extends BlockSolid {

    public BlockFurnaceBurning() {
        this(0);
    }

    public BlockFurnaceBurning(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BURNING_FURNACE;
    }

    @Override
    public String getName() {
        return "Burning Furnace";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 17.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getLightLevel() {
        return 13;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        int faces[] = {4, 2, 5, 3};
        this.meta = faces[player != null ? player.getDirection() : 0];
        this.level.setBlock(block, this, true, true);
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.FURNACE)
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

        new BlockEntityFurnace(this.level.getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);

        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, new BlockAir(), true, true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity t = this.level.getBlockEntity(this);
            BlockEntityFurnace furnace;
            if (t instanceof BlockEntityFurnace) {
                furnace = (BlockEntityFurnace) t;
            } else {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.FURNACE)
                        .putInt("x", this.x)
                        .putInt("y", this.y)
                        .putInt("z", this.z);
                furnace = new BlockEntityFurnace(this.level.getChunk((this.x) >> 4, (this.z) >> 4), nbt);
            }

            if (furnace.namedTag.contains("Lock") && furnace.namedTag.get("Lock") instanceof StringTag) {
                if (!furnace.namedTag.getString("Lock").equals(item.getCustomName())) {
                    return true;
                }
            }

            player.addWindow(furnace.getInventory());
        }

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                {Item.FURNACE, 0, 1}
            };
        } else {
            return new int[0][0];
        }
    }
}

package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.utils.BlockColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snake1999 on 2016/1/11.
 * Package cn.nukkit.block in project nukkit
 */
public class BlockRail extends BlockFlowable {

    public BlockRail() {
        this(0);
    }

    public BlockRail(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Rail";
    }

    @Override
    public int getId() {
        return RAIL;
    }

    @Override
    public double getHardness() {
        return 0.7;
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).isTransparent()) {
                this.level.useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{
                {Item.RAIL, 0, 1}
        };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        Block down = this.getSide(BlockVector3.SIDE_DOWN);
        if (down == null) return false;
        if (down.isTransparent()) return false;
        int[][] arrayXZ = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        int[] arrayY = new int[]{0, 1, -1};
        List<BlockVector3> connected = new ArrayList<>();
        for (int[] xz : arrayXZ) {
            int x = xz[0];
            int z = xz[1];
            for (int y : arrayY) {
                BlockVector3 v3 = new BlockVector3(x, y, z).add(this);
                Block v3block = this.level.getBlock(v3);
                if (v3block == null) continue;
                if (!isRailBlock(v3block.getId()) || !isValidRailMeta(v3block.getDamage())) continue;
                if (!(v3block instanceof BlockRail)) continue;
                this.connectRail(v3block);
                connected.add(v3block);
            }
            if (connected.size() >= 2) break;
        }

        if (connected.size() == 1) {
            BlockVector3 v3 = connected.get(0).subtract(this);
            this.meta = (v3.y != 1) ? (v3.x == 0 ? 0 : 1) : (int) (v3.z == 0 ? (v3.x / -2) + 2.5 : (v3.z / 2) + 4.5);
        } else if (connected.size() == 2) {
            BlockVector3[] subtract = new BlockVector3[2];
            for (int i = 0; i < connected.size(); i++) {
                subtract[i] = connected.get(i).subtract(this);
            }
            if (Math.abs(subtract[0].x) == Math.abs(subtract[1].z) && Math.abs(subtract[1].x) == Math.abs(subtract[0].z)) {
                BlockVector3 v3 = connected.get(0).subtract(this).add(connected.get(1).subtract(this));
                this.meta = v3.x == 1 ? (v3.z == 1 ? 6 : 9) : (v3.z == 1 ? 7 : 8);
            } else if (subtract[0].y == 1 || subtract[1].y == 1) {
                BlockVector3 v3 = subtract[0].y == 1 ? subtract[0] : subtract[1];
                this.meta = v3.x == 0 ? (v3.z == -1 ? 4 : 5) : (v3.x == 1 ? 2 : 3);
            } else {
                this.meta = subtract[0].x == 0 ? 0 : 1;
            }
        }
        this.level.setBlock(this, Block.get(this.getId(), this.getDamage()), true, true);
        return true;
    }

    /************ Rail Connecting Part ***********/
    /****
     * todo: too complex, need to simplify
     ****/

    protected BlockVector3[] canConnectRail(Block block) {
        if (!(block instanceof BlockRail)) return null;
        if (this.distanceSquared(block) > 2) return null;
        BlockVector3[] result = checkRail(this);
        if (result.length == 2) return null;
        return result;
    }

    protected void connectRail(Block rail) {
        BlockVector3[] connected = canConnectRail(rail);
        if (connected == null) return;
        if (connected.length == 1) {
            BlockVector3 v3 = connected[0].subtract(this);
            this.meta = (v3.y != 1) ? (v3.x == 0 ? 0 : 1) : (int) (v3.z == 0 ? (v3.x / -2) + 2.5 : (v3.z / 2) + 4.5);
        } else if (connected.length == 2) {
            BlockVector3[] subtract = new BlockVector3[2];
            for (int i = 0; i < connected.length; i++) {
                subtract[i] = connected[i].subtract(this);
            }
            if (Math.abs(subtract[0].x) == Math.abs(subtract[1].z) && Math.abs(subtract[1].x) == Math.abs(subtract[0].z)) {
                BlockVector3 v3 = connected[0].subtract(this).add(connected[1].subtract(this));
                this.meta = v3.x == 1 ? (v3.z == 1 ? 6 : 9) : (v3.z == 1 ? 7 : 8);
            } else if (subtract[0].y == 1 || subtract[1].y == 1) {
                BlockVector3 v3 = subtract[0].y == 1 ? subtract[0] : subtract[1];
                this.meta = v3.x == 0 ? (v3.z == -1 ? 4 : 5) : (v3.x == 1 ? 2 : 3);
            } else {
                this.meta = subtract[0].x == 0 ? 0 : 1;
            }
        }
        this.level.setBlock(this, Block.get(this.getId(), this.getDamage()), true, true);
    }

    protected static BlockVector3[] checkRail(Block rail) {
        if (!(rail instanceof BlockRail)) return null;
        int damage = rail.getDamage();
        if (damage < 0 || damage > 10) return null;
        int[][][] delta = new int[][][]{
                {{0, 1}, {0, -1}},
                {{1, 0}, {-1, 0}},
                {{1, 0}, {-1, 0}},
                {{1, 0}, {-1, 0}},
                {{0, 1}, {0, -1}},
                {{0, 1}, {0, -1}},
                {{1, 0}, {0, 1}},
                {{0, 1}, {-1, 0}},
                {{-1, 0}, {0, -1}},
                {{0, -1}, {1, 0}}
        };
        int[] deltaY = new int[]{0, 1, -1};
        int[][] blocks = delta[damage];
        List<BlockVector3> connected = new ArrayList<>();
        for (int y : deltaY) {
            BlockVector3 v3 = new BlockVector3(
                    rail.x + blocks[0][0],
                    rail.y + y,
                    rail.z + blocks[0][1]
            );
            int idToConnect = rail.level.getBlockIdAt(v3.x, v3.y, v3.z);
            int metaToConnect = rail.level.getBlockDataAt(v3.x, v3.y, v3.z);
            if (!isRailBlock(idToConnect) || !isValidRailMeta(metaToConnect)) continue;
            int xDiff = damage - v3.x;
            int zDiff = damage - v3.z;
            for (int[] xz : blocks) {
                if (xz[0] != xDiff || xz[1] != zDiff) continue;
                connected.add(v3);
            }
        }
        for (int y : deltaY) {
            BlockVector3 v3 = new BlockVector3(
                    rail.x + blocks[1][0],
                    rail.y + y,
                    rail.z + blocks[1][1]
            );
            int idToConnect = rail.level.getBlockIdAt(v3.x, v3.y, v3.z);
            int metaToConnect = rail.level.getBlockDataAt(v3.x, v3.y, v3.z);
            if (!isRailBlock(idToConnect) || !isValidRailMeta(metaToConnect)) continue;
            int xDiff = damage - v3.x;
            int zDiff = damage - v3.z;
            for (int[] xz : blocks) {
                if (xz[0] != xDiff || xz[1] != zDiff) continue;
                connected.add(v3);
            }
        }
        return connected.toArray(new BlockVector3[connected.size()]);
    }

    protected static boolean isRailBlock(int id) {
        switch (id) {
            case RAIL:
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    protected static boolean isValidRailMeta(int meta) {
        return !(meta < 0 || meta > 10);
    }

}
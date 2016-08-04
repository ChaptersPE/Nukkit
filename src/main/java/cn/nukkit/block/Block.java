package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.Position;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.BlockColor;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Block extends BlockVector3 implements Metadatable, Cloneable {
    public static final int AIR = 0;
    public static final int STONE = 1;
    public static final int GRASS = 2;
    public static final int DIRT = 3;
    public static final int COBBLESTONE = 4;
    public static final int COBBLE = 4;
    public static final int PLANK = 5;
    public static final int PLANKS = 5;
    public static final int WOODEN_PLANK = 5;
    public static final int WOODEN_PLANKS = 5;
    public static final int SAPLING = 6;
    public static final int SAPLINGS = 6;
    public static final int BEDROCK = 7;
    public static final int WATER = 8;
    public static final int STILL_WATER = 9;
    public static final int LAVA = 10;
    public static final int STILL_LAVA = 11;
    public static final int SAND = 12;
    public static final int GRAVEL = 13;
    public static final int GOLD_ORE = 14;
    public static final int IRON_ORE = 15;
    public static final int COAL_ORE = 16;
    public static final int LOG = 17;
    public static final int WOOD = 17;
    public static final int TRUNK = 17;
    public static final int LEAVES = 18;
    public static final int LEAVE = 18;
    public static final int SPONGE = 19;
    public static final int GLASS = 20;
    public static final int LAPIS_ORE = 21;
    public static final int LAPIS_BLOCK = 22;

    public static final int DISPENSER = 23;

    public static final int SANDSTONE = 24;
    public static final int NOTEBLOCK = 25;
    public static final int BED_BLOCK = 26;
    public static final int POWERED_RAIL = 27;
    public static final int DETECTOR_RAIL = 28;

    public static final int COBWEB = 30;
    public static final int TALL_GRASS = 31;
    public static final int BUSH = 32;
    public static final int DEAD_BUSH = 32;
    public static final int WOOL = 35;
    public static final int DANDELION = 37;
    public static final int POPPY = 38;
    public static final int ROSE = 38;
    public static final int FLOWER = 38;
    public static final int RED_FLOWER = 38;
    public static final int BROWN_MUSHROOM = 39;
    public static final int RED_MUSHROOM = 40;
    public static final int GOLD_BLOCK = 41;
    public static final int IRON_BLOCK = 42;
    public static final int DOUBLE_SLAB = 43;
    public static final int DOUBLE_SLABS = 43;
    public static final int SLAB = 44;
    public static final int SLABS = 44;
    public static final int BRICKS = 45;
    public static final int BRICKS_BLOCK = 45;
    public static final int TNT = 46;
    public static final int BOOKSHELF = 47;
    public static final int MOSS_STONE = 48;
    public static final int MOSSY_STONE = 48;
    public static final int OBSIDIAN = 49;
    public static final int TORCH = 50;
    public static final int FIRE = 51;
    public static final int MONSTER_SPAWNER = 52;
    public static final int WOOD_STAIRS = 53;
    public static final int WOODEN_STAIRS = 53;
    public static final int OAK_WOOD_STAIRS = 53;
    public static final int OAK_WOODEN_STAIRS = 53;
    public static final int CHEST = 54;
    public static final int REDSTONE_WIRE = 55;
    public static final int DIAMOND_ORE = 56;
    public static final int DIAMOND_BLOCK = 57;
    public static final int CRAFTING_TABLE = 58;
    public static final int WORKBENCH = 58;
    public static final int WHEAT_BLOCK = 59;
    public static final int FARMLAND = 60;
    public static final int FURNACE = 61;
    public static final int BURNING_FURNACE = 62;
    public static final int LIT_FURNACE = 62;
    public static final int SIGN_POST = 63;
    public static final int DOOR_BLOCK = 64;
    public static final int WOODEN_DOOR_BLOCK = 64;
    public static final int WOOD_DOOR_BLOCK = 64;
    public static final int LADDER = 65;
    public static final int RAIL = 66;
    public static final int COBBLE_STAIRS = 67;
    public static final int COBBLESTONE_STAIRS = 67;
    public static final int WALL_SIGN = 68;
    public static final int LEVER = 69;
    public static final int STONE_PRESSURE_PLATE = 70;
    public static final int IRON_DOOR_BLOCK = 71;
    public static final int WOODEN_PRESSURE_PLATE = 72;
    public static final int REDSTONE_ORE = 73;
    public static final int GLOWING_REDSTONE_ORE = 74;
    public static final int LIT_REDSTONE_ORE = 74;

    public static final int REDSTONE_TORCH = 76;

    public static final int SNOW = 78;
    public static final int SNOW_LAYER = 78;
    public static final int ICE = 79;
    public static final int SNOW_BLOCK = 80;
    public static final int CACTUS = 81;
    public static final int CLAY_BLOCK = 82;
    public static final int REEDS = 83;
    public static final int SUGARCANE_BLOCK = 83;

    public static final int FENCE = 85;
    public static final int PUMPKIN = 86;
    public static final int NETHERRACK = 87;
    public static final int SOUL_SAND = 88;
    public static final int GLOWSTONE = 89;
    public static final int GLOWSTONE_BLOCK = 89;
    public static final int NETHER_PORTAL = 90;
    public static final int LIT_PUMPKIN = 91;
    public static final int JACK_O_LANTERN = 91;
    public static final int CAKE_BLOCK = 92;

    public static final int INVISIBLE_BEDROCK = 95;
    public static final int TRAPDOOR = 96;

    public static final int STONE_BRICKS = 98;
    public static final int STONE_BRICK = 98;

    public static final int BROWN_MUSHROOM_BLOCK = 99;
    public static final int RED_MUSHROOM_BLOCK = 100;

    public static final int IRON_BAR = 101;
    public static final int IRON_BARS = 101;
    public static final int GLASS_PANE = 102;
    public static final int GLASS_PANEL = 102;
    public static final int MELON_BLOCK = 103;
    public static final int PUMPKIN_STEM = 104;
    public static final int MELON_STEM = 105;
    public static final int VINE = 106;
    public static final int VINES = 106;
    public static final int FENCE_GATE = 107;
    public static final int FENCE_GATE_OAK = 107;
    public static final int BRICK_STAIRS = 108;
    public static final int STONE_BRICK_STAIRS = 109;
    public static final int MYCELIUM = 110;
    public static final int WATER_LILY = 111;
    public static final int LILY_PAD = 111;
    public static final int NETHER_BRICKS = 112;
    public static final int NETHER_BRICK_BLOCK = 112;
    public static final int NETHER_BRICK_FENCE = 113;
    public static final int NETHER_BRICKS_STAIRS = 114;

    public static final int ENCHANTING_TABLE = 116;
    public static final int ENCHANT_TABLE = 116;
    public static final int ENCHANTMENT_TABLE = 116;
    public static final int BREWING_STAND_BLOCK = 117;
    public static final int BREWING_BLOCK = 117;
    public static final int CAULDRON_BLOCK = 118;
    public static final int END_PORTAL = 119;
    public static final int END_PORTAL_FRAME = 120;
    public static final int END_STONE = 121;

    public static final int REDSTONE_LAMP = 123;
    public static final int LIT_REDSTONE_LAMP = 124;

    public static final int ACTIVATOR_RAIL = 126;

    public static final int SANDSTONE_STAIRS = 128;
    public static final int EMERALD_ORE = 129;

    public static final int EMERALD_BLOCK = 133;
    public static final int SPRUCE_WOOD_STAIRS = 134;
    public static final int SPRUCE_WOODEN_STAIRS = 134;
    public static final int BIRCH_WOOD_STAIRS = 135;
    public static final int BIRCH_WOODEN_STAIRS = 135;
    public static final int JUNGLE_WOOD_STAIRS = 136;
    public static final int JUNGLE_WOODEN_STAIRS = 136;

    public static final int COBBLE_WALL = 139;
    public static final int STONE_WALL = 139;
    public static final int COBBLESTONE_WALL = 139;
    public static final int FLOWER_POT_BLOCK = 140;
    public static final int CARROT_BLOCK = 141;
    public static final int POTATO_BLOCK = 142;

    public static final int SKULL_BLOCK = 144;
    public static final int ANVIL = 145;
    public static final int TRAPPED_CHEST = 146;
    public static final int LIGHT_WEIGHTED_PRESSURE_PLATE = 147;
    public static final int HEAVY_WEIGHTED_PRESSURE_PLATE = 148;
    public static final int UNPOWERED_COMPARATOR = 149;
    public static final int POWERED_COMPARATOR = 150;
    public static final int DAYLIGHT_DETECTOR = 151;
    public static final int REDSTONE_BLOCK = 152;
    public static final int QUARTZ_ORE = 153;

    public static final int HOPPER_BLOCK = 154;
    public static final int QUARTZ_BLOCK = 155;
    public static final int QUARTZ_STAIRS = 156;
    public static final int DOUBLE_WOOD_SLAB = 157;
    public static final int DOUBLE_WOODEN_SLAB = 157;
    public static final int DOUBLE_WOOD_SLABS = 157;
    public static final int DOUBLE_WOODEN_SLABS = 157;
    public static final int WOOD_SLAB = 158;
    public static final int WOODEN_SLAB = 158;
    public static final int WOOD_SLABS = 158;
    public static final int WOODEN_SLABS = 158;
    public static final int STAINED_CLAY = 159;
    public static final int STAINED_HARDENED_CLAY = 159;

    public static final int LEAVES2 = 161;
    public static final int LEAVE2 = 161;
    public static final int WOOD2 = 162;
    public static final int TRUNK2 = 162;
    public static final int LOG2 = 162;
    public static final int ACACIA_WOOD_STAIRS = 163;
    public static final int ACACIA_WOODEN_STAIRS = 163;
    public static final int DARK_OAK_WOOD_STAIRS = 164;
    public static final int DARK_OAK_WOODEN_STAIRS = 164;

    public static final int SLIME_BLOCK = 165;

    public static final int IRON_TRAPDOOR = 167;
    public static final int HAY_BALE = 170;
    public static final int CARPET = 171;
    public static final int HARDENED_CLAY = 172;
    public static final int COAL_BLOCK = 173;
    public static final int PACKED_ICE = 174;
    public static final int DOUBLE_PLANT = 175;

    public static final int DAYLIGHT_DETECTOR_INVERTED = 178;

    public static final int FENCE_GATE_SPRUCE = 183;
    public static final int FENCE_GATE_BIRCH = 184;
    public static final int FENCE_GATE_JUNGLE = 185;
    public static final int FENCE_GATE_DARK_OAK = 186;
    public static final int FENCE_GATE_ACACIA = 187;

    public static final int SPRUCE_DOOR_BLOCK = 193;
    public static final int BIRCH_DOOR_BLOCK = 194;
    public static final int JUNGLE_DOOR_BLOCK = 195;
    public static final int ACACIA_DOOR_BLOCK = 196;
    public static final int DARK_OAK_DOOR_BLOCK = 197;

    public static final int GRASS_PATH = 198;
    public static final int ITEM_FRAME_BLOCK = 199;

    public static final int PODZOL = 243;
    public static final int BEETROOT_BLOCK = 244;
    public static final int STONECUTTER = 245;
    public static final int GLOWING_OBSIDIAN = 246;

    public Level level;

    public static Class[] list = null;
    public static Block[] fullList = null;
    public static int[] light = null;
    public static int[] lightFilter = null;
    public static boolean[] solid = null;
    public static double[] hardness = null;
    public static boolean[] transparent = null;
    public AxisAlignedBB boundingBox = null;
    protected int meta = 0;
    protected int powerLevel = 0;
    protected boolean powerSource = false;

    protected Block(Integer meta) {
        this.meta = (meta != null ? meta : 0);
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        if (list == null) {
            list = new Class[256];
            fullList = new Block[4096];
            light = new int[256];
            lightFilter = new int[256];
            solid = new boolean[256];
            hardness = new double[256];
            transparent = new boolean[256];

            list[AIR] = BlockAir.class;
            list[STONE] = BlockStone.class;
            list[GRASS] = BlockGrass.class;
            list[DIRT] = BlockDirt.class;
            list[COBBLESTONE] = BlockCobblestone.class;
            list[PLANKS] = BlockPlanks.class;
            list[SAPLING] = BlockSapling.class;
            list[BEDROCK] = BlockBedrock.class;
            list[WATER] = BlockWater.class;
            list[STILL_WATER] = BlockWaterStill.class;
            list[LAVA] = BlockLava.class;
            list[STILL_LAVA] = BlockLavaStill.class;
            list[SAND] = BlockSand.class;
            list[GRAVEL] = BlockGravel.class;
            list[GOLD_ORE] = BlockOreGold.class;
            list[IRON_ORE] = BlockOreIron.class;
            list[COAL_ORE] = BlockOreCoal.class;
            list[WOOD] = BlockWood.class;
            list[LEAVES] = BlockLeaves.class;
            list[SPONGE] = BlockSponge.class;
            list[GLASS] = BlockGlass.class;
            list[LAPIS_ORE] = BlockOreLapis.class;
            list[LAPIS_BLOCK] = BlockLapis.class;
            list[SANDSTONE] = BlockSandstone.class;
            list[NOTEBLOCK] = BlockNoteblock.class;
            list[BED_BLOCK] = BlockBed.class;
            list[COBWEB] = BlockCobweb.class;
            list[TALL_GRASS] = BlockTallGrass.class;
            list[DEAD_BUSH] = BlockDeadBush.class;
            list[WOOL] = BlockWool.class;
            list[DANDELION] = BlockDandelion.class;

            list[FLOWER] = BlockFlower.class;

            list[BROWN_MUSHROOM] = BlockMushroomBrown.class;
            list[RED_MUSHROOM] = BlockMushroomRed.class;
            list[GOLD_BLOCK] = BlockGold.class;
            list[IRON_BLOCK] = BlockIron.class;
            list[DOUBLE_SLAB] = BlockDoubleSlab.class;
            list[SLAB] = BlockSlab.class;
            list[BRICKS_BLOCK] = BlockBricks.class;
            list[TNT] = BlockTNT.class;
            list[BOOKSHELF] = BlockBookshelf.class;
            list[MOSS_STONE] = BlockMossStone.class;
            list[OBSIDIAN] = BlockObsidian.class;
            list[TORCH] = BlockTorch.class;
            list[FIRE] = BlockFire.class;
            list[MONSTER_SPAWNER] = BlockMobSpawner.class;
            list[WOOD_STAIRS] = BlockStairsWood.class;
            list[CHEST] = BlockChest.class;
            list[REDSTONE_WIRE] = BlockRedstoneWire.class;
            list[DIAMOND_ORE] = BlockOreDiamond.class;
            list[DIAMOND_BLOCK] = BlockDiamond.class;
            list[WORKBENCH] = BlockWorkbench.class;
            list[WHEAT_BLOCK] = BlockWheat.class;
            list[FARMLAND] = BlockFarmland.class;
            list[FURNACE] = BlockFurnace.class;
            list[BURNING_FURNACE] = BlockFurnaceBurning.class;
            list[SIGN_POST] = BlockSignPost.class;
            list[WOOD_DOOR_BLOCK] = BlockDoorWood.class;
            list[LADDER] = BlockLadder.class;

            list[COBBLESTONE_STAIRS] = BlockStairsCobblestone.class;
            list[WALL_SIGN] = BlockWallSign.class;

            list[IRON_DOOR_BLOCK] = BlockDoorIron.class;
            list[REDSTONE_ORE] = BlockOreRedstone.class;
            list[GLOWING_REDSTONE_ORE] = BlockOreRedstoneGlowing.class;

            list[REDSTONE_TORCH] = BlockRedstoneTorch.class;

            list[SNOW_LAYER] = BlockSnowLayer.class;
            list[ICE] = BlockIce.class;
            list[SNOW_BLOCK] = BlockSnow.class;
            list[CACTUS] = BlockCactus.class;
            list[CLAY_BLOCK] = BlockClay.class;
            list[SUGARCANE_BLOCK] = BlockSugarcane.class;
            list[FENCE] = BlockFence.class;
            list[PUMPKIN] = BlockPumpkin.class;
            list[NETHERRACK] = BlockNetherrack.class;
            list[SOUL_SAND] = BlockSoulSand.class;
            list[GLOWSTONE_BLOCK] = BlockGlowstone.class;
            list[NETHER_PORTAL] = BlockNetherPortal.class;
            list[LIT_PUMPKIN] = BlockPumpkinLit.class;
            list[CAKE_BLOCK] = BlockCake.class;
            list[ITEM_FRAME_BLOCK] = BlockItemFrame.class;
            list[INVISIBLE_BEDROCK] = BlockBedrockInvisible.class;
            list[TRAPDOOR] = BlockTrapdoor.class;

            list[STONE_BRICKS] = BlockBricksStone.class;

            list[RED_MUSHROOM_BLOCK] = BlockHugeMushroomRed.class;
            list[BROWN_MUSHROOM_BLOCK] = BlockHugeMushroomBrown.class;

            list[IRON_BARS] = BlockIronBars.class;
            list[GLASS_PANE] = BlockGlassPane.class;
            list[MELON_BLOCK] = BlockMelon.class;
            list[LIT_REDSTONE_LAMP] = BlockLitRedstoneLamp.class;
            list[PUMPKIN_STEM] = BlockStemPumpkin.class;
            list[MELON_STEM] = BlockStemMelon.class;
            list[VINE] = BlockVine.class;
            list[FENCE_GATE] = BlockFenceGate.class;

            list[BRICK_STAIRS] = BlockStairsBrick.class;
            list[STONE_BRICK_STAIRS] = BlockStairsStoneBrick.class;

            list[MYCELIUM] = BlockMycelium.class;
            list[WATER_LILY] = BlockWaterLily.class;
            list[NETHER_BRICKS] = BlockNetherBrick.class;
            list[NETHER_BRICK_FENCE] = BlockFenceNetherBrick.class;
            list[NETHER_BRICKS_STAIRS] = BlockStairsNetherBrick.class;

            list[ENCHANTING_TABLE] = BlockEnchantingTable.class;

            list[END_PORTAL_FRAME] = BlockEndPortalFrame.class;
            list[END_STONE] = BlockEndStone.class;

            list[SANDSTONE_STAIRS] = BlockStairsSandstone.class;

            list[EMERALD_ORE] = BlockOreEmerald.class;
            list[EMERALD_BLOCK] = BlockEmerald.class;

            list[SPRUCE_WOOD_STAIRS] = BlockStairsSpruce.class;
            list[BIRCH_WOOD_STAIRS] = BlockStairsBirch.class;
            list[JUNGLE_WOOD_STAIRS] = BlockStairsJungle.class;
            list[STONE_WALL] = BlockWall.class;

            list[CARROT_BLOCK] = BlockCarrot.class;
            list[POTATO_BLOCK] = BlockPotato.class;
            list[ANVIL] = BlockAnvil.class;

            list[REDSTONE_BLOCK] = BlockRedstone.class;

            list[QUARTZ_ORE] = BlockOreQuartz.class;
            list[QUARTZ_BLOCK] = BlockQuartz.class;
            list[QUARTZ_STAIRS] = BlockStairsQuartz.class;
            list[DOUBLE_WOOD_SLAB] = BlockDoubleSlabWood.class;
            list[WOOD_SLAB] = BlockSlabWood.class;
            list[STAINED_CLAY] = BlockClayStained.class;

            list[LEAVES2] = BlockLeaves2.class;
            list[WOOD2] = BlockWood2.class;
            list[ACACIA_WOOD_STAIRS] = BlockStairsAcacia.class;
            list[DARK_OAK_WOOD_STAIRS] = BlockStairsDarkOak.class;

            list[SLIME_BLOCK] = BlockSlime.class;

            list[IRON_TRAPDOOR] = BlockTrapdoorIron.class;
            list[HAY_BALE] = BlockHayBale.class;
            list[CARPET] = BlockCarpet.class;
            list[HARDENED_CLAY] = BlockClayHardened.class;
            list[COAL_BLOCK] = BlockCoal.class;
            list[PACKED_ICE] = BlockPackedIce.class;
            list[DOUBLE_PLANT] = BlockDoublePlant.class;

            list[FENCE_GATE_SPRUCE] = BlockFenceGateSpruce.class;
            list[FENCE_GATE_BIRCH] = BlockFenceGateBirch.class;
            list[FENCE_GATE_JUNGLE] = BlockFenceGateJungle.class;
            list[FENCE_GATE_DARK_OAK] = BlockFenceGateDarkOak.class;
            list[FENCE_GATE_ACACIA] = BlockFenceGateAcacia.class;

            list[GRASS_PATH] = BlockGrassPath.class;

            list[PODZOL] = BlockPodzol.class;
            list[BEETROOT_BLOCK] = BlockBeetroot.class;
            list[STONECUTTER] = BlockStonecutter.class;
            list[GLOWING_OBSIDIAN] = BlockObsidianGlowing.class;
            list[BREWING_STAND_BLOCK] = BlockBrewingStand.class;
            list[ACACIA_DOOR_BLOCK] = BlockDoorAcacia.class;
            list[SPRUCE_DOOR_BLOCK] = BlockDoorSpruce.class;
            list[DARK_OAK_DOOR_BLOCK] = BlockDoorDarkOak.class;
            list[BIRCH_DOOR_BLOCK] = BlockDoorBirch.class;
            list[JUNGLE_DOOR_BLOCK] = BlockDoorJungle.class;
            list[DAYLIGHT_DETECTOR] = BlockDaylightDetector.class;
            list[DAYLIGHT_DETECTOR_INVERTED] = BlockDaylightDetectorInverted.class;
            list[RAIL] = BlockRail.class;
            list[ACTIVATOR_RAIL] = BlockRailActivator.class;
            list[DETECTOR_RAIL] = BlockRailDetector.class;
            list[POWERED_RAIL] = BlockRailPowered.class;
            list[FLOWER_POT_BLOCK] = BlockFlowerPot.class;
            list[LEVER] = BlockLever.class;
            list[HEAVY_WEIGHTED_PRESSURE_PLATE] = BlockWeightedPressurePlateHeavy.class;
            list[LIGHT_WEIGHTED_PRESSURE_PLATE] = BlockWeightedPressurePlateLight.class;
            list[REDSTONE_LAMP] = BlockRedstoneLamp.class;
            list[STONE_PRESSURE_PLATE] = BlockPressurePlateStone.class;
            list[WOODEN_PRESSURE_PLATE] = BlockPressurePlateWood.class;

            list[SKULL_BLOCK] = BlockSkull.class;

            for (int id = 0; id < 256; id++) {
                Class c = list[id];
                if (c != null) {
                    Block block;
                    try {
                        block = (Block) c.newInstance();
                        Constructor constructor = c.getDeclaredConstructor(int.class);
                        constructor.setAccessible(true);
                        for (int data = 0; data < 16; ++data) {
                            fullList[(id << 4) | data] = (Block) constructor.newInstance(data);
                        }
                    } catch (Exception e) {
                        Server.getInstance().getLogger().error("Error while registering " + c.getName(), e);
                        return;
                    }

                    solid[id] = block.isSolid();
                    transparent[id] = block.isTransparent();
                    hardness[id] = block.getHardness();
                    light[id] = block.getLightLevel();

                    if (block.isSolid()) {
                        if (block.isTransparent()) {
                            if (block instanceof BlockLiquid || block instanceof BlockIce) {
                                lightFilter[id] = 2;
                            } else {
                                lightFilter[id] = 1;
                            }
                        } else {
                            lightFilter[id] = 15;
                        }
                    } else {
                        lightFilter[id] = 1;
                    }
                } else {
                    lightFilter[id] = 1;
                    for (int data = 0; data < 16; ++data) {
                        fullList[(id) << 4 | data] = new BlockUnknown(id, data);
                    }
                }
            }
        }
    }

    public static Block get(int id) {
        return get(id, 0);
    }

    public static Block get(int id, Integer meta) {
        return get(id, meta, null, null);
    }

    @SuppressWarnings("unchecked")
    public static Block get(int id, Integer meta, BlockVector3 pos, Level level) {
        Block block;
        try {
            Class c = list[id];
            if (c != null) {
                Constructor constructor = c.getDeclaredConstructor(int.class);
                constructor.setAccessible(true);
                block = (Block) constructor.newInstance(meta);
            } else {
                block = new BlockUnknown(id, meta);
            }
        } catch (Exception e) {
            block = new BlockUnknown(id, meta);
        }

        if (pos != null) {
            block.x = pos.x;
            block.y = pos.y;
            block.z = pos.z;
            block.level = level;
        }
        return block;
    }

    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz) {
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        return this.level.setBlock(this, this, true, true);
    }

    public boolean isBreakable(Item item) {
        return true;
    }

    public int tickRate() {
        return 10;
    }

    public boolean onBreak(Item item) {
        return this.level.setBlock(this, new BlockAir(), true, true);
    }

    public int onUpdate(int type) {
        return 0;
    }

    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    public boolean onActivate(Item item, Player player) {
        return false;
    }

    public double getHardness() {
        return 10;
    }

    public double getResistance() {
        return 1;
    }

    public int getBurnChance() {
        return 0;
    }

    public int getBurnAbility() {
        return 0;
    }

    public int getToolType() {
        return ItemTool.TYPE_NONE;
    }

    public double getFrictionFactor() {
        return 0.6;
    }

    public int getLightLevel() {
        return 0;
    }

    public boolean canBePlaced() {
        return true;
    }

    public boolean canBeReplaced() {
        return false;
    }

    public boolean isTransparent() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean canBeFlowedInto() {
        return false;
    }

    public boolean canBeActivated() {
        return false;
    }

    public boolean hasEntityCollision() {
        return false;
    }

    public boolean canPassThrough() {
        return false;
    }

    public BlockColor getColor() {
        return BlockColor.VOID_BLOCK_COLOR;
    }

    public abstract String getName();

    public abstract int getId();

    public void addVelocityToEntity(Entity entity, Vector3 vector) {

    }

    public final int getDamage() {
        return this.meta;
    }

    public final void setDamage(Integer meta) {
        this.meta = (meta == null ? 0 : meta & 0x0f);
    }

    final public void position(BlockVector3 v, Level level) {
        this.x = (int) v.x;
        this.y = (int) v.y;
        this.z = (int) v.z;
        this.level = level;
        this.boundingBox = null;
    }

    public int[][] getDrops(Item item) {
        if (this.getId() < 0 || this.getId() > list.length) { //Unknown blocks
            return new int[0][0];
        } else {
            return new int[][]{
                    {this.getId(), this.getDamage(), 1}
            };
        }
    }

    public double getBreakTime(Item item) {
        double base = this.getHardness() * 1.5;
        if (this.canBeBrokenWith(item)) {
            if (this.getToolType() == ItemTool.TYPE_SHEARS && item.isShears()) {
                base /= 15;
            } else if (
                    (this.getToolType() == ItemTool.TYPE_PICKAXE && item.isPickaxe()) ||
                            (this.getToolType() == ItemTool.TYPE_AXE && item.isAxe()) ||
                            (this.getToolType() == ItemTool.TYPE_SHOVEL && item.isShovel())
                    ) {
                int tier = item.getTier();
                switch (tier) {
                    case ItemTool.TIER_WOODEN:
                        base /= 2;
                        break;
                    case ItemTool.TIER_STONE:
                        base /= 4;
                        break;
                    case ItemTool.TIER_IRON:
                        base /= 6;
                        break;
                    case ItemTool.TIER_DIAMOND:
                        base /= 8;
                        break;
                    case ItemTool.TIER_GOLD:
                        base /= 12;
                        break;
                }
            }
        } else {
            base *= 3.33;
        }

        if (item.isSword()) {
            base *= 0.5;
        }

        return base;
    }

    public boolean canBeBrokenWith(Item item) {
        return this.getHardness() != -1;
    }

    public Block getSide(int side) {
        return this.getSide(side, 1);
    }

    public Block getSide(int side, int step) {
        if (this.isValid()) {
            return this.level.getBlock(super.getSide(side, step));
        }
        return Block.get(Item.AIR, 0, new BlockVector3(this.x, this.y, this.z).getSide(side, step), level);
    }

    @Override
    public String toString() {
        return "Block[" + this.getName() + "] (" + this.getId() + ":" + this.getDamage() + ")";
    }

    public boolean collidesWithBB(AxisAlignedBB bb) {
        AxisAlignedBB bb1 = this.getBoundingBox();
        return bb1 != null && bb.intersectsWith(bb1);
    }

    public void onEntityCollide(Entity entity) {

    }

    public AxisAlignedBB getBoundingBox() {
        if (this.boundingBox == null) {
            this.boundingBox = this.recalculateBoundingBox();
        }
        return this.boundingBox;
    }

    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                this.x,
                this.y,
                this.z,
                this.x + 1,
                this.y + 1,
                this.z + 1
        );
    }

    public MovingObjectPosition calculateIntercept(Vector3 pos1, Vector3 pos2) {
        AxisAlignedBB bb = this.getBoundingBox();
        if (bb == null) {
            return null;
        }

        Vector3 v1 = pos1.getIntermediateWithXValue(pos2, bb.minX);
        Vector3 v2 = pos1.getIntermediateWithXValue(pos2, bb.maxX);
        Vector3 v3 = pos1.getIntermediateWithYValue(pos2, bb.minY);
        Vector3 v4 = pos1.getIntermediateWithYValue(pos2, bb.maxY);
        Vector3 v5 = pos1.getIntermediateWithZValue(pos2, bb.minZ);
        Vector3 v6 = pos1.getIntermediateWithZValue(pos2, bb.maxZ);

        if (v1 != null && !bb.isVectorInYZ(v1)) {
            v1 = null;
        }

        if (v2 != null && !bb.isVectorInYZ(v2)) {
            v2 = null;
        }

        if (v3 != null && !bb.isVectorInXZ(v3)) {
            v3 = null;
        }

        if (v4 != null && !bb.isVectorInXZ(v4)) {
            v4 = null;
        }

        if (v5 != null && !bb.isVectorInXY(v5)) {
            v5 = null;
        }

        if (v6 != null && !bb.isVectorInXY(v6)) {
            v6 = null;
        }

        Vector3 vector = v1;

        if (v2 != null && (vector == null || pos1.distanceSquared(v2) < pos1.distanceSquared(vector))) {
            vector = v2;
        }

        if (v3 != null && (vector == null || pos1.distanceSquared(v3) < pos1.distanceSquared(vector))) {
            vector = v3;
        }

        if (v4 != null && (vector == null || pos1.distanceSquared(v4) < pos1.distanceSquared(vector))) {
            vector = v4;
        }

        if (v5 != null && (vector == null || pos1.distanceSquared(v5) < pos1.distanceSquared(vector))) {
            vector = v5;
        }

        if (v6 != null && (vector == null || pos1.distanceSquared(v6) < pos1.distanceSquared(vector))) {
            vector = v6;
        }

        if (vector == null) {
            return null;
        }

        int f = -1;

        if (vector == v1) {
            f = 4;
        } else if (vector == v2) {
            f = 5;
        } else if (vector == v3) {
            f = 0;
        } else if (vector == v4) {
            f = 1;
        } else if (vector == v5) {
            f = 2;
        } else if (vector == v6) {
            f = 3;
        }

        return MovingObjectPosition.fromBlock((int) this.x, (int) this.y, (int) this.z, f, vector.add(this.x, this.y, this.z));

    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) throws Exception {
        if (this.level != null) {
            this.level.getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
        }
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) throws Exception {
        if (this.level != null) {
            return this.level.getBlockMetadata().getMetadata(this, metadataKey);

        }
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) throws Exception {
        return this.level != null && this.level.getBlockMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) throws Exception {
        if (this.level != null) {
            this.level.getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
        }
    }

    public Block clone() {
        return (Block) super.clone();
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int getPowerLevel(int side) {
        return this.getSide(side).getPowerLevel();
    }

    public boolean isNeighborPowered() {
        return this.getNeighborPowerLevel() > 0;
    }

    public int getNeighborPowerLevel() {
        int energy = 0;
        int tempLevel;
        tempLevel = this.getSide(SIDE_DOWN).getPowerLevel();
        energy = tempLevel > energy ? tempLevel : energy;
        tempLevel = this.getSide(SIDE_UP).getPowerLevel();
        energy = tempLevel > energy ? tempLevel : energy;
        for (int side : new int[]{Vector3.SIDE_NORTH, Vector3.SIDE_SOUTH, Vector3.SIDE_WEST, Vector3.SIDE_EAST}) {
            tempLevel = this.getSide(side).getPowerLevel();
            energy = tempLevel > energy ? tempLevel : energy;
        }
        return energy;
    }

    public boolean isPowered() {
        return this.powerLevel > 0;
    }

    public boolean isPowerSource() {
        return this.powerSource;
    }

    public void setPowerSource(boolean isSource) {
        this.powerSource = isSource;
    }

    public String getLocationHash() {
        String str = "";
        str = String.valueOf((int) this.x);
        str += ":";
        str += String.valueOf((int) this.y);
        str += ":";
        str += String.valueOf((int) this.z);
        return str;
    }

    public int getDropExp() {
        return 0;
    }

    public boolean isValid() {
        return this.level != null;
    }
}

package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;

import java.util.Random;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockFire extends BlockFlowable {

    public BlockFire() {
        this(0);
    }

    public BlockFire(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return FIRE;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public String getName() {
        return "Fire Block";
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.hasEffect(Effect.FIRE_RESISTANCE)) {
            EntityDamageByBlockEvent ev = new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.CAUSE_FIRE, 1);
            entity.attack(ev);
        }

        EntityCombustByBlockEvent ev = new EntityCombustByBlockEvent(this, entity, 8);
        if (entity instanceof EntityArrow) {
            ev.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(ev);
        if (!ev.isCancelled() && entity instanceof Player && !((Player) entity).isCreative()) {
            entity.setOnFire(ev.getDuration());
        }
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[0][0];
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_RANDOM) {
            if (!this.isBlockTopFacingSurfaceSolid(this.getSide(Vector3.SIDE_DOWN)) && !this.canNeighborBurn()) {
                this.level.setBlock(this, new BlockAir(), true);
            }

            return Level.BLOCK_UPDATE_NORMAL;
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            boolean forever = this.getSide(Vector3.SIDE_DOWN).getId() == Block.NETHERRACK;

            Random random = this.level.rand;

            //TODO: END

            if (!this.isBlockTopFacingSurfaceSolid(this.getSide(Vector3.SIDE_DOWN)) && !this.canNeighborBurn()) {
                this.level.setBlock(this, new BlockAir(), true);
            }

            if (!forever && this.level.isRaining() &&
                    (this.level.canBlockSeeSky(this) ||
                            this.level.canBlockSeeSky(this.getSide(SIDE_EAST)) ||
                            this.level.canBlockSeeSky(this.getSide(SIDE_WEST)) ||
                            this.level.canBlockSeeSky(this.getSide(SIDE_SOUTH)) ||
                            this.level.canBlockSeeSky(this.getSide(SIDE_NORTH)))
                    ) {
                this.level.setBlock(this, new BlockAir(), true);
            } else {
                int meta = this.getDamage();

                if (meta < 15) {
                    this.meta = meta + random.nextInt(3);
                    this.level.setBlock(this, this, true);
                }

                this.level.scheduleUpdate(this, this.tickRate() + random.nextInt(10));

                if (!forever && !this.canNeighborBurn()) {
                    if (!this.isBlockTopFacingSurfaceSolid(this.getSide(Vector3.SIDE_DOWN)) || meta > 3) {
                        this.level.setBlock(this, new BlockAir(), true);
                    }
                } else if (!forever && !(this.getSide(Vector3.SIDE_DOWN).getBurnAbility() > 0) && meta == 15 && random.nextInt(4) == 0) {
                    this.level.setBlock(this, new BlockAir(), true);
                } else {
                    int o = 0;

                    //TODO: decrease the o if the rainfall values are high

                    this.tryToCatchBlockOnFire(this.getSide(SIDE_EAST), 300 + o, meta);
                    this.tryToCatchBlockOnFire(this.getSide(SIDE_WEST), 300 + o, meta);
                    this.tryToCatchBlockOnFire(this.getSide(SIDE_DOWN), 250 + o, meta);
                    this.tryToCatchBlockOnFire(this.getSide(SIDE_UP), 250 + o, meta);
                    this.tryToCatchBlockOnFire(this.getSide(SIDE_SOUTH), 300 + o, meta);
                    this.tryToCatchBlockOnFire(this.getSide(SIDE_NORTH), 300 + o, meta);

                    BlockVector3 updateBlock = new BlockVector3();
                    for (int x = (this.x - 1); x <= (this.x + 1); ++x) {
                        updateBlock.x = x;
                        for (int z = (this.z - 1); z <= (this.z + 1); ++z) {
                            updateBlock.z = z;
                            for (int y = (this.y - 1); y <= (this.y + 4); ++y) {
                                updateBlock.y = y;
                                if (x != this.x || y != this.y || z != this.z) {
                                    int k = 100;

                                    if (y > this.y + 1) {
                                        k += (y - (this.y + 1)) * 100;
                                    }

                                    int chance = this.getChanceOfNeighborsEncouragingFire(this.level.getBlock(updateBlock));

                                    if (chance > 0) {
                                        int t = (chance + 40 + this.level.getServer().getDifficulty() * 7) / (meta + 30);

                                        //TODO: decrease the t if the rainfall values are high

                                        if (t > 0 && random.nextInt(k) <= t) {
                                            int damage = meta + random.nextInt(5) / 4;

                                            if (damage > 15) {
                                                damage = 15;
                                            }

                                            this.level.setBlock(updateBlock, new BlockFire(damage), true);
                                            this.level.scheduleUpdate(updateBlock, this.tickRate());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    private void tryToCatchBlockOnFire(Block block, int bound, int damage) {
        int burnAbility = block.getBurnAbility();

        Random random = this.level.rand;

        if (random.nextInt(bound) < burnAbility) {

            if (random.nextInt(damage + 10) < 5) {
                int meta = damage + random.nextInt(5) / 4;

                if (meta > 15) {
                    meta = 15;
                }

                this.level.setBlock(block, new BlockFire(meta), true);
                this.level.scheduleUpdate(block, this.tickRate());
            } else {
                BlockBurnEvent ev = new BlockBurnEvent(block);
                this.level.getServer().getPluginManager().callEvent(ev);

                if (!ev.isCancelled()) {
                    this.level.setBlock(block, new BlockAir(), true);
                }
            }

            if (block instanceof BlockTNT) {
                ((BlockTNT) block).prime();
            }
        }
    }

    private int getChanceOfNeighborsEncouragingFire(Block block) {
        if (block.getId() != AIR) {
            return 0;
        } else {
            int chance = 0;
            chance = Math.max(chance, block.getSide(SIDE_EAST).getBurnChance());
            chance = Math.max(chance, block.getSide(SIDE_WEST).getBurnChance());
            chance = Math.max(chance, block.getSide(SIDE_DOWN).getBurnChance());
            chance = Math.max(chance, block.getSide(SIDE_UP).getBurnChance());
            chance = Math.max(chance, block.getSide(SIDE_SOUTH).getBurnChance());
            chance = Math.max(chance, block.getSide(SIDE_NORTH).getBurnChance());
            return chance;
        }
    }

    public boolean canNeighborBurn() {
        for (int face = 0; face <= 5; face++) {
            if (this.getSide(face).getBurnChance() > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isBlockTopFacingSurfaceSolid(Block block) {
        if (block != null) {
            if (block.isSolid()) {
                return true;
            } else {
                if (block instanceof BlockStairs &&
                        (block.getDamage() & 4) == 4) {

                    return true;
                } else if (block instanceof BlockSlab &&
                        (block.getDamage() & 8) == 8) {

                    return true;
                } else if (block instanceof BlockSnowLayer &&
                        (block.getDamage() & 7) == 7) {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int tickRate() {
        return 30;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}

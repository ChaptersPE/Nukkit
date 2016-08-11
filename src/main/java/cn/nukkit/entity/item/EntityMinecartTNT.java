package cn.nukkit.entity.item;

import cn.nukkit.block.Block;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/1/30.
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public class EntityMinecartTNT extends EntityMinecartEmpty {

    // TODO: 2016/1/30 NETWORK_ID

    public EntityMinecartTNT(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public boolean onUpdate(int currentTick) {
        Block downSide = this.level.getBlock(new BlockVector3(this.getLocation().floor()));
        if (downSide.getId() == Block.ACTIVATOR_RAIL && downSide.isPowered()) {
            explode();
            kill();
        }
        return true;
    }

    public void explode() {
        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, 4);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, event.getForce(), this);
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
    }

}

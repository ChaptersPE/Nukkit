package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * author: ItsLucas
 * Nukkit Project
 */

public class ObjectTallGrass {
    public static void growGrass(ChunkManager level, BlockVector3 pos, NukkitRandom random, int count, int radius) {
        int[][] arr = {
                {Block.DANDELION, 0},
                {Block.POPPY, 0},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1}
        };
        int arrC = arr.length - 1;
        for (int c = 0; c < count; c++) {
            int x = random.nextRange(pos.x - radius, pos.x + radius);
            int z = random.nextRange(pos.z - radius, pos.z + radius);

            if (level.getBlockIdAt(x, pos.y + 1, z) == Block.AIR && level.getBlockIdAt(x, pos.y, z) == Block.GRASS) {
                int[] t = arr[random.nextRange(0, arrC)];
                level.setBlockIdAt(x, pos.y + 1, z, t[0]);
                level.setBlockDataAt(x, pos.y + 1, z, t[1]);
            }
        }
    }
}

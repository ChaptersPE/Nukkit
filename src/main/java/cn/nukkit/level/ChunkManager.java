package cn.nukkit.level;

import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.IntVector2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface ChunkManager {

    int getBlockIdAt(int x, int y, int z);

    void setBlockIdAt(int x, int y, int z, int id);

    int getBlockDataAt(int x, int y, int z);

    void setBlockDataAt(int x, int y, int z, int data);

    BaseFullChunk getChunk(int chunkX, int chunkZ);

    BaseFullChunk getChunk(int chunkX, int chunkZ, boolean create);

    BaseFullChunk getChunk(IntVector2 pos);

    void setChunk(IntVector2 pos);

    void setChunk(IntVector2 pos, BaseFullChunk chunk);

    long getSeed();
}

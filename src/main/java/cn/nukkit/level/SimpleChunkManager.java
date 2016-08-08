package cn.nukkit.level;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.IntVector2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SimpleChunkManager implements ChunkManager {
    protected Map<IntVector2, FullChunk> chunks = new ConcurrentHashMap<>();

    protected final long seed;

    public SimpleChunkManager(long seed) {
        this.seed = seed;
    }

    @Override
    public int getBlockIdAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockId(x & 0xf, y & 0x7f, z & 0xf);
        }
        return 0;
    }

    @Override
    public void setBlockIdAt(int x, int y, int z, int id) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockId(x & 0xf, y & 0x7f, z & 0xf, id);
        }
    }

    @Override
    public int getBlockDataAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x & 0xf, y & 0x7f, z & 0xf);
        }
        return 0;
    }

    @Override
    public void setBlockDataAt(int x, int y, int z, int data) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockData(x & 0xf, y & 0x7f, z & 0xf, data);
        }
    }

    @Override
    public BaseFullChunk getChunk(int chunkX, int chunkZ) {
        return this.getChunk(new IntVector2(chunkX, chunkZ));
    }

    @Override
    public BaseFullChunk getChunk(int chunkX, int chunkZ, boolean create) {
        return this.getChunk(new IntVector2(chunkX, chunkZ));
    }

    @Override
    public BaseFullChunk getChunk(IntVector2 pos) {
        return this.chunks.containsKey(pos) ? (BaseFullChunk) this.chunks.get(pos) : null;
    }

    @Override
    public void setChunk(IntVector2 pos) {
        this.setChunk(pos, null);
    }

    @Override
    public void setChunk(IntVector2 pos, BaseFullChunk chunk) {
        if (chunk == null) {
            this.chunks.remove(pos);
            return;
        }
        this.chunks.put(pos, chunk);
    }

    public void cleanChunks() {
        this.chunks = new HashMap<>();
    }

    @Override
    public long getSeed() {
        return seed;
    }
}

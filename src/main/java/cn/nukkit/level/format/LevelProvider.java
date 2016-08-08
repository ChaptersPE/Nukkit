package cn.nukkit.level.format;

import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.IntVector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;

import java.util.Map;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface LevelProvider {
    byte ORDER_YZX = 0;
    byte ORDER_ZXY = 1;

    AsyncTask requestChunkTask(IntVector2 pos);

    String getPath();

    String getGenerator();

    Map<String, Object> getGeneratorOptions();

    BaseFullChunk getChunk(IntVector2 pos);

    BaseFullChunk getChunk(IntVector2 pos, boolean create);

    void saveChunks();

    void saveChunk(IntVector2 pos);

    void unloadChunks();

    boolean loadChunk(IntVector2 pos);

    boolean loadChunk(IntVector2 pos, boolean create);

    boolean unloadChunk(IntVector2 pos);

    boolean unloadChunk(IntVector2 pos, boolean safe);

    boolean isChunkGenerated(IntVector2 pos);

    boolean isChunkPopulated(IntVector2 pos);

    boolean isChunkLoaded(IntVector2 pos);

    void setChunk(IntVector2 pos, FullChunk chunk);

    String getName();

    boolean isRaining();

    void setRaining(boolean raining);

    int getRainTime();

    void setRainTime(int rainTime);

    boolean isThundering();

    void setThundering(boolean thundering);

    int getThunderTime();

    void setThunderTime(int thunderTime);

    long getCurrentTick();

    void setCurrentTick(long currentTick);

    long getTime();

    void setTime(long value);

    long getSeed();

    void setSeed(long value);

    Vector3 getSpawn();

    void setSpawn(Vector3 pos);

    Map<IntVector2, ? extends FullChunk> getLoadedChunks();

    void doGarbageCollection();

    Level getLevel();

    void close();

    void saveLevelData();

    void updateLevelName(String name);
}

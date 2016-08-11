package cn.nukkit.level.format.leveldb;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.leveldb.key.FlagsKey;
import cn.nukkit.level.format.leveldb.key.TerrainKey;
import cn.nukkit.level.format.leveldb.key.VersionKey;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.IntVector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.*;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.*;
import java.nio.ByteOrder;
import java.util.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class LevelDB implements LevelProvider {

    protected Map<IntVector2, Chunk> chunks = new HashMap<>();

    protected DB db;

    protected Level level;

    protected final String path;

    protected CompoundTag levelData;

    public LevelDB(Level level, String path) {
        this.level = level;
        this.path = path;
        File file_path = new File(this.path);
        if (!file_path.exists()) {
            file_path.mkdirs();
        }

        try (FileInputStream stream = new FileInputStream(this.getPath() + "level.dat")) {
            stream.skip(8);
            CompoundTag levelData = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
            if (levelData != null) {
                this.levelData = levelData;
            } else {
                throw new IOException("LevelData can not be null");
            }
        } catch (IOException e) {
            throw new LevelException("Invalid level.dat");
        }

        if (!this.levelData.contains("generatorName")) {
            this.levelData.putString("generatorName", Generator.getGenerator("DEFAULT").getSimpleName().toLowerCase());
        }

        if (!this.levelData.contains("generatorOptions")) {
            this.levelData.putString("generatorOptions", "");
        }

        try {
            this.db = Iq80DBFactory.factory.open(new File(this.getPath() + "/db"), new Options().createIfMissing(true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProviderName() {
        return "leveldb";
    }

    public static byte getProviderOrder() {
        return ORDER_ZXY;
    }

    public static boolean usesChunkSection() {
        return false;
    }

    public static boolean isValid(String path) {
        return new File(path + "/level.dat").exists() && new File(path + "/db").isDirectory();
    }

    public static void generate(String path, String name, long seed, Class<? extends Generator> generator) throws IOException {
        generate(path, name, seed, generator, new HashMap<>());
    }

    public static void generate(String path, String name, long seed, Class<? extends Generator> generator, Map<String, String> options) throws IOException {
        if (!new File(path + "/db").exists()) {
            new File(path + "/db").mkdirs();
        }

        CompoundTag levelData = new CompoundTag("")
                .putLong("currentTick", 0)
                .putInt("DayCycleStopTime", -1)
                .putInt("GameType", 0)
                .putInt("Generator", Generator.getGeneratorType(generator))
                .putBoolean("hasBeenLoadedInCreative", false)
                .putLong("LastPlayed", System.currentTimeMillis() / 1000)
                .putString("LevelName", name)
                .putFloat("lightningLevel", 0)
                .putInt("lightningTime", new Random().nextInt())
                .putInt("limitedWorldOriginX", 128)
                .putInt("limitedWorldOriginY", 70)
                .putInt("limitedWorldOriginZ", 128)
                .putInt("Platform", 0)
                .putFloat("rainLevel", 0)
                .putInt("rainTime", new Random().nextInt())
                .putLong("RandomSeed", seed)
                .putByte("spawnMobs", 0)
                .putInt("SpawnX", 128)
                .putInt("SpawnY", 70)
                .putInt("SpawnZ", 128)
                .putInt("storageVersion", 4)
                .putLong("Time", 0)
                .putLong("worldStartCount", ((long) Integer.MAX_VALUE) & 0xffffffffL);

        byte[] data = NBTIO.write(levelData, ByteOrder.LITTLE_ENDIAN);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Binary.writeLInt(3));
        outputStream.write(Binary.writeLInt(data.length));
        outputStream.write(data);

        Utils.writeFile(path + "level.dat", new ByteArrayInputStream(outputStream.toByteArray()));

        DB db = Iq80DBFactory.factory.open(new File(path + "/db"), new Options().createIfMissing(true));
        db.close();
    }

    @Override
    public void saveLevelData() {
        try {
            byte[] data = NBTIO.write(levelData, ByteOrder.LITTLE_ENDIAN);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(Binary.writeLInt(3));
            outputStream.write(Binary.writeLInt(data.length));
            outputStream.write(data);

            Utils.writeFile(path + "level.dat", new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AsyncTask requestChunkTask(IntVector2 pos) {
        FullChunk chunk = this.getChunk(pos, false);
        if (chunk == null) {
            throw new ChunkException("Invalid Chunk sent");
        }

        byte[] tiles = new byte[0];

        if (!chunk.getBlockEntities().isEmpty()) {
            List<CompoundTag> tagList = new ArrayList<>();

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof BlockEntitySpawnable) {
                    tagList.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound());
                }
            }

            try {
                tiles = NBTIO.write(tagList, ByteOrder.LITTLE_ENDIAN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        BinaryStream extraData = new BinaryStream();
        extraData.putLInt(chunk.getBlockExtraDataArray().size());
        for (Integer key : chunk.getBlockExtraDataArray().values()) {
            extraData.putLInt(key);
            extraData.putLShort(chunk.getBlockExtraDataArray().get(key));
        }

        BinaryStream stream = new BinaryStream();
        stream.put(chunk.getBlockIdArray());
        stream.put(chunk.getBlockDataArray());
        stream.put(chunk.getBlockSkyLightArray());
        stream.put(chunk.getBlockLightArray());
        for (int height : chunk.getHeightMapArray()) {
            stream.putByte((byte) (height & 0xff));
        }
        for (int color : chunk.getBiomeColorArray()) {
            stream.put(Binary.writeInt(color));
        }
        stream.put(extraData.getBuffer());
        stream.put(tiles);

        this.getLevel().chunkRequestCallback(pos, stream.getBuffer());

        return null;
    }

    @Override
    public void unloadChunks() {
        for (Chunk chunk : new ArrayList<>(this.chunks.values())) {
            this.unloadChunk(chunk.getVector2(), false);
        }
        this.chunks = new HashMap<>();
    }

    @Override
    public String getGenerator() {
        return this.levelData.getString("generatorName");
    }

    @Override
    public Map<String, Object> getGeneratorOptions() {
        return new HashMap<String, Object>() {
            {
                put("preset", levelData.getString("generatorOptions"));
            }
        };
    }

    @Override
    public Map<IntVector2, Chunk> getLoadedChunks() {
        return this.chunks;
    }

    @Override
    public boolean isChunkLoaded(IntVector2 pos) {
        return this.chunks.containsKey(pos);
    }

    @Override
    public void saveChunks() {
        for (Chunk chunk : this.chunks.values()) {
            this.saveChunk(chunk.getVector2());
        }
    }

    @Override
    public boolean loadChunk(IntVector2 pos) {
        return this.loadChunk(pos, false);
    }

    @Override
    public boolean loadChunk(IntVector2 pos, boolean create) {
        if (this.chunks.containsKey(pos)) {
            return true;
        }

        this.level.timings.syncChunkLoadDataTimer.startTiming();
        Chunk chunk = this.readChunk(pos.x, pos.z);
        if (chunk == null && create) {
            chunk = Chunk.getEmptyChunk(pos.x, pos.z, this);
        }
        this.level.timings.syncChunkLoadDataTimer.stopTiming();
        if (chunk != null) {
            this.chunks.put(pos, chunk);
            return true;
        }

        return false;
    }

    private Chunk readChunk(int chunkX, int chunkZ) {
        byte[] data;
        if (!this.chunkExists(chunkX, chunkZ) || (data = this.db.get(TerrainKey.create(chunkX, chunkZ).toArray())) == null) {
            return null;
        }

        byte[] flags = this.db.get(FlagsKey.create(chunkX, chunkZ).toArray());
        if (flags == null) {
            flags = new byte[]{0x03};
        }

        return Chunk.fromBinary(
                Binary.appendBytes(
                        Binary.writeLInt(chunkX),
                        Binary.writeLInt(chunkZ),
                        data,
                        flags)
                , this);
    }

    private void writeChunk(Chunk chunk) {
        byte[] binary = chunk.toBinary(true);
        this.db.put(TerrainKey.create(chunk.getX(), chunk.getZ()).toArray(), Binary.subBytes(binary, 8, binary.length - 1));
        this.db.put(FlagsKey.create(chunk.getX(), chunk.getZ()).toArray(), Binary.subBytes(binary, binary.length - 1));
        this.db.put(VersionKey.create(chunk.getX(), chunk.getZ()).toArray(), new byte[]{0x02});
    }

    @Override
    public boolean unloadChunk(IntVector2 pos) {
        return this.unloadChunk(pos, true);
    }

    @Override
    public boolean unloadChunk(IntVector2 pos, boolean safe) {
        Chunk chunk = this.chunks.containsKey(pos) ? this.chunks.get(pos) : null;
        if (chunk != null && chunk.unload(false, safe)) {
            this.chunks.remove(pos);
            return true;
        }

        return false;
    }

    @Override
    public void saveChunk(IntVector2 pos) {
        if (this.isChunkLoaded(pos)) {
            this.writeChunk(this.getChunk(pos));
        }
    }

    @Override
    public Chunk getChunk(IntVector2 pos) {
        return this.getChunk(pos, false);
    }

    @Override
    public Chunk getChunk(IntVector2 pos, boolean create) {
        if (this.chunks.containsKey(pos)) {
            return this.chunks.get(pos);
        } else {
            this.loadChunk(pos, create);
            return this.chunks.containsKey(pos) ? this.chunks.get(pos) : null;
        }
    }

    public DB getDatabase() {
        return db;
    }

    @Override
    public void setChunk(IntVector2 pos, FullChunk chunk) {
        if (!(chunk instanceof Chunk)) {
            throw new ChunkException("Invalid Chunk class");
        }
        chunk.setProvider(this);

        chunk.setX(pos.x);
        chunk.setZ(pos.z);

        if (this.chunks.containsKey(pos) && !this.chunks.get(pos).equals(chunk)) {
            this.unloadChunk(pos, false);
        }

        this.chunks.put(pos, (Chunk) chunk);
    }

    public static ChunkSection createChunkSection(int Y) {
        return null;
    }

    private boolean chunkExists(int chunkX, int chunkZ) {
        return this.db.get(VersionKey.create(chunkX, chunkZ).toArray()) != null;
    }

    @Override
    public boolean isChunkGenerated(IntVector2 pos) {
        return this.chunkExists(pos.x, pos.z) && this.getChunk(pos, false) != null;
    }

    @Override
    public boolean isChunkPopulated(IntVector2 pos) {
        return this.getChunk(pos) != null;
    }

    @Override
    public void close() {
        this.unloadChunks();
        try {
            this.db.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.level = null;
    }

    @Override
    public String getPath() {
        return path;
    }

    public Server getServer() {
        return this.level.getServer();
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return this.levelData.getString("LevelName");
    }

    @Override
    public boolean isRaining() {
        return this.levelData.getFloat("rainLevel") > 0;
    }

    @Override
    public void setRaining(boolean raining) {
        this.levelData.putFloat("rainLevel", raining ? 1.0f : 0);
    }

    @Override
    public int getRainTime() {
        return this.levelData.getInt("rainTime");
    }

    @Override
    public void setRainTime(int rainTime) {
        this.levelData.putInt("rainTime", rainTime);
    }

    @Override
    public boolean isThundering() {
        return this.levelData.getFloat("lightningLevel") > 0;
    }

    @Override
    public void setThundering(boolean thundering) {
        this.levelData.putFloat("lightningLevel", thundering ? 1.0f : 0);
    }

    @Override
    public int getThunderTime() {
        return this.levelData.getInt("lightningTime");
    }

    @Override
    public void setThunderTime(int thunderTime) {
        this.levelData.putInt("lightningTime", thunderTime);
    }

    @Override
    public long getCurrentTick() {
        return this.levelData.getLong("currentTick");
    }

    @Override
    public void setCurrentTick(long currentTick) {
        this.levelData.putLong("currentTick", currentTick);
    }

    @Override
    public long getTime() {
        return this.levelData.getLong("Time");
    }

    @Override
    public void setTime(long value) {
        this.levelData.putLong("Time", value);
    }

    @Override
    public long getSeed() {
        return this.levelData.getLong("RandomSeed");
    }

    @Override
    public void setSeed(long value) {
        this.levelData.putLong("RandomSeed", value);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(this.levelData.getInt("SpawnX"), this.levelData.getInt("SpawnY"), this.levelData.getInt("SpawnZ"));
    }

    @Override
    public void setSpawn(Vector3 pos) {
        this.levelData.putInt("SpawnX", (int) pos.x);
        this.levelData.putInt("SpawnY", (int) pos.y);
        this.levelData.putInt("SpawnZ", (int) pos.z);
    }

    @Override
    public void doGarbageCollection() {

    }

    public CompoundTag getLevelData() {
        return levelData;
    }

    public void updateLevelName(String name) {
        if (!this.getName().equals(name)) {
            this.levelData.putString("LevelName", name);
        }
    }
}

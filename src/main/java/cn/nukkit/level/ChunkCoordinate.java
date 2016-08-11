package cn.nukkit.level;

import cn.nukkit.math.IntVector2;

public class ChunkCoordinate extends IntVector2 {

	public static final int CHUNK_X_SIZE = 16;
	public static final int CHUNK_Y_SIZE = 256;
	public static final int CHUNK_Z_SIZE = 16;
	private static final int CHUNK_HALF_X_SIZE = CHUNK_X_SIZE / 2;
	private static final int CHUNK_HALF_Z_SIZE = CHUNK_Z_SIZE / 2;

	public ChunkCoordinate() {

	}

	public ChunkCoordinate(int x, int z) {
		super(x, z);
	}

	public int getBlockXCenter() {
		return x * CHUNK_X_SIZE + CHUNK_HALF_X_SIZE;
	}

	public int getBlockZCenter() {
		return z * CHUNK_Z_SIZE + CHUNK_HALF_Z_SIZE;
	}

	public int getBlockX()
	{
		return x * CHUNK_X_SIZE;
	}

	public int getBlockZ()
	{
		return z * CHUNK_Z_SIZE;
	}
}
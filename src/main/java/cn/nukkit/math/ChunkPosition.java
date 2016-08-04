package cn.nukkit.math;

final public class ChunkPosition {
	public final int x;
	public final int z;

	public ChunkPosition() {
		this(0, 0);
	}

	public ChunkPosition(int x) {
		this(x, 0);
	}

	public ChunkPosition(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	@Override
	public String toString() {
		return "ChunkPosition(x=" + this.x + ",z=" + this.z + ")";
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null) return false;
		if (ob == this) return true;

		if (!(ob instanceof ChunkPosition)) return false;

		return this.x == ((ChunkPosition)ob).x && this.z == ((ChunkPosition)ob).z;
	}

	@Override
	public int hashCode() {
		return x * 31 + z;
	}
}
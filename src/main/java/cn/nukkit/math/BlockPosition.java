package cn.nukkit.math;

final public class BlockPosition {
	private final int x;
	private final int y;
	private final int z;

	public BlockPosition() {
		this(0, 0, 0);
	}

	public BlockPosition(int x) {
		this(x, 0, 0);
	}

	public BlockPosition(int x, int y) {
		this(x, y, 0);
	}

	public BlockPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	@Override
	public String toString() {
		return "BlockPosition(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ")";
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null) return false;
		if (ob == this) return true;

		if (!(ob instanceof BlockPosition)) return false;

		return this.x == ((BlockPosition)ob).x && this.z == ((BlockPosition)ob).z;
	}

	@Override
	public int hashCode() {
		return x + (y << 8) + (z << 16);
	}
}
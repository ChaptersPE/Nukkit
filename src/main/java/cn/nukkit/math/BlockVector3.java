package cn.nukkit.math;

final public class BlockVector3 {
	public static final int SIDE_DOWN = 0;
	public static final int SIDE_UP = 1;
	public static final int SIDE_NORTH = 2;
	public static final int SIDE_SOUTH = 3;
	public static final int SIDE_WEST = 4;
	public static final int SIDE_EAST = 5;

	public int x;
	public int y;
	public int z;

	public BlockVector3(double x) {
		this(x, 0, 0);
	}

	public BlockVector3(double x, double y) {
		this(x, y, 0);
	}

	public BlockVector3(double x, double y, double z) {
		this.x = (int)x;
		this.y = (int)y;
		this.z = (int)z;
	}

	public BlockVector3() {
		this(0, 0, 0);
	}

	public BlockVector3(int x) {
		this(x, 0, 0);
	}

	public BlockVector3(int x, int y) {
		this(x, y, 0);
	}

	public BlockVector3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockVector3(Vector3 pos) {
		this(pos.x, pos.y, pos.z);
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

	public BlockVector3 getSide(int side) {
		return this.getSide(side, 1);
	}

	public BlockVector3 getSide(int side, int step) {
		switch (side) {
			case BlockVector3.SIDE_DOWN:
				return new BlockVector3(this.x, this.y - step, this.z);
			case BlockVector3.SIDE_UP:
				return new BlockVector3(this.x, this.y + step, this.z);
			case BlockVector3.SIDE_NORTH:
				return new BlockVector3(this.x, this.y, this.z - step);
			case BlockVector3.SIDE_SOUTH:
				return new BlockVector3(this.x, this.y, this.z + step);
			case BlockVector3.SIDE_WEST:
				return new BlockVector3(this.x - step, this.y, this.z);
			case BlockVector3.SIDE_EAST:
				return new BlockVector3(this.x + step, this.y, this.z);
			default:
				return this;
		}
	}

	public static int getOppositeSide(int side) {
		switch (side) {
			case BlockVector3.SIDE_DOWN:
				return BlockVector3.SIDE_UP;
			case BlockVector3.SIDE_UP:
				return BlockVector3.SIDE_DOWN;
			case BlockVector3.SIDE_NORTH:
				return BlockVector3.SIDE_SOUTH;
			case BlockVector3.SIDE_SOUTH:
				return BlockVector3.SIDE_NORTH;
			case BlockVector3.SIDE_WEST:
				return BlockVector3.SIDE_EAST;
			case BlockVector3.SIDE_EAST:
				return BlockVector3.SIDE_WEST;
			default:
				return -1;
		}
	}

	@Override
	public String toString() {
		return "BlockVector3(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ")";
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null) return false;
		if (ob == this) return true;

		if (!(ob instanceof BlockVector3)) return false;

		return this.x == ((BlockVector3)ob).x && this.z == ((BlockVector3)ob).z;
	}

	@Override
	public int hashCode() {
		return x + (y << 8) + (z << 16);
	}
}
package cn.nukkit.math;

public class IntVector2 {
	public int x;
	public int z;

	public IntVector2() {
		this(0, 0);
	}

	public IntVector2(int x) {
		this(x, 0);
	}

	public IntVector2(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public IntVector2 setComponents(int x, int z) {
		this.x = x;
		this.z = z;
		return this;
	}

	@Override
	public String toString() {
		return "IntVector2(x=" + this.x + ",z=" + this.z + ")";
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null) return false;
		if (ob == this) return true;

		if (!(ob instanceof IntVector2)) return false;

		return this.x == ((IntVector2)ob).x && this.z == ((IntVector2)ob).z;
	}

	@Override
	public int hashCode() {
		return x * 31 + z;
	}
}
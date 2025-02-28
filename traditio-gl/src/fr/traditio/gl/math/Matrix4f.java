package fr.traditio.gl.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

public final class Matrix4f {

	private static final int SIZE = 16;

	private final float[] m;

	public Matrix4f() {
		this.m = new float[SIZE];
		identity();
	}

	public Matrix4f(Matrix4f matrix) {
		this.m = new float[SIZE];
		System.arraycopy(matrix.m, 0, m, 0, SIZE);
	}

	public Matrix4f identity() {
		set(0, 0, 1);
		set(0, 1, 0);
		set(0, 2, 0);
		set(0, 3, 0);

		set(1, 0, 0);
		set(1, 1, 1);
		set(1, 2, 0);
		set(1, 3, 0);

		set(2, 0, 0);
		set(2, 1, 0);
		set(2, 2, 1);
		set(2, 3, 0);

		set(3, 0, 0);
		set(3, 1, 0);
		set(3, 2, 0);
		set(3, 3, 1);
		return this;
	}

	public void get(FloatBuffer buffer) {
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				buffer.put(m[i * 4 + j]);
			}
		}

		buffer.flip();
	}

	private void set(int x, int y, float value) {
		this.m[x * 4 + y] = value;
	}

	@Override
	public String toString() {
		return "Matrix4f [" + Arrays.toString(m) + "]";
	}
}

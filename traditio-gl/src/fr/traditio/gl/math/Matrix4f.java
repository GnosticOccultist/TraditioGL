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

	public Matrix4f translate(float x, float y, float z) {
		set(3, 0, Math.fma(get(0, 0), x, Math.fma(get(1, 0), y, Math.fma(get(2, 0), z, get(3, 0)))));
		set(3, 1, Math.fma(get(0, 1), x, Math.fma(get(1, 1), y, Math.fma(get(2, 1), z, get(3, 1)))));
		set(3, 2, Math.fma(get(0, 2), x, Math.fma(get(1, 2), y, Math.fma(get(2, 2), z, get(3, 2)))));
		set(3, 3, Math.fma(get(0, 3), x, Math.fma(get(1, 3), y, Math.fma(get(2, 3), z, get(3, 3)))));
		return this;
	}

	public Matrix4f rotate(float angle, float x, float y, float z) {
		/*
		 * Normalize rotation axis to avoid skewing issues. OpenGL's glRotatef
		 * implicitly normalizes (x, y, z). glRotatef in fixed-function OpenGL relies on
		 * axis-angle rotation, which can suffer from gimbal lock under certain
		 * conditions.
		 * 
		 * TODO: Use quaternion to avoid skewing and gimbal lock issues instead of euler
		 * angles.
		 */
		float scalar = (float) Math.sqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
		if (scalar == 0f) {
			return this;
		}

		scalar = 1.0f / scalar;
		x *= scalar;
		y *= scalar;
		z *= scalar;

		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		float C = 1.0f - c;
		float xx = x * x, xy = x * y, xz = x * z;
		float yy = y * y, yz = y * z;
		float zz = z * z;
		float rm00 = xx * C + c;
		float rm01 = xy * C + z * s;
		float rm02 = xz * C - y * s;
		float rm10 = xy * C - z * s;
		float rm11 = yy * C + c;
		float rm12 = yz * C + x * s;
		float rm20 = xz * C + y * s;
		float rm21 = yz * C - x * s;
		float rm22 = zz * C + c;
		float nm00 = get(0, 0) * rm00 + get(1, 0) * rm01 + get(2, 0) * rm02;
		float nm01 = get(0, 1) * rm00 + get(1, 1) * rm01 + get(2, 1) * rm02;
		float nm02 = get(0, 2) * rm00 + get(1, 2) * rm01 + get(2, 2) * rm02;
		float nm03 = get(0, 3) * rm00 + get(1, 3) * rm01 + get(2, 3) * rm02;
		float nm10 = get(0, 0) * rm10 + get(1, 0) * rm11 + get(2, 0) * rm12;
		float nm11 = get(0, 1) * rm10 + get(1, 1) * rm11 + get(2, 1) * rm12;
		float nm12 = get(0, 2) * rm10 + get(1, 2) * rm11 + get(2, 2) * rm12;
		float nm13 = get(0, 3) * rm10 + get(1, 3) * rm11 + get(2, 3) * rm12;
		set(2, 0, get(0, 0) * rm20 + get(1, 0) * rm21 + get(2, 0) * rm22);
		set(2, 1, get(0, 1) * rm20 + get(1, 1) * rm21 + get(2, 1) * rm22);
		set(2, 2, get(0, 2) * rm20 + get(1, 2) * rm21 + get(2, 2) * rm22);
		set(2, 3, get(0, 3) * rm20 + get(1, 3) * rm21 + get(2, 3) * rm22);
		set(0, 0, nm00);
		set(0, 1, nm01);
		set(0, 2, nm02);
		set(0, 3, nm03);
		set(1, 0, nm10);
		set(1, 1, nm11);
		set(1, 2, nm12);
		set(1, 3, nm13);
		return this;
	}

	public Matrix4f perspective(float fovY, float aspectRatio, float zNear, float zFar) {
		float tanFOV = (float) Math.tan(Math.toRadians(fovY / 2));

		set(0, 0, 1 / (tanFOV * aspectRatio));
		set(1, 1, 1 / tanFOV);
		set(2, 2, (zFar + zNear) / (zNear - zFar));
		set(3, 2, (2 * zFar * zNear) / (zNear - zFar));
		set(2, 3, -1);
		set(3, 3, 0);

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

	private float get(int x, int y) {
		return m[x * 4 + y];
	}

	private void set(int x, int y, float value) {
		this.m[x * 4 + y] = value;
	}

	@Override
	public String toString() {
		return "Matrix4f [" + Arrays.toString(m) + "]";
	}
}

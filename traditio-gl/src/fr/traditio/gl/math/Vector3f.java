package fr.traditio.gl.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Vector3f {

	private float x, y, z;

	public Vector3f() {
		super();
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f set(FloatBuffer buffer) {
		this.x = buffer.get();
		this.y = buffer.get();
		this.z = buffer.get();
		return this;
	}

	public Vector3f set(IntBuffer buffer) {
		this.x = buffer.get();
		this.y = buffer.get();
		this.z = buffer.get();
		return this;
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public float z() {
		return z;
	}

	@Override
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}

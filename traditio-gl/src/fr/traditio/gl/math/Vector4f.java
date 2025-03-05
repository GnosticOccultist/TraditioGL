package fr.traditio.gl.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Vector4f {

	private float x, y, z, w;

	public Vector4f() {
		super();
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4f set(FloatBuffer buffer) {
		this.x = buffer.get();
		this.y = buffer.get();
		this.z = buffer.get();
		this.w = buffer.get();
		return this;
	}

	public Vector4f set(IntBuffer buffer) {
		this.x = buffer.get();
		this.y = buffer.get();
		this.z = buffer.get();
		this.w = buffer.get();
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

	public float w() {
		return w;
	}

	@Override
	public String toString() {
		return "Vector4f [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
}

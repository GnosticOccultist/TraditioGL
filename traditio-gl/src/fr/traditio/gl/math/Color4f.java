package fr.traditio.gl.math;

public final class Color4f {

	private float r, g, b, a;

	public Color4f() {
		super();
	}

	public Color4f(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color4f(Color4f c) {
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
		this.a = c.a;
	}

	public Color4f set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public float r() {
		return r;
	}

	public float g() {
		return g;
	}

	public float b() {
		return b;
	}

	public float a() {
		return a;
	}

	@Override
	public String toString() {
		return "Color4f [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
	}
}

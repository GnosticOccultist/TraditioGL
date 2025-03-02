package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GLCapabilities;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.Vector3f;

class Mesh {

	protected static final int VERTEX_SIZE = 3 + 4 + 2 + 3;

	protected static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

	private final float[] vertices;

	protected int currentIndex = 0;

	private int flushCount = 0;

	private boolean started = false;

	private VertexProcessor processor;

	private PrimitiveMode mode = PrimitiveMode.TRIANGLES;

	protected Mesh(int capacity, GLCapabilities capabilities) {
		this.vertices = new float[capacity * SPRITE_SIZE];
		
		// DSA support.
		if (capabilities.OpenGL45 || capabilities.GL_ARB_direct_state_access) {
			this.processor = new DirectVertexProcessor();
		} else {
			this.processor = new BindingVertexProcessor();
		}
	}

	public void start() {
		if (started) {
			throw new IllegalStateException("Mesh is already started!");
		}

		var sizeInBytes = vertices.length * Float.BYTES;
		// Vertex allocation size has changed update vertex buffers.
		if (processor.getAllocatedSize() != sizeInBytes) {
			processor.allocData(vertices.length * Float.BYTES);
		}

		this.flushCount = 0;

		this.started = true;
	}

	protected boolean ensureCapacity(int capacity) {
		var newSize = currentIndex + capacity;
		if (newSize >= vertices.length) {
			flush();
			return true;
		}

		return false;
	}

	protected void putVertex(float x, float y, float z, Color4f c, Vector3f t, Vector3f n) {
		ensureCapacity(12);

		vertices[currentIndex] = x;
		vertices[currentIndex + 1] = y;
		vertices[currentIndex + 2] = z;
		vertices[currentIndex + 3] = c.r();
		vertices[currentIndex + 4] = c.g();
		vertices[currentIndex + 5] = c.b();
		vertices[currentIndex + 6] = c.a();
		vertices[currentIndex + 7] = t.x();
		vertices[currentIndex + 8] = t.y();
		vertices[currentIndex + 9] = n.x();
		vertices[currentIndex + 10] = n.y();
		vertices[currentIndex + 11] = n.z();

		this.currentIndex += 12;
	}

	protected void putVertex(float x, float y, float z, Color4f c) {
		putVertex(x, y, z, c.r(), c.g(), c.b(), c.a(), 0, 0, 0, 0, 0);
	}

	protected void putVertex(float x, float y, float z, float r, float g, float b, float a, float s, float t, float nx,
			float ny, float nz) {
		ensureCapacity(12);

		vertices[currentIndex] = x;
		vertices[currentIndex + 1] = y;
		vertices[currentIndex + 2] = z;
		vertices[currentIndex + 3] = r;
		vertices[currentIndex + 4] = g;
		vertices[currentIndex + 5] = b;
		vertices[currentIndex + 6] = a;
		vertices[currentIndex + 7] = s;
		vertices[currentIndex + 8] = t;
		vertices[currentIndex + 9] = nx;
		vertices[currentIndex + 10] = ny;
		vertices[currentIndex + 11] = nz;

		this.currentIndex += 12;
	}

	public void flush() {
		if (currentIndex == 0) {
			return;
		}

		processor.populateData(vertices);

		processor.draw(mode, currentIndex);

		this.currentIndex = 0;
		this.flushCount++;
	}

	public void finish() {
		if (!started) {
			throw new IllegalStateException("Mesh hasn't been started!");
		}

		this.flushCount = 0;

		this.started = false;
	}

	public void destroy() {
		processor.destroy();
	}

	public void setMode(PrimitiveMode mode) {
		this.mode = mode;
	}

	public boolean isStarted() {
		return started;
	}

	public int flushCount() {
		return flushCount;
	}
}

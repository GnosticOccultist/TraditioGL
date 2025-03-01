package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.Vector3f;

class Mesh {

	protected static final int VERTEX_SIZE = 3 + 4 + 2 + 3;

	protected static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

	private final float[] vertices;

	protected int currentIndex = 0;

	private int flushCount = 0;

	private boolean started = false;

	private int vao = -1, vbo = -1;

	private long allocatedSize;

	private PrimitiveMode mode = PrimitiveMode.TRIANGLES;

	protected Mesh(int capacity) {
		this.vertices = new float[capacity * SPRITE_SIZE];
	}

	public void start() {
		if (started) {
			throw new IllegalStateException("Mesh is already started!");
		}

		var sizeInBytes = vertices.length * Float.BYTES;
		// Vertex allocation size has changed update vertex buffers.
		if (allocatedSize != sizeInBytes) {
			allocData(vertices.length * Float.BYTES);
		}

		this.flushCount = 0;

		this.started = true;
	}

	public void allocData(long size) {
		if (allocatedSize == size) {
			return;
		}

		if (vao == -1) {
			vao = GL30.glGenVertexArrays();
		}

		GL30.glBindVertexArray(vao);

		if (vbo == -1) {
			vbo = GL15.glGenBuffers();
		}

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, GL15.GL_DYNAMIC_DRAW);

		GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 12 * Float.BYTES, 0);
		GL30.glEnableVertexAttribArray(0);

		GL30.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, true, 12 * Float.BYTES, 3 * Float.BYTES);
		GL30.glEnableVertexAttribArray(1);

		GL30.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 12 * Float.BYTES, 7 * Float.BYTES);
		GL30.glEnableVertexAttribArray(2);

		GL30.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 12 * Float.BYTES, 9 * Float.BYTES);
		GL30.glEnableVertexAttribArray(3);

		this.allocatedSize = size;
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

		if (vao == -1 || vbo == -1) {
			throw new IllegalArgumentException("Vertex buffer not yet allocated!");
		}

		GL30.glBindVertexArray(vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);

		GL30.glBindVertexArray(vao);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glEnableVertexAttribArray(3);

		GL15.glDrawArrays(mode.toGLMode(), 0, currentIndex);

		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);

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
		GL15.glDeleteBuffers(vbo);
		vbo = -1;

		GL30.glDeleteVertexArrays(vao);
		vao = -1;

		allocatedSize = 0;
	}

	public boolean isStarted() {
		return started;
	}

	public int flushCount() {
		return flushCount;
	}
}

package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;

public class DirectVertexProcessor implements VertexProcessor {

	private int vao = -1, vbo = -1;

	private long allocatedSize;

	@Override
	public void allocData(long size) {
		if (allocatedSize == size) {
			return;
		}

		if (vao == -1) {
			vao = GL45.glCreateVertexArrays();
		}

		if (vbo == -1) {
			vbo = GL45.glCreateBuffers();
		}

		GL45.glNamedBufferData(vbo, size, GL15.GL_DYNAMIC_DRAW);

		GL45.glVertexArrayVertexBuffer(vao, 0, vbo, 0, 12 * Float.BYTES);
		GL45.glEnableVertexArrayAttrib(vao, 0);
		GL45.glVertexArrayAttribFormat(vao, 0, 3, GL11.GL_FLOAT, false, 0);
		GL45.glVertexArrayAttribBinding(vao, 0, 0);
		
		GL45.glVertexArrayVertexBuffer(vao, 1, vbo, 3 * Float.BYTES, 12 * Float.BYTES);
		GL45.glEnableVertexArrayAttrib(vao, 1);
		GL45.glVertexArrayAttribFormat(vao, 1, 4, GL11.GL_FLOAT, true, 3 * Float.BYTES);
		GL45.glVertexArrayAttribBinding(vao, 1, 0);
		
		GL45.glVertexArrayVertexBuffer(vao, 2, vbo, 7 * Float.BYTES, 12 * Float.BYTES);
		GL45.glEnableVertexArrayAttrib(vao, 2);
		GL45.glVertexArrayAttribFormat(vao, 2, 2, GL11.GL_FLOAT, false, 7 * Float.BYTES);
		GL45.glVertexArrayAttribBinding(vao, 2, 0);
		
		GL45.glVertexArrayVertexBuffer(vao, 3, vbo, 9 * Float.BYTES, 12 * Float.BYTES);
		GL45.glEnableVertexArrayAttrib(vao, 3);
		GL45.glVertexArrayAttribFormat(vao, 3, 3, GL11.GL_FLOAT, false, 9 * Float.BYTES);
		GL45.glVertexArrayAttribBinding(vao, 3, 0);

		this.allocatedSize = size;
	}

	@Override
	public void populateData(float[] data) {
		if (vao == -1 || vbo == -1) {
			throw new IllegalArgumentException("Vertex buffer not yet allocated!");
		}

		GL45.glNamedBufferSubData(vbo, 0, data);
	}

	@Override
	public void draw(PrimitiveMode mode, int amount) {
		if (vao == -1 || vbo == -1) {
			throw new IllegalArgumentException("Vertex buffer not yet allocated!");
		}

		GL45.glBindVertexArray(vao);
		GL45.glEnableVertexArrayAttrib(vao, 0);
		GL45.glEnableVertexArrayAttrib(vao, 1);
		GL45.glEnableVertexArrayAttrib(vao, 2);
		GL45.glEnableVertexArrayAttrib(vao, 3);

		GL15.glDrawArrays(mode.toGLMode(), 0, amount);

		GL45.glDisableVertexArrayAttrib(vao, 3);
		GL45.glDisableVertexArrayAttrib(vao, 2);
		GL45.glDisableVertexArrayAttrib(vao, 1);
		GL45.glDisableVertexArrayAttrib(vao, 0);
		GL45.glBindVertexArray(0);
	}

	@Override
	public long getAllocatedSize() {
		return allocatedSize;
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
		vbo = -1;

		GL30.glDeleteVertexArrays(vao);
		vao = -1;

		allocatedSize = 0;
	}
}

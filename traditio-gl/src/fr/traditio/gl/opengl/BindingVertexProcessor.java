package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class BindingVertexProcessor implements VertexProcessor {

	private int vao = -1, vbo = -1;

	private long allocatedSize;

	@Override
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

	@Override
	public void populateData(float[] data) {
		if (vao == -1 || vbo == -1) {
			throw new IllegalArgumentException("Vertex buffer not yet allocated!");
		}

		GL30.glBindVertexArray(vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
	}

	@Override
	public void draw(PrimitiveMode mode, int amount) {
		if (vao == -1 || vbo == -1) {
			throw new IllegalArgumentException("Vertex buffer not yet allocated!");
		}

		GL30.glBindVertexArray(vao);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glEnableVertexAttribArray(3);

		GL15.glDrawArrays(mode.toGLMode(), 0, amount);

		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
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

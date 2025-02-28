package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;

enum PrimitiveMode {

	POINTS,

	LINES,

	LINE_STRIP,

	LINE_LOOP,

	TRIANGLES,

	TRIANGLE_STRIP,

	TRIANGLE_FAN,

	QUADS,

	QUAD_STRIP,

	POLYGON;

	int toGLMode() {
		switch (this) {
		case POINTS:
			return GL11.GL_POINTS;
		case LINES:
			return GL11.GL_LINES;
		case LINE_STRIP:
			return GL11.GL_LINE_STRIP;
		case LINE_LOOP:
			return GL11.GL_LINE_LOOP;
		case TRIANGLES:
			return GL11.GL_TRIANGLES;
		case TRIANGLE_STRIP:
			return GL11.GL_TRIANGLE_STRIP;
		case TRIANGLE_FAN:
			return GL11.GL_TRIANGLE_FAN;
		case QUADS:
		case QUAD_STRIP:
		case POLYGON:
			return GL11.GL_TRIANGLES;
		default:
			throw new IllegalStateException("Invalid primitive mode " + this);
		}
	}
}

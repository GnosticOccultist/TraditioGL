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

	static PrimitiveMode fromGL(int mode) {
		switch (mode) {
		case GL11.GL_POINTS:
			return PrimitiveMode.POINTS;
		case GL11.GL_LINES:
			return PrimitiveMode.LINES;
		case GL11.GL_LINE_STRIP:
			return PrimitiveMode.LINE_STRIP;
		case GL11.GL_LINE_LOOP:
			return PrimitiveMode.LINE_LOOP;
		case GL11.GL_TRIANGLES:
			return PrimitiveMode.TRIANGLES;
		case GL11.GL_TRIANGLE_STRIP:
			return PrimitiveMode.TRIANGLE_STRIP;
		case GL11.GL_TRIANGLE_FAN:
			return PrimitiveMode.TRIANGLE_FAN;
		case GL11.GL_QUADS:
			return PrimitiveMode.QUADS;
		case GL11.GL_QUAD_STRIP:
			return PrimitiveMode.QUAD_STRIP;
		case GL11.GL_POLYGON:
			return PrimitiveMode.POLYGON;
		default:
			throw new IllegalStateException("Invalid OpenGL primitive mode " + mode);
		}
	}
}

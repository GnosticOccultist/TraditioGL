package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;

public class TGL11 {

	public static final int GL_MODELVIEW = 0x1700;
	public static final int GL_PROJECTION = 0x1701;
	public static final int GL_TEXTURE = 0x1702;
	public static final int GL_DEPTH_BUFFER_BIT = 0x100;
	public static final int GL_STENCIL_BUFFER_BIT = 0x400;
	public static final int GL_COLOR_BUFFER_BIT = 0x4000;

	public static final int GL_DEPTH_TEST = 0xB71;

	static final int TGL_NO_DRAW = -1;
	public static final int GL_POINTS = 0x0;
	public static final int GL_LINES = 0x1;
	public static final int GL_LINE_LOOP = 0x2;
	public static final int GL_LINE_STRIP = 0x3;
	public static final int GL_TRIANGLES = 0x4;
	public static final int GL_TRIANGLE_STRIP = 0x5;
	public static final int GL_TRIANGLE_FAN = 0x6;
	public static final int GL_QUADS = 0x7;
	public static final int GL_QUAD_STRIP = 0x8;
	public static final int GL_POLYGON = 0x9;

	public static void glEnable(int target) {
		TGLContext.checkContext();
		GL11.glEnable(target);
	}

	public static void glDisable(int target) {
		TGLContext.checkContext();
		GL11.glDisable(target);
	}

	public static void glMatrixMode(int mode) {
		var c = TGLContext.get();
		if (c.matrixMode != mode) {
			c.matrixMode = mode;
		}
	}

	public static void glLoadIdentity() {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		var matrix = stack.peek();
		matrix.identity();
	}

	public static void glPushMatrix() {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		stack.push();
	}

	public static void glPopMatrix() {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		stack.pop();
	}

	public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		var matrix = stack.peek();
		matrix.perspective(fovy, aspect, zNear, zFar);
	}

	public static void glTranslatef(float x, float y, float z) {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		var matrix = stack.peek();
		matrix.translate(x, y, z);
	}

	public static void glRotatef(float angle, float x, float y, float z) {
		var c = TGLContext.get();
		var stack = c.getOrCreateMatrixStack();
		var matrix = stack.peek();
		matrix.rotate(angle, x, y, z);
	}

	public static void glClear(int mask) {
		TGLContext.checkContext();
		GL11.glClear(mask);
	}

	public static void glClearColor(float r, float g, float b, float a) {
		var c = TGLContext.get();
		if (c.r != r || c.g != g || c.b != b || c.a != a) {
			c.r = r;
			c.g = g;
			c.b = b;
			c.a = a;
			GL11.glClearColor(r, g, b, a);
		}
	}

	public static void glViewport(int x, int y, int w, int h) {
		var c = TGLContext.get();
		if (c.vx != x || c.vy != y || c.vw != w || c.vh != h) {
			c.vx = x;
			c.vy = y;
			c.vw = w;
			c.vh = h;
			GL11.glViewport(x, y, w, h);
		}
	}

	public static void glColor4f(float r, float g, float b, float a) {
		var c = TGLContext.get();
		c.diffuseColor.set(r, g, b, a);
	}

	public static void glBegin(int mode) {
		var c = TGLContext.get();
		if (c.drawMode != mode) {
			c.drawMode = mode;
		}

		c.prepareShader();
		c.mesh.start();
	}

	public static void glEnd() {
		var c = TGLContext.get();
		c.mesh.flush();
		c.mesh.finish();
	}

	public static void glVertex3f(float x, float y, float z) {
		var c = TGLContext.get();
		var m = c.mesh;
		m.putVertex(x, y, z, 1, 1, 1, 1, 0, 0, 0);
	}

	public static void glRectf(float x1, float y1, float x2, float y2) {
		var c = TGLContext.get();
		var m = c.mesh;
		glBegin(GL_TRIANGLES);
		m.putVertex(x1, y1, 0, 1, 1, 1, 1, 0, 0, 0);
		m.putVertex(x1, y2, 0, 1, 1, 1, 1, 0, 0, 0);
		m.putVertex(x2, y1, 0, 1, 1, 1, 1, 0, 0, 0);
		m.putVertex(x2, y1, 0, 1, 1, 1, 1, 0, 0, 0);
		m.putVertex(x1, y2, 0, 1, 1, 1, 1, 0, 0, 0);
		m.putVertex(x2, y2, 0, 1, 1, 1, 1, 0, 0, 0);
		glEnd();
	}

	public static void printContext() {
		var c = TGLContext.get();
		System.out.println(c);
	}
}

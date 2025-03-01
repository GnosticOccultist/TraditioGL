package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class TGL11 {

	public static final int GL_MODELVIEW = 0x1700;
	public static final int GL_PROJECTION = 0x1701;
	public static final int GL_TEXTURE = 0x1702;
	public static final int GL_DEPTH_BUFFER_BIT = 0x100;
	public static final int GL_STENCIL_BUFFER_BIT = 0x400;
	public static final int GL_COLOR_BUFFER_BIT = 0x4000;

	public static final int GL_DEPTH_TEST = 0xB71;
	public static final int GL_CULL_FACE = 0xB44;
	public static final int GL_TEXTURE_2D = 0xDE1;

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

	public static final int GL_NONE = 0;
	public static final int GL_FRONT_LEFT = 0x400;
	public static final int GL_FRONT_RIGHT = 0x401;
	public static final int GL_BACK_LEFT = 0x402;
	public static final int GL_BACK_RIGHT = 0x403;
	public static final int GL_FRONT = 0x404;
	public static final int GL_BACK = 0x405;
	public static final int GL_LEFT = 0x406;
	public static final int GL_RIGHT = 0x407;
	public static final int GL_FRONT_AND_BACK = 0x408;
	public static final int GL_AUX0 = 0x409;
	public static final int GL_AUX1 = 0x40A;
	public static final int GL_AUX2 = 0x40B;
	public static final int GL_AUX3 = 0x40C;

	public static final int GL_POINT = 0x1B00;
	public static final int GL_LINE = 0x1B01;
	public static final int GL_FILL = 0x1B02;

	public static void glEnable(int target) {
		var c = TGLContext.get();
		if (target == GL_DEPTH_TEST && !c.depthTest) {
			c.mesh.flush();
			c.depthTest = true;
			GL11.glEnable(target);
		} else if (target == GL_CULL_FACE && !c.applyCullFace) {
			c.mesh.flush();
			c.applyCullFace = true;
			GL11.glEnable(target);
		} else if (target == GL_TEXTURE_2D && !c.enableTex2D) {
			c.mesh.flush();
			c.enableTex2D = true;
			c.changeDefine("USE_TEXTURE", true);
		}
	}

	public static void glDisable(int target) {
		var c = TGLContext.get();
		if (target == GL_DEPTH_TEST && c.depthTest) {
			c.mesh.flush();
			c.depthTest = false;
			GL11.glDisable(target);
		} else if (target == GL_CULL_FACE && c.applyCullFace) {
			c.mesh.flush();
			c.applyCullFace = false;
			GL11.glDisable(target);
		} else if (target == GL_TEXTURE_2D && c.enableTex2D) {
			c.mesh.flush();
			c.enableTex2D = false;
			c.changeDefine("USE_TEXTURE", false);
		}
	}

	public static void glBindTexture(int target, int texture) {
		var c = TGLContext.get();
		if (c.boundTex2D != texture) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(target, texture);
			c.boundTex2D = texture;
		}
	}

	public static void glPolygonMode(int face, int mode) {
		var c = TGLContext.get();
		if (face != c.polyFace || mode != c.polyFill) {
			c.mesh.flush();
			c.polyFace = face;
			c.polyFill = mode;

			GL11.glPolygonMode(face, mode);
		}
	}

	public static void glCullFace(int mode) {
		var c = TGLContext.get();
		if (mode != c.cullFace) {
			c.mesh.flush();
			c.cullFace = mode;

			GL11.glCullFace(mode);
		}
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

	public static void glBegin(int mode) {
		var c = TGLContext.get();
		if (c.drawMode != mode) {
			c.drawMode = mode;
		}

		if (c.boundTex2D == 0) {
			// No bound texture in context, so ignore the define in shader.
			c.changeDefine("USE_TEXTURE", false);
		} else if (c.boundTex2D != 0 && c.enableTex2D) {
			c.changeDefine("USE_TEXTURE", true);
		}

		c.prepareShader();
		c.mesh.start();
	}

	public static void glEnd() {
		var c = TGLContext.get();
		c.mesh.flush();
		c.mesh.finish();
	}

	public static void glColor3f(float r, float g, float b) {
		var c = TGLContext.get();
		c.vertexColor.set(r, g, b, 1.0f);
	}

	public static void glColor4f(float r, float g, float b, float a) {
		var c = TGLContext.get();
		c.vertexColor.set(r, g, b, a);
	}

	public static void glTexCoord2f(float s, float t) {
		var c = TGLContext.get();
		c.vertexTex.set(s, t, 0);
	}

	public static void glNormal3f(float nx, float ny, float nz) {
		var c = TGLContext.get();
		c.vertexNorm.set(nx, ny, nz);
	}

	public static void glVertex3f(float x, float y, float z) {
		var c = TGLContext.get();
		var m = c.mesh;
		m.putVertex(x, y, z, c.vertexColor, c.vertexTex, c.vertexNorm);
	}

	public static void glRectf(float x1, float y1, float x2, float y2) {
		var c = TGLContext.get();
		var m = c.mesh;
		glBegin(GL_TRIANGLES);
		m.putVertex(x1, y1, 0, c.vertexColor);
		m.putVertex(x1, y2, 0, c.vertexColor);
		m.putVertex(x2, y1, 0, c.vertexColor);
		m.putVertex(x2, y1, 0, c.vertexColor);
		m.putVertex(x1, y2, 0, c.vertexColor);
		m.putVertex(x2, y2, 0, c.vertexColor);
		glEnd();
	}

	public static void printContext() {
		var c = TGLContext.get();
		System.out.println(c);
	}
}

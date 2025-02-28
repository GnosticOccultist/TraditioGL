package fr.traditio.gl.opengl;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GLCapabilities;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.MatrixStack;

public class TGLContext {

	private static final ThreadLocal<TGLContext> CONTEXT_LOCAL = new ThreadLocal<>();

	/**
	 * The OpenGL context capabilities.
	 */
	GLCapabilities capabilities;
	/**
	 * The current clear color.
	 */
	float r, g, b, a;

	int vx, vy, vw, vh;

	int matrixMode = TGL11.GL_MODELVIEW;

	final Map<Integer, MatrixStack> matrixStacks = new HashMap<>();

	final Color4f diffuseColor = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);

	final Shader shader = new Shader("base");

	int drawMode = TGL11.TGL_NO_DRAW;

	final Mesh mesh = new Mesh(600);

	TGLContext(GLCapabilities capabilities) {
		this.capabilities = capabilities;
		CONTEXT_LOCAL.set(this);

		initialize();
	}

	private void initialize() {
		prepareShader();
	}

	void prepareShader() {
		shader.uniformMat4("projection", getOrCreateMatrixStack(TGL11.GL_PROJECTION).peek());
		shader.uniformMat4("modelView", getOrCreateMatrixStack(TGL11.GL_MODELVIEW).peek());
		shader.uniform4f("diffuseColor", diffuseColor.r(), diffuseColor.g(), diffuseColor.b(), diffuseColor.a());
	}

	MatrixStack getOrCreateMatrixStack() {
		return getOrCreateMatrixStack(matrixMode);
	}

	MatrixStack getOrCreateMatrixStack(int matrixMode) {
		var stack = matrixStacks.get(matrixMode);
		if (stack == null) {
			stack = new MatrixStack();
			matrixStacks.put(matrixMode, stack);
		}

		return stack;
	}

	private void cleanup() {
		shader.cleanup();
		mesh.destroy();
	}

	@Override
	public String toString() {
		var sb = new StringBuilder("TGLContext [");
		sb.append("\n");
		sb.append("\tglClearColor(" + r + ", " + g + ", " + b + ", " + a + ")");
		sb.append("\n");
		sb.append("\tglMatrixMode(" + matrixMode + ")");
		sb.append("\n");
		sb.append("\tmatrixStacks(" + matrixStacks + ")");
		sb.append("\n]");

		return sb.toString();
	}

	public static void checkContext() {
		var context = CONTEXT_LOCAL.get();
		if (context == null) {
			throw new IllegalStateException("No context is current on thread " + Thread.currentThread().getName());
		}
	}

	public static TGLContext get() {
		var context = CONTEXT_LOCAL.get();
		if (context == null) {
			throw new IllegalStateException("No context is current on thread " + Thread.currentThread().getName());
		}

		return context;
	}

	public static void release() {
		var context = CONTEXT_LOCAL.get();
		if (context != null) {
			context.cleanup();
		}

		CONTEXT_LOCAL.set(null);
	}
}

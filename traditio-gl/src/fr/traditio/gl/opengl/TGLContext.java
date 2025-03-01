package fr.traditio.gl.opengl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GLCapabilities;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.MatrixStack;
import fr.traditio.gl.math.Vector3f;

public class TGLContext {

	private static final ThreadLocal<TGLContext> CONTEXT_LOCAL = new ThreadLocal<>();

	public static final List<String> DEFINE_NAMES = Arrays.asList("USE_TEXTURE");

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

	final Color4f vertexColor = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);

	final Vector3f vertexTex = new Vector3f(0.0f, 0.0f, 0.0f);

	final Vector3f vertexNorm = new Vector3f(0.0f, 0.0f, 0.0f);

	final DefineSet emptySet = new DefineSet(DEFINE_NAMES.size());

	DefineSet currentSet;

	Shader currentShader = null;

	final Map<DefineSet, Shader> shaders = new HashMap<>();

	int drawMode = TGL11.TGL_NO_DRAW;

	boolean depthTest = false;

	int polyFace = TGL11.GL_FRONT_AND_BACK;
	int polyFill = TGL11.GL_FILL;

	boolean applyCullFace = false;
	int cullFace = TGL11.GL_BACK;

	final Mesh mesh = new Mesh(600);

	boolean enableTex2D = false;

	int boundTex2D = 0;

	TGLContext(GLCapabilities capabilities) {
		this.capabilities = capabilities;
		CONTEXT_LOCAL.set(this);

		initialize();
	}

	private void initialize() {
		emptySet.set(DEFINE_NAMES.indexOf("USE_TEXTURE"), false);
		currentSet = new DefineSet(emptySet);

		currentShader = new Shader("base", emptySet);
		shaders.put(emptySet, currentShader);

		prepareShader();
	}

	boolean changeDefine(String define, boolean value) {
		var changed = currentSet.set(DEFINE_NAMES.indexOf(define), value);
		if (changed) {
			var shader = shaders.get(currentSet);
			if (shader == null) {
				var defines = new DefineSet(currentSet);
				shader = new Shader("base", defines);
				shaders.put(defines, shader);
			}

			currentShader = shader;
			System.out.println(currentShader);
			return true;
		}

		return false;
	}

	void prepareShader() {
		currentShader.uniformMat4("projection", getOrCreateMatrixStack(TGL11.GL_PROJECTION).peek());
		currentShader.uniformMat4("modelView", getOrCreateMatrixStack(TGL11.GL_MODELVIEW).peek());

		if (boundTex2D != 0 && enableTex2D) {
			currentShader.uniformi("texture_sampler", 0);
		}
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
		for (var shader : shaders.values()) {
			shader.cleanup();
		}

		mesh.destroy();
	}

	@Override
	public String toString() {
		var sb = new StringBuilder("TGLContext [");
		sb.append("\n");
		sb.append("\tglClearColor(" + r + ", " + g + ", " + b + ", " + a + ")");
		sb.append("\n");
		sb.append("\tdepthTest(" + depthTest + ")");
		sb.append("\n");
		sb.append("\tapplyCullFace(" + applyCullFace + ")");
		sb.append("\n");
		sb.append("\tcullFace(" + cullFace + ")");
		sb.append("\n");
		sb.append("\tpolyFace(" + polyFace + ")");
		sb.append("\n");
		sb.append("\tpolyFill(" + polyFill + ")");
		sb.append("\n");
		sb.append("\tenableTex2D(" + enableTex2D + ")");
		sb.append("\n");
		sb.append("\tglMatrixMode(" + matrixMode + ")");
		sb.append("\n");
		sb.append("\tmatrixStacks(" + matrixStacks + ")");
		sb.append("\n");
		sb.append("\tvertexColor(" + vertexColor + ")");
		sb.append("\n");
		sb.append("\tvertexTex(" + vertexTex + ")");
		sb.append("\n");
		sb.append("\tvertexNorm(" + vertexNorm + ")");
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

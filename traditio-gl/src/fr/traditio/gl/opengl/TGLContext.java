package fr.traditio.gl.opengl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GLCapabilities;

import fr.traditio.gl.display.Display;
import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.MatrixStack;
import fr.traditio.gl.math.Vector3f;

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

	final Color4f vertexColor = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);

	final Vector3f vertexTex = new Vector3f(0.0f, 0.0f, 0.0f);

	final Vector3f vertexNorm = new Vector3f(0.0f, 0.0f, 0.0f);

	final Map<String, MaterialTechnique> techniques = new HashMap<>();

	MaterialTechnique currentTechnique;

	Framebuffer msFramebuffer = null;

	boolean depthTest = false;

	int polyFace = TGL11.GL_FRONT_AND_BACK;
	int polyFill = TGL11.GL_FILL;

	boolean applyCullFace = false;
	int cullFace = TGL11.GL_BACK;

	Mesh mesh;

	boolean enableTex2D = false;

	int boundTex2D = 0;

	boolean enableFog = false;
	int fogMode = TGL11.GL_EXP;
	Color4f fogColor = new Color4f(0.0f, 0.0f, 0.0f, 0.0f);
	float fogDensity = 1.0f;
	float fogStart = 0.0f;
	float fogEnd = 1.0f;
	boolean useFogCoord = false;
	float fogCoord = 0.0f;

	boolean enableMultisample = false;
	int sampleCount = TGL13.GL_SAMPLE_COUNT_1_BIT;

	boolean sRGB = false;

	boolean enableLighting = false;
	Light[] lights = null;

	TGLContext(GLCapabilities capabilities) {
		this.capabilities = capabilities;
		CONTEXT_LOCAL.set(this);

		initialize();
	}

	private void initialize() {
		mesh = new Mesh(600, capabilities);

		var maxNumLight = TGL11.glGetInteger(TGL11.GL_MAX_LIGHTS);
		lights = new Light[maxNumLight];

		Arrays.fill(lights, new Light());

		lights[0].diffuse.set(1.0f, 1.0f, 1.0f, 1.0f);
		lights[0].specular.set(1.0f, 1.0f, 1.0f, 1.0f);

		sampleCount = Display.getPixelFormat().getSamples();
		changeTechnique("base");
	}

	void changeTechnique(String name) {
		if (currentTechnique != null && currentTechnique.name.equals(name)) {
			return;
		}

		var technique = techniques.get(name);
		if (technique == null) {
			technique = new MaterialTechnique(name);
			techniques.put(name, technique);
		}

		if (currentTechnique != null) {
			var defines = currentTechnique.currentSet;
			technique.changeDefine(defines);
		}

		technique.prepareShader(this);
		currentTechnique = technique;
	}

	boolean changeSampleCount(int sampleCount) {
		var oldSampleCount = msFramebuffer == null ? TGL13.GL_SAMPLE_COUNT_1_BIT : msFramebuffer.getSampleCount();
		var changed = sampleCount != oldSampleCount;
		if (!changed) {
			return false;
		}

		if (msFramebuffer != null) {
			msFramebuffer.cleanup();
		}

		if (sampleCount <= TGL13.GL_SAMPLE_COUNT_1_BIT) {
			msFramebuffer = null;
		} else {
			msFramebuffer = new Framebuffer(capabilities, Display.getWidth(), Display.getHeight(), sampleCount);
		}

		return true;
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
		currentTechnique.cleanup();
		mesh.destroy();
	}

	public void resized() {
		// Resize framebuffer.
		if (enableMultisample && msFramebuffer != null) {
			msFramebuffer.cleanup();
			msFramebuffer = new Framebuffer(capabilities, Display.getWidth(), Display.getHeight(), sampleCount);
		}
	}

	public void postRender() {
		// Bind framebuffer before clearing the current buffer.
		if (enableMultisample && msFramebuffer != null) {
			msFramebuffer.blit();
		}
	}

	@Override
	public String toString() {
		var sb = new StringBuilder("TGLContext [");
		sb.append("\n");
		sb.append("\tglClearColor(" + r + ", " + g + ", " + b + ", " + a + ")");
		sb.append("\n");
		sb.append("\tglViewport(" + vx + ", " + vy + ", " + vw + ", " + vh + ")");
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
		sb.append("\tboundTex2D(" + boundTex2D + ")");
		sb.append("\n");
		sb.append("\tenableFog(" + enableFog + ")");
		sb.append("\n");
		sb.append("\tuseFogCoord(" + useFogCoord + ")");
		sb.append("\n");
		sb.append("\tfogMode(" + fogMode + ")");
		sb.append("\n");
		sb.append("\tfogColor(" + fogColor + ")");
		sb.append("\n");
		sb.append("\tfogDensity(" + fogDensity + ")");
		sb.append("\n");
		sb.append("\tfogStart(" + fogStart + ")");
		sb.append("\n");
		sb.append("\tfogEnd(" + fogEnd + ")");
		sb.append("\n");
		sb.append("\tglMatrixMode(" + matrixMode + ")");
		sb.append("\n");
		sb.append("\tmatrixStacks(" + matrixStacks + ")");
		sb.append("\n");
		sb.append("\tcurrentTechnique(" + currentTechnique + ")");
		sb.append("\n");
		sb.append("\tmsFramebuffer(" + msFramebuffer + ")");
		sb.append("\n");
		sb.append("\tsampleCount(" + sampleCount + ")");
		sb.append("\n");
		sb.append("\tenableMultisample(" + enableMultisample + ")");
		sb.append("\n");
		sb.append("\tsRGB(" + sRGB + ")");
		sb.append("\n");
		sb.append("\tvertexColor(" + vertexColor + ")");
		sb.append("\n");
		sb.append("\tvertexTex(" + vertexTex + ")");
		sb.append("\n");
		sb.append("\tvertexNorm(" + vertexNorm + ")");
		sb.append("\n");
		sb.append("\tfogCoord(" + fogCoord + ")");
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

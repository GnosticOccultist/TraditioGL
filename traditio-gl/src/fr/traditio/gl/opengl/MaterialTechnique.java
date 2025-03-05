package fr.traditio.gl.opengl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MaterialTechnique {

	public static final List<String> DEFINE_NAMES = Arrays.asList("USE_TEXTURE", "USE_FOG", "FOG_LINEAR", "FOG_EXP",
			"FOG_EXP2", "USE_FOG_COORD");

	final String name;

	final DefineSet emptySet = new DefineSet(DEFINE_NAMES.size());

	DefineSet currentSet;

	Shader currentShader = null;

	final Map<DefineSet, Shader> shaders = new HashMap<>();

	MaterialTechnique(String name) {
		this.name = name;

		emptySet.set(DEFINE_NAMES.indexOf("USE_TEXTURE"), false);
		emptySet.set(DEFINE_NAMES.indexOf("USE_FOG"), false);
		emptySet.set(DEFINE_NAMES.indexOf("FOG_LINEAR"), false);
		emptySet.set(DEFINE_NAMES.indexOf("FOG_EXP"), true);
		emptySet.set(DEFINE_NAMES.indexOf("FOG_EXP2"), false);
		emptySet.set(DEFINE_NAMES.indexOf("USE_FOG_COORD"), false);
		currentSet = new DefineSet(emptySet);

		currentShader = new Shader(name, emptySet);
		shaders.put(emptySet, currentShader);
	}

	void prepareShader(TGLContext c) {
		currentShader.uniformMat4("projection", c.getOrCreateMatrixStack(TGL11.GL_PROJECTION).peek());
		currentShader.uniformMat4("modelView", c.getOrCreateMatrixStack(TGL11.GL_MODELVIEW).peek());

		if (c.boundTex2D != 0 && c.enableTex2D) {
			currentShader.uniformi("texture_sampler", 0);
		}

		if (c.enableFog) {
			currentShader.uniform4f("fogColor", c.fogColor.r(), c.fogColor.g(), c.fogColor.b(), c.fogColor.a());
			if (c.fogMode == TGL11.GL_LINEAR) {
				currentShader.uniform2f("fogLinearRange", c.fogStart, c.fogEnd);
			} else if (c.fogMode == TGL11.GL_EXP || c.fogMode == TGL11.GL_EXP2) {
				currentShader.uniformf("fogDensity", c.fogDensity);
			}
		}
	}

	boolean changeDefine(String define, boolean value) {
		var changed = currentSet.set(DEFINE_NAMES.indexOf(define), value);
		if (changed) {
			var shader = shaders.get(currentSet);
			if (shader == null) {
				var defines = new DefineSet(currentSet);
				shader = new Shader(name, defines);
				shaders.put(defines, shader);
			}

			currentSet = shader.defines;
			currentShader = shader;
			return true;
		}

		return false;
	}

	void changeDefine(DefineSet defines) {
		var shader = shaders.get(defines);
		if (shader == null) {
			var def = new DefineSet(defines);
			shader = new Shader(name, def);
			shaders.put(def, shader);
		}

		currentSet = shader.defines;
		currentShader = shader;
	}

	void cleanup() {
		for (var shader : shaders.values()) {
			shader.cleanup();
		}
	}

	@Override
	public String toString() {
		return "MaterialTechnique [name=" + name + ", currentSet=" + currentSet + ", currentShader=" + currentShader
				+ ", shaders=" + shaders + "]";
	}
}

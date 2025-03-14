package fr.traditio.gl.opengl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.system.MemoryStack;

import fr.traditio.gl.math.Matrix4f;

class Shader {

	private static final int UNBOUND_ID = 0;

	private static final int INVALID_ID = -1;

	private final String name;

	private int programId = INVALID_ID;

	DefineSet defines;

	private int ubo = INVALID_ID;

	Shader(String name, DefineSet defines) {
		this.name = name;
		this.defines = defines;
	}

	void createUBO(long size) {
		if (ubo != INVALID_ID) {
			return;
		}

		use();

		ubo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, ubo);

		// Allocate memory.
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, size, GL15.GL_DYNAMIC_DRAW);

		// Bind UBO to binding point 1.
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 1, ubo);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}

	void updateUBO(ByteBuffer buffer) {
		if (ubo == INVALID_ID) {
			return;
		}

		use();

		buffer.flip();

		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, ubo);
		GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}

	public void uniformMat4(String name, Matrix4f value) {
		use();
		var loc = GL20.glGetUniformLocation(programId, name);
		try (var stack = MemoryStack.stackPush()) {
			var buffer = stack.mallocFloat(16);
			value.get(buffer);
			GL20.glUniformMatrix4fv(loc, false, buffer);
		}
	}

	public void uniformi(String name, int v) {
		use();
		var loc = GL20.glGetUniformLocation(programId, name);
		GL20.glUniform1i(loc, v);
	}

	public void uniformf(String name, float v) {
		use();
		var loc = GL20.glGetUniformLocation(programId, name);
		GL20.glUniform1f(loc, v);
	}

	public void uniform2f(String name, float x, float y) {
		use();
		var loc = GL20.glGetUniformLocation(programId, name);
		GL20.glUniform2f(loc, x, y);
	}

	public void uniform4f(String name, float x, float y, float z, float w) {
		use();
		var loc = GL20.glGetUniformLocation(programId, name);
		GL20.glUniform4f(loc, x, y, z, w);
	}

	public void use() {
		if (programId <= 0) {
			this.programId = GL20.glCreateProgram();
			if (programId == 0) {
				throw new RuntimeException("Couldn't create program '" + name + "'!");
			}

			var shaderName = "/shaders/" + name + ".vert";
			var vertex = attachShader(shaderName, GL20.GL_VERTEX_SHADER);
			shaderName = "/shaders/" + name + ".frag";
			var frag = attachShader(shaderName, GL20.GL_FRAGMENT_SHADER);

			GL20.glLinkProgram(programId);
			var success = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
			if (success != GL11.GL_TRUE) {
				throw new RuntimeException(
						"Error linking shader program: " + GL20.glGetProgramInfoLog(programId, 1024));
			}

			GL20.glValidateProgram(programId);
			success = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
			if (success != GL11.GL_TRUE) {
				throw new RuntimeException(
						"Error validating shader program: " + GL20.glGetProgramInfoLog(programId, 1024));
			}

			detachShader(vertex);
			detachShader(frag);
		}

		GL20.glUseProgram(programId);
	}

	private int attachShader(String resourceName, int shaderType) {
		assert resourceName != null;

		var type = "unknown";
		if (shaderType == GL20.GL_VERTEX_SHADER) {
			type = "vertex";
		} else if (shaderType == GL20.GL_FRAGMENT_SHADER) {
			type = "fragment";
		}

		var id = GL20.glCreateShader(shaderType);
		if (id == 0) {
			throw new RuntimeException("Error creating shader of type " + type);
		}

		var sourceCode = readAsString(resourceName, StandardCharsets.UTF_8, "\\A");
		GL20.glShaderSource(id, sourceCode);

		GL20.glCompileShader(id);
		var compileStatus = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
		if (compileStatus == GL11.GL_FALSE) {
			var log = GL20.glGetShaderInfoLog(id, 1024);
			throw new RuntimeException("Error compiling " + type + " shader " + resourceName + ": " + log);
		}

		GL20.glAttachShader(programId, id);
		return id;
	}

	public String readAsString(String path, Charset charset, String pattern) {
		StringBuilder sb = new StringBuilder();
		sb.append("#version 420");
		sb.append('\n');

		for (int i = 0; i < MaterialTechnique.DEFINE_NAMES.size(); i++) {
			if (!defines.isSet(i)) {
				continue;
			}

			sb.append("#define ").append(MaterialTechnique.DEFINE_NAMES.get(i)).append(' ');
			sb.append('\n');
		}

		sb.append('\n');

		try (var scanner = new Scanner(Shader.class.getResourceAsStream(path), charset.name())) {
			var result = scanner.useDelimiter(pattern).next();
			sb.append(result);
		}

		return sb.toString();
	}

	private void detachShader(int shaderId) {
		GL20.glDetachShader(programId, shaderId);
	}

	public void unuse() {
		GL20.glUseProgram(UNBOUND_ID);
	}

	public String getName() {
		return name;
	}

	public int id() {
		assert programId != INVALID_ID;
		assert programId != UNBOUND_ID;
		return programId;
	}

	public void cleanup() {
		unuse();

		if (ubo != INVALID_ID) {
			GL15.glDeleteBuffers(ubo);
			this.ubo = INVALID_ID;
		}

		if (programId != INVALID_ID) {
			GL20.glDeleteProgram(programId);
			this.programId = INVALID_ID;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Shader [name=" + name + ", programId=" + programId + ", defines=");

		for (int i = 0; i < MaterialTechnique.DEFINE_NAMES.size(); ++i) {
			if (!defines.isSet(i)) {
				continue;
			}

			sb.append(MaterialTechnique.DEFINE_NAMES.get(i)).append(' ');
		}
		return sb.toString();
	}
}

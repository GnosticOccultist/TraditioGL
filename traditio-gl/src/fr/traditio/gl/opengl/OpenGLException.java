package fr.traditio.gl.opengl;

import org.lwjgl.opengl.ARBImaging;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class OpenGLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenGLException() {
		super();
	}

	public OpenGLException(int glErrorCode) {
		this(createErrorMessage(glErrorCode));
	}

	OpenGLException(String message) {
		super(message);
	}

	private static String createErrorMessage(int gl_error_code) {
		var errorString = translateGLErrorString(gl_error_code);
		return errorString + " (" + gl_error_code + ")";
	}

	public static String translateGLErrorString(int error_code) {
		switch (error_code) {
		case GL11.GL_NO_ERROR:
			return "No error";
		case GL11.GL_INVALID_ENUM:
			return "Invalid enum";
		case GL11.GL_INVALID_VALUE:
			return "Invalid value";
		case GL11.GL_INVALID_OPERATION:
			return "Invalid operation";
		case GL11.GL_STACK_OVERFLOW:
			return "Stack overflow";
		case GL11.GL_STACK_UNDERFLOW:
			return "Stack underflow";
		case GL11.GL_OUT_OF_MEMORY:
			return "Out of memory";
		case ARBImaging.GL_TABLE_TOO_LARGE:
			return "Table too large";
		case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
			return "Invalid framebuffer operation";
		default:
			return null;
		}
	}
}

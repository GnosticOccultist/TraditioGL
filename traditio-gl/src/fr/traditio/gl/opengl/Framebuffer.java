package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GLCapabilities;

import fr.traditio.gl.display.Display;

final class Framebuffer {

	private int id = -1;

	private int width;

	private int height;

	private int sampleCount;

	private boolean dsaSupport;

	protected Framebuffer(GLCapabilities capabilities, int width, int height, int sampleCount) {
		this.sampleCount = sampleCount;
		this.width = width;
		this.height = height;
		this.dsaSupport = (capabilities.OpenGL45 || capabilities.GL_ARB_direct_state_access);

		// DSA support.
		if (dsaSupport) {
			this.id = GL45.glCreateFramebuffers();
			var colorBuffer = GL45.glCreateTextures(GL32.GL_TEXTURE_2D_MULTISAMPLE);
			GL45.glTextureStorage2DMultisample(colorBuffer, sampleCount,
					Display.getPixelFormat().isSRGB() ? GL21.GL_SRGB8 : GL11.GL_RGB8, width, height, true);
			GL45.glNamedFramebufferTexture(id, GL30.GL_COLOR_ATTACHMENT0, colorBuffer, 0);

			var rbo = GL45.glCreateRenderbuffers();
			GL45.glNamedRenderbufferStorageMultisample(rbo, sampleCount, GL30.GL_DEPTH24_STENCIL8, width, height);
			GL45.glNamedFramebufferRenderbuffer(id, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo);

			// Check framebuffer status
			var r = GL45.glCheckNamedFramebufferStatus(id, GL30.GL_FRAMEBUFFER);
			if (r != GL30.GL_FRAMEBUFFER_COMPLETE) {
				throw new IllegalStateException("Multisampled FBO isn't complete, error= '" + r + "' !");
			}
		} else {
			this.id = GL30.glGenFramebuffers();
			bind();

			var colorBuffer = GL11.glGenTextures();
			GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, colorBuffer);
			GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, sampleCount,
					Display.getPixelFormat().isSRGB() ? GL21.GL_SRGB8 : GL11.GL_RGB8, width, height, true);
			GL32.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL32.GL_TEXTURE_2D_MULTISAMPLE,
					colorBuffer, 0);

			var rbo = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, sampleCount, GL30.GL_DEPTH24_STENCIL8, width,
					height);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo);

			// Check framebuffer status
			var r = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
			if (r != GL30.GL_FRAMEBUFFER_COMPLETE) {
				throw new IllegalStateException("Multisampled FBO isn't complete, error= '" + r + "' !");
			}

			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		}
	}

	void bind() {
		bind(GL30.GL_FRAMEBUFFER);
	}

	void bind(int target) {
		GL30.glBindFramebuffer(target, id);
	}

	void blit() {
		if (dsaSupport) {
			GL45.glBlitNamedFramebuffer(id, 0, 0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(),
					TGL11.GL_COLOR_BUFFER_BIT, TGL11.GL_NEAREST);
		} else {
			bind(GL30.GL_READ_FRAMEBUFFER);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
			GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(),
					TGL11.GL_COLOR_BUFFER_BIT, TGL11.GL_NEAREST);
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		}
	}

	int getSampleCount() {
		return sampleCount;
	}

	int getWidth() {
		return width;
	}

	int getHeight() {
		return height;
	}

	void cleanup() {
		GL30.glDeleteFramebuffers(id);
		this.id = -1;
	}

	@Override
	public String toString() {
		return "Framebuffer [id=" + id + ", width=" + width + ", height=" + height + ", sampleCount=" + sampleCount
				+ "]";
	}
}

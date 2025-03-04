package fr.traditio.gl.demo;

import static fr.traditio.gl.opengl.TGL11.*;
import static fr.traditio.gl.opengl.TGL14.*;
import static fr.traditio.gl.opengl.TGL30.*;

import org.lwjgl.system.MemoryStack;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;
import fr.traditio.gl.display.PixelFormat;

public class HelloTraditioGL {

	private static float rotation;

	public static void main(String[] args) {
		try {
			// Create Display using LWJGL 2 style.
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("HelloTraditioGL");
			Display.setResizable(true);
			Display.create(PixelFormat.DEFAULT_MSAA);
		} catch (TraditioGLException ex) {
			ex.printStackTrace();
		}

		// Enable some render state.
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_FRAMEBUFFER_SRGB);
		glEnable(GL_FOG);
		glFogi(GL_FOG_MODE, GL_EXP2);
		glFogf(GL_FOG_DENSITY, 0.10f);
		glFogi(GL_FOG_COORDINATE_SOURCE, GL_FRAGMENT_DEPTH);

		// Modify sample count at runtime.
		glHint(GL_SAMPLE_COUNT_HINT, GL_SAMPLE_COUNT_32_BIT);

		try (var stack = MemoryStack.stackPush()) {
			var buffer = stack.floats(0.7f, 0.2f, 0.7f, 1.0f);
			buffer.rewind();
			glFogfv(GL_FOG_COLOR, buffer);
		}

		// Define clear color to black no alpha (default).
		glClearColor(0, 0, 0, 0);

		// Load 2D texture and bind it.
		var tex = new Texture("/textures/wood.jpg");
		glBindTexture(GL_TEXTURE_2D, tex.id());

		// Load perspective projection matrix and define viewport dimensions.
		loadMatrix();

		// Main rendering loop.
		while (!Display.isCloseRequested()) {

			// Clear color and depth buffer.
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// If display gets resized, update projection matrix and viewport.
			if (Display.wasResized()) {
				loadMatrix();
			}

			// Load model view matrix, translate and rotate it.
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glTranslatef(0, 0, -5);
			glRotatef((float) Math.toRadians(rotation), 1.0f, 1.0f, 1.0f);

			// Update rotation for next loop.
			rotation = (rotation + 0.2f) % 360;

			// Draw 3D cube using triangles.
			glBegin(GL_TRIANGLES);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, 0.5f);

			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, 0.5f, -0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);

			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, 0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(0.5f, -0.5f, -0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, 0.5f, -0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(0.5f, -0.5f, -0.5f);

			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, -0.5f);

			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, 0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, 0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, -0.5f, -0.5f);

			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, -0.5f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(0.5f, -0.5f, -0.5f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(0.5f, 0.5f, -0.5f);

			// Finish draw command and send vertex data to GPU.
			glEnd();

			printContext();

			// Update display, swap buffers.
			Display.update();
		}

		// Destroy texture and display.
		tex.cleanup();
		Display.destroy();
	}

	private static void loadMatrix() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(70.0f, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 100f);
	}
}

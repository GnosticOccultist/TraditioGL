package fr.traditio.gl.demo;

import static fr.traditio.gl.opengl.TGL11.GL_COLOR_BUFFER_BIT;
import static fr.traditio.gl.opengl.TGL11.GL_CULL_FACE;
import static fr.traditio.gl.opengl.TGL11.GL_DEPTH_BUFFER_BIT;
import static fr.traditio.gl.opengl.TGL11.GL_DEPTH_TEST;
import static fr.traditio.gl.opengl.TGL11.GL_MODELVIEW;
import static fr.traditio.gl.opengl.TGL11.GL_PROJECTION;
import static fr.traditio.gl.opengl.TGL11.GL_TEXTURE_2D;
import static fr.traditio.gl.opengl.TGL11.GL_TRIANGLES;
import static fr.traditio.gl.opengl.TGL11.glBegin;
import static fr.traditio.gl.opengl.TGL11.glBindTexture;
import static fr.traditio.gl.opengl.TGL11.glClear;
import static fr.traditio.gl.opengl.TGL11.glClearColor;
import static fr.traditio.gl.opengl.TGL11.glEnable;
import static fr.traditio.gl.opengl.TGL11.glEnd;
import static fr.traditio.gl.opengl.TGL11.glLoadIdentity;
import static fr.traditio.gl.opengl.TGL11.glMatrixMode;
import static fr.traditio.gl.opengl.TGL11.glRotatef;
import static fr.traditio.gl.opengl.TGL11.glTexCoord2f;
import static fr.traditio.gl.opengl.TGL11.glTranslatef;
import static fr.traditio.gl.opengl.TGL11.glVertex3f;
import static fr.traditio.gl.opengl.TGL11.glViewport;
import static fr.traditio.gl.opengl.TGL11.gluPerspective;
import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;
import fr.traditio.gl.opengl.Texture;

public class HelloTraditioGL {

	private static float rotation;

	public static void main(String[] args) {
		try {
			// Create Display using LWJGL 2 style.
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("HelloTraditioGL");
			Display.setResizable(true);
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		// Enable some render state.
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);

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
			glTranslatef(0, 0, -2);
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

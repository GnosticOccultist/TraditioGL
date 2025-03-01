package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;
import fr.traditio.gl.opengl.Texture;

import static fr.traditio.gl.opengl.TGL11.*;

public class DisplayDemo {

	private static float rotation;

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("DisplayDemo");
			Display.setResizable(true);
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0, 0, 0, 0);

		Texture tex = new Texture("/textures/wood.jpg");
		glBindTexture(GL_TEXTURE_2D, tex.id());

		loadMatrix();

		while (!Display.isCloseRequested()) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (Display.wasResized()) {
				loadMatrix();
			}

			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glTranslatef(0, 0, -2);
			glRotatef((float) Math.toRadians(rotation), 1.0f, 1.0f, 1.0f);

			rotation = (rotation + 0.2f) % 360;

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

			glEnd();

			glRectf(-0.5f, 0.5f, 0.5F, -0.5f);

			Display.update();
		}

		Display.destroy();
	}

	private static void loadMatrix() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(70.0f, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 100f);
	}
}

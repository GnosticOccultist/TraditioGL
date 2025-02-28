package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;
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
		glClearColor(0, 0, 0, 0);

		loadMatrix();

		glColor4f(1, 0, 1, 1);

		while (!Display.isCloseRequested()) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (Display.wasResized()) {
				loadMatrix();
			}

			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glTranslatef(0, 0, -5);
			glRotatef((float) Math.toRadians(rotation), 1.0f, 1.0f, 1.0f);

			rotation = (rotation + 0.5f) % 360;

			glBegin(GL_TRIANGLES);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glVertex3f(0.5f, -0.5f, 0.5f);

			glVertex3f(-0.5f, 0.5f, -0.5f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glVertex3f(0.5f, 0.5f, -0.5f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glVertex3f(0.5f, 0.5f, 0.5f);

			glVertex3f(0.5f, 0.5f, 0.5f);
			glVertex3f(0.5f, -0.5f, 0.5f);
			glVertex3f(0.5f, -0.5f, -0.5f);
			glVertex3f(0.5f, 0.5f, -0.5f);
			glVertex3f(0.5f, 0.5f, 0.5f);
			glVertex3f(0.5f, -0.5f, -0.5f);

			glVertex3f(-0.5f, -0.5f, -0.5f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glVertex3f(-0.5f, 0.5f, 0.5f);
			glVertex3f(-0.5f, 0.5f, -0.5f);

			glVertex3f(0.5f, -0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glVertex3f(0.5f, -0.5f, 0.5f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glVertex3f(0.5f, -0.5f, -0.5f);

			glVertex3f(0.5f, -0.5f, -0.5f);
			glVertex3f(-0.5f, -0.5f, -0.5f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glVertex3f(0.5f, -0.5f, -0.5f);
			glVertex3f(-0.5f, 0.5f, -0.5f);
			glVertex3f(0.5f, 0.5f, -0.5f);

			glEnd();

			glRectf(-0.5f, 0.5f, 0.5F, -0.5f);

			printContext();

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

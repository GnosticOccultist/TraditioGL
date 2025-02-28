package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;
import static fr.traditio.gl.opengl.TGL11.*;

public class DisplayDemo {

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("DisplayDemo");
			Display.setResizable(true);
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {

			glClear(GL_COLOR_BUFFER_BIT);

			glClearColor(0, 0, 0, 0);
			
			if (Display.wasResized()) {
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
			}

			glRectf(-0.5f, 0.5f, 0.5F, -0.5f);

			printContext();

			Display.update();
		}

		Display.destroy();
	}
}

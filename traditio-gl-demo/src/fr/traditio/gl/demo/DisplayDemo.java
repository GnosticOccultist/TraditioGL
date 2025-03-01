package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;

import static fr.traditio.gl.opengl.TGL11.*;

public class DisplayDemo {

	public static void main(String[] args) {
		try {
			// Create Display using LWJGL 2 style.
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("DisplayDemo");
			Display.setResizable(true);
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		// Define clear color to magenta.
		glClearColor(1, 0, 1, 1);

		// Main rendering loop.
		while (!Display.isCloseRequested()) {

			// Clear color buffer.
			glClear(GL_COLOR_BUFFER_BIT);

			// Update display, swap buffers.
			Display.update();
		}

		// Destroy display.
		Display.destroy();
	}
}

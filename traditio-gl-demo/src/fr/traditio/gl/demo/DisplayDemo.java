package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.DisplayMode;

public class DisplayDemo {

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("DisplayDemo");
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {
			try {
				Display.setDisplayMode(new DisplayMode(1280, 720));
			} catch (TraditioGLException e) {
				e.printStackTrace();
			}
			
			Display.update();
		}

		Display.destroy();
	}
}

package fr.traditio.gl.demo;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;

public class DisplayDemo {

	public static void main(String[] args) {
		try {
			Display.create();
		} catch (TraditioGLException e) {
			e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {
			Display.update();
		}
		
		Display.destroy();
	}
}

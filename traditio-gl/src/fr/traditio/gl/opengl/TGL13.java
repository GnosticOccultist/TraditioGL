package fr.traditio.gl.opengl;

import fr.traditio.gl.display.Display;

public class TGL13 extends TGL11 {

	public static final int GL_MULTISAMPLE = 0x809D;

	protected TGL13() {
		throw new UnsupportedOperationException();
	}

	public static void glEnable(int target) {
		TGL11.glEnable(target);
		var c = TGLContext.get();
		if (target == GL_MULTISAMPLE && !c.enableMultisample) {
			c.enableMultisample = true;
			c.changeSampleCount(Display.getPixelFormat().getSamples());
		}
	}

	public static void glDisable(int target) {
		TGL11.glDisable(target);
		var c = TGLContext.get();
		if (target == GL_MULTISAMPLE && c.enableMultisample) {
			c.enableMultisample = false;
			c.changeSampleCount(0);
		}
	}
}

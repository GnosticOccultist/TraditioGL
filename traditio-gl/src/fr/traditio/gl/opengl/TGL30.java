package fr.traditio.gl.opengl;

import org.lwjgl.opengl.GL11;

public class TGL30 extends TGL21 {

	public static final int GL_FRAMEBUFFER_SRGB = 0x8DB9;

	protected TGL30() {
		throw new UnsupportedOperationException();
	}

	public static void glEnable(int target) {
		TGL13.glEnable(target);
		var c = TGLContext.get();
		if (target == GL_FRAMEBUFFER_SRGB && !c.sRGB) {
			c.sRGB = true;
			GL11.glEnable(target);
		}
	}

	public static void glDisable(int target) {
		TGL13.glDisable(target);
		var c = TGLContext.get();
		if (target == GL_FRAMEBUFFER_SRGB && c.sRGB) {
			c.sRGB = false;
			GL11.glDisable(target);
		}
	}
}

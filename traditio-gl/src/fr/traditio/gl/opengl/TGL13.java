package fr.traditio.gl.opengl;

public class TGL13 extends TGL11 {

	public static final int GL_MULTISAMPLE = 0x809D;

	public static final int GL_SAMPLE_COUNT_HINT = 0x809D;
	public static final int GL_SAMPLE_COUNT_1_BIT = 1;
	public static final int GL_SAMPLE_COUNT_2_BIT = 2;
	public static final int GL_SAMPLE_COUNT_4_BIT = 4;
	public static final int GL_SAMPLE_COUNT_8_BIT = 8;
	public static final int GL_SAMPLE_COUNT_16_BIT = 16;
	public static final int GL_SAMPLE_COUNT_32_BIT = 32;

	protected TGL13() {
		throw new UnsupportedOperationException();
	}

	public static void glEnable(int target) {
		TGL11.glEnable(target);
		var c = TGLContext.get();
		if (target == GL_MULTISAMPLE && !c.enableMultisample) {
			c.enableMultisample = true;
			c.changeSampleCount(c.sampleCount);
		}
	}

	public static void glDisable(int target) {
		TGL11.glDisable(target);
		var c = TGLContext.get();
		if (target == GL_MULTISAMPLE && c.enableMultisample) {
			c.enableMultisample = false;
			c.changeSampleCount(GL_SAMPLE_COUNT_1_BIT);
		}
	}

	public static void glHint(int target, int hint) {
		var c = TGLContext.get();
		if (target == GL_SAMPLE_COUNT_HINT && c.sampleCount != hint) {
			c.sampleCount = hint;
			c.changeSampleCount(hint);
		} else {
			TGL11.glHint(target, hint);
		}
	}
}

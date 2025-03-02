package fr.traditio.gl.opengl;

public class TGL14 extends TGL11 {

	public static final int GL_FOG_COORDINATE_SOURCE = 0x8450;
	public static final int GL_FOG_COORDINATE = 0x8451;
	public static final int GL_FRAGMENT_DEPTH = 0x8452;

	public static void glFogi(int pname, int param) {
		var c = TGLContext.get();
		var changed = false;
		TGL11.glFogi(pname, param);
		switch (pname) {
		case GL_FOG_COORDINATE_SOURCE:
			changed = (c.useFogCoord && param != GL_FOG_COORDINATE) || (!c.useFogCoord && param == GL_FOG_COORDINATE);
			c.useFogCoord = param == GL_FOG_COORDINATE;
			if (changed) {
				c.changeDefine("USE_FOG_COORD", c.useFogCoord);
			}
			break;
		}
	}

	public static void glFogf(int pname, float param) {
		var c = TGLContext.get();
		var changed = false;
		TGL11.glFogf(pname, param);
		switch (pname) {
		case GL_FOG_COORDINATE_SOURCE:
			changed = c.useFogCoord && param != GL_FOG_COORDINATE;
			c.useFogCoord = param == GL_FOG_COORDINATE;
			if (changed) {
				c.changeDefine("USE_FOG_COORD", c.useFogCoord);
			}
			break;
		}
	}

	public static void glFogCoordf(float coord) {
		var c = TGLContext.get();
		c.fogCoord = coord;
	}
}

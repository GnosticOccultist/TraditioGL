package fr.traditio.gl.opengl;

import java.nio.ByteBuffer;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.Vector3f;
import fr.traditio.gl.math.Vector4f;

class Light {

	static final int SIZE = (4 + 4 + 4 + 4 + 4 + 3 + 3 + 2) * Float.BYTES;

	boolean enabled = false;

	final Vector4f position = new Vector4f(0.0f, 0.0f, 1.0f, 0.0f);

	final Color4f ambient = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Color4f diffuse = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Color4f specular = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Vector3f direction = new Vector3f(0.0f, 0.0f, -1.0f);

	float spotExponent = 0.0f;

	float spotCutoff = 180.0f;

	final Vector3f attenuation = new Vector3f(1.0f, 0.0f, 0.0f);

	void fill(ByteBuffer buffer) {
		buffer.putInt((byte) (enabled ? 1 : 0));
		buffer.putInt(0).putInt(0).putInt(0);
		buffer.putFloat(position.x()).putFloat(position.y()).putFloat(position.z()).putFloat(position.w());
		buffer.putFloat(ambient.r()).putFloat(ambient.g()).putFloat(ambient.b()).putFloat(ambient.a());
		buffer.putFloat(diffuse.r()).putFloat(diffuse.g()).putFloat(diffuse.b()).putFloat(diffuse.a());
		buffer.putFloat(specular.r()).putFloat(specular.g()).putFloat(specular.b()).putFloat(specular.a());
		buffer.putFloat(direction.x()).putFloat(direction.y()).putFloat(direction.z());
		buffer.putFloat(spotExponent);
		buffer.putFloat(attenuation.x()).putFloat(attenuation.y()).putFloat(attenuation.z());
		buffer.putFloat(spotCutoff);
	}
}

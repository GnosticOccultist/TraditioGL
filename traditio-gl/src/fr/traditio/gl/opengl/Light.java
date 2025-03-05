package fr.traditio.gl.opengl;

import fr.traditio.gl.math.Color4f;
import fr.traditio.gl.math.Vector3f;
import fr.traditio.gl.math.Vector4f;

class Light {

	boolean enabled = false;

	final Vector4f position = new Vector4f(0.0f, 0.0f, 1.0f, 0.0f);

	final Color4f ambient = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Color4f diffuse = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Color4f specular = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);

	final Vector3f direction = new Vector3f(0.0f, 0.0f, -1.0f);

	float spotExponent = 0.0f;

	float spotCutoff = 180.0f;

	final Vector3f attenuation = new Vector3f(1.0f, 0.0f, 0.0f);
}

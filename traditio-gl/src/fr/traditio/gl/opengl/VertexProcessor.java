package fr.traditio.gl.opengl;

public interface VertexProcessor {

	void allocData(long size);

	void populateData(float[] data);

	void draw(PrimitiveMode mode, int amount);

	long getAllocatedSize();

	void destroy();
}

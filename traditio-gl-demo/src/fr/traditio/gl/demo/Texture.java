package fr.traditio.gl.demo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static fr.traditio.gl.opengl.TGL11.*;

public final class Texture {

	private int id = -1;

	private int width, height;

	public Texture(String path) {
		int[] pixels = null;
		try {
			BufferedImage img = ImageIO.read(Texture.class.getResource(path));
			width = img.getWidth();
			height = img.getHeight();
			pixels = new int[width * height];
			img.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[pixels.length];
		for (int i = 0; i < data.length; ++i) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);

		var buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data);
		buffer.flip();

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		
		MemoryUtil.memFree(buffer);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		this.id = id;
	}

	public int id() {
		return id;
	}

	public void cleanup() {
		glDeleteTextures(id);
		this.id = -1;
	}
}

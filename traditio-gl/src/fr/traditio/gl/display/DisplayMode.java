package fr.traditio.gl.display;

public final class DisplayMode {

	/**
	 * The properties of the display mode.
	 */
	private final int width, height, bpp, freq;
	/**
	 * Whether the instance can be used for fullscreen modes.
	 */
	private final boolean fullscreen;

	/**
	 * Constructs a new <code>DisplayMode</code>. Display modes constructed through
	 * the public constructor can only be used to specify the dimensions of the
	 * {@link Display} in windowed mode. To get the available modes for fullscreen
	 * modes, use {@link Display#getAvailableDisplayModes()}.
	 *
	 * @param width  The display width.
	 * @param height The display height.
	 * @see Display
	 */
	public DisplayMode(int width, int height) {
		this(width, height, 0, 0, false);
	}

	DisplayMode(int width, int height, int bpp, int freq) {
		this(width, height, bpp, freq, true);
	}

	DisplayMode(int width, int height, int bpp, int freq, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.bpp = bpp;
		this.freq = freq;
		this.fullscreen = fullscreen;
	}

	public boolean isFullscreenCapable() {
		return fullscreen;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getBitsPerPixel() {
		return bpp;
	}

	public int getFrequency() {
		return freq;
	}

	/**
	 * Tests for <code>DisplayMode</code> equality.
	 *
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}

		DisplayMode dm = (DisplayMode) obj;
		return dm.width == width && dm.height == height && dm.bpp == bpp && dm.freq == freq;
	}

	/**
	 * Retrieves the hashcode for this <code>DisplayMode</code>.
	 *
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return width ^ height ^ freq ^ bpp;
	}

	/**
	 * Retrieves a String representation of this <code>DisplayMode</code>.
	 *
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(32);
		sb.append(width);
		sb.append(" x ");
		sb.append(height);
		sb.append(" x ");
		sb.append(bpp);
		sb.append(" @");
		sb.append(freq);
		sb.append("Hz");
		return sb.toString();
	}
}

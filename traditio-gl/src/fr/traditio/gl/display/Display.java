package fr.traditio.gl.display;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.opengl.OpenGLException;

/**
 * 
 * @author GnosticOccultist
 */
public class Display {

	/**
	 * The X coordinate of the window.
	 */
	private static int x = -1;
	/**
	 * The Y coordinate of the window.
	 */
	private static int y = -1;
	/**
	 * The width of the window.
	 */
	private static int width = 0;
	/**
	 * The height of the window.
	 */
	private static int height = 0;
	/**
	 * The title of the window.
	 */
	private static String title = "Game";
	/**
	 * Whether fullscreen mode is enabled.
	 */
	private static boolean fullscreen;
	/**
	 * The buffer swap interval.
	 */
	private static int swapInterval;
	/**
	 * Whether the window is created.
	 */
	private static boolean created;
	/**
	 * Whether the window is resizable.
	 */
	private static boolean resizable;

	private static long glfwWindowHandle;

	private static final DisplayMode initialMode;

	private static DisplayMode currentMode;
	private static GLCapabilities capabilities;

	static {
		try {
			currentMode = initialMode = init();
		} catch (TraditioGLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Private constructor to limit instantiation of <code>Display</code>.
	 */
	private Display() {
	}

	private static DisplayMode init() throws TraditioGLException {
		if (!GLFW.glfwInit()) {
			throw new TraditioGLException("Unable to initialize GLFW!");
		}

		var monitor = GLFW.glfwGetPrimaryMonitor();
		var videoMode = GLFW.glfwGetVideoMode(monitor);

		var bpp = videoMode.redBits() + videoMode.blueBits() + videoMode.greenBits();
		var primaryMode = new DisplayMode(videoMode.width(), videoMode.height(), bpp, videoMode.refreshRate(), false);

		return primaryMode;
	}

	public static void create() throws TraditioGLException {
		if (isCreated()) {
			throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
		}

		createWindow();

		makeCurrentAndSetSwapInterval();
	}

	private static void createWindow() throws TraditioGLException {
		if (isCreated()) {
			return;
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

		glfwWindowHandle = GLFW.glfwCreateWindow(getEffectiveMode().getWidth(), getEffectiveMode().getHeight(), title,
				MemoryUtil.NULL, MemoryUtil.NULL);
		if (glfwWindowHandle == MemoryUtil.NULL) {
			destroyWindow();
			throw new TraditioGLException("Failed to create the GLFW window!");
		}

		width = Display.getDisplayMode().getWidth();
		height = Display.getDisplayMode().getHeight();

		created = true;

		assert glfwWindowHandle != MemoryUtil.NULL;
		GLFW.glfwShowWindow(glfwWindowHandle);
	}

	private static void makeCurrentAndSetSwapInterval() throws TraditioGLException {
		assert glfwWindowHandle != MemoryUtil.NULL;
		GLFW.glfwMakeContextCurrent(glfwWindowHandle);
		capabilities = GL.createCapabilities();

		try {
			checkGLError();
		} catch (OpenGLException ex) {
			System.err.println("OpenGL error during context creation: " + ex.getMessage());
		}
		setSwapInterval(swapInterval);
	}

	/**
	 * Update the <code>Display</code>. If the window is visible clears the dirty
	 * flag and calls swapBuffers() and finally polls the input devices.
	 */
	public static void update() {
		update(true);
	}

	/**
	 * Update the window. If the window is visible clears the dirty flag and calls
	 * swapBuffers() and finally polls the input devices if processMessages is true.
	 *
	 * @param processMessages Poll input devices if true
	 */
	public static void update(boolean processMessages) {
		if (!isCreated())
			throw new IllegalStateException("Display not created!");

		// TODO: We paint only when the window is visible or dirty
		try {
			swapBuffers();
		} catch (TraditioGLException ex) {
			throw new RuntimeException(ex);
		}

		if (processMessages) {
			GLFW.glfwPollEvents();
		}
	}

	/**
	 * Swap the display buffers. This method is called from update(), and should
	 * normally not be called by the application.
	 *
	 * @throws OpenGLException if an OpenGL error has occured since the last call to
	 *                         glGetError()
	 */
	public static void swapBuffers() throws TraditioGLException {
		if (!isCreated()) {
			throw new IllegalStateException("Display not created!");
		}

		if (Configuration.DEBUG.get(false)) {
			checkGLError();
		}

		assert glfwWindowHandle != MemoryUtil.NULL;
		GLFW.glfwSwapBuffers(glfwWindowHandle);
	}

	public static void destroy() {
		destroyWindow();
	}

	private static void destroyWindow() {
		if (glfwWindowHandle != MemoryUtil.NULL) {
			Callbacks.glfwFreeCallbacks(glfwWindowHandle);

			GLFW.glfwDestroyWindow(glfwWindowHandle);
			glfwWindowHandle = MemoryUtil.NULL;
		}

		created = false;

		GLFW.glfwTerminate();
	}

	public static DisplayMode getDisplayMode() {
		return currentMode;
	}

	public static void setDisplayMode(DisplayMode mode) throws TraditioGLException {
		if (mode == null) {
			throw new NullPointerException("Display mode must be non-null!");
		}

		currentMode = mode;
		if (isCreated()) {
			destroyWindow();

			createWindow();
		}
	}

	private static void checkGLError() throws OpenGLException {
		var err = GL11.glGetError();
		if (err != GL11.GL_NO_ERROR) {
			throw new OpenGLException(err);
		}
	}

	private static DisplayMode getEffectiveMode() {
		return currentMode;
	}

	public static DisplayMode[] getAvailableDisplayModes() throws TraditioGLException {
		throw new TraditioGLException("Not implemented yet!");
	}

	/**
	 * Return whether the user or operating system has asked the window
	 * <code>Display</code> to close.
	 * 
	 * @return Whether the window should close.
	 */
	public static boolean isCloseRequested() {
		if (!isCreated()) {
			throw new IllegalStateException("Cannot determine close requested state of uncreated window!");
		}

		assert glfwWindowHandle != MemoryUtil.NULL;
		return GLFW.glfwWindowShouldClose(glfwWindowHandle);
	}

	/**
	 * Return whether the window <code>Display</code> is created.
	 * 
	 * @return Whether a window is created.
	 */
	public static boolean isCreated() {
		return created;
	}

	/**
	 * Return whether the window <code>Display</code> is resizable.
	 * 
	 * @return Whether the window is resizable.
	 */
	public static boolean isResizable() {
		return resizable;
	}

	/**
	 * Enable or disable vertical monitor synchronization. This call is a
	 * best-attempt at changing the vertical refresh synchronization of the monitor,
	 * and is not guaranteed to be successful.
	 *
	 * @param sync Whether to synchronize monitor.
	 */
	public static void setVSyncEnabled(boolean sync) {
		setSwapInterval(sync ? 1 : 0);
	}

	/**
	 * Set the buffer swap interval. This call is a best-attempt at changing the
	 * monitor swap interval, which is the minimum periodicity of color buffer
	 * swaps, measured in video frame periods, and is not guaranteed to be
	 * successful.
	 * <p/>
	 * A video frame period is the time required to display a full frame of video
	 * data.
	 *
	 * @param value The swap interval in frames, 0 to disable.
	 */
	public static void setSwapInterval(int value) {
		swapInterval = value;
		if (isCreated()) {
			GLFW.glfwSwapInterval(swapInterval);
		}
	}
}

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
	/**
	 * The window native handle.
	 */
	private static long glfwWindowHandle = MemoryUtil.NULL;
	/**
	 * The initial display mode.
	 */
	private static final DisplayMode initialMode;
	/**
	 * The current display mode.
	 */
	private static DisplayMode currentMode;
	/**
	 * The OpenGL context capabilities.
	 */
	private static GLCapabilities capabilities;

	static {
		try {
			currentMode = initialMode = init();
			Runtime.getRuntime().addShutdownHook(new Thread(Display::terminate));
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
		GLFW.glfwSetErrorCallback(
				(error, desc) -> System.err.println("GLFW error: " + error + " description: " + desc));

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
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_POSITION_X, getWindowX());
		GLFW.glfwWindowHint(GLFW.GLFW_POSITION_Y, getWindowY());

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
		makeCurrent();

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
	}

	private static void terminate() {
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public static DisplayMode getDisplayMode() {
		return currentMode;
	}

	public static void setDisplayMode(DisplayMode mode) throws TraditioGLException {
		if (mode == null) {
			throw new NullPointerException("Display mode must be non-null!");
		}
		
		if (currentMode.equals(mode)) {
			return;
		}

		currentMode = mode;
		if (isCreated()) {
			destroyWindow();

			try {
				createWindow();
				makeCurrentAndSetSwapInterval();
			} catch (TraditioGLException ex) {
				throw ex;
			}
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
	 * Returns whether the <code>Display</code>'s context is current in the current
	 * thread.
	 * 
	 * @return Whether the context is current.
	 */
	public static boolean isCurrent() {
		var current = GLFW.glfwGetCurrentContext();
		if (current == MemoryUtil.NULL) {
			return false;
		}

		return current == glfwWindowHandle;
	}

	public static void makeCurrent() throws TraditioGLException {
		if (glfwWindowHandle == MemoryUtil.NULL) {
			throw new TraditioGLException("Cannot make display's context current on non-existent window!");
		}

		GLFW.glfwMakeContextCurrent(glfwWindowHandle);
		capabilities = GL.createCapabilities();
	}

	public static void releaseContext() throws TraditioGLException {
		if (!isCurrent()) {
			throw new TraditioGLException("Cannot release non-current display's context!");
		}

		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
		capabilities = null;
	}

	private static int getWindowX() {
		if (!isFullscreen()) {
			// If no display location set, center window.
			if (x == -1) {
				return Math.max(0, (initialMode.getWidth() - currentMode.getWidth()) / 2);
			} else {
				return x;
			}
		} else {
			return 0;
		}
	}

	private static int getWindowY() {
		if (!isFullscreen()) {
			// If no display location set, center window.
			if (y == -1) {
				return Math.max(0, (initialMode.getHeight() - currentMode.getHeight()) / 2);
			} else {
				return y;
			}
		} else {
			return 0;
		}
	}

	public static int getWidth() {
		if (Display.isFullscreen()) {
			return Display.getDisplayMode().getWidth();
		}

		return width;
	}

	public static int getHeight() {
		if (Display.isFullscreen()) {
			return Display.getDisplayMode().getHeight();
		}

		return height;
	}

	public static boolean isFullscreen() {
		return fullscreen;
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
	 * Enable or disable the window <code>Display</code> to be resized.
	 *
	 * @param enable Whether the window should be resizable.
	 */
	public static void setResizable(boolean enable) {
		if (resizable == enable) {
			return;
		}

		resizable = enable;
		if (isCreated()) {
			destroyWindow();

			try {
				createWindow();
				makeCurrentAndSetSwapInterval();
			} catch (TraditioGLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Return the title of the window <code>Display</code>.
	 *
	 * @return The window title or empty for no title.
	 */
	public static String getTitle() {
		return title;
	}

	/**
	 * Set the title of the window <code>Display</code>.
	 *
	 * @param newTitle The new window title or null for no title.
	 */
	public static void setTitle(String newTitle) {
		if (newTitle == null) {
			newTitle = "";
		}
		title = newTitle;
		if (isCreated()) {
			assert glfwWindowHandle != MemoryUtil.NULL;
			GLFW.glfwSetWindowTitle(glfwWindowHandle, title);
		}
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

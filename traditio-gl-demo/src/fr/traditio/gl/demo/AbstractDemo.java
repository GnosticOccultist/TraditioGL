package fr.traditio.gl.demo;

import static fr.traditio.gl.opengl.TGL11.GL_COLOR_BUFFER_BIT;
import static fr.traditio.gl.opengl.TGL11.GL_DEPTH_BUFFER_BIT;
import static fr.traditio.gl.opengl.TGL11.glClear;
import static fr.traditio.gl.opengl.TGL11.printContext;

import fr.traditio.gl.TraditioGLException;
import fr.traditio.gl.display.Display;
import fr.traditio.gl.display.PixelFormat;

public abstract class AbstractDemo {

	protected long lastTime = System.currentTimeMillis();

	protected String title = null;

	protected Timer timer = new Timer();

	protected void start(PixelFormat format) throws TraditioGLException {
		Display.create(format);

		initialize();

		title = Display.getTitle();

		timer.reset();

		// Main rendering loop.
		while (!Display.isCloseRequested()) {
			// Clear color and depth buffer.
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// If display gets resized, update projection matrix and viewport.
			if (Display.wasResized()) {
				resized();
			}

			timer.update();

			if (System.currentTimeMillis() - lastTime > 1000) {
				Display.setTitle(title + " " + String.format("%.2f fps", timer.getFrameRate()));
				lastTime = System.currentTimeMillis();

				printContext();
			}

			render(timer);

			// Update display, swap buffers.
			Display.update();
		}

		// Destroy display.
		cleanup();
		Display.destroy();
	}

	protected abstract void initialize();

	protected abstract void render(Timer timer);

	protected abstract void cleanup();

	protected abstract void resized();

	protected class Timer {

		protected static final long TIMER_RESOLUTION = 1_000_000_000L;
		protected static final double INVERSE_TIMER_RESOLUTION = 1.0 / Timer.TIMER_RESOLUTION;

		protected long startTime;
		protected long previousFrameTime;
		protected double tpf;
		protected double fps;

		public Timer() {
			startTime = System.nanoTime();
		}

		public double getTimeInSeconds() {
			return getTime() * Timer.INVERSE_TIMER_RESOLUTION;
		}

		public long getTime() {
			return System.nanoTime() - startTime;
		}

		public long getResolution() {
			return Timer.TIMER_RESOLUTION;
		}

		public double getFrameRate() {
			return fps;
		}

		public double getTimePerFrame() {
			return tpf;
		}

		public long getPreviousFrameTime() {
			return previousFrameTime;
		}

		public void update() {
			var time = getTime();
			tpf = (time - previousFrameTime) * Timer.INVERSE_TIMER_RESOLUTION;
			fps = 1.0 / tpf;
			previousFrameTime = time;
		}

		public void reset() {
			startTime = System.nanoTime();
			previousFrameTime = getTime();
		}
	}
}

package net.pixael;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import net.fantasticfantasy.mainkit.AbstractUtil;
import net.fantasticfantasy.mainkit.BuffersTool;
import net.fantasticfantasy.mainkit.ConsoleManager;
import net.fantasticfantasy.mainkit.Logger;
import net.fantasticfantasy.mainkit.maths.Vector2f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.client.Crash;
import net.pixael.client.GLStateManager;
import net.pixael.client.Options;
import net.pixael.client.Settings;
import net.pixael.client.Textures;
import net.pixael.client.gui.GUIElement;
import net.pixael.client.gui.GUIScreen;
import net.pixael.client.gui.TitleScreen;
import net.pixael.client.init.PixaelConfiguration;
import net.pixael.entity.Player;
import net.pixael.init.Assets;
import net.pixael.init.UserSettings;
import net.pixael.render.BlockRenderer;
import net.pixael.render.GUIRenderer;
import net.pixael.render.GUIShapeDrawer;
import net.pixael.render.SkyRenderer;
import net.pixael.render.data.Color;
import net.pixael.render.data.GLDataManager;
import net.pixael.util.ResourcesUtil;
import net.pixael.world.World;

public class Pixael {
	
	private static Pixael staticPixael;
	
	private long window;
	private int width, height;
	private boolean running;
	
	private Settings settings;
	private Options options;
	private Logger logger;

	private Thread tickingThread;
	
	private GUIRenderer guiRenderer;
	private GUIShapeDrawer guiDrawer;
	private GUIElement splashscreen;
	private List<GUIScreen> screens;
	private int currentScreen;
	
	private World world;
	private Player player;
	private GUIElement cursor;
	private boolean worldFocus;
	private SkyRenderer skyRenderer;
	
	//Temporary
	private BlockRenderer br;
	
	/**
	 * Creates a {@code Pixael} game instance with the specified configuration
	 * 
	 * @param conf - The {@code Pixael} game configuration
	 */
	public Pixael(PixaelConfiguration conf) {
		staticPixael = this;
		this.width = conf.width;
		this.height = conf.height;
		this.player = new Player(conf.username);
	}
	
	public void start() {
		try {
			Thread.currentThread().setName("Pixael Main Client");
			this.logger = new Logger();
			if (GLFW.glfwInit()) {
				this.logger.info("Initialized GLFW!");
			} else {
				Crash crash = new Crash("Could not initialize GLFW",
						"An unexpected error happened while initializing GLFW...");
				crash.send();
			}
			ConsoleManager.start();
			this.settings = UserSettings.load("settings");
			this.options = new Options(this.settings);
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			this.window = GLFW.glfwCreateWindow(this.width, this.height, "Pixael " + getVersion(), MemoryUtil.NULL, GLFW.GLFW_FALSE);
			int[] srcdim = AbstractUtil.getUserScreenSize();
			GLFW.glfwSetWindowSizeLimits(this.window, 820, 560, 9999, 9999);
			GLFW.glfwSetWindowPos(this.window, (srcdim[0] - this.width) / 2, (srcdim[1] - this.height) / 2);
			GLFW.glfwShowWindow(this.window);
			GLFW.glfwSetKeyCallback(this.window, Keyboard.getKeyboard());
			GLFW.glfwSetMouseButtonCallback(this.window, Mouse.getInstance());
			GLFW.glfwMakeContextCurrent(this.window);
			GL.createCapabilities();
			if (ResourcesUtil.makeUnexistingFilesAndDirectories()) {
				this.logger.info("Created missing files and directories");
			}
			GLDataManager.init();
			GLStateManager.setToDefaultGLConfiguration();
			Assets.init();
			this.setWindowIcons();
			this.guiRenderer = new GUIRenderer();
			this.splashscreen = new GUIElement(Textures.get("pixael:splash_screen"), 0, 0, this.width, this.height);
			this.showSplashScreen();
			this.guiDrawer = new GUIShapeDrawer();
			this.cursor = new GUIElement(Textures.get("pixael:world_cursor"), this.width / 2 - 7, this.height / 2 - 7, 14, 14);
			this.screens = new ArrayList<>();
			this.screens.add(new TitleScreen());
			this.br = new BlockRenderer();
			this.skyRenderer = new SkyRenderer();
			GLStateManager.setClearColor(Color.CYAN);
			this.running = true;
			this.startTickingThread();
			//Thread.sleep(3000);
			this.run();
		} catch (Exception e) {
			this.handleException(e);
		}
	}
	
	private void run() {
		try {
			GLFW.glfwSetCursorPos(this.window, this.width / 2, this.height / 2);
			while (this.running) {
				this.loop();
			}
		} catch (Exception e) {
			this.handleException(e);
		}
		this.shutdown();
	}
	
	private void loop() throws Exception {
		if (GLFW.glfwWindowShouldClose(this.window)) {
			this.stop();
		}
		this.clear();
		this.resizeWindow();
		if (this.worldFocus) {
			this.grabMouseCursor();
			this.skyRenderer.render();
			this.br.render();
			this.guiRenderer.processElement(this.cursor);
			this.guiRenderer.render();
		}
		this.drawCurrentScreen();
		GLFW.glfwPollEvents();
		this.handleKeyEvents();
		GLFW.glfwSwapBuffers(this.window);
	}
	
	private void tick() throws Exception {
		this.updateCurrentScreen();
	}
	
	private void handleKeyEvents() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			this.requestWorldFocus(true);
		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			this.requestWorldFocus(false);
		}
		if (this.worldFocus) {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
				this.player.move(new Vector3f(0, 0, 0.1f));
			} if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
				this.player.move(new Vector3f(0, 0, -0.1f));
			} if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
				this.player.move(new Vector3f(0.1f, 0, 0));
			} if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
				this.player.move(new Vector3f(-0.1f, 0, 0));
			} if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
				this.player.move(new Vector3f(0, 0.1f, 0));
			} if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				this.player.move(new Vector3f(0, -0.1f, 0));
			}
		}
		if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			System.out.println("Right");
		}
	}
	
	private void resizeWindow() {
		int[] w = new int[1];
		int[] h = new int[1];
		GLFW.glfwGetWindowSize(this.window, w, h);
		if (w[0] != this.width || h[0] != this.height) {
			GL11.glViewport(0, 0, w[0], h[0]);
			this.width = w[0];
			this.height = h[0];
			this.splashscreen.setDimension(w[0], h[0]);
			this.cursor.setLocation(w[0] / 2 - 7, h[0] / 2 - 7);
		}
	}
	
	public void stop() {
		this.running = false;
	}
	
	private void handleException(Exception e) {
		Crash crash = new Crash("Unexpected error",
				"An unexpected error happened :/\n\n" + Crash.throwableToString(e));
		crash.send();
	}
	
	private void shutdown() {
		try {
			this.logger.info("Stopping");
			ConsoleManager.stop();
			UserSettings.save(this.settings);
			Textures.unloadAll();
			this.guiRenderer.cleanUp();
			this.skyRenderer.cleanUp();
			GLDataManager.close();
			this.tickingThread.join();
			GLFW.glfwDestroyWindow(this.window);
			GLFW.glfwTerminate();
			this.logger.info("LWJGL 3 - www.lwjgl.org");
			this.logger.info("PNGDecoder - github.com/MathiasMann/");
			System.out.println("\n\tPixael - Fantastic Fantasy - www.fantasticfantasy.net/pixael/");
			System.exit(0);
		} catch (Exception e) {
			this.handleException(e);
		}
	}
	
	private void grabMouseCursor() {
		int xcenter = this.width / 2;
		int ycenter = this.height / 2;
		float[] mouseLoc = this.getMousePointerLocation();
		float xmove = mouseLoc[0] - xcenter;
		float ymove = mouseLoc[1] - ycenter;
		float sensivity = 6f / this.settings.getf("mouse_sensivity");
		float playerXRot = (float) xmove / sensivity;
		float playerYRot = (float) ymove / sensivity;
		Vector2f mouseMove = new Vector2f(playerYRot, playerXRot);
		this.player.rotate(mouseMove.toVector3f());
		GLFW.glfwSetCursorPos(this.window, xcenter, ycenter);
	}
	
	private void startTickingThread() throws Exception {
		final long targetTime = 1000 / 20;
		this.tickingThread = new Thread(new Runnable() {
			public void run() {
				try {
					Logger logger = new Logger();
					long timeOverflow = 0;
					boolean overflowStatus = false;
					while (Pixael.this.running) {
						long waitTime = timeOverflow > 50 ? 50 : timeOverflow;
						long start = System.nanoTime();
						Pixael.this.tick();
						long elapsed = System.nanoTime() - start;
						long toMillis = elapsed / 1000000;
						long wait = targetTime - toMillis;
						if (wait >= 0) {
							long newWait = wait - waitTime;
							newWait = newWait < 0 ? 0 : newWait;
							Thread.sleep(newWait);
							timeOverflow -= waitTime;
							if (overflowStatus && timeOverflow < 50) {
								logger.info("Ticking is now back to normal!");
								overflowStatus = false;
							}
						} else {
							timeOverflow += -wait;
							if (timeOverflow > 75) {
								logger.warn("Ticking is taking way to much time! (Having now " + timeOverflow + " overflowing millis!)");
								overflowStatus = true;
							}
						}
					}
				} catch (Exception e) {
					Pixael.this.handleException(e);
				}
			}
		}, "Pixael Ticking Thread");
		this.tickingThread.start();
	}
	
	private void drawCurrentScreen() {
		if (this.currentScreen == -1) {
			return;
		}
		this.screens.get(this.currentScreen).process(this.guiRenderer);
		this.guiRenderer.render();
	}
	
	private void updateCurrentScreen() {
		if (this.currentScreen == -1) {
			return;
		}
		this.screens.get(this.currentScreen).tick();
	}
	
	private void setWindowIcons() throws IOException {
		IntBuffer w = MemoryUtil.memAllocInt(1), h = MemoryUtil.memAllocInt(1), comp = MemoryUtil.memAllocInt(1);
		ByteBuffer icon16 = this.readIcon(ResourcesUtil.createAssetInputStream("icon16.png"), 2048);
		ByteBuffer icon32 = this.readIcon(ResourcesUtil.createAssetInputStream("icon32.png"), 4096);
		try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
			ByteBuffer px16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
			icons.position(0).width(w.get(0)).height(h.get(0)).pixels(px16);
			ByteBuffer px32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
			icons.position(1).width(w.get(0)).height(h.get(0)).pixels(px32);
			icons.position(0);
			GLFW.glfwSetWindowIcon(this.window, icons);
			STBImage.stbi_image_free(px16);
			STBImage.stbi_image_free(px32);
		}
		MemoryUtil.memFree(w);
		MemoryUtil.memFree(h);
		MemoryUtil.memFree(comp);
	}
	
	private ByteBuffer readIcon(InputStream icon, int size) throws IOException {
		ByteBuffer buffer = BuffersTool.createByteBuffer(size);
		try (ReadableByteChannel rbc = Channels.newChannel(icon)) {
			while (true) {
				int bytes = rbc.read(buffer);
				if (bytes == -1) {
					break;
				}
				if (buffer.remaining() == 0) {
					buffer = BuffersTool.resizeBuffer(buffer, buffer.capacity() * 2);
				}
			}
		}
		buffer.flip();
		return buffer;
	}
	
	private void showSplashScreen() {
		this.clear();
		this.guiRenderer.processElement(this.splashscreen);
		this.guiRenderer.render();
		GLFW.glfwSwapBuffers(this.window);
	}
	
	private void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ACCUM_BUFFER_BIT);
	}
	
	public void requestWorldFocus(boolean focus) {
		if (this.worldFocus == focus) {
			return;
		}
		this.worldFocus = focus;
		if (focus) {
			GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
			GLFW.glfwSetCursorPos(this.window, this.width / 2, this.height / 2);
			this.currentScreen = -1;
		} else {
			GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			this.currentScreen = 0;
		}
	}
	
	public int[] getWindowSize() {
		return new int[] {this.width, this.height};
	}
	
	public float[] getMousePointerLocation() {
		double[] x = new double[1];
		double[] y = new double[1];
		GLFW.glfwGetCursorPos(this.window, x, y);
		return new float[] {(float) x[0], (float) y[0]};
	}
	
	public Options getOptions() {
		return this.options;
	}
	
	public GUIShapeDrawer getGUIDrawer() {
		return this.guiDrawer;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public static String getVersion() {
		return "pre-alpha 0.0.0.1";
	}
	
	public static Pixael getPixael() {
		return staticPixael;
	}
}

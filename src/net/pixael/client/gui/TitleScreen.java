package net.pixael.client.gui;

import net.pixael.Mouse;
import net.pixael.Pixael;
import net.pixael.client.Textures;

public class TitleScreen extends GUIScreen {
	
	private GUIElement pixaelLogo;
	private GUIElement background;
	private GUIElement startButton;
	private int[] lastWinDim;
	
	public TitleScreen() {
		super();
		this.pixaelLogo = new GUIElement(Textures.get("pixael:title_screen_pixael_logo"), 0, 25, 450, 175);
		this.background = new GUIElement(Textures.get("pixael:title_screen_back"));
		this.startButton = new GUIElement(Textures.get("pixael:title_screen_button_start"), 0, 400, 280, 91);
		this.lastWinDim = new int[2];
	}
	
	public void init() {}
	public void close() {}
	
	public void tick() {
		int[] winDim = Pixael.getPixael().getWindowSize();
		if (this.lastWinDim[0] != winDim[0] || this.lastWinDim[1] != winDim[1]) {
			this.pixaelLogo.setX((winDim[0] - this.pixaelLogo.getWidth()) / 2);
			this.background.setDimension(winDim[0], winDim[1]);
			this.startButton.setX((winDim[0] - this.startButton.getWidth()) / 2);
			if (winDim[1] < 600) {
				this.startButton.setHeight(65);
				this.startButton.setY(winDim[1] - this.startButton.getHeight() - 125);
			} else if (winDim[1] < 650) {
				this.startButton.setHeight(78);
				this.startButton.setY(winDim[1] - this.startButton.getHeight() - 150);
			} else {
				this.startButton.setHeight(91);
				this.startButton.setY(winDim[1] - this.startButton.getHeight() - 175);
			}
		}
		float[] mLoc = Pixael.getPixael().getMousePointerLocation();
		if (this.startButton.isHovered((int) mLoc[0], (int) mLoc[1])) {
			this.startButton.setImage(Textures.get("pixael:title_screen_button_start_hover"));
			if (Mouse.isButtonDown(Mouse.LEFT_BUTTON)) {
				//... set to main game menu GUIScreen
			}
		} else {
			this.startButton.setImage(Textures.get("pixael:title_screen_button_start"));
		}
		this.lastWinDim = winDim;
	}
	
	protected void prepareToRender() {
		super.toRender.add(this.background);
		super.toRender.add(this.pixaelLogo);
		super.toRender.add(this.startButton);
	}
}

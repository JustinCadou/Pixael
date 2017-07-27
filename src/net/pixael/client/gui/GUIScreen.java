package net.pixael.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.pixael.render.GUIRenderer;

public abstract class GUIScreen {
	
	protected List<GUIElement> toRender;
	
	public GUIScreen() {
		this.toRender = new ArrayList<>();
	}
	
	public abstract void init();
	public abstract void close();
	public abstract void tick();
	protected abstract void prepareToRender();
	
	public void process(GUIRenderer renderer) {
		this.prepareToRender();
		for (GUIElement element : this.toRender) {
			renderer.processElement(element);
		}
		this.toRender.clear();
	}
}

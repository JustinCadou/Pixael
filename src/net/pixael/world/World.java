package net.pixael.world;

public class World {
	
	public final String name;
	private long currentTimeTicks;
	
	public World(String name) {
		this.name = name;
	}
	
	public void tick() {
		this.currentTimeTicks++;
	}
	
	public void setTime(long newTime) {
		this.currentTimeTicks = newTime;
	}
	
	public long getTime() {
		return this.currentTimeTicks;
	}
}

package net.pixael.world;

import java.util.ArrayList;
import java.util.List;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.block.Block;

public class Region {
	
	private List<Block> blocks;
	private Vector3f position;
	
	protected Region(Vector3f position) {
		this.position = position;
		this.blocks = new ArrayList<>();
	}
	
	protected Region(Vector3f position, List<Block> blocks) {
		this.position = position;
		this.blocks = blocks;
	}
}

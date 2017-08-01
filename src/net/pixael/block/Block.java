package net.pixael.block;

import net.pixael.render.data.RawModel;
import net.pixael.render.data.Texture;
import net.pixael.util.Registery;

public class Block {
	
	private static Registery<Block> blocks;
	
	private RawModel model;
	private Texture texture;
	private float solidity;
	
	protected Block() {
		this.solidity = 1f;
	}
	
	protected Block setSolidity(float solidity) {
		this.solidity = solidity;
		return this;
	}
	
	protected void setModel(RawModel model) {
		this.model = model;
	}
	
	protected void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public RawModel getModel() {
		return this.model;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public float getSolidity() {
		return this.solidity;
	}
	
	public static void registerBlocks() {
		blocks = new Registery<>();
		registerBlock("air", (new Block()).setSolidity(0));
		registerBlock("grass", (new Block()));
		registerBlock("stone", (new Block()));
		registerBlock("wood", (new Block()));
		registerBlock("planks", (new Block()));
	}
	
	private static void registerBlock(String name, Block block) {
		blocks.put(name, block);
	}
	
	public static Registery<Block> getRegisteredBlocks() {
		return blocks;
	}
}

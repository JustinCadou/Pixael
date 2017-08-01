package net.pixael.init;

import java.io.InputStream;
import java.util.Scanner;

import net.pixael.block.Block;
import net.pixael.client.Blocks;
import net.pixael.client.Models;
import net.pixael.client.Textures;
import net.pixael.jdt.JDTList;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JSONParser;
import net.pixael.util.Registery;
import net.pixael.util.ResourcesUtil;

public class Assets {
	
	public static void init() throws Exception {
		InputStream in = ResourcesUtil.createAssetInputStream("assets.list");
		StringBuilder assetRaw = new StringBuilder();
		try (Scanner reader = new Scanner(in)) {
			while (reader.hasNextLine()) {
				assetRaw.append(reader.nextLine());
			}
		}
		String raw = assetRaw.toString().replaceAll("\t", "");
		JSONParser parser = new JSONParser(raw);
		JDTObject assets = (JDTObject) parser.parse();
		JDTList textures = (JDTList) assets.getTag("textures");
		Textures.loadAll("pixael", textures);
		JDTList models = (JDTList) assets.getTag("models");
		Models.loadAll("pixael", models);
		Block.registerBlocks();
		Registery<Block> blocks = Block.getRegisteredBlocks();
		Blocks.loadAll("pixael", blocks);
	}
}

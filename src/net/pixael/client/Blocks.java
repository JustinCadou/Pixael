package net.pixael.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.pixael.block.Block;
import net.pixael.block.BlockMaker;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JSONParser;
import net.pixael.util.Registery;
import net.pixael.util.ResourcesUtil;

public class Blocks {
	
	private static Map<String, Block> blocks;
	
	public static Block getBlock(String name) {
		return blocks.get(name);
	}
	
	public static void loadAll(String pack, Registery<Block> blocksData) throws IOException {
		blocks = new HashMap<>();
		List<Block> blks = blocksData.listValues();
		int i = 0;
		for (String source : blocksData.keySet()) {
			blocks.put(pack + ":" + source, blks.get(i));
			if (source.equals("air")) {
				continue;
			}
			InputStream in = ResourcesUtil.createAssetInputStream("blockmodels\\" + source + ".bmd");
			StringBuilder src = new StringBuilder();
			try (Scanner reader = new Scanner(in)) {
				while (reader.hasNextLine()) {
					src.append(reader.nextLine());
				}
			}
			String bmd = src.toString().replaceAll("\t", "");
			JSONParser parser = new JSONParser(bmd);
			JDTObject jdt = (JDTObject) parser.parse();
			BlockMaker.makeBlock(jdt, blks.get(i++));
		}
	}
}

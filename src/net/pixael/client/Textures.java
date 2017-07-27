package net.pixael.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.pixael.jdt.JDTBoolean;
import net.pixael.jdt.JDTList;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JDTString;
import net.pixael.render.data.GLDataManager;
import net.pixael.render.data.Texture;
import net.pixael.util.ResourcesUtil;

public class Textures {
	
	private static Map<String, Texture> textures;
	
	public static Texture get(String name) {
		return textures.get(name);
	}
	
	public static void loadAll(String pack, JDTList texturesData) throws IOException {
		textures = new HashMap<>();
		List<Object> texs = texturesData.getValue();
		for (Object tex : texs) {
			JDTObject jdt = (JDTObject) tex;
			JDTString resLoc = (JDTString) jdt.getTag("resource");
			InputStream in = ResourcesUtil.createAssetInputStream(resLoc.getValue());
			JDTBoolean smooth = (JDTBoolean) jdt.getTag("smooth");
			boolean flat = true;
			if (smooth != null) {
				flat = !smooth.getValue();
			}
			Texture texture = GLDataManager.loadTexture(in, flat);
			JDTString texname = (JDTString) jdt.getTag("name");
			textures.put(pack + ":" + texname.getValue(), texture);
		}
	}
	
	public static void unloadAll() {
		textures.clear();
	}
}

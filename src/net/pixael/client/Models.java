package net.pixael.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.pixael.jdt.JDTList;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JDTString;
import net.pixael.render.data.RawModel;
import net.pixael.util.OBJFileReader;
import net.pixael.util.ResourcesUtil;

public class Models {
	
	private static Map<String, RawModel> models;
	
	public static RawModel get(String name) {
		return models.get(name);
	}
	
	public static void loadAll(String pack, JDTList modelsData) throws IOException {
		models = new HashMap<>();
		List<Object> mdls = modelsData.getValue();
		for (Object mdl : mdls) {
			JDTObject jdt = (JDTObject) mdl;
			JDTString resLoc = (JDTString) jdt.getTag("resource");
			InputStream in = ResourcesUtil.createAssetInputStream(resLoc.getValue());
			RawModel model = OBJFileReader.loadOBJFile(in);
			JDTString mdlname = (JDTString) jdt.getTag("name");
			models.put(pack + ":" + mdlname.getValue(), model);
		}
	}
	
	public static void unloadAll() {
		models.clear();
	}
}

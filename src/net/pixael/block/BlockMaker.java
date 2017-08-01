package net.pixael.block;

import java.io.IOException;
import java.io.InputStream;
import net.pixael.client.Models;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JDTString;
import net.pixael.render.data.GLDataManager;
import net.pixael.render.data.RawModel;
import net.pixael.render.data.Texture;
import net.pixael.util.ResourcesUtil;

public class BlockMaker {
	
	public static void makeBlock(JDTObject blockData, Block block) throws IOException {
		JDTString modeln = (JDTString) blockData.getTag("model");
		RawModel model = Models.get(modeln.getValue());
		block.setModel(model);
		JDTString texn = (JDTString) blockData.getTag("texture");
		InputStream in = ResourcesUtil.createAssetInputStream("textures\\blocks\\" + texn.getValue() + ".png");
		Texture tex = GLDataManager.loadTexture(in, true);
		block.setTexture(tex);
	}
}

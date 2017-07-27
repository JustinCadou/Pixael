package net.pixael.init;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.fantasticfantasy.mainkit.CollectionUtil;
import net.pixael.MissingResourcesException;
import net.pixael.client.Settings;
import net.pixael.jdt.JDTBase;
import net.pixael.jdt.JDTBoolean;
import net.pixael.jdt.JDTByte;
import net.pixael.jdt.JDTDouble;
import net.pixael.jdt.JDTInt;
import net.pixael.jdt.JDTLong;
import net.pixael.jdt.JDTObject;
import net.pixael.jdt.JDTShort;
import net.pixael.jdt.JDTString;
import net.pixael.jdt.JSONIOTools;
import net.pixael.jdt.JSONParser;
import net.pixael.util.ResourcesUtil;

public class UserSettings {
	
	public static Settings load(String name) throws IOException {
		Settings settings = new Settings();
		File file = new File(ResourcesUtil.getUserResourcesDirectory() + name + ".pxd");
		if (!file.exists()) {
			throw new MissingResourcesException(new String[] {name + ".pxd"});
		}
		StringBuilder sb = new StringBuilder();
		try (Scanner reader = new Scanner(file)) {
			while (reader.hasNextLine()) {
				sb.append(reader.nextLine());
			}
		}
		String filecontent = sb.toString().replaceAll("\t", "");
		JSONParser parser = new JSONParser(filecontent);
		JDTObject obj = (JDTObject) parser.parse();
		for (JDTBase tag : obj.getValue()) {
			settings.put(tag.getName(), tag.getValue());
		}
		return settings;
	}
	
	public static void save(Settings settings) throws IOException {
		File file = new File(ResourcesUtil.getUserResourcesDirectory() + "settings.pxd");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();
		}
		JDTObject jdt = new JDTObject();
		Map<String, Object> map = settings.map();
		List<String> keys = CollectionUtil.setToList(map.keySet());
		List<Object> values = CollectionUtil.collectionToList(map.values());
		for (int i = 0; i < keys.size(); i++) {
			JDTBase tag = null;
			Object value = values.get(i);
			if (value instanceof Boolean) {
				tag = new JDTBoolean();
				((JDTBoolean) tag).setValue((Boolean) value);
			} else if (value instanceof Byte) {
				tag = new JDTByte();
				((JDTByte) tag).setValue((Byte) value);
			} else if (value instanceof Double) {
				tag = new JDTDouble();
				((JDTDouble) tag).setValue((Double) value);
			} else if (value instanceof Integer) {
				tag = new JDTInt();
				((JDTInt) tag).setValue((Integer) value);
			} else if (value instanceof Long) {
				tag = new JDTLong();
				((JDTLong) tag).setValue((Long) value);
			} else if (value instanceof Short) {
				tag = new JDTShort();
				((JDTShort) tag).setValue((Short) value);
			} else if (value instanceof String) {
				tag = new JDTString();
				((JDTString) tag).setValue((String) value);
			}
			tag.setName(keys.get(i));
			jdt.addTag(tag);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			JSONIOTools.write(jdt, writer);
		}
	}
}

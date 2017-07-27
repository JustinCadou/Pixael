package net.pixael.client.init;

import java.util.List;
import net.fantasticfantasy.mainkit.Argument;
import net.fantasticfantasy.mainkit.Argument.ValueType;
import net.fantasticfantasy.mainkit.ArgumentParser;
import net.fantasticfantasy.mainkit.CollectionUtil;
import net.pixael.Pixael;
import net.pixael.util.ResourcesUtil;

public class Main {
	
	/**
	 * {@code Main} class parse arguments and start {@link net.pixael.Pixael Pixael}
	 * 
	 * @param args - The arguments passed whether by the launcher or the {@link Default Default} class
	 */
	public static void main(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		Argument arg1 = parser.get("-u").withDefaultValue(playerName());
		Argument arg2 = parser.get("-w").valueType(ValueType.INTEGER).withDefaultValue(860);
		Argument arg3 = parser.get("-h").valueType(ValueType.INTEGER).withDefaultValue(640);
		Argument arg4 = parser.get("-res").withDefaultValue(resourcesPath());
		parser.parse(args);
		List<Argument> ignored = parser.getIgnoredArguments();
		if (!ignored.isEmpty()) {
			System.out.println("Ignored:");
			CollectionUtil.printList(ignored, "\t", "\n");
		}
		PixaelConfiguration config = new PixaelConfiguration();
		config.username = (String) arg1.value();
		config.width = (Integer) arg2.value();
		config.height = (Integer) arg3.value();
		String userpath = (String) arg4.value();
		if (!ResourcesUtil.validate(userpath, ResourcesUtil.TYPE_DIRECTORY)) {
			ResourcesUtil.make(userpath);
		}
		ResourcesUtil.setUserDirectory(userpath);
		(new Pixael(config)).start();
	}
	
	private static String playerName() {
		int one = (int) System.nanoTime() % 0xFFFF;
		String two = Integer.toHexString(one);
		String three = two.substring(two.length() - (3 < two.length() ? two.length() : 3), two.length());
		return "Player" + three.toUpperCase();
	}
	
	private static String resourcesPath() {
		String one = ResourcesUtil.getUserHome();
		boolean two = ResourcesUtil.validate(one + "\\AppData\\", ResourcesUtil.TYPE_DIRECTORY);
		if (two) {
			String three = one + "\\AppData\\Roaming\\..pixael\\";
			return three;
		} else {
			return one + "\\..pixael\\";
		}
	}
}

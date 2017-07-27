import net.fantasticfantasy.mainkit.CollectionUtil;
import net.pixael.client.init.Main;

public class Default {
	
	/**
	 * {@code Default} class is used to modify arguments passed by the launcher.
	 * 
	 * @param args - The arguments passed by the launcher
	 */
	public static void main(String[] args) {
		Main.main(CollectionUtil.<String>concat(args, new String[] {"-u Angel_Gamer", "-w 1080", "-h 680"}));
	}
}

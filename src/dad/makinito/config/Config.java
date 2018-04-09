package dad.makinito.config;

import java.util.ResourceBundle;

public class Config {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("dad.makinito.config.config");

	public static String getVersion() {
		return BUNDLE.getString("makinito.version");
	}
	
	public static Integer getMemoryCapacity() {
		return Integer.parseInt(BUNDLE.getString("makinito.memory.capacity"));
	}
	
}
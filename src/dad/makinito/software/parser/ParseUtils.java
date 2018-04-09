package dad.makinito.software.parser;

public class ParseUtils {

	public static Integer binaryToInteger(String binary) {
		return Integer.parseInt(binary, 2);
	}
	
	public static Integer hexToInteger(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
}

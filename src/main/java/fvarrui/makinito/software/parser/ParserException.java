package fvarrui.makinito.software.parser;

@SuppressWarnings("serial")
public class ParserException extends Exception {
	private int line;

	public ParserException(int line, String message) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}

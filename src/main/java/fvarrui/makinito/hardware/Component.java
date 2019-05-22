package fvarrui.makinito.hardware;

/**
 * Todas las piezas que forman Makinito heredan de Component de forma directa o indirecta.
 *  
 * @author Francisco Vargas
 */
public abstract class Component {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return toString("");
	}

	public String toString(String tabs) {
		return tabs + name;
	}
	
	public abstract void reset();

}

package dad.makinito.hardware;

/**
 * Las señales de control las activa el secuenciador en el orden adecuado para 
 * cumplir su cometido (ejecutar las instrucciones del programa cargado en memoria).
 * 
 * Las señales de control actúan sobre un componente, provocando que éste realice
 * alguna acción. Por ejemplo, la señal de lectura (LECT) actúa sobre la memoria 
 * principal para que ésta lea el contenido la dirección del registro RD y lo 
 * copie en el registro RM.   
 *  
 * @author Francisco Vargas
 */
public abstract class ControlSignal {
	private String name;
	private String description;
	private Component component;

	public ControlSignal() {
		super();
	}
	
	public ControlSignal(String name, Component component, String description) {
		super();
		this.name = name;
		this.component = component;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String gttDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract void handleActivate();
	
	public void activate() {
		System.out.println(name + ": " + description + ".");
		handleActivate();
	}
	
	@Override
	public String toString() {
		return name; // "Señal de control " + name + " actúa sobre " + component.getName();
	}

}

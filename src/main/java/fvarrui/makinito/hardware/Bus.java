package fvarrui.makinito.hardware;

/**
 * Bus: vía de conexión entre los distintos componentes de la aquitectura. Podemos considerarlo
 * como un registro intermedio empleado para intercambiar información entre los componentes
 * que interconecta.
 *  
 * @author Francisco Vargas
 */
public class Bus extends Register {

	public Bus(String name) {
		super(name);
	}

}

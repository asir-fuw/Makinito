package dad.makinito.hardware;

/**
 * Excepci�n geneal que lanza Makinito en caso de producirse cualquier error y no poder continuar
 * su ejecuci�n, durante su inicializaci�n o durante la carga de un programa. 
 * 
 * Contiene un mensaje que describe el error ocurrido, as� como en algunos casos
 * la exepci�n original producida.
 * 
 * @author Francisco Vargas
 *
 */
@SuppressWarnings("serial")
public class MakinitoException extends RuntimeException {

	public MakinitoException(String message, Throwable cause) {
		super(message, cause);
	}

	public MakinitoException(String message) {
		super(message);
	}
	
}

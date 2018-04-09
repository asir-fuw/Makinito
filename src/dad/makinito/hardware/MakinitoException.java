package dad.makinito.hardware;

/**
 * Excepción geneal que lanza Makinito en caso de producirse cualquier error y no poder continuar
 * su ejecución, durante su inicialización o durante la carga de un programa. 
 * 
 * Contiene un mensaje que describe el error ocurrido, así como en algunos casos
 * la exepción original producida.
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

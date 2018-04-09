package dad.makinito.hardware;

/**
 * Fases por las que pasa el secuenciador de forma c�clica:
 * - Carga (fetch)				: 	recupera una instrucci�n de la memoria y la lleva a la unidad de control.
 * - Decodificaci�n (decode)	: 	determina las se�ales de control a ejecutar para llevar a cabo la instrucci�n cargada.
 * - Ejecuci�n (execute)		:	activa las se�ales de control generadas en la fase anterior.
 *  
 * @author Francisco Vargas
 */
public enum Phase {
	FETCH,
	DECODE,
	EXECUTE;
}

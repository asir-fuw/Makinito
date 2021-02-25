package io.github.fvarrui.makinito.hardware;

/**
 * Fases por las que pasa el secuenciador de forma cíclica:
 * - Carga (fetch)				: 	recupera una instrucción de la memoria y la lleva a la unidad de control.
 * - Decodificación (decode)	: 	determina las señales de control a ejecutar para llevar a cabo la instrucción cargada.
 * - Ejecución (execute)		:	activa las señales de control generadas en la fase anterior.
 *  
 * @author fvarrui
 */
public enum Phase {
	FETCH,
	DECODE,
	EXECUTE;
}

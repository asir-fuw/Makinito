package io.github.fvarrui.makinito.software;

/**
 * Modo de direccionamiento
 * @author Francisco Vargas
 */
public enum AddressingMode {
	NOT_USED,					// cuando no se especifica operando
	IMMEDIATE,					// inmediato: valor contenido en CDE
	DIRECT,						// directo: valor contenido en [CDE]
	INDIRECT,					// indirecto: valor contenido en [[CDE]]
	REGISTER,					// registro: valor contenido en el registro
	DIRECT_WITH_REGISTER,		// directo con registro: valor contenido en [registro]
	INDIRECT_WITH_REGISTER		// indirecto con registro: valor contenido en [[registro]]
}

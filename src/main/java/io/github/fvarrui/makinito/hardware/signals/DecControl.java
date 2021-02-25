package io.github.fvarrui.makinito.hardware.signals;

import io.github.fvarrui.makinito.hardware.ControlSignal;
import io.github.fvarrui.makinito.hardware.Decoder;

public class DecControl extends ControlSignal {

	public DecControl(Decoder decoder) {
		super("DEC", decoder, "Decodifica la instrucción en RI y genera las señales de control para el secuenciador");
	}
	
	@Override
	public void handleActivate() {
		((Decoder) getComponent()).decode();
	}

}

package fvarrui.makinito.hardware.signals;

import fvarrui.makinito.hardware.ControlSignal;
import fvarrui.makinito.hardware.Decoder;

public class DecControl extends ControlSignal {

	public DecControl(Decoder decoder) {
		super("DEC", decoder, "Decodifica la instrucci�n en RI y genera las se�ales de control para el secuenciador");
	}
	
	@Override
	public void handleActivate() {
		((Decoder) getComponent()).decode();
	}

}

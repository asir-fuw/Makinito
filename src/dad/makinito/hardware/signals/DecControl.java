package dad.makinito.hardware.signals;

import dad.makinito.hardware.Decoder;
import dad.makinito.hardware.ControlSignal;

public class DecControl extends ControlSignal {

	public DecControl(Decoder decoder) {
		super("DEC", decoder, "Decodifica la instrucci�n en RI y genera las se�ales de control para el secuenciador");
	}
	
	@Override
	public void handleActivate() {
		((Decoder) getComponent()).decode();
	}

}

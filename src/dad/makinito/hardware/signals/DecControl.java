package dad.makinito.hardware.signals;

import dad.makinito.hardware.Decoder;
import dad.makinito.hardware.ControlSignal;

public class DecControl extends ControlSignal {

	public DecControl(Decoder decoder) {
		super("DEC", decoder, "Decodifica la instrucción en RI y genera las señales de control para el secuenciador");
	}
	
	@Override
	public void handleActivate() {
		((Decoder) getComponent()).decode();
	}

}

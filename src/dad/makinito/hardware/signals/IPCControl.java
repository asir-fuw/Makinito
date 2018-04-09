package dad.makinito.hardware.signals;

import dad.makinito.hardware.Component;
import dad.makinito.hardware.Register;
import dad.makinito.hardware.ControlSignal;

public class IPCControl extends ControlSignal {

	public IPCControl(Component component) {
		super("ICP", component, "Incrementa el valor del registro Contador de Programa (CP)");
	}
	
	@Override
	public void handleActivate() {
		((Register) getComponent()).increase();
	}

}

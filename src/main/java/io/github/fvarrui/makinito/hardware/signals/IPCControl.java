package io.github.fvarrui.makinito.hardware.signals;

import io.github.fvarrui.makinito.hardware.Component;
import io.github.fvarrui.makinito.hardware.ControlSignal;
import io.github.fvarrui.makinito.hardware.Register;

public class IPCControl extends ControlSignal {

	public IPCControl(Component component) {
		super("ICP", component, "Incrementa el valor del registro Contador de Programa (CP)");
	}
	
	@Override
	public void handleActivate() {
		((Register) getComponent()).increase();
	}

}

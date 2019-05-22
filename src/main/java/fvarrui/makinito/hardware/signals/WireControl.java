package fvarrui.makinito.hardware.signals;

import fvarrui.makinito.hardware.ControlSignal;
import fvarrui.makinito.hardware.Wire;

public class WireControl extends ControlSignal {

	public WireControl(Wire wire) {
		super();
		setName(wire.getName());
		setComponent(wire);
		setDescription("Mover información desde '" + wire.getSource().getName() + "' hasta '" + wire.getTarget().getName() + "'");
	}
	
	public void handleActivate() {
		((Wire) getComponent()).activate();
	}

}

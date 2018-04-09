package dad.makinito.hardware.signals;

import dad.makinito.hardware.Wire;
import dad.makinito.hardware.ControlSignal;

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

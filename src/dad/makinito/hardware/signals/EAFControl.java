package dad.makinito.hardware.signals;

import dad.makinito.hardware.Bus;
import dad.makinito.hardware.Wire;
import dad.makinito.hardware.Register;

public class EAFControl extends WireControl {

	public EAFControl(Wire wire) {
		super(wire);
		if (getName().matches("CDE1-.*"))
			setDescription("Mover información desde CDE1 de la instrucción en RI hasta '" + wire.getTarget().getName() + "'");
		else
			setDescription("Mover información desde CDE2 de la instrucción en RI hasta '" + wire.getTarget().getName() + "'");
	}
	
	@Override
	public void handleActivate() {
		Wire wire = (Wire) getComponent();
		Register ir = wire.getSource();
		Bus bus = (Bus) wire.getTarget();
		if (getName().matches("CDE1-.*")) {
			bus.setContent(ir.getInstruction().getOperands().get(0).getEffectiveAddressField());
		} else {
			bus.setContent(ir.getInstruction().getOperands().get(1).getEffectiveAddressField());
		}
	}

}

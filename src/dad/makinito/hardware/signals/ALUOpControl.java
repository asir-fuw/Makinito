package dad.makinito.hardware.signals;

import dad.makinito.hardware.Operator;
import dad.makinito.hardware.ControlSignal;

public class ALUOpControl extends ControlSignal {
	
	public ALUOpControl(Operator operator) {
		super("UAL-OP(" + operator.getName() + ")", operator, "Realiza la operación AC " + operator.getName() + " RT, guarda el resultado en AC y actualiza RE");
	}

	@Override
	public void handleActivate() {
		((Operator) getComponent()).operate();
	}

}

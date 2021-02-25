package io.github.fvarrui.makinito.hardware.signals;

import io.github.fvarrui.makinito.hardware.ControlSignal;
import io.github.fvarrui.makinito.hardware.Operator;

public class ALUOpControl extends ControlSignal {
	
	public ALUOpControl(Operator operator) {
		super("UAL-OP(" + operator.getName() + ")", operator, "Realiza la operación AC " + operator.getName() + " RT, guarda el resultado en AC y actualiza RE");
	}

	@Override
	public void handleActivate() {
		((Operator) getComponent()).operate();
	}

}

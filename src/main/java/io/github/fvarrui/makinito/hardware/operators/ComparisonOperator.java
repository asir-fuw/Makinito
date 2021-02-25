package io.github.fvarrui.makinito.hardware.operators;

import io.github.fvarrui.makinito.hardware.Operator;

public class ComparisonOperator extends Operator {

	public ComparisonOperator() {
		super("=");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		if (in1 == in2) {
			getFlagRegister().setZero(true);
		} else if (in1 < in2) {
			getFlagRegister().setNegative(true);
		}
		return null; // devuelvo null para que el operador no intente guardar el resultado en el registro de destino
	}

}

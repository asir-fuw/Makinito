package io.github.fvarrui.makinito.hardware.operators;

import io.github.fvarrui.makinito.hardware.MakinitoException;
import io.github.fvarrui.makinito.hardware.Operator;

public class DivisionOperator extends Operator {

	public DivisionOperator() {
		super("/");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		try {
			return in1 / in2;
		} catch (ArithmeticException e) {
			throw new MakinitoException("Error en el operador de división: " + e.getMessage(), e);
		}
	}

}

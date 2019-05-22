package fvarrui.makinito.hardware.operators;

import fvarrui.makinito.hardware.Operator;

public class MultiplicationOperator extends Operator {

	public MultiplicationOperator() {
		super("*");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		return in1 * in2;
	}

}

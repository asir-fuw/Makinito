package io.github.fvarrui.makinito.hardware.operators;

import io.github.fvarrui.makinito.hardware.Operator;

public class SubstractionOperator extends Operator {

	public SubstractionOperator() {
		super("-");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		return in1 - in2;
	}

}

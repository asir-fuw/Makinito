package io.github.fvarrui.makinito.encoder;

import java.util.ArrayList;
import java.util.List;

import io.github.fvarrui.makinito.software.Instruction;
import io.github.fvarrui.makinito.software.Operand;

public class EncodedInstruction {
	private String opcode;
	private List<EncodedOperand> operands = new ArrayList<>();

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public List<EncodedOperand> getOperands() {
		return operands;
	}

	public EncodedInstruction fromInstruction(Instruction i) {
		switch (i.getOpCode()) {
		case "MOVER":
			opcode = "0000";
			break;
		case "SUMAR":
			opcode = "0001";
			break;
		case "RESTAR":
			opcode = "0010";
			break;
		case "MULTIPLICAR":
			opcode = "0011";
			break;
		case "DIVIDIR":
			opcode = "0100";
			break;
		case "COMPARAR":
			opcode = "0101";
			break;
		case "SALTAR":
			opcode = "0110";
			break;
		default:
			opcode = "1111";
			break;
		}
		operands.clear();
		for (Operand operand : i.getOperands()) {
			operands.add(new EncodedOperand().fromOperand(operand));
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(opcode);
		for (EncodedOperand eo : getOperands()) {
			buffer.append(eo.toString());
		}
		if (operands.size() == 0) {
			buffer.append("0000000000000000000000");
		} else if (operands.size() == 1) {
			buffer.append("00000000000");
		}
		return buffer.toString();
	}

}

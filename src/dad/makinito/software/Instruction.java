package dad.makinito.software;

import java.util.ArrayList;
import java.util.List;

public class Instruction extends Info {
	private String opCode;
	private List<Operand> operands = new ArrayList<Operand>();
	
	public Instruction(String opCode) {
		super();
		this.opCode = opCode;
	}

	public Instruction() {
		super();
	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public final List<Operand> getOperands() {
		return operands;
	}
	
	@Override
	public String toString() {
		boolean first = true;
		StringBuffer buffer = new StringBuffer();
		buffer.append(opCode + " ");
		for (Operand op : operands) {
			if (!first) {
				buffer.append(",");
			} else {
				first = false;
			}
			buffer.append(op);
		}
		return buffer.toString().trim();
	}

}

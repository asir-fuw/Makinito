package io.github.fvarrui.makinito.software;

import java.util.ArrayList;
import java.util.List;

public class Program {
	private String name;
	private List<Datum> data = new ArrayList<Datum>();
	private List<Instruction> instructions = new ArrayList<Instruction>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final List<Datum> getData() {
		return data;
	}

	public final List<Instruction> getInstructions() {
		return instructions;
	}
	
	public int getFirstInstructionAddress() {
		return data.size();
	}
	
	public Info [] asArray() {
		int size = data.size() + instructions.size();
		Info [] program = new Info[size];
		int i = 0;
		for (Datum datum : data) {
			program[i] = datum;
			i++;
		}
		for (Instruction instruction : instructions) {
			program[i] = instruction;
			i++;
		}
		return program;
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean plain) {
		StringBuffer buffer = new StringBuffer();
		if (!plain) {
			buffer.append("PROGRAMA: " + getName() + "\n");
		}
		int memoryAddress = 0;
		if (!plain) {
			buffer.append("DATOS {\n");
		}
		for (Datum datum : data) {
			buffer.append(memoryAddress + ":\t" + datum + "\n");
			memoryAddress++;
		}
		if (!plain) {
			buffer.append("}\nINSTRUCCIONES:\n");
		}
		for (Instruction instruction : instructions) {
			buffer.append(memoryAddress + ":\t" + instruction + "\n");
			memoryAddress++;
		}
		if (!plain) {
			buffer.append("}\n");
		}
		return buffer.toString();
	}
	
}

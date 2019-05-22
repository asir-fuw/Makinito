package fvarrui.makinito.hardware.microcode;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class SignalInstruction extends MicroInstruction {

	public SignalInstruction() {
		super();
	}
	
	public SignalInstruction(String name) {
		super(name);
	}
	
}

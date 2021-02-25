package io.github.fvarrui.makinito.hardware.microcode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlSeeAlso({ MacroInstruction.class, SignalInstruction.class })
public abstract class MicroInstruction {
	private String name;

	public MicroInstruction() {
	}

	public MicroInstruction(String name) {
		super();
		this.name = name;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

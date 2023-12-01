package io.github.fvarrui.makinito.hardware.microcode;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "value" })
public class MicroParameter {
	private String name;
	private String value;

	public MicroParameter() {
	}

	public MicroParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

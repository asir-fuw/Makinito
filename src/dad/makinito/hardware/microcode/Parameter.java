package dad.makinito.hardware.microcode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import dad.makinito.software.AddressingMode;

@XmlType
public class Parameter {
	private AddressingMode addressingMode;
	private String name;
	
	public Parameter() {
	}
	
//	public Parameter(AddressingMode addressingMode) {
//		this(addressingMode, null);
//	}
	
	public Parameter(AddressingMode addressingMode, String name) {
		this.addressingMode = addressingMode;
		this.name = name;
	}

	@XmlAttribute
	public AddressingMode getAddressingMode() {
		return addressingMode;
	}

	public void setAddressingMode(AddressingMode addressingMode) {
		this.addressingMode = addressingMode;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		if (addressingMode != null)
			switch (addressingMode) {
			case IMMEDIATE:					return "X"; 
			case DIRECT:					return "[X]";
			case INDIRECT:					return "[[X]]";
			case REGISTER:					return "REG";
			case NOT_USED:					return "";
			case DIRECT_WITH_REGISTER:		return "[REG]";
			case INDIRECT_WITH_REGISTER:	return "[[REG]]";
			default:
				break;
			}
		else
			return name;
		return null;
	}

}

package fvarrui.makinito.software;

public class Operand {
	
	private AddressingMode addressingMode; 		// modo de direccionamiento
	private Datum effectiveAddressField; 		// campo de dirección efectiva
	private String registerName;				// información extra
	
	public Operand() {}
	
	public Operand(AddressingMode addressingMode, Datum effectiveAddressField) {
		this.addressingMode = addressingMode;
		this.effectiveAddressField = effectiveAddressField;
	}
	
	public Operand(AddressingMode addressingMode, int effectiveAddressField) {
		this(addressingMode, new Datum(effectiveAddressField));
	}

	public Operand(int effectiveAddressField, String registerName) {
		this(AddressingMode.REGISTER, effectiveAddressField, registerName);
	}

	public Operand(AddressingMode addressingMode, int effectiveAddressField, String registerName) {
		this(addressingMode, new Datum(effectiveAddressField));
		setRegisterName(registerName);
	}

	public AddressingMode getAddressingMode() {
		return addressingMode;
	}

	public void setAddressingMode(AddressingMode addressingMode) {
		this.addressingMode = addressingMode;
	}

	public Datum getEffectiveAddressField() {
		return effectiveAddressField;
	}

	public void setEffectiveAddressField(Datum effectiveAddressField) {
		this.effectiveAddressField = effectiveAddressField;
	}

	public String getRegisterName() {
		return registerName;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Operand)) return false;
		Operand operand = (Operand) obj;
		if (operand.getAddressingMode() == getAddressingMode()) {
			if (operand.getAddressingMode() == AddressingMode.REGISTER && operand.getEffectiveAddressField().equals(getEffectiveAddressField())) {
				return true;
			} else if (operand.getAddressingMode() != AddressingMode.REGISTER) {
				return true;
			}	
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		switch(addressingMode) {
		case IMMEDIATE:	buffer.append(effectiveAddressField); break;
		case DIRECT:	buffer.append("[" + effectiveAddressField + "]"); break;
		case INDIRECT:	buffer.append("[[" + effectiveAddressField + "]]"); break;
		case REGISTER:	buffer.append(registerName); break;
		default:		break;
		}
		return buffer.toString();
	}

}

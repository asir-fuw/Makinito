package io.github.fvarrui.makinito.encoder;

import org.apache.commons.codec.binary.BinaryCodec;

import io.github.fvarrui.makinito.software.AddressingMode;
import io.github.fvarrui.makinito.software.Operand;

public class EncodedOperand {
	private String md;
	private String cde;

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getCde() {
		return cde;
	}

	public void setCde(String cde) {
		this.cde = cde;
	}
	
	public EncodedOperand fromOperand(Operand operand) {
		switch (operand.getAddressingMode()) {
		case IMMEDIATE: md = "001"; break;
		case DIRECT: 	md = "010"; break;
		case INDIRECT: 	md = "011"; break;
		case REGISTER: 	md = "100"; break;
		default: 		md = "000"; break;
		}
		if (operand.getAddressingMode() == AddressingMode.REGISTER) {
			switch (operand.getRegisterName()) {
			case "AC": cde = "00000000"; break;
			case "RT": cde = "00000001"; break;
			case "RE": cde = "00000010"; break;
			case "RI": cde = "00000011"; break;
			case "CP": cde = "00000100"; break;
			}
		} else {
			cde = BinaryCodec.toAsciiString(new byte[] {(byte)operand.getEffectiveAddressField().getValue().intValue()});
		}
		return this;
	}
	
	@Override
	public String toString() {
		return getMd() + getCde();
	}

}

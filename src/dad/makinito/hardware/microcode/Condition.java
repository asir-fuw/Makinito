package dad.makinito.hardware.microcode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Condition {
	private Boolean zero = false;
	private Boolean negative = false;
	private Boolean overflow = false;
	private Boolean carry = false;

	public Condition() {
		super();
	}
	
	public Condition(Boolean zero, Boolean negative, Boolean overflow, Boolean carry) {
		super();
		this.zero = zero;
		this.negative = negative;
		this.overflow = overflow;
		this.carry = carry;
	}

	@XmlAttribute
	public Boolean isZero() {
		return zero;
	}

	public void setZero(Boolean zero) {
		this.zero = zero;
	}

	@XmlAttribute
	public Boolean isNegative() {
		return negative;
	}

	public void setNegative(Boolean negative) {
		this.negative = negative;
	}

	@XmlAttribute
	public Boolean isOverflow() {
		return overflow;
	}

	public void setOverflow(Boolean overflow) {
		this.overflow = overflow;
	}

	public Boolean isCarry() {
		return carry;
	}

	@XmlAttribute
	public void setCarry(Boolean carry) {
		this.carry = carry;
	}

}

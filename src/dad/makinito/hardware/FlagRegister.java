package dad.makinito.hardware;

import dad.makinito.software.Datum;

/**
 * Registro de estado. Contiene información sobre la última operación realizada en la UAL. 
 * 
 * @author Francisco Vargas
 */
public class FlagRegister extends Register {
	
	private static final int ZERO_BIT 		= 1; 
	private static final int NEGATIVE_BIT 	= 2; 
	private static final int CARRY_BIT 		= 4; 
	private static final int OVERFLOW_BIT 	= 8; 

	public FlagRegister() {
		super("RE");
		setContent(new Datum(0));
	}
	
	private int getValue() {
		return ((Datum)getContent()).getValue();
	}

	private void setValor(Integer value) {
		setContent(new Datum(value));
	}
	
	private int setBit(int valor, int bitMask, boolean bitValue) {
		return bitValue ? valor | bitMask : valor & ~bitMask;
	}

	private boolean getBit(int valor, int bitMask) {
		return ((valor & bitMask) != 0);
	}

	public void setOverflow(boolean value) {
		setValor(setBit(getValue(), OVERFLOW_BIT, value)); 
	}
	
	public boolean isOverflow() {
		return getBit(getValue(), OVERFLOW_BIT); 
	}

	public void setCarry(boolean value) {
		setValor(setBit(getValue(), CARRY_BIT, value)); 
	}

	public boolean isCarry() {
		return getBit(getValue(), CARRY_BIT); 
	}

	public void setZero(boolean value) {
		setValor(setBit(getValue(), ZERO_BIT, value)); 
	}

	public boolean isZero() {
		return getBit(getValue(), ZERO_BIT); 
	}

	public void setNegative(boolean value) {
		setValor(setBit(getValue(), NEGATIVE_BIT, value)); 
	}
	
	public boolean isNegative() {
		return getBit(getValue(), NEGATIVE_BIT); 
	}
	
	public void reset() {
		setZero(false);
		setNegative(false);
		setCarry(false);
		setOverflow(false);
	}
	
	@Override
	public String toString(String tabs) {
		return tabs + getName() + "=" 
				+ (isZero() ? "Z" : "-") 
				+ (isNegative() ? "N" : "-") 
				+ (isCarry() ? "C" : "-") 
				+ (isOverflow() ? "O" : "-");
	}

}

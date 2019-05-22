package fvarrui.makinito.hardware;

import fvarrui.makinito.software.Datum;

/**
 * Circuito combinacional de la ALU que lleva a cabo alguna operación.
 * 
 * Toma los valores de sus dos entradas, realiza la operación corespondiente, y 
 * coloca el resultado en la salida y actualiza el registro de estados (RE).
 * 
 * @author Francisco Vargas
 */
public abstract class Operator extends Component {

	private Register in1;
	private Register in2;
	private Register out;
	private FlagRegister flags;
	
	public Operator(String name) {
		super();
		setName(name);
	}
	
	public Register getIn1() {
		return in1;
	}

	public void setIn1(Register in1) {
		this.in1 = in1;
	}

	public Register getIn2() {
		return in2;
	}

	public void setIn2(Register in2) {
		this.in2 = in2;
	}

	public Register getOut() {
		return out;
	}

	public void setOut(Register out) {
		this.out = out;
	}

	public FlagRegister getFlagRegister() {
		return flags;
	}

	public void setFlagRegister(FlagRegister flags) {
		this.flags = flags;
	}

	public void operate() {
		// reiniciamos el registo de estados
		flags.reset(); 

		// recuperamos el contenido de los registros con los que se opera
		int in1 = ((Datum)getIn1().getContent()).getValue();
		int in2 = ((Datum)getIn2().getContent()).getValue();
		
		// se realiza la operación
		Integer res = handleOperate(in1, in2);
		
		// si el resultado es nulo, no se hace nada
		if (res != null) {
			
			// se actualiza el registro de estados
			if (res < 0) getFlagRegister().setNegative(true);
			else if (res == 0) getFlagRegister().setZero(true);
			
			// se almacena el resultado en el registro de destino
			getOut().setContent(new Datum(res));
			
		}
	}
	
	protected abstract Integer handleOperate(Integer in1, Integer in2);
	
	@Override
	public void reset() { }
	
	@Override
	public String toString(String tabs) {
		return tabs + "operator(" + getName() + ")";
	}

}

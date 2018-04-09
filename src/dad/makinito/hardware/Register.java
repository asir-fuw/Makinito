package dad.makinito.hardware;

import dad.makinito.software.Datum;
import dad.makinito.software.Info;
import dad.makinito.software.Instruction;

/**
 * Pequeña memoria o almacén de datos, que puede contener datos, direcciones o instrucciones.  
 * 
 * @author Francisco Vargas
 */
public class Register extends Component {
	private Info content;
	
	public Register(String name) {
		setName(name);
		content = new Datum(0);
	}

	public Info getContent() {
		return content;
	}

	public void setContent(Info content) {
		this.content = content;
	}
	
	public Datum getDatum() {
		return (Datum) this.content;
	}
	
	public void setDatum(int value) {
		this.content = new Datum(value);
	}

	public Instruction getInstruction() {
		return (Instruction) this.content;
	}

	public void increase() {
		if (content instanceof Datum) {
			Datum datum = (Datum) content;
			setDatum(datum.getValue() + 1);
		}
	}
	
	public String toString(String tabs) {
		return super.toString(tabs) + "=" + content.toString();
	}

	@Override
	public void reset() {
		setDatum(0);
	}
	
}

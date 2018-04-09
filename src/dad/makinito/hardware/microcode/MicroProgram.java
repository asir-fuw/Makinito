package dad.makinito.hardware.microcode;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class MicroProgram {

	private String name;
	private Boolean macro = false;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private ArrayList<MicroInstruction> microInstructions = new ArrayList<MicroInstruction>();
	private List<Condition> conditions = new ArrayList<Condition>();

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public Boolean isMacro() {
		if (macro == false)
			return null;
		return macro;
	}

	public void setMacro(Boolean macro) {
		this.macro = macro;
	}

	@XmlElementWrapper(name = "microInstructions")
	@XmlElements( {
		@XmlElement(name="macroInstruction", type=MacroInstruction.class),
		@XmlElement(name="signalInstruction", type=SignalInstruction.class),
	})
	public List<MicroInstruction> getMicroInstructions() {
		return microInstructions;
	}

	@XmlElement(name = "parameter")
	@XmlElementWrapper(name = "parameters")
	public List<Parameter> getParameters() {
		return parameters;
	}

	@XmlElementWrapper(name="conditions")
	@XmlElement(name="condition")
	public List<Condition> getConditions() {
		return conditions;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name + " ");
		int i = 0 ;
		for (Parameter p : parameters) {
			buffer.append(p + ((i < parameters.size() - 1) ? "," : ""));
			i++;
		}
		return buffer.toString();
	}
	
}

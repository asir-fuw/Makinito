package dad.makinito.hardware.microcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class MacroInstruction extends MicroInstruction {
	private List<MicroParameter> parameters = new ArrayList<MicroParameter>();

	public MacroInstruction() {
		super();
	}

	public MacroInstruction(String name, MicroParameter... parameters) {
		super(name);
		this.parameters.addAll(Arrays.asList(parameters));
	}

	@XmlElement(name="parameter")
	public List<MicroParameter> getParameters() {
		return parameters;
	}

}

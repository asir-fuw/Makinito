package fvarrui.makinito.hardware.microcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class MacroInstruction extends MicroInstruction {
	private List<MicroParameter> microParameters = new ArrayList<MicroParameter>();

	public MacroInstruction() {
		super();
	}

	public MacroInstruction(String name, MicroParameter... parameters) {
		super(name);
		this.microParameters.addAll(Arrays.asList(parameters));
	}

	@XmlElement(name="microParameter")
	public List<MicroParameter> getMicroParameters() {
		return microParameters;
	}

}

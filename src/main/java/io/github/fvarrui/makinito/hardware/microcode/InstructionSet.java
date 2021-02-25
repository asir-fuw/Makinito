package io.github.fvarrui.makinito.hardware.microcode;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.github.fvarrui.makinito.hardware.Makinito;
import io.github.fvarrui.makinito.software.AddressingMode;
import io.github.fvarrui.makinito.software.Instruction;
import io.github.fvarrui.makinito.software.Operand;

@XmlRootElement
public class InstructionSet {
	private ArrayList<MicroProgram> microprograms;

	public InstructionSet() {
		microprograms = new ArrayList<MicroProgram>();
	}
	
	@XmlElement(name="microProgram")
	public ArrayList<MicroProgram> getMicroPrograms() {
		return microprograms;
	}

	public void add(MicroProgram microProgram) {
		microprograms.add(microProgram);
	}
	
	public MicroProgram search(String name, MacroInstruction macro, Map<String, String> parameters) {
		for (MicroProgram microprogram : microprograms) {
			if (microprogram.getName().equals(name) && 
				microprogram.getParameters().size() == macro.getMicroParameters().size()) {
				boolean ok = true;
				for (int i = 0; i < microprogram.getParameters().size() && ok; i++) {
					Parameter parameter = microprogram.getParameters().get(i);
					MicroParameter microParameter = macro.getMicroParameters().get(i);
					if (!parameter.getName().equals(microParameter.getName())) {
						ok = false;
					}
				}
				if (ok) return microprogram;
			}
		}
		return null;
	}
	
	public MicroProgram search(Instruction instruction, Makinito makinito) {
		for (MicroProgram microprogram : microprograms) {
			if (microprogram.getName().equals(instruction.getOpCode()) && 
				microprogram.getParameters().size() == instruction.getOperands().size()) {
				boolean ok = true;
				for (int i = 0; i < microprogram.getParameters().size() && ok; i++) {
					Parameter parameter = microprogram.getParameters().get(i);
					Operand operand = instruction.getOperands().get(i);
					if (parameter.getAddressingMode() != operand.getAddressingMode()) {
						ok = false;
					} 
				}
				if (ok) return microprogram;
			}
		}
		return null;
	}
	
	public static void save(InstructionSet iset, File file) throws Exception {
		JAXBContext context = JAXBContext.newInstance(InstructionSet.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(iset, file);
	}
	
	public static InstructionSet read(URL url) throws Exception {
		JAXBContext context = JAXBContext.newInstance(InstructionSet.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (InstructionSet) unmarshaller.unmarshal(url);
	}

	public static void crear() throws Exception {
		
		MicroProgram mover = new MicroProgram();
		mover.setName("MOV");
		mover.getConditions().add(new Condition(true, true, null, false));
		mover.getParameters().add(new Parameter(AddressingMode.REGISTER, "reg"));
		mover.getParameters().add(new Parameter(AddressingMode.IMMEDIATE, null));
		mover.getMicroInstructions().add(new SignalInstruction("BD-AC"));
		mover.getMicroInstructions().add(new SignalInstruction("AC-RT"));
		mover.getMicroInstructions().add(new MacroInstruction("OPERAND-TO-ALU", new MicroParameter("cde", "1"),  new MicroParameter("reg", "AC")));
		mover.getMicroInstructions().add(new SignalInstruction("ICP"));
		
		InstructionSet is = new InstructionSet();
		is.getMicroPrograms().add(mover);
		
		save(is, new File("instruction-set.xml"));
		
	}
	
	public static void leer() throws Exception {
		
		InstructionSet is = read(new File("instruction-set.xml").toURI().toURL());
		
		System.out.println(is.getMicroPrograms());
		
	}
	
	public static void main(String[] args) throws Exception {
//		crear();
		leer();
	}
	
}

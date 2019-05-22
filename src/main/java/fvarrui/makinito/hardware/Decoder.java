package fvarrui.makinito.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fvarrui.makinito.hardware.microcode.Condition;
import fvarrui.makinito.hardware.microcode.InstructionSet;
import fvarrui.makinito.hardware.microcode.MacroInstruction;
import fvarrui.makinito.hardware.microcode.MicroInstruction;
import fvarrui.makinito.hardware.microcode.MicroParameter;
import fvarrui.makinito.hardware.microcode.MicroProgram;
import fvarrui.makinito.hardware.microcode.Parameter;
import fvarrui.makinito.hardware.microcode.SignalInstruction;
import fvarrui.makinito.software.Instruction;
import fvarrui.makinito.software.Operand;

/**
 * El decodificador se encarga de, a partir de la instrucción almacenada en el registro de instrucción (RI)
 * y de los flags del Registro de Estados, determinar el conjunto de señales de control adecuadas para 
 * ejecutar la instrucción.
 * 
 * @author Francisco Vargas
 */
public class Decoder extends Component {
	
	private static final String INSTRUCTION_SET_FILE = "/fvarrui/makinito/hardware/microcode/microcode.xml";

	private FlagRegister flagsRegister;						// registro de estado
	private Register decoderInstructionRegister;			// registro de instrucción del decodificador
	private Sequencer sequencer;
	private Map<String, ControlSignal> allSignals;
	private Makinito makinito;
	private InstructionSet instructionSet;

	public Decoder(Sequencer sequencer, Makinito makinito) {
		super();
		setName("Decodificador");
		decoderInstructionRegister = new Register("RID");
		this.sequencer = sequencer;
		this.makinito = makinito;
		try {
			this.instructionSet = InstructionSet.read(getClass().getResource(INSTRUCTION_SET_FILE));
		} catch (Exception e) {
			throw new MakinitoException("No ha sido posible cargar los microprogramas en el decodificador desde " + INSTRUCTION_SET_FILE, e);
		}
	}
	
	public void init(Map<String, ControlSignal> signals) {
		this.allSignals = signals;
	}

	public Register getDecoderInstructionRegister() {
		return decoderInstructionRegister;
	}
	
	public void setFlagRegister(FlagRegister flagsRegister) {
		this.flagsRegister = flagsRegister;
	}
	
	public InstructionSet getInstructionSet() {
		return instructionSet;
	}

	private Map<String, String> operandsToMap(List<Operand> operands, List<Parameter> parameters) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			if (parameter.getName() != null) {
				Operand operand = operands.get(i);
				map.put(parameter.getName(), operand.getRegisterName());
			}
		}
		return map;
	}
	
	private boolean checkConditions(List<Condition> conditions) {
		if (conditions.isEmpty()) return true;
		for (Condition condition : conditions) {
			if (checkCondition(condition)) return true;
		}
		return false;
	}
	
	private boolean checkCondition(Condition condition) {
		if (condition == null) return true;
		
		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isCarry() != null && condition.isCarry() != flagsRegister.isCarry()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isNegative() != null && condition.isNegative() != flagsRegister.isNegative()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isOverflow() != null && condition.isOverflow() != flagsRegister.isOverflow()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isZero() != null && condition.isZero() != flagsRegister.isZero()) {
			return false;
		}

		
		return true;
	}
	
	public MicroProgram searchMicroProgram(Instruction instruction) {
		MicroProgram mp = instructionSet.search(instruction, makinito);
		if (mp == null || (mp.isMacro() != null && mp.isMacro())) {
			return null;
		}
		return mp;
	}
	
	public boolean isValidInstruction(Instruction instruction) {
		return (searchMicroProgram(instruction) != null);
	}
	
	private List<ControlSignal> decode(Instruction instruction) {
		List<ControlSignal> controlSignals = new ArrayList<ControlSignal>();
		
		MicroProgram mp = searchMicroProgram(instruction);
		
		if (checkConditions(mp.getConditions())) {
			
			Map<String, String> parameters = operandsToMap(instruction.getOperands(), mp.getParameters());
			List<String> signals = getSignals(mp, parameters);
			for (String signal : signals) {
				controlSignals.add(allSignals.get(signal));
			}

		} else {
			controlSignals.add(allSignals.get("ICP"));
		}
		
		System.out.println(controlSignals);
		
		return optimize(controlSignals);
	}

	private String translate(Map<String, String> parameters, String text) {
		for (String name : parameters.keySet()) {
			String value = parameters.get(name);
			text = text.replaceAll("\\$" + name, value);
		}
		return text;
	}
	
	private Map<String, String> parametersToMap(List<MicroParameter> microParameters) {
		Map<String, String> map = new HashMap<String, String>();
		for (MicroParameter mp : microParameters) {
			map.put(mp.getName(), mp.getValue());
		}
		return map;
	}
	
	private List<String> getSignals(MicroProgram mp, Map<String, String> parameters) {
		List<String> signals = new ArrayList<String>();
		
		for (MicroInstruction mi : mp.getMicroInstructions()) {
			
			if (mi instanceof SignalInstruction) {
				
				signals.add(translate(parameters, mi.getName()));
				
			} else if (mi instanceof MacroInstruction) {
				MacroInstruction macro = (MacroInstruction) mi;
				
				String macroName = translate(parameters, macro.getName());
				
				MicroProgram subMi = instructionSet.search(macroName, macro, parameters);

				Map<String, String> subMiParameters = parametersToMap(macro.getMicroParameters());
				Map<String, String> translatedParameters = new HashMap<String, String>();
				for (String name : subMiParameters.keySet()) {
					String value = subMiParameters.get(name);
					translatedParameters.put(name, translate(parameters, value));
				}
				
				signals.addAll(getSignals(subMi, translatedParameters));
			}
			
		}
		
		return signals;
	}
	
	private List<ControlSignal> optimize(List<ControlSignal> controlSignals) {
		List<ControlSignal> optimized = new ArrayList<ControlSignal>(controlSignals);
		ControlSignal previous = null;
		for (int i = 0; i < controlSignals.size(); i++) {
			ControlSignal signal = controlSignals.get(i); 
			if (previous != null) {
				
				if (previous.getName().contains("-") && signal.getName().contains("-")) {
					String [] prev = previous.getName().split("-");
					String [] curr = signal.getName().split("-");
					if (prev[0].equals(curr[1]) && prev[1].equals(curr[0])) {
						optimized.remove(signal);
						optimized.remove(previous);
						signal = null;
					}
				}
			}
			previous = signal;
		}
		return optimized;
	}

	public void decode() {
		Instruction instruction = (Instruction) getDecoderInstructionRegister().getContent();
		List<ControlSignal> signals = decode(instruction);
		sequencer.getExecution().clear();
		sequencer.getExecution().addAll(signals);
	}
	
	@Override
	public String toString(String tabs) {
		return 
				tabs + getName() + "{\n" +
				tabs + "\t" + decoderInstructionRegister.toString() + "\n" + 
				tabs + "}";
	}

	@Override
	public void reset() {
		flagsRegister.reset();
		decoderInstructionRegister.reset();
	}

//	public static void main(String[] args) {
//		Makinito makinito = new Makinito();
//		Decoder decoder = makinito.getCPU().getControlUnit().getDecoder();
//		
//		makinito.getCPU().getALU().getFlagsRegister().setNegative(false);
//		makinito.getCPU().getALU().getFlagsRegister().setZero(false);
//		
//		Instruction i = new Instruction("MULTIPLICAR");
//		i.getOperands().add(new Operand(AddressingMode.IMMEDIATE, 5, null));
//		i.getOperands().add(new Operand(AddressingMode.REGISTER, makinito.encodeRegister("AC"), "AC"));
//		i.getOperands().add(new Operand(AddressingMode.REGISTER, makinito.encodeRegister("AC"), "AC"));
//		i.getOperands().add(new Operand(AddressingMode.DIRECT, 12, null));
//		i.getOperands().add(new Operand(AddressingMode.DIRECT, 12, null));
//		i.getOperands().add(new Operand(AddressingMode.INDIRECT, 9, null));
//		i.getOperands().add(new Operand(AddressingMode.INDIRECT, 9, null));
//		i.getOperands().add(new Operand(AddressingMode.REGISTER, makinito.encodeRegister("RT"), "RT"));
//		i.getOperands().add(new Operand(AddressingMode.IMMEDIATE, 5, null));
//		
//		System.out.println(i);
//		
//		List<ControlSignal> signals = decoder.decode(i);
//		
//		System.out.println(signals);
//	}
	
}

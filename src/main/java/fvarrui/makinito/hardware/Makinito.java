package fvarrui.makinito.hardware;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fvarrui.makinito.config.Config;
import fvarrui.makinito.hardware.events.MakinitoListener;
import fvarrui.makinito.hardware.operators.AdditionOperator;
import fvarrui.makinito.hardware.operators.ComparisonOperator;
import fvarrui.makinito.hardware.operators.DivisionOperator;
import fvarrui.makinito.hardware.operators.MultiplicationOperator;
import fvarrui.makinito.hardware.operators.SubstractionOperator;
import fvarrui.makinito.hardware.signals.ALUOpControl;
import fvarrui.makinito.hardware.signals.DecControl;
import fvarrui.makinito.hardware.signals.EAFControl;
import fvarrui.makinito.hardware.signals.IPCControl;
import fvarrui.makinito.hardware.signals.ReadControl;
import fvarrui.makinito.hardware.signals.TermControl;
import fvarrui.makinito.hardware.signals.WireControl;
import fvarrui.makinito.hardware.signals.WriteControl;
import fvarrui.makinito.software.Program;
import fvarrui.makinito.software.parser.Parser;
import fvarrui.makinito.software.parser.ParserException;

/**
 * Máquina que simula la aquitectura Von Neumann, con los siguientes componentes
 * interconectados mediante "cables" (Wire):
 * 
 * - CPU
 * 		- Unidad de control
 * 		- Unidad aritmético-lógica
 * - Memoria principal
 * - Bus de datos
 * - Bus de direcciones
 * - Reloj
 * 
 * @author Francisco Vargas 
 */
public class Makinito extends FunctionalUnit {

	private static final Integer FREQUENCY = 1000; //
	
	private boolean finish;
	
	private Program program = null;

	private CPU cpu;
	private Memory memory;
	private Clock clock;
	private Bus addressBus;
	private Bus dataBus;
	private Map<String, Wire> wires = new HashMap<String, Wire>();
	
	private Map<String, Register> registers = new HashMap<String, Register>();
	private List<String> registerCodes = new ArrayList<String>();

	private Map<String, ControlSignal> signals = new HashMap<String, ControlSignal>();

	public List<MakinitoListener> listeners = new ArrayList<MakinitoListener>();
	
	public Makinito() {
		super();
		setName("Makinito");
		initUnits();
		initBuses();
		initWires();
		initControlSignals();
		initRegisters();
		
		getComponents().add(cpu);
		getComponents().add(memory);
		getComponents().add(dataBus);
		getComponents().add(addressBus);
	}

	private void initUnits() {
		memory = new Memory(Config.getMemoryCapacity());
		
		cpu = new CPU(this);
		
		cpu.getALU().addOperator(new AdditionOperator());
		cpu.getALU().addOperator(new SubstractionOperator());
		cpu.getALU().addOperator(new MultiplicationOperator());
		cpu.getALU().addOperator(new DivisionOperator());
		cpu.getALU().addOperator(new ComparisonOperator());
		
		cpu.getControlUnit().getDecoder().setFlagRegister(cpu.getALU().getFlagsRegister());
		
		clock = new Clock(FREQUENCY, cpu.getControlUnit().getSequencer());
	}

	private void initBuses() {
		addressBus = new Bus("Bus de Direcciones");
		dataBus = new Bus("Bus de Datos");
	}
	
	private void initRegisters() {
		addRegister(cpu.getALU().getAccumulator()); 					// AC
		addRegister(cpu.getALU().getTemporaryRegister());				// RT
		addRegister(cpu.getALU().getFlagsRegister());					// RE
		addRegister(cpu.getControlUnit().getInstructionRegister());		// RI		
		addRegister(cpu.getControlUnit().getProgramCounter());			// CP		
	}

	private void addRegister(Register register) {
		registers.put(register.getName(), register);
		registerCodes.add(register.getName());
	}

	private void addWire(Wire wire) {
		wires.put(wire.getName(), wire);
	}

	private void addSignal(ControlSignal signal) {
		signals.put(signal.getName(), signal);
	}

	private void initWires() {

		// cableado de la memoria
		addWire(new Wire("RM-BD", memory.getMemoryRegister(), dataBus)); 
		addWire(new Wire("BD-RM", dataBus, memory.getMemoryRegister())); 
		addWire(new Wire("BDI-RD", addressBus, memory.getAddressRegister()));
		addWire(new Wire("BD-RD", dataBus, memory.getAddressRegister()));
		addWire(new Wire("BDI-RM", addressBus, memory.getMemoryRegister()));
		addWire(new Wire("RM-BDI", memory.getMemoryRegister(), addressBus));

		// cableado de la unidad de control
		addWire(new Wire("CP-BDI", cpu.getControlUnit().getProgramCounter(), addressBus)); 
		addWire(new Wire("BDI-CP", addressBus, cpu.getControlUnit().getProgramCounter())); 
		addWire(new Wire("CP-BD", cpu.getControlUnit().getProgramCounter(), dataBus)); 
		addWire(new Wire("BD-CP", dataBus, cpu.getControlUnit().getProgramCounter())); 
		addWire(new Wire("BD-RI", dataBus, cpu.getControlUnit().getInstructionRegister())); 
		addWire(new Wire("CDE1-BD", cpu.getControlUnit().getInstructionRegister(), dataBus)); 
		addWire(new Wire("CDE2-BD", cpu.getControlUnit().getInstructionRegister(), dataBus)); 
		addWire(new Wire("CDE1-BDI", cpu.getControlUnit().getInstructionRegister(), addressBus)); 
		addWire(new Wire("CDE2-BDI", cpu.getControlUnit().getInstructionRegister(), addressBus)); 
		addWire(new Wire("RI-DEC", cpu.getControlUnit().getInstructionRegister(), cpu.getControlUnit().getDecoder().getDecoderInstructionRegister())); 

		// cableado de la unidad aritmético-lógica
		addWire(new Wire("AC-BD", cpu.getALU().getAccumulator(), dataBus)); 
		addWire(new Wire("BD-AC", dataBus, cpu.getALU().getAccumulator())); 
		addWire(new Wire("RT-BD", cpu.getALU().getTemporaryRegister(), dataBus)); 
		addWire(new Wire("BD-RT", dataBus, cpu.getALU().getTemporaryRegister())); 

	}

	private void initControlSignals() {

		// crea una señal de control por cada cable
		for (Wire wire : wires.values()) {
			if (wire.getName().matches("CDE[12]-.*"))
				addSignal(new EAFControl(wire));
			else
				addSignal(new WireControl(wire));
		}

		// señales de control de la memoria
		addSignal(new ReadControl(memory));
		addSignal(new WriteControl(memory));

		// señales de control de la UC
		addSignal(new IPCControl(cpu.getControlUnit().getProgramCounter()));

		// señales de control de la UAL
		for (Operator operator : cpu.getALU().getOperators()) {
			addSignal(new ALUOpControl(operator));
		}

		// señales de control del decodificador
		addSignal(new DecControl(cpu.getControlUnit().getDecoder()));

		// señales de control de Makinito
		addSignal(new TermControl(this));

		// inicializa la UC con las posibles señales de control
		cpu.getControlUnit().init(signals);
	}

	public void loadProgram(Program program) {
		this.memory.clear();
		this.program = program;
		reset();
	}
	
	public void loadProgram(File file) throws IOException, ParserException {
		loadProgram(new Parser(this).parse(file));
	}
	
	public Program getProgram() {
		return program;
	}

	public CPU getCPU() {
		return cpu;
	}

	public Memory getMemory() {
		return memory;
	}

	public Clock getClock() {
		return clock;
	}

	public Bus getAddressBus() {
		return addressBus;
	}

	public Bus getDataBus() {
		return dataBus;
	}

	public Map<String, Wire> getWires() {
		return wires;
	}

	public Map<String, ControlSignal> getSignals() {
		return signals;
	}
	
	public Map<String, Register> getRegisters() {
		return registers;
	}
	
	public String decodeRegister(int registerCode) {
		return registerCodes.get(registerCode);
	}

	public int encodeRegister(String registerName) {
		return registerCodes.indexOf(registerName);
	}

	public void finish() {
		this.finish = true;
		fireFinishedEvent();
	}
	
	public void executeSignal() {
		if (program == null) {
			throw new MakinitoException("No hay ningún programa cargado en la memoria de Makinito.");
		}
		if (isFinished()) {
			throw new MakinitoException("El programa ha terminado su ejecución.");
		}
		clock.pulse();
	}
	
	public void executeInstruction() {
		do {
			executeSignal();
		} while (!cpu.getControlUnit().getSequencer().isNewInstruction() && !finish);
	}
	
	public boolean isFinished() {
		return finish;
	}
	
	public void reset() {
		if (program != null) {
			finish = false;
			
			memory.reset();
			cpu.reset();
			dataBus.reset();
			addressBus.reset();
			
			memory.setInfo(0, program.asArray());
			cpu.getControlUnit().getProgramCounter().setDatum(program.getFirstInstructionAddress());
		}
	}

	public void start(boolean signalLevelExecution) {
		System.out.println("---> INICIANDO LA EJECUCION DEL PROGRAMA <---");
		finish = false;
		while (!finish) {
			if (signalLevelExecution)
				executeSignal();
			else
				executeInstruction();
			System.out.println(this);
			if (clock.getFrequency() > 0) {
				try {
					Thread.sleep(clock.getFrequency());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("---> FIN DEL PROGRAMA <---");
	}
	
	private void fireFinishedEvent() {
		for (MakinitoListener listener : listeners) {
			listener.finished();
		}
	}

	public List<MakinitoListener> getListeners() {
		return listeners;
	}
	
}

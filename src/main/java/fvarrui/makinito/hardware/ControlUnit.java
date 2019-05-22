package fvarrui.makinito.hardware;

import java.util.Map;

/**
 * La unidad de control es la unidad funcional encargada de recuperar instrucciones de 
 * la memoria principal (cargarlas), decodificarlas, ejecutarlas y pasar a la siguiente
 * instrucci�n de forma c�clica, hasta que se active la se�al de control TERM que detiene
 * la m�quina. 
 * 
 * @author Francisco Vargas
 *
 */
public class ControlUnit extends FunctionalUnit {
	private Sequencer sequencer;
	private Decoder decoder;
	
	private Register instructionRegister;	// registro de instrucci�n
	private Register programCounter;		// contador de programa
	
	public ControlUnit(Makinito makinito) {
		setName("UC");
		
		sequencer = new Sequencer();
		decoder = new Decoder(sequencer, makinito);
		instructionRegister = new Register("RI");
		programCounter = new Register("CP");
		
		getComponents().add(sequencer);
		getComponents().add(decoder);
		getComponents().add(instructionRegister);
		getComponents().add(programCounter);
	}
	
	public void init(Map<String, ControlSignal> signals) {
		sequencer.init(signals);
		decoder.init(signals);
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public Decoder getDecoder() {
		return decoder;
	}

	public Register getInstructionRegister() {
		return instructionRegister;
	}

	public Register getProgramCounter() {
		return programCounter;
	}
	
	@Override
	public void reset() {
		instructionRegister.reset();
		programCounter.reset();
		sequencer.reset();
		decoder.reset();
	}
	
}

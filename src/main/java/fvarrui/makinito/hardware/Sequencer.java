package fvarrui.makinito.hardware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * El secuenciador es el encargado de activar las señales de control en el orden adecuado
 * para ejecutar las instrucciones, así como cargarlas y decodificarlas. 
 * 
 * Funciona de forma síncrona marcado por el reloj del sistema.   
 * 
 * @author fvarrui
 *
 */
public class Sequencer extends Component {
	
	private Phase phase; 
	private Integer current = 0;
	
	private List<ControlSignal> loading = new ArrayList<ControlSignal>();
	private List<ControlSignal> decofification = new ArrayList<ControlSignal>();
	private List<ControlSignal> execution = new ArrayList<ControlSignal>();

	private List<ControlSignal> signals;
	
	public Sequencer() {
		super();
		setName("Secuenciador");
	}

	public void init(Map<String, ControlSignal> senales) {
		initLoading(senales);
		initDecodification(senales);
		reset();
	}
	
	public Phase getPhase() {
		return phase;
	}

	public final Integer getCurrent() {
		return current;
	}

	public boolean isNewInstruction() {
		return (phase == Phase.FETCH && current == 0);
	}

	private void initLoading(Map<String, ControlSignal> signals) {
		loading.add(signals.get("CP-BDI"));
		loading.add(signals.get("BDI-RD"));
		loading.add(signals.get("LECT"));
		loading.add(signals.get("RM-BD"));
		loading.add(signals.get("BD-RI"));
	}
	
	private void initDecodification(Map<String, ControlSignal> signals) {
		decofification.add(signals.get("RI-DEC"));
		decofification.add(signals.get("DEC"));
	}

	public List<ControlSignal> getExecution() {
		return this.execution;
	}
	
	private void nextPhase() {
		switch (this.phase) {
		case FETCH: 
			this.phase = Phase.DECODE;
			this.signals = decofification;
			break;
		case DECODE: 
			this.phase = Phase.EXECUTE;
			this.signals = execution;
			break;
		case EXECUTE: 
			this.phase = Phase.FETCH;
			this.signals = loading;
			break;
		}
	}

	public void execute() {
		signals.get(current).activate();
		current++;
		if (current >= signals.size()) {
			{ nextPhase(); } while (signals.isEmpty());
			current = 0;				
		}
	}
	
	public final List<ControlSignal> getSignals() {
		return signals;
	}

	@Override
	public String toString(String tabs) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(tabs + getName() + " {\n");
		buffer.append(tabs + "\t" + "fase=" + phase + "[" + current + "]\n");
		buffer.append(tabs + "\t" + "nuevaInstruccion=" + isNewInstruction() + "\n");
		buffer.append(tabs + "\t" + "ejecucion {\n");
		int i = 0;
		for (ControlSignal senal : signals) {
			buffer.append(tabs + "\t" + "\t" + "[" + i + "] " + senal + "\n");
			i++;
		}
		buffer.append(tabs + "\t" + "}\n");
		buffer.append(tabs + "}");
		return buffer.toString();
	}
	
	public String getCurrentSignalDescription() {
		if (current == -1 || signals.get(current) == null)
			return "";
		return signals.get(current).gttDescription();
	}

	@Override
	public void reset() {
		this.phase = Phase.FETCH;
		this.current = 0;
		this.signals = loading;
	}
	
}

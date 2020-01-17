package fvarrui.makinito.hardware;

/**
 * Reloj del sistema: encargado de marcar los tiempos del secuenciador para que éste lance 
 * de forma ordenada en cada pulso la señal de control adecuada.
 * 
 * @author fvarrui
 *
 */
public class Clock extends Component {
	private Integer frequency;
	private Sequencer sequencer;

	public Clock(Integer frequency, Sequencer sequencer) {
		super();
		this.frequency = frequency;
		this.sequencer = sequencer;
	}
	
	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public void setSequencer(Sequencer sequencer) {
		this.sequencer = sequencer;
	}

	public void pulse() {
		sequencer.execute();
	}

	@Override
	public void reset() {
	}

}

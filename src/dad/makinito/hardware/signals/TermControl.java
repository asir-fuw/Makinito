package dad.makinito.hardware.signals;

import dad.makinito.hardware.Makinito;
import dad.makinito.hardware.ControlSignal;

public class TermControl extends ControlSignal {
	
	public TermControl(Makinito makinito) {
		super("TERM", makinito, "Termina el programa y detiene a Makinito");
	}

	@Override
	public void handleActivate() {
		((Makinito)getComponent()).finish();
	}

}

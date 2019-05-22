package fvarrui.makinito.hardware.signals;

import fvarrui.makinito.hardware.ControlSignal;
import fvarrui.makinito.hardware.Makinito;

public class TermControl extends ControlSignal {
	
	public TermControl(Makinito makinito) {
		super("TERM", makinito, "Termina el programa y detiene a Makinito");
	}

	@Override
	public void handleActivate() {
		((Makinito)getComponent()).finish();
	}

}

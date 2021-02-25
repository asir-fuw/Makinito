package io.github.fvarrui.makinito.hardware.signals;

import io.github.fvarrui.makinito.hardware.ControlSignal;
import io.github.fvarrui.makinito.hardware.Makinito;

public class TermControl extends ControlSignal {
	
	public TermControl(Makinito makinito) {
		super("TERM", makinito, "Termina el programa y detiene a Makinito");
	}

	@Override
	public void handleActivate() {
		((Makinito)getComponent()).finish();
	}

}

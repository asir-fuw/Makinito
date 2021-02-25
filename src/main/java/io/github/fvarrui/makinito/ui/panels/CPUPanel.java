package io.github.fvarrui.makinito.ui.panels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CPUPanel extends JPanel {
	
	private ControlUnitPanel controlUnitPanel;
	private ALUPanel aluPanel;

	public CPUPanel() {
		initPanel();
		initComponents();
	}

	private void initPanel() {
		setBorder(BorderFactory.createTitledBorder("CPU:"));
		setLayout(new GridLayout(1, 2));
	}

	private void initComponents() {
		controlUnitPanel = new ControlUnitPanel();
		aluPanel = new ALUPanel();
		
		add(aluPanel);
		add(controlUnitPanel);
	}

	public ControlUnitPanel getControlUnitPanel() {
		return controlUnitPanel;
	}

	public ALUPanel getALUPanel() {
		return aluPanel;
	}
	
}

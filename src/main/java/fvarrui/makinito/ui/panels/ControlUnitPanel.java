package fvarrui.makinito.ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fvarrui.makinito.hardware.ControlSignal;
import fvarrui.makinito.hardware.Phase;
import fvarrui.makinito.software.Info;

@SuppressWarnings("serial")
public class ControlUnitPanel extends JPanel {

	private JList<String> signalsList;
	private DefaultListModel<String> signalsModel;
	private JTextField instructionRegisterText; 
	private JTextField programCounterText; 
	private JTextField phaseText; 
	
	public ControlUnitPanel() {
		initPanel();
		initComponents();
	}

	private void initPanel() {
		setBorder(BorderFactory.createTitledBorder("UC:"));
		setLayout(new GridBagLayout());
	}

	private void initComponents() {
		
		signalsModel = new DefaultListModel<String>();
		signalsList = new JList<String>(signalsModel);
		signalsList.setEnabled(false);
		
		JLabel instructionRegisterLabel = new JLabel("RI");
		instructionRegisterLabel.setToolTipText("Registro de Instrucci�n");

		JLabel programCounterLabel = new JLabel("CP");
		programCounterLabel.setToolTipText("Contador de Programa");

		JLabel phaseLabel = new JLabel("Fase");
		phaseLabel.setToolTipText("Fase actual del secuenciador");

		instructionRegisterText = new JTextField();
		instructionRegisterText.setEnabled(false);
		instructionRegisterText.setHorizontalAlignment(JTextField.CENTER);

		programCounterText = new JTextField();
		programCounterText.setEnabled(false);
		programCounterText.setHorizontalAlignment(JTextField.CENTER);

		phaseText = new JTextField();
		phaseText.setEnabled(false);
		phaseText.setHorizontalAlignment(JTextField.CENTER);

		add(phaseLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(phaseText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(new JScrollPane(signalsList), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(programCounterLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(programCounterText, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(instructionRegisterLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(instructionRegisterText, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

	}
	
	public void setSignals(List<ControlSignal> signals) {
		signalsModel.clear();
		int pos = 0;
		for (ControlSignal signal : signals) {
			signalsModel.addElement(pos + ":  " + signal.getName());
			pos++;
		}
	}

	public void setPhase(Phase phase) {
		switch (phase) {
		case DECODE:	phaseText.setText("DECODIFICACION"); break;
		case EXECUTE:	phaseText.setText("EJECUCION"); break;
		case FETCH:		phaseText.setText("CARGA"); break;
		}
	}

	public void setProgramCounter(Info info) {
		programCounterText.setText(info.toString());
	}
	
	public void setInstructionRegister(Info info) {
		instructionRegisterText.setText(info.toString());
	}

	public void markSignal(int pos) {
		signalsList.setSelectedIndex(pos);
		signalsList.ensureIndexIsVisible(signalsList.getSelectedIndex());
	}
	
}

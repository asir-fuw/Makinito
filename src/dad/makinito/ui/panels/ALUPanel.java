package dad.makinito.ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dad.makinito.software.Info;

@SuppressWarnings("serial")
public class ALUPanel extends JPanel {

	private JTextField accumulatorText;
	private JTextField temporaryRegisterText;
	private JTextField flagsRegisterText;
	
	public ALUPanel() {
		initPanel();
		initComponents();
	}

	private void initPanel() {
		setBorder(BorderFactory.createTitledBorder("UAL:"));
		setLayout(new GridBagLayout());
	}

	private void initComponents() {
		JLabel accumulatorLabel = new JLabel("AC");
		accumulatorLabel.setToolTipText("Registro Acumulador");

		JLabel temporaryRegisterLabel = new JLabel("RT");
		temporaryRegisterLabel.setToolTipText("Registro Temporal");

		JLabel flagsRegisterLabel = new JLabel("RE");
		flagsRegisterLabel.setToolTipText("Registro de Estado");

		accumulatorText = new JTextField();
		accumulatorText.setEnabled(false);
		accumulatorText.setHorizontalAlignment(JTextField.CENTER);

		temporaryRegisterText = new JTextField();
		temporaryRegisterText.setEnabled(false);
		temporaryRegisterText.setHorizontalAlignment(JTextField.CENTER);

		flagsRegisterText = new JTextField();
		flagsRegisterText.setEnabled(false);
		flagsRegisterText.setHorizontalAlignment(JTextField.CENTER);

		add(accumulatorLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(accumulatorText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(temporaryRegisterLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(temporaryRegisterText, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		add(flagsRegisterLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		add(flagsRegisterText, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	}
	
	public void setAccumulator(Info info) {
		accumulatorText.setText(info.toString());
	}

	public void setTemporaryRegister(Info info) {
		temporaryRegisterText.setText(info.toString());
	}

	public void setFlagsRegister(String info) {
		flagsRegisterText.setText(info);
	}

}

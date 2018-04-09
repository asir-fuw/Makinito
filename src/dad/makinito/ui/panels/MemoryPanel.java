package dad.makinito.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dad.makinito.software.Info;

@SuppressWarnings("serial")
public class MemoryPanel extends JPanel {

	private JTextField memoryRegisterText;
	private JTextField addressRegisterText;
	private JList<String> contentList;
	private DefaultListModel<String> contentModel;

	public MemoryPanel() {
		super();
		initPanel();
		initComponents();
	}

	private void initPanel() {
		setBorder(BorderFactory.createTitledBorder("Memoria:"));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 0));
	}

	private void initComponents() {
		contentModel = new DefaultListModel<String>();
		contentList = new JList<String>(contentModel);
		contentList.setEnabled(false);

		JLabel addressRegisterLabel = new JLabel("RD");
		addressRegisterLabel.setToolTipText("Registro de Direcciones");
		
		JLabel memoryRegisterLabel = new JLabel("RM"); 
		memoryRegisterLabel.setToolTipText("Registro de Memoria");
		
		addressRegisterText = new JTextField();
		addressRegisterText.setHorizontalAlignment(JTextField.CENTER);
		addressRegisterText.setEnabled(false);
		
		memoryRegisterText = new JTextField();
		memoryRegisterText.setHorizontalAlignment(JTextField.CENTER);
		memoryRegisterText.setEnabled(false);
		
		JPanel registersPanel = new JPanel(new GridBagLayout());
		registersPanel.add(addressRegisterLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		registersPanel.add(addressRegisterText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		registersPanel.add(memoryRegisterLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		registersPanel.add(memoryRegisterText, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		add(new JScrollPane(contentList), BorderLayout.CENTER);
		add(registersPanel, BorderLayout.SOUTH);
	}
	
	public void setContent(Info [] content) {
		contentModel.clear();
		int pos = 0;
		for (Info info : content) {
			if (info != null)
				contentModel.addElement(pos + ":  " + info);
			pos++;
		}
	}
	
	public void setAddressRegister(Info info) {
		addressRegisterText.setText(info.toString());
	}
	
	public void setMemoryRegister(Info info) {
		memoryRegisterText.setText(info.toString());
	}

	public void markInstruction(int pos) {
		contentList.setSelectedIndex(pos);
		contentList.ensureIndexIsVisible(contentList.getSelectedIndex());
	}

}

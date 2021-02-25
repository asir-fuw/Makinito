package io.github.fvarrui.makinito.ui.panels;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.github.fvarrui.makinito.software.Info;

@SuppressWarnings("serial")
public class BusPanel extends JPanel {

	private String title;
	
	private JTextField contentText;
	
	public BusPanel(String title) {
		this.title = title;
		initPanel();
		initComponents();
	}

	private void initPanel() {
		setBorder(BorderFactory.createTitledBorder(title + ":"));
		setLayout(new FlowLayout(FlowLayout.CENTER));
	}

	private void initComponents() {
		contentText = new JTextField(20);
		contentText.setEnabled(false);
		contentText.setHorizontalAlignment(JTextField.CENTER);
		add(contentText);
	}
	
	public void setContent(Info info) {
		contentText.setText(info.toString());
	}
	
}

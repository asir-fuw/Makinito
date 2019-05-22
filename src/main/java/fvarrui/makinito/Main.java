package fvarrui.makinito;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fvarrui.makinito.i18n.Messages;
import fvarrui.makinito.ui.MakinitoFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					new MakinitoFrame().setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
							Messages.getString("makinitoFrame.errorDialog.main.message") + ":\n\n" + e.getMessage(), 
							Messages.getString("makinitoFrame.errorDialog.main.title"), 
							JOptionPane.ERROR_MESSAGE
						);
					e.printStackTrace();
				}
			}
		});
	}

}

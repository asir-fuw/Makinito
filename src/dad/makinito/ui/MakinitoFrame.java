package dad.makinito.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import dad.makinito.config.Config;
import dad.makinito.hardware.Makinito;
import dad.makinito.hardware.MakinitoException;
import dad.makinito.hardware.events.MakinitoAdapter;
import dad.makinito.i18n.Messages;
import dad.makinito.software.parser.ParserException;
import dad.makinito.ui.panels.BusPanel;
import dad.makinito.ui.panels.CPUPanel;
import dad.makinito.ui.panels.MemoryPanel;
import dad.makinito.ui.resources.Icons;

@SuppressWarnings("serial")
public class MakinitoFrame extends JFrame {
	
	private static final String LAF_NIMBUS = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	private static final int FREQUENCY = 500;
	
	private Makinito makinito;
	private File file;
	
	private Timer timer;
	
	private JCheckBox instructionCheck;
	private JButton stopButton, startButton, stepButton, instButton, restartButton, reloadButton;
	private JSpinner frequencySpinner;
	
	private JLabel infoLabel;
	
	private BusPanel dataBusPanel, addressBusPanel;
	private CPUPanel cpuPanel;
	private MemoryPanel memoryPanel;
	
	public MakinitoFrame() {
		initMakinito();
		initFrame();
		initComponents();
		init();
	}

	private void initMakinito() {
		this.makinito = new Makinito();
		this.makinito.getClock().setFrequency(FREQUENCY);		
		this.makinito.getListeners().add(new MakinitoAdapter() {
			public void finished() {
				onMakinitoFinished();
			};
		});
	}

	private void initFrame() {
		setSize(720, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(Icons.LOGO_SMALL);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { onWindowClosing(e); }
		});
	}

	private void initComponents() {
		initMenuBar();
		
		timer = new Timer(0, new ActionListener() {
			public void actionPerformed(ActionEvent e) { onTimerActionPerformed(e); }
		});
		
		JPanel makinitoPanel = initMakinitoPanel();
		JPanel infoPanel = initInfoPanel();
		JPanel controlPanel = initControlPanel();
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(makinitoPanel, BorderLayout.CENTER);
		centerPanel.add(infoPanel, BorderLayout.SOUTH);
		
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}
	
	private JPanel initInfoPanel() {
		infoLabel = new JLabel();
		
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoPanel.add(infoLabel);
		
		return infoPanel;
	}

	private JPanel initControlPanel() {
		JPanel controlPanel = new JPanel(new FlowLayout());
		
		instructionCheck = new JCheckBox(Messages.getString("makinitoFrame.controlPanel.instruccionCheck.text"));
		instructionCheck.setToolTipText(Messages.getString("makinitoFrame.controlPanel.instruccionCheck.toolTipText"));

		stopButton = new JButton(Icons.PAUSE);
		stopButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.stopButton.toolTipText"));
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onStopButtonActionPerformed(e); }
		});

		startButton = new JButton(Icons.PLAY);
		startButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.startButton.toolTipText"));
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onStartButtonActionPerformed(e); }
		});

		stepButton = new JButton(Icons.STEP);
		stepButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.stepButton.toolTipText"));
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onStepButtonActionPerformed(e); }
		});

		instButton = new JButton(Icons.NEXT);
		instButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.instButton.toolTipText"));
		instButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onInstButtonActionPerformed(e); }
		});
		
		restartButton = new JButton(Icons.RELOAD);
		restartButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.restartButton.toolTipText"));
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onRestartButtonActionPerformed(e); }
		});
		
		reloadButton = new JButton(Messages.getString("makinitoFrame.controlPanel.reloadButton.text"));
		reloadButton.setToolTipText(Messages.getString("makinitoFrame.controlPanel.reloadButton.toolTipText"));
		reloadButton.setPreferredSize(new Dimension((int)reloadButton.getPreferredSize().getWidth(), (int)restartButton.getPreferredSize().getHeight()));
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onReloadButtonActionPerformed(e); }
		});
		
		SpinnerModel frequencyModel = new SpinnerNumberModel(FREQUENCY, 1, 10000, 100);
		frequencySpinner = new JSpinner(frequencyModel);
		frequencySpinner.setToolTipText(Messages.getString("makinitoFrame.controlPanel.frequencySpinner.toolTipText"));
		frequencySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) { onFrecuenciaSpinnerStateChanged(e); }
		});
		
		controlPanel.add(instructionCheck);
		controlPanel.add(stopButton);
		controlPanel.add(startButton);
		controlPanel.add(stepButton);
		controlPanel.add(instButton);
		controlPanel.add(restartButton);
		controlPanel.add(reloadButton);
		controlPanel.add(new JLabel(Messages.getString("makinitoFrame.controlPanel.frequencyLabel.text")));
		controlPanel.add(frequencySpinner);
		controlPanel.add(new JLabel("ms"));
		
		return controlPanel;
	}

	private JPanel initMakinitoPanel() {
		JPanel centerPanel = new JPanel(new GridBagLayout());
		
		memoryPanel = new MemoryPanel(); 
		cpuPanel = new CPUPanel(); 
		dataBusPanel = new BusPanel(Messages.getString("makinitoFrame.makinitoPanel.dataBusPanel.title"));
		addressBusPanel = new BusPanel(Messages.getString("makinitoFrame.makinitoPanel.addressBusPanel.title"));
		
		centerPanel.add(cpuPanel, new GridBagConstraints(0, 0, 1, 1, 0.66, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		centerPanel.add(dataBusPanel, new GridBagConstraints(0, 1, 1, 1, 0.66, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		centerPanel.add(addressBusPanel, new GridBagConstraints(0, 2, 1, 1, 0.66, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		centerPanel.add(memoryPanel, new GridBagConstraints(1, 0, 1, 3, 0.33, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		return centerPanel;
	}

	private void initMenuBar() {
		JMenuItem openMenuItem = new JMenuItem(Messages.getString("makinitoFrame.fileMenu.openMenuItem.text"), Icons.OPEN);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpenMenuItemActionPerformed(e);
			}
		});

		JMenuItem exitMenuItem = new JMenuItem(Messages.getString("makinitoFrame.fileMenu.exitMenuItem.text"));
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExitMenuItemActionPerformed(e);
			}
		});

		JMenu fileMenu = new JMenu(Messages.getString("makinitoFrame.fileMenu.text"));
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		JMenuItem aboutMenuItem = new JMenuItem(Messages.getString("makinitoFrame.helpMenu.aboutMenuItem.text"));
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onAboutMenuItemActionPerformed(e);
			}
		});
		
		JMenu helpMenu = new JMenu(Messages.getString("makinitoFrame.helpMenu.text"));
		helpMenu.add(aboutMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);
	}
	
	protected void onAboutMenuItemActionPerformed(ActionEvent e) {
		new AboutDialog(this).setVisible(true);
	}

	protected void onWindowClosing(WindowEvent e) {
		exit();
	}
	
	protected void onRestartButtonActionPerformed(ActionEvent e) {
		restart();
	}
	
	protected void onReloadButtonActionPerformed(ActionEvent e) {
		loadProgram(file);
	}

	protected void onOpenMenuItemActionPerformed(ActionEvent e) {
		abrir();
	}

	protected void onTimerActionPerformed(ActionEvent e) {
		try {
			if (instructionCheck.isSelected())
				makinito.executeInstruction();
			else
				makinito.executeSignal();
			update();
		} catch (MakinitoException ex) {
			onStopButtonActionPerformed(e);
			checkIfFinished(ex);
		}
	}

	protected void onExitMenuItemActionPerformed(ActionEvent e) {
		exit();
	}

	protected void onStopButtonActionPerformed(ActionEvent e) {
		stop();
	}

	protected void onStartButtonActionPerformed(ActionEvent e) {
		start();
	}

	protected void onStepButtonActionPerformed(ActionEvent e) {
		nextControlSignal();
	}

	protected void onInstButtonActionPerformed(ActionEvent e) {
		nextInstruction();
	}

	protected void onFrecuenciaSpinnerStateChanged(ChangeEvent e) {
		makinito.getClock().setFrequency((int)frequencySpinner.getValue());
		timer.setDelay(makinito.getClock().getFrequency());
	}	
	
	private void start() {
		timer.setDelay(makinito.getClock().getFrequency());
		timer.start();
		updateControls(false);
	}

	private void nextControlSignal() {
		try {
			makinito.executeSignal();
			update();
		} catch (MakinitoException ex) {
			checkIfFinished(ex);
		}		
	}

	private void checkIfFinished(MakinitoException ex) {
		if (makinito.isFinished())
			JOptionPane.showMessageDialog(this, 
					ex.getMessage() + "\n\n" + Messages.getString("makinitoFrame.warningDialog.mustRestart.message"), 
					Messages.getString("makinitoFrame.warningDialog.mustRestart.title"), 
					JOptionPane.WARNING_MESSAGE
				);
		else 
			JOptionPane.showMessageDialog(this, 
					ex.getMessage(), 
					Messages.getString("makinitoFrame.errorDialog.title"), 
					JOptionPane.ERROR_MESSAGE
				);
	}
	
	private void nextInstruction() {
		try {
			makinito.executeInstruction();
			update();
		} catch (MakinitoException ex) {
			checkIfFinished(ex);
		}
	}
	
	private void stop() {
		timer.stop();
		updateControls(true);
	}
	
	private void exit() {
		int result = JOptionPane.showConfirmDialog(this, 
				Messages.getString("makinitoFrame.confirmDialog.exit.message"), 
				Messages.getString("makinitoFrame.confirmDialog.exit.title"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
			);
		if (result == JOptionPane.YES_OPTION) {
			stop();
			dispose();
		}
	}
	
	private void updateControls(boolean activar) {
		stopButton.setEnabled(!activar);
		startButton.setEnabled(activar);
		stepButton.setEnabled(activar);
		instButton.setEnabled(activar);		
	}
	
	private void init() {
		updateControls(true);
		update();
		setTitle("Makinito " + Config.getVersion() + ((makinito.getProgram() != null) ? " - " + makinito.getProgram().getName() : ""));
	}
	
	private void abrir() {
		stop();
		JFileChooser openDialog = new JFileChooser(new File("."));
		FileFilter allFileFilter = openDialog.getChoosableFileFilters()[0];
		openDialog.removeChoosableFileFilter(openDialog.getChoosableFileFilters()[0]);
		openDialog.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("makinitoFrame.openDialog.filter.makiFiles"), "maki"));
		openDialog.addChoosableFileFilter(allFileFilter);
		openDialog.setSelectedFile(file);
		int resultado = openDialog.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File newFile = openDialog.getSelectedFile();
			if (loadProgram(newFile)) file = newFile;
		}		
	}
	
	private boolean loadProgram(File file) {
		boolean loaded = true;
		try {
			stop();
			if (file != null) {
				makinito.loadProgram(file);
				init();
				JOptionPane.showMessageDialog(this, 
						Messages.getString("makinitoFrame.infoDialog.loadProgram.message", file.getName()), 
						Messages.getString("makinitoFrame.infoDialog.loadProgram.title"), 
						JOptionPane.INFORMATION_MESSAGE
					);
			} else {
				JOptionPane.showMessageDialog(this, 
						Messages.getString("makinitoFrame.warningDialog.loadProgram.message"), 
						Messages.getString("makinitoFrame.warningDialog.loadProgram.title"), 
						JOptionPane.ERROR_MESSAGE
					);
				loaded = false;
			}
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, 
					Messages.getString("makinitoFrame.errorDialog.loadProgram.message", file.getName()) + "\n\n" + e1.getMessage(), 
					Messages.getString("makinitoFrame.errorDialog.loadProgram.title"), 
					JOptionPane.ERROR_MESSAGE
				);
			e1.printStackTrace();
			loaded = false;
		} catch (ParserException e1) {
			JOptionPane.showMessageDialog(this, 
					Messages.getString("makinitoFrame.errorDialog.parseProgram.message", file.getName(), e1.getLine()) + "\n\n" +  e1.getMessage(), 
					Messages.getString("makinitoFrame.errorDialog.parseProgram.title"), 
					JOptionPane.ERROR_MESSAGE
				);
			e1.printStackTrace();
			loaded = false;
		} 
		return loaded;
	}
	
	private void restart() {
		stop();
		makinito.reset();
		update();		
	}
	
	private void update() {
		
		// actualiza el reloj
		frequencySpinner.setValue(makinito.getClock().getFrequency());
		
		// actualiza el panel de la memoria
		memoryPanel.setContent(makinito.getMemory().getContent());
		memoryPanel.setAddressRegister(makinito.getMemory().getAddressRegister().getContent());
		memoryPanel.setMemoryRegister(makinito.getMemory().getMemoryRegister().getContent());
		memoryPanel.markInstruction(makinito.getCPU().getControlUnit().getProgramCounter().getDatum().getValue());
		
		// actualiza los buses
		dataBusPanel.setContent(makinito.getDataBus().getContent());
		addressBusPanel.setContent(makinito.getAddressBus().getContent());

		// actualiza unidad aritmético-lógica
		cpuPanel.getALUPanel().setAccumulator(makinito.getCPU().getALU().getAccumulator().getContent());
		cpuPanel.getALUPanel().setTemporaryRegister(makinito.getCPU().getALU().getTemporaryRegister().getContent());
		cpuPanel.getALUPanel().setFlagsRegister(makinito.getCPU().getALU().getFlagsRegister().toString().split("=")[1]);

		// actualiza unidad de control
		cpuPanel.getControlUnitPanel().setInstructionRegister(makinito.getCPU().getControlUnit().getInstructionRegister().getContent());
		cpuPanel.getControlUnitPanel().setProgramCounter(makinito.getCPU().getControlUnit().getProgramCounter().getContent());
		cpuPanel.getControlUnitPanel().setPhase(makinito.getCPU().getControlUnit().getSequencer().getPhase());
		cpuPanel.getControlUnitPanel().setSignals(makinito.getCPU().getControlUnit().getSequencer().getSignals());
		cpuPanel.getControlUnitPanel().markSignal(makinito.getCPU().getControlUnit().getSequencer().getCurrent());
		
		String status = makinito.getCPU().getControlUnit().getSequencer().getCurrentSignalDescription();
		infoLabel.setText(status);

	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(LAF_NIMBUS);
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

	protected void onMakinitoFinished() {
		stop();
		JOptionPane.showMessageDialog(this, 
				Messages.getString("makinitoFrame.infoDialog.makinitoFinished.message"), 
				Messages.getString("makinitoFrame.infoDialog.makinitoFinished.title"), 
				JOptionPane.INFORMATION_MESSAGE
			);
	}

}

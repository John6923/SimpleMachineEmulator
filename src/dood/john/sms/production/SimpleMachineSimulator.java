package dood.john.sms.production;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class SimpleMachineSimulator extends JFrame{
	
	JMenuBar menuBar;
	JMenu file;
	JMenuItem restart;
	JMenuItem step;
	JMenuItem play;
	JMenuItem pause;
	JMenuItem exit;
	JMenu edit;
	JMenuItem setFrequency;
	
	JTextField[] main_memory;
	JPanel registers;
	JLabel[] register_label;
	JTextField[] register;
	JLabel pchalt;
	
	Machine machine;
	int[] initialData;
	
	int frequency = 1;
	Timer timer;
	boolean paused = true;
	
	public SimpleMachineSimulator(int[] data){
		initialData = data;
		machine = new Machine(initialData);
		
		timer = new Timer(1000/frequency, new StepHandler());

		setTitle("Simple Machine Simulator");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.stop();
				super.windowClosing(e);
			}
		});
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		restart = new JMenuItem("Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				machine = new Machine(initialData);
				updateUI();
			}
		});
		step = new JMenuItem("Step");
		step.addActionListener(new StepHandler());
		play = new JMenuItem("Play");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timer.start();
				paused = false;
			}
		});
		pause = new JMenuItem("Pause");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timer.stop();
				paused = true;
			}
		});
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				setVisible(false);
			}
		});
		
		edit = new JMenu("Edit");
		setFrequency = new JMenuItem("Set Frequency");
		setFrequency.addActionListener(new FrequencyHandler());
		
		menuBar.add(file);
		menuBar.add(edit);
		
		file.add(restart);
		file.addSeparator();
		file.add(step);
		file.addSeparator();
		file.add(play);
		file.add(pause);
		file.addSeparator();
		file.add(exit);
		
		edit.add(setFrequency);
		
		setJMenuBar(menuBar);
		
		c.add(new JLabel("Main Memory:"));
		
		main_memory = new JTextField[256];
		for(int i = 0; i < 256; i+=16){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.add(new JLabel(String.format("0x%02X", i)));
			for(int j = i; j < i + 16; j++){
				main_memory[j] = new JTextField(String.format("0x%02X",initialData[j]),3);
				main_memory[j].setEditable(false);
				panel.add(main_memory[j]);
			}
			c.add(panel);
		}
		
		c.add(new JLabel("Registers:"));
		
		registers = new JPanel();
		registers.setLayout(new GridLayout(2,8));
		register_label = new JLabel[16];
		register = new JTextField[16];
		for(int i = 0; i < 16; i++){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			register_label[i] = new JLabel("0x" + Integer.toHexString(i));
			register[i] = new JTextField("0x00");
			register[i].setEditable(false);
			panel.add(register_label[i]);
			panel.add(register[i]);
			registers.add(panel);
		}
		c.add(registers);
		
		pchalt = new JLabel("PC: 0x00, Not Halted, Frequency: 1 Hz, Paused");
		c.add(pchalt);
		
		pack();
	}


	public class StepHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(!machine.halted()){
				machine.step();
				updateUI();
			}
			else{
				timer.stop();
				JOptionPane.showMessageDialog(SimpleMachineSimulator.this, "Execution has finished");
			}
		}
	}
	
	public class FrequencyHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try{
				frequency = Integer.parseInt(JOptionPane.showInputDialog(SimpleMachineSimulator.this, "Set a frequency (Hz): "));
			}
			catch(NumberFormatException ex)  {
				JOptionPane.showMessageDialog(SimpleMachineSimulator.this, "Could not parse input");
			}
			timer.setDelay(1000/frequency);
			updateUI();
		}
	}
	
	public void updateUI(){
		int[] state = machine.getState();
		int pc = state[256 + 16];
		boolean halted = machine.halted();
		for(int i = 0; i < 256; i++){
			main_memory[i].setText(String.format("0x%02X",state[i]));
		}
		for(int i = 256; i < 256 + 16; i++){
			register[i-256].setText(String.format("0x%02X",state[i]));
		}
		pchalt.setText(String.format("PC: 0x%02X, %s, Frequency: %d Hz, %s", pc, (halted ? "Halted" : "Not Halted"), frequency, (paused ? "Paused" : "Not Paused")));
	}
}

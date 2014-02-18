import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	JMenu edit;
	JMenuItem binary;
	JMenuItem decimal;
	JMenuItem hex;
	JMenuItem setFrequency;
	
	JPanel main_memorys;
	JLabel[] main_memory2;
	JLabel[] main_memory10;
	JLabel[] main_memory16;
	JTextField[] main_memory;
	JPanel registers;
	JLabel[] register2;
	JLabel[] register10;
	JLabel[] register16;
	JTextField[] register;
	JLabel pchalt;
	
	Machine machine;
	int[] initialData;
	
	int frequency = 1;
	Timer timer;
	
	public SimpleMachineSimulator(int[] data){
		initialData = data;
		
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		restart = new JMenuItem("Restart");
		step = new JMenuItem("Step");
		play = new JMenuItem("Play");
		pause = new JMenuItem("Pause");
		
		edit = new JMenu("Edit");
		binary = new JMenuItem("Binary");
		decimal = new JMenuItem("Decimal");
		hex = new JMenuItem("Hexadecimal");
		setFrequency = new JMenuItem("Set Frequency");
		
		menuBar.add(file);
		menuBar.add(edit);
		
		file.add(restart);
		file.addSeparator();
		file.add(step);
		file.addSeparator();
		file.add(play);
		file.add(pause);
		
		edit.add(binary);
		edit.add(decimal);
		edit.add(hex);
		edit.addSeparator();
		edit.add(setFrequency);
		
		main_memorys = new JPanel();
		main_memorys.setLayout(new GridLayout(16,16));
		main_memory2 = new JLabel[256];
		main_memory10 = new JLabel[256];
		main_memory16 = new JLabel[256];
		main_memory = new JTextField[256];
		for(int i = 0; i < 256; i++){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			main_memory2[i] = new JLabel(Integer.toBinaryString(i));
			main_memory10[i] = new JLabel(Integer.toString(i));
			main_memory16[i] = new JLabel(Integer.toHexString(i));
			main_memory[i] = new JTextField();
			main_memory[i].setEditable(false);
			panel.add(main_memory2[i]);
			panel.add(main_memory10[i]);
			panel.add(main_memory16[i]);
			panel.add(main_memory[i]);
			main_memorys.add(panel);
		}
		
		registers = new JPanel();
		registers.setLayout(new GridLayout(2,8));
		register2 = new JLabel[16];
		register10 = new JLabel[16];
		register16 = new JLabel[16];
		register = new JTextField[16];
		for(int i = 0; i < 16; i++){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			register2[i] = new JLabel(Integer.toBinaryString(i));
			register10[i] = new JLabel(Integer.toString(i));
			register16[i] = new JLabel(Integer.toHexString(i));
			register[i] = new JTextField();
			register[i].setEditable(false);
			panel.add(register2[i]);
			panel.add(register10[i]);
			panel.add(register16[i]);
			panel.add(register[i]);
			registers.add(panel);
		}
		
		setTitle("Simple Machine Simulator");
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		pack();
	}

}

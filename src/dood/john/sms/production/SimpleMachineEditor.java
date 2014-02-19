package dood.john.sms.production;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class SimpleMachineEditor extends JFrame{
	
	JMenuBar menuBar;
	JMenu fileMenu;
	
	JMenuItem startButton;
	JMenuItem exitButton;
	JMenuItem clear;
	
	JTextField[] main_memory;
	
	int[] data = new int[256];
	
	public SimpleMachineEditor(){
		setTitle("Simple Machine Emulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		startButton = new JMenuItem("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateData();
				new SimpleMachineSimulator(data).setVisible(true);
			}
		});
		exitButton = new JMenuItem("Exit");
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(-1);
			}	
		});
		clear = new JMenuItem("Clear (Irreversable)");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data = new int[256];
				updateEditor();
			}
		});
		
		menuBar.add(fileMenu);
		
		fileMenu.add(startButton);
		fileMenu.addSeparator();
		fileMenu.add(clear);
		fileMenu.addSeparator();
		fileMenu.add(exitButton);
		
		setJMenuBar(menuBar);
		
		main_memory = new JTextField[256];
		for(int i = 0; i < 256; i+=16){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
			panel.add(new JLabel(String.format("0x%02X",i)));
			for(int j = i; j < i + 16; j++){
				main_memory[j] = new JTextField(Integer.toBinaryString(0), 5);
				panel.add(main_memory[j]);
			}
			c.add(panel);
		}
		
		pack();
	}
	
	private void updateEditor(){
		for(int i = 0; i < 256; i++){
			main_memory[i].setText(Integer.toString(data[i], 16));
		}
	}
	
	private void updateData(){
		for(int i = 0; i < 256; i++){
			int val = 0;
			try{
				val = Integer.parseInt(main_memory[i].getText(),16);
			}
			catch(NumberFormatException e){
			}
			if(val < 0 || val >= 256) val = 0;
			data[i] = val;
		}
	}
}

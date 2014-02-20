package dood.john.sms;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class SimpleMachineEditor extends JFrame{
	
	JMenuBar menuBar;
	JMenu fileMenu;
	
	JMenuItem startButton;
	JMenuItem exitButton;
	JMenuItem clear;
	JMenuItem loadFromText;
	JMenuItem loadFromTextWord;
	JMenuItem exportAsText;
	JMenuItem exportAsTextWord;
	
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
		loadFromText = new JMenuItem("Load from text (Byte)");
		loadFromText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String string = readText("Enter program two hexadecimal digits at a time\neg. 12 34 56 78 9A BC DE F0");
				if(string == null) return;
				String[] ustrings = string.split(" ");
				ArrayList<String> strings = new ArrayList<String>();
				for(String s : ustrings){
					if(!s.equals(""))
						strings.add(s);
				}
				int i;
				for(i = 0; i < strings.size() && i < 256; i++){
					try{
						data[i] = Integer.parseInt(strings.get(i),16);
					}
					catch(NumberFormatException ex){
						data[i] = 0;
					}
				}
				for(; i < 256; i++){
					data[i] = 0;
				}
				updateEditor();
			}
		});
		loadFromTextWord = new JMenuItem("Load from text (Word)");
		loadFromTextWord.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String string = readText("Enter program four hexadecimal digits at a time\nFor proper parsing a trailing byte should be suffixed with 00\neg. 1234 5678 9ABC DE00");
				if(string == null) return;
				String[] ustrings = string.split(" ");
				ArrayList<String> strings = new ArrayList<String>();
				for(String s : ustrings){
					if(!s.equals(""))
						strings.add(s);
				}
				int i;
				for(i = 0; i < strings.size() && i < 128; i++){
					try{
						int j;
						data[2*i] = (j =Integer.parseInt(strings.get(i),16))/256;
						data[2*i+1] = j%256;
					}
					catch(NumberFormatException ex){
						data[2*i] = 0;
						data[2*i+1] = 0;
					}
				}
				for(; i < 128; i++){
					data[2*i] = data[2*i +1] = 0;
				}
				updateEditor();
			}
		});
		exportAsText = new JMenuItem("Export as text (Byte)"); 
		exportAsText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateData();
				String string = "";
				for(int i = 0; i < 256; i++){
					string += String.format("%02X ", data[i]);
				}
				JOptionPane.showInputDialog(SimpleMachineEditor.this, "The text representation of the main memory as hexadecimal bytes", "Export (Byte)",JOptionPane.INFORMATION_MESSAGE,null,null,string);
			}
		});
		exportAsTextWord = new JMenuItem("Export as text (Word)");
		exportAsTextWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateData();
				String string = "";
				for(int i = 0; i < 255; i+=2){
					string += String.format("%02X%02X ", data[i], data[i+1]);
				}
				JOptionPane.showInputDialog(SimpleMachineEditor.this, "The text representation of the main memory as hexadecimal words", "Export (Byte)",JOptionPane.INFORMATION_MESSAGE,null,null,string);
			}
		});
		
		menuBar.add(fileMenu);
		
		fileMenu.add(startButton);
		fileMenu.addSeparator();
		fileMenu.add(loadFromText);
		fileMenu.add(loadFromTextWord);
		fileMenu.add(exportAsText);
		fileMenu.add(exportAsTextWord);
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
			main_memory[i].setText(String.format("%X", data[i]));
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
	
	private String readText(String message){
		try {
			return JOptionPane.showInputDialog(message);
		}
		catch(NullPointerException e){
			return " ";
		}
	}
}

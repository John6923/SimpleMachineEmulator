package dood.john.sms.production;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class SimpleMachineEditor extends JFrame{
	private final int GRID_EDITOR = 1;
	private final int AREA_EDITOR = 2;
	
	private final int BINARY = 2;
	private final int DECIMAL = 10;
	private final int HEXADECIMAL = 16;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu editMenu;
	JMenu viewMenu;
	
	JMenuItem startButton;
	JMenuItem exitButton;
	JMenuItem clear;
	
	JMenuItem decimel;
	JMenuItem hexadecimal;
	JMenuItem binary;
	
	JMenuItem areaEditor;
	JMenuItem gridEditor;
	
	JPanel gridPanel;
	JTextField[] fieldArray = new JTextField[256];
	
	JTextArea inputArea;
	
	
	
	int currentEditor;
	int currentBase;
	
	int[] data = new int[256];
	
	public SimpleMachineEditor(){
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		viewMenu = new JMenu("View");
		
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
		
		decimel = new JMenuItem("Decimal Editing");
		decimel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sbDecimal();
			}
		});
		hexadecimal = new JMenuItem("Hexadecimal Editing");
		hexadecimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sbHexadecimal();
			}
		});
		binary = new JMenuItem("Binary Editing");
		binary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sbBinary();
			}
		});
		
		areaEditor = new JMenuItem("Area");
		areaEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				svArea();
			}
		});
		gridEditor = new JMenuItem("Grid");
		gridEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				svGrid();
			}
		});
		
		menuBar.add(fileMenu);
		//menuBar.add(editMenu);
		//menuBar.add(viewMenu);
		
		fileMenu.add(startButton);
		fileMenu.add(clear);
		fileMenu.add(exitButton);
		
		editMenu.add(binary);
		editMenu.add(decimel);
		editMenu.add(hexadecimal);
		
		//viewMenu.add(areaEditor);
		viewMenu.add(gridEditor);
		
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(16,16));
		for(int i = 0; i < 256; i++){
			gridPanel.add(fieldArray[i] = new JTextField(Integer.toBinaryString(0), 5));
		}
		inputArea = new JTextArea(10, 50);
		inputArea.setLineWrap(true);
		
		setTitle("Simple Machine Emulator");
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(gridPanel);
		c.add(inputArea);
		pack();
		svGrid();
		sbHexadecimal();
	}
	
	
	private void svArea(){
		updateData();
		if(currentBase == DECIMAL && currentEditor == AREA_EDITOR);
		currentEditor = AREA_EDITOR;
		updateEditor();
		decimel.setVisible(false);
		gridPanel.setVisible(false);
		inputArea.setVisible(true);
		pack();
	}
	
	private void svGrid(){
		updateData();
		currentEditor = GRID_EDITOR;
		updateEditor();
		decimel.setVisible(true);
		gridPanel.setVisible(true);
		inputArea.setVisible(false);
		pack();
	}
	
	private void sbBinary(){
		updateData();
		currentBase = BINARY;
		updateEditor();
	}
	
	private void sbDecimal(){
		updateData();
		currentBase = DECIMAL;
		updateEditor();
	}
	
	private void sbHexadecimal(){
		updateData();
		currentBase = HEXADECIMAL;
		updateEditor();
	}
	
	private void updateEditor(){
		if(currentEditor == GRID_EDITOR){
			for(int i = 0; i < 256; i++){
				fieldArray[i].setText(Integer.toString(data[i], currentBase));
			}
		}
		else if(currentEditor == AREA_EDITOR){
			String string = "";
			for(int i = 0; i < 256; i++){
				string += String.format((currentBase == BINARY ? "%08b" : "%02x"), data[i]);
			}
			inputArea.setText(string);
		}
	}
	
	private void updateData(){
		String errors = "";
		if(currentEditor == GRID_EDITOR){
			for(int i = 0; i < 256; i++){
				int val = 0;
				try{
					val = Integer.parseInt(fieldArray[i].getText(),currentBase);
				}
				catch(NumberFormatException e){
					errors+=" " + i;
				}
				if(val < 0 || val >= 256) val = 0;
				data[i] = val;
			}
		}
		else if(currentEditor == AREA_EDITOR){
			String string = inputArea.getText();
			if(currentBase == BINARY){
				for(int i = 0; i/8 < 256; i+=8){
					int val = 0;
					try{
						val = Integer.parseInt(string.substring(i, i + 8),currentBase);
					}
					catch(NumberFormatException e){
						errors+=" " + i/8;
					}
					catch(StringIndexOutOfBoundsException e){
						errors += " " + i/8 + " +";
						break;
					}
					finally{
						if(val < 0 || val >= 256) val = 0;
						data[i/8] = val;
					}
				}
			}
			else if(currentBase == HEXADECIMAL){
				for(int i = 0; i/2 < 256; i+=2){
					int val = 0;
					try{
						val = Integer.parseInt(string.substring(i, i + 2),currentBase);
					}
					catch(NumberFormatException e){
						errors += " " + i/2;
					}
					catch(StringIndexOutOfBoundsException e){
						errors += " " + i/2 + " +";
						break;
					}
					finally{
						if(val < 0 || val >= 256) val = 0;
						data[i/2] = val;
					}
				}
			}
		}
		if(!errors.equals("")){
			if(errors.length() > 100){
				String nerrors = "";
				for(int i = 0; i < errors.length(); i += 50){
					nerrors+=errors.substring(i,(i+50 < errors.length() ? i + 50 : errors.length())) + "\n";
				}
				errors = nerrors;
			}
			//JOptionPane.showMessageDialog(this, "Formating error in byte(s)\n " + errors);
		}
	}
}

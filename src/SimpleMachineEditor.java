import java.awt.Container;
import java.awt.GridLayout;

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
		exitButton = new JMenuItem("Exit");
		
		decimel = new JMenuItem("Decimal Editing");
		hexadecimal = new JMenuItem("Hexadecimal Editing");
		binary = new JMenuItem("Binary Editing");
		
		areaEditor = new JMenuItem("Area");
		
		gridEditor = new JMenuItem("Grid");
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		
		fileMenu.add(startButton);
		fileMenu.add(exitButton);
		
		editMenu.add(binary);
		editMenu.add(decimel);
		editMenu.add(hexadecimal);
		
		viewMenu.add(areaEditor);
		viewMenu.add(gridEditor);
		
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(16,16));
		for(int i = 0; i < 256; i++){
			gridPanel.add(fieldArray[i] = new JTextField(5));
		}
		inputArea = new JTextArea(20, 5);
		
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(gridPanel);
		c.add(inputArea);
		pack();
	}
	
	
	private void svArea(){
		updateData();
		currentEditor = AREA_EDITOR;
		updateEditor();
		decimel.setVisible(false);
		gridPanel.setVisible(false);
		inputArea.setVisible(true);
		pack();
	}
	
	private void svGrid(){
		
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
		if(currentEditor == GRID_EDITOR){
			for(int i = 0; i < 256; i++){
				int val = 0;
				try{
					val = Integer.parseInt(fieldArray[i].getText(),currentBase);
				}
				catch(NumberFormatException e){
					JOptionPane.showMessageDialog(this, "Formating error in byte " + i);
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
						JOptionPane.showMessageDialog(this, "Formating error in byte " + i/8);
					}
					if(val < 0 || val >= 256) val = 0;
					data[i/8] = val;
				}
			}
			else if(currentBase == HEXADECIMAL){
				for(int i = 0; i/2 < 256; i+=2){
					int val = 0;
					try{
						val = Integer.parseInt(string.substring(i, i + 2),currentBase);
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(this, "Formating error in byte " + i/2);
					}
					if(val < 0 || val >= 256) val = 0;
					data[i/8] = val;
				}
			}
		}
	}
}

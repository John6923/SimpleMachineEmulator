package dood.john.sms.test;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFrame;

import dood.john.sms.production.SimpleMachineSimulator;


public class SimpleMachineSimulatorLauncher {
	public static void main(String[] args){
		InputStream is = SimpleMachineSimulatorLauncher.class.getResourceAsStream("./hex.hex");
		Scanner scanner = new Scanner(is);
		int[] data = new int[256];
		int dp = 0;
		while(dp < 256 && scanner.hasNext()){
			data[dp++] = Integer.parseInt(scanner.nextLine(), 16);
		}
		scanner.close();
		SimpleMachineSimulator sms = new SimpleMachineSimulator(data);
		sms.setVisible(true);
		sms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}

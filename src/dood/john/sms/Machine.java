package dood.john.sms;

/*
 * Example Program:
11 2F 12 2E 23 00 20 00 24 FF 53 31 52 24 B2 12 
B0 0A 33 2D C0 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 02 
 * 
 * Multiplies the numbers in 0x2E (3) and 0x2F (2) and stores the result in 0x2D
 */

public class Machine {
	private int[] main_memory;
	private int pc;
	private int[] register;
	boolean halted;
	
	public Machine(int[] main_memory){
		this.main_memory = main_memory;
		pc = 0;
		register = new int[16];
		halted = false;
	}
	
	public void step(){
		if(halted) return;
		int opcode = main_memory[pc]/16;
		int operand1 = main_memory[pc]%16, operand2 = main_memory[(pc+1)%256]/16, operand3 = main_memory[(pc+1)%256]%16;
		pc+=2;
		pc%=256;
		switch(opcode){
		case 1:
			register[operand1] = main_memory[(operand2 << 4) + operand3];
			break;
		case 2:
			register[operand1] = (operand2 << 4) + operand3;
			break;
		case 3:
			main_memory[(operand2 << 4) + operand3] = register[operand1];
			break;
		case 4:
			register[operand3] = register[operand2];
			break;
		case 5:
			register[operand1] = add2(register[operand2],register[operand3]);
			break;
		case 6:
			register[operand1] = addf(register[operand2],register[operand3]);
			break;
		case 7:
			register[operand1] = register[operand2] | register[operand3];
			break;
		case 8:
			register[operand1] = register[operand2] & register[operand3];
			break;
		case 9:
			register[operand1] = register[operand2] ^ register[operand3];
			break;
		case 0xA:
			register[operand1] = register[operand1] >> operand3;
			break;
		case 0xB:
			if(register[operand1] == register[0]){
				pc = (operand2 << 4) + operand3;
			}
			break;
		case 0xC:
			halted = true;
			break;
		}
		if(halted){
			pc--;
			pc%=256;
		}
	}
	
	private int add2(int a, int b){
		int ret = a + b;
		if(ret > 255)
			ret -= 256;
		return ret;
	}
	
	private int addf(int a, int b){
		int numa = ((a>>7) == 1 ? -1 : 1) * (a % 16 << a /16 % 8);
		int numb= ((b>>7) == 1 ? -1 : 1) * (b % 16 << (b /16 % 8));
		int c = numa + numb;
		int i = 0;
		while((c / (2 << i)) >= (1 << 4))
			i++;
		return ((c < 0 ? 1 : 0) << 7) + (i << 4) + (c >> i);
	}
	
	public int[] getState(){
		int[] ret = new int[256 + 16 + 1];
		for(int i = 0; i < 256; i++){
			ret[i] = main_memory[i];
		}
		for(int i = 0; i < 16; i++){
			ret[i+256] = register[i];
		}
		ret[256 + 16] = pc;
		return ret;
	}
	
	public String toString(){
		String ret = "";
		int[] state = getState();
		ret += state[0];
		for(int i = 1; i < (256 + 16 + 1); i++){
			ret += " " + state[i];
		}
		return ret;
	}
	
	public boolean halted(){
		return halted;
	}

}

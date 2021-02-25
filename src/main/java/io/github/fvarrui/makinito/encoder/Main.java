package io.github.fvarrui.makinito.encoder;
import java.io.File;
import java.io.IOException;

import io.github.fvarrui.makinito.hardware.Makinito;
import io.github.fvarrui.makinito.software.Instruction;
import io.github.fvarrui.makinito.software.Program;
import io.github.fvarrui.makinito.software.parser.ParserException;

public class Main {

	public static void main(String[] args) throws IOException {
		try {
			
			Makinito makinito = new Makinito();
			Program programa = makinito.loadProgram(new File("codificar.maki"));
			for (Instruction i : programa.getInstructions()) {
				EncodedInstruction ei = new EncodedInstruction().fromInstruction(i);
				System.out.println("Encoded instruction: " + ei + " <---> Decoded instruction: " + i);	
				System.out.println("Control signals: " + makinito.getCPU().getControlUnit().getDecoder().decode(i));
				System.out.println();
			}
			
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}

}

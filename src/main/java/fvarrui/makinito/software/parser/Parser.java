package fvarrui.makinito.software.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fvarrui.makinito.hardware.Makinito;
import fvarrui.makinito.software.AddressingMode;
import fvarrui.makinito.software.Datum;
import fvarrui.makinito.software.Instruction;
import fvarrui.makinito.software.Operand;
import fvarrui.makinito.software.Program;

public class Parser {
	
	private static final boolean LOG_ENABLED = false;
	
	private static final String DECIMAL 				= "-?\\d+";
	private static final String BINARY 					= "[0-1]+b";
	private static final String HEXADECIMAL 			= "[0-9a-fA-F]+h";
	private static final String SPACE 					= "\\h*";
	private static final String COMMENT 				= SPACE + ";.*";
	private static final String OPT_COMMENT 			= SPACE + ";?.*";
	private static final String ID 						= "\\w+";
	private static final String VALUE 					= DECIMAL + "|" + BINARY + "|" + HEXADECIMAL;
	
	private static final String DIRECT_WITH_VALUE 		= "\\[(" + VALUE + ")\\]";
	private static final String DIRECT_WITH_ID 			= "\\[" + ID + "\\]";
	private static final String INDIRECT_WITH_VALUE 	= "\\[\\[(" + VALUE + ")\\]\\]";
	private static final String INDIRECT_WITH_ID 		= "\\[\\[" + ID + "\\]\\]";

	private static final String DATUM 					= "(" + VALUE + "|" + ID + ")";
	private static final String IMMEDIATE				= DATUM;
	private static final String DIRECT					= "\\[" + DATUM + "\\]";
	private static final String INDIRECT				= "\\[" + DIRECT + "\\]";
	private static final String OPERAND					= "(" + IMMEDIATE + "|" + DIRECT + "|" + INDIRECT + ")";

	private static final String INSTRUCTION				= "^(" + ID + ":)?" + SPACE + ID + SPACE + OPERAND + "?" + "(," + OPERAND + ")*" + OPT_COMMENT;
	private static final String ASSIGNMENT 				= SPACE + ID + SPACE + "=" + SPACE + "(" + VALUE + "|\\?)" + OPT_COMMENT;
	
	private static final String BEGIN_CODE 				= "^BEGIN-CODE" + OPT_COMMENT;
	private static final String END_CODE 				= "^END-CODE" + OPT_COMMENT;
	private static final String BEGIN_DATA 				= "^BEGIN-DATA" + OPT_COMMENT;
	private static final String END_DATA 				= "^END-DATA" + OPT_COMMENT;
	
	private static final int OUT = -1;
	private static final int DATA_IN = 0;
	private static final int DATA_OUT = 1;
	private static final int CODE_IN = 2;
	private static final int CODE_OUT = 3;
	
	private Makinito makinito;
	
	private int lineNumber;
	private int memoryAddress;
	private int status;
	private Program program = null;
	
	public Parser(Makinito makinito) {
		this.makinito = makinito;
	}
	
	private List<Operand> parseOperands(String line, Map<String, Integer> labels) throws ParserException {
		List<Operand> operands = new ArrayList<Operand>();
		if (line != null && !line.matches(COMMENT)) {
			String [] aOperands = line.split(",");
			for (String operand : aOperands) {
				Operand op = new Operand();
				
				// registro
				if (operand.matches(ID) && isRegister(operand)) {				
					op.setAddressingMode(AddressingMode.REGISTER);
					op.setEffectiveAddressField(new Datum(makinito.encodeRegister(operand)));
					op.setRegisterName(operand);
				} 
				// número decimal
				else if (operand.matches(DECIMAL)) {			
					op.setAddressingMode(AddressingMode.IMMEDIATE);
					op.setEffectiveAddressField(new Datum(Integer.parseInt(operand)));
				} 
				// número binario
				else if (operand.matches(BINARY)) {			
					op.setAddressingMode(AddressingMode.IMMEDIATE);
					op.setEffectiveAddressField(new Datum(ParseUtils.binaryToInteger(operand.substring(0, operand.length() - 1))));
				} 
				// número hexadecimal
				else if (operand.matches(HEXADECIMAL)) {		
					op.setAddressingMode(AddressingMode.IMMEDIATE);
					op.setEffectiveAddressField(new Datum(ParseUtils.hexToInteger(operand.substring(0, operand.length() - 1))));
				} 
				// variable
				else if (operand.matches(ID)) {		
					op.setAddressingMode(AddressingMode.IMMEDIATE);
					if (labels.get(operand) == null) {
						throw new ParserException(lineNumber, "Operando no válido: el identificador '" + operand + "' no existe.");
					}
					op.setEffectiveAddressField(new Datum(labels.get(operand)));
				} 
				// dirección de memoria
				else if (operand.matches(DIRECT_WITH_VALUE)) {				
					operand = operand.substring(1, operand.length() - 1);
					op.setAddressingMode(AddressingMode.DIRECT);
					op.setEffectiveAddressField(new Datum(Integer.parseInt(operand)));
				}
				// dirección de memoria
				else if (operand.matches(DIRECT_WITH_ID)) {		
					operand = operand.substring(1, operand.length() - 1);
					if (isRegister(operand)) {
						throw new ParserException(lineNumber, "Operando no válido: modo de direccionamiento no válido. No se puede usar un registro en el modo de direccionamiento directo '[" + operand + "]'.");
					}
					op.setAddressingMode(AddressingMode.DIRECT);
					if (labels.get(operand) == null) {
						throw new ParserException(lineNumber, "Operando no válido: el identificador '" + operand + "' no existe.");
					}
					op.setEffectiveAddressField(new Datum(labels.get(operand)));
				}
				// direcci�n de memoria de la dirección de memoria
				else if (operand.matches(INDIRECT_WITH_VALUE)) {	
					operand = operand.substring(2, operand.length() - 2);
					op.setAddressingMode(AddressingMode.INDIRECT);
					op.setEffectiveAddressField(new Datum(Integer.parseInt(operand)));
				}
				// direcci�n de memoria de la dirección de memoria
				else if (operand.matches(INDIRECT_WITH_ID)) {	
					operand = operand.substring(2, operand.length() - 2);
					if (isRegister(operand)) {
						throw new ParserException(lineNumber, "Operando no válido: modo de direccionamiento no válido. No se puede usar un registro en el modo de direccionamiento directo '[" + operand + "]'.");
					}
					op.setAddressingMode(AddressingMode.INDIRECT);
					if (labels.get(operand) == null) {
						throw new ParserException(lineNumber, "Operando no válido; el identificador '" + operand + "' no existe.");
					}
					op.setEffectiveAddressField(new Datum(labels.get(operand)));
				}
				// desconocido
				else {
					throw new ParserException(lineNumber, "Operando no válido en el bloque de datos: " + operand);
				}
				operands.add(op);
			}
		}

		return operands;
	}

	private boolean isRegister(String id) {
		return makinito.getRegisters().get(id) != null;
	}

	private Instruction parseInstruction(String line, Map<String, Integer> labels, boolean first) throws ParserException {
		line = line.trim().toUpperCase();
		
		line = line.replaceAll("[!;]*\\h*,\\h*", ",");	// quita cualquier espacio antes o después de las comas fuera de los comentarios 

		String parts[] = line.split("\\h+");

		String mnemonic;
		String operands;

		// hay etiqueta
		if (line.matches("^" + ID + ":" + SPACE + "$")) {
			String etiqueta = parts[0];
			throw new ParserException(lineNumber, "Se encontró la etiqueta '" + etiqueta + "' suelta, sin instrucción.");
		} else if (line.matches("^" + ID + ":.*")) {
			mnemonic = parts[1];
			operands = parts.length > 2 ? parts[2] : null;
			parseLabel(line, labels, memoryAddress, first);
		} else {
			mnemonic = parts[0];
			operands = parts.length > 1 ? parts[1] : null;
		}
		
		Instruction instruction = null;
		if (!first) {
			instruction = new Instruction();
			instruction.setOpCode(mnemonic);
			instruction.getOperands().addAll(parseOperands(operands, labels));
		}
		
		return instruction;		
	}
	
	private Datum parseDatum(String line) {
		line = line.trim();
		String parts[] = line.split(SPACE + "=" + SPACE);
		String value = parts[1].split("\\h+")[0];
		return new Datum("?".equals(value) ? 0 : Integer.parseInt(value));
	}
	
	private void parseIdentifier(String line, Map<String, Integer> labels, Integer memoryAddress, boolean first) {
		if (first) {
			line = line.trim();
			String parts[] = line.split(SPACE + "=" + SPACE);
			labels.put(parts[0].toUpperCase(), memoryAddress);
		}
	}
	
	private void parseLabel(String line, Map<String, Integer> labels, Integer memoryAddress, boolean first) {
		if (first) {
			line = line.trim();
			String parts[] = line.split(":");
			labels.put(parts[0].toUpperCase(), memoryAddress);
		}
	}

	public Program parse(File file) throws IOException, ParserException {
		return parse(file, null);
	}
	
	private Program parse(File file, Map<String, Integer> labels) throws IOException, ParserException {
		boolean first = (labels != null);

		program = null;

		if (first) {
			log("[PASADA1]");
		} else {
			labels = new HashMap<String, Integer>();
			parse(file, labels);
			program = new Program();
			program.setName(file.getName());
			log("[PASADA2]");
		}
		
		String line = null;
		lineNumber = 1;
		memoryAddress = 0;
		status = OUT;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try { 
			while ((line = reader.readLine()) != null) {
				
				line = line.toUpperCase();
				
				// ignora l�neas vac�as y comentarios
				if (line.trim().isEmpty() || line.matches(COMMENT)) {
					// lo ignoramos
				}
				// entra en el bloque de codigo 
				else if (line.matches(BEGIN_CODE) && (status == OUT || status == DATA_OUT)) {
					log("Entrando en bloque de CODIGO");
					status = CODE_IN;
				} 
				// sale del bloque de c�digo
				else if (line.matches(END_CODE) && status == CODE_IN) {
					log("Saliendo de bloque de CODIGO");
					status = CODE_OUT;
				}
				// encuentra END_CODE sin BEGIN_CODE
				else if (line.matches(END_CODE) && status != CODE_IN) {
					throw new ParserException(lineNumber, "Se ha encontrado final de bloque de código (END-CODE) sin abrir previamente (BEGIN-CODE)");
				}
				// entra en el bloque de datos 
				else if (line.matches(BEGIN_DATA) && (status == OUT || status == CODE_OUT)) {
					log("Entrando de bloque de DATOS");
					status = DATA_IN;
				} 
				// sale del bloque de datos
				else if (line.matches(END_DATA) && status == DATA_IN) {
					log("Saliendo de bloque de DATOS");
					status = DATA_OUT;
				} 
				// encuentra END_DATA sin BEGIN_DATA
				else if (line.matches(END_DATA) && status != DATA_IN) {
					throw new ParserException(lineNumber, "Se ha encontrado final de bloque de datos (END-DATA) sin abrir previamente (BEGIN-DATA)");
				}
				// encuentra dato
				else if (status == DATA_IN) {

					if (line.matches(ASSIGNMENT)) {

						if (first)
							parseIdentifier(line, labels, memoryAddress, first);
						else {
							
							try {
								program.getData().add(parseDatum(line));
							} catch (NumberFormatException e) {
								throw new ParserException(lineNumber, "Error en el bloque de datos:\n\n" + line);
							}
								
						}
						
						memoryAddress++;
						
					} else {
						throw new ParserException(lineNumber, "Se ha encontrado algo raro en el bloque de datos:\n\n" + line);
					}
					
				}
				// encuentra instruccion (s�lo procesa las instrucciones en la segunda pasada)
				else if (status == CODE_IN) {
					
					if (line.matches(INSTRUCTION)) {

						Instruction instruction = parseInstruction(line, labels, first);
						if (!first) {
							if (!makinito.getCPU().getControlUnit().getDecoder().isValidInstruction(instruction)) {
								throw new ParserException(lineNumber, "Instrucción no válida: " + instruction 
										+ "\n\nRevisa el nombre de la instrucción y si el número de operandos y/o los modos de direccionamiento"
										+ " son válidos para esta instrucción.");
							}
							program.getInstructions().add(instruction);
						}
						memoryAddress++;
						
					} else {
						throw new ParserException(lineNumber, "Se ha encontrado algo raro en el bloque de código:\n\n" + line);
					}
					
				} else {
					throw new ParserException(lineNumber, "Se ha encontrado algo raro por fuera de los bloques de datos y de código:\n\n" + line);
				}
	
				lineNumber++;
			}
			
			if (status == CODE_IN) {
				throw new ParserException(lineNumber, "No se ha cerrado el bloque de código (falta " + END_CODE + ")");
			}
			else if (status == DATA_IN) {
				throw new ParserException(lineNumber, "No se ha cerrado el bloque de datos (falta " + END_DATA + ")");
			}
			
			// a�ade al final del programa la instrucci�n TERMINAR en la segunda pasada
			if (!first && (program.getInstructions().isEmpty() || !(program.getInstructions().get(program.getInstructions().size() - 1)).getOpCode().equals("TERMINAR"))) {
				program.getInstructions().add(new Instruction("TERMINAR"));
			}
			
		} finally {
			reader.close();
		}
		
		return program;
	}
	
	public void log(String mensaje) {
		if (LOG_ENABLED)
			System.out.println(mensaje);
	}
	
}

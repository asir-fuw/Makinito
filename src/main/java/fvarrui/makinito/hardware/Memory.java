package fvarrui.makinito.hardware;

import fvarrui.makinito.software.Datum;
import fvarrui.makinito.software.Info;

/**
 * Memoria principal (RAM) de la máquina. Contiene datos e instrucciones del programa
 * en ejecución.
 * 
 * @author Francisco Vargas
 */
public class Memory extends FunctionalUnit {
	private Info[] content;
	private Register memoryRegister; 	// registro de memoria
	private Register addressRegister; 	// registro de direcciones

	public Memory(int capacity) {
		setName("Memoria");
		content = new Info[capacity];
		memoryRegister = new Register("RM");
		addressRegister = new Register("RD");
		getComponents().add(memoryRegister);
		getComponents().add(addressRegister);
	}

	public Info[] getContent() {
		return content;
	}

	public void setInfo(int address, Info info) {
		content[address] = info;
	}

	public void setInfo(int base, Info[] info) {
		if (base + info.length > content.length) {
			throw new MakinitoException("Capacidad de memoria insuficiente");
		}
		for (int i = 0; i < info.length; i++) {
			setInfo(i + base, info[i]);
		}
	}

	public void read() {
		Datum address = (Datum) addressRegister.getContent();
		if (address.getValue() < 0 || address.getValue() >= content.length) {
			throw new MakinitoException("Intento de acceso para lectura fuera del rango de memoria.\n- Dirección: " + address.getValue() + "\n- Rango de direcciones válido: 0-" + (content.length - 1));
		}
		if (content[address.getValue()] == null) {
			throw new MakinitoException("Intento de acceso para lectura en celda de memoria no asignada al programa.\n- Dirección: " + address.getValue());
		}
		memoryRegister.setContent(content[address.getValue()]);
	}

	public void write() {
		Datum address = (Datum) addressRegister.getContent();
		if (address.getValue() < 0 || address.getValue() >= content.length) {
			throw new MakinitoException("Intento de acceso para escritura fuera del rango de memoria.\n- Dirección: " + address.getValue() + "\n- Rango de direcciones válido: 0-" + (content.length - 1));
		}
		if (content[address.getValue()] == null) {
			throw new MakinitoException("Intento de acceso para escritura en celda de memoria no asignada al programa.\n- Dirección: " + address.getValue());
		}
		content[address.getValue()] = memoryRegister.getContent();
	}

	public Register getMemoryRegister() {
		return memoryRegister;
	}

	public Register getAddressRegister() {
		return addressRegister;
	}

	@Override
	public String toString(String tabs) {
		int direccion = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append(tabs + getName() + " {\n");
		buffer.append(tabs + "\t" + "capacidad=" + content.length + "\n");
		buffer.append(tabs + "\t" + getAddressRegister() + "\n");
		buffer.append(tabs + "\t" + getMemoryRegister() + "\n");
		buffer.append(tabs + "\t" + "contenido {\n");
		for (Info info : content) {
			buffer.append(tabs + "\t\t" + "[" + String.format("%3d", direccion) + "] ");
			buffer.append(info == null ? "" : info);
			buffer.append("\n");
			direccion++;
		}
		buffer.append(tabs + "\t" + "}\n");
		buffer.append(tabs + "}");
		return buffer.toString();
	}

	public void clear() {
		for (int i = 0; i < content.length; i++) {
			content[i] = null;
		}
	}

	@Override
	public void reset() {
		memoryRegister.reset();
		addressRegister.reset();
	}

}

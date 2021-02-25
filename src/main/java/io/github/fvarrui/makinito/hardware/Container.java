package io.github.fvarrui.makinito.hardware;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente capaz de contener otros componentes. Las unidades funcionales, como la unidad 
 * de control (Control Unit) son de tipo Container. 
 *  
 * @author fvarrui
 */
public abstract class Container extends Component {
	private List<Component> components = new ArrayList<Component>();

	public final List<Component> getComponents() {
		return components;
	}
	
	public String toString(String tabs) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(tabs + getName() + " {\n");
		for (Component component : components) {
			if (component instanceof Container)
				buffer.append(((Container) component).toString(tabs + "\t") + "\n");
			else 
				buffer.append(component.toString(tabs + "\t") + "\n");
		}
		buffer.append(tabs + "}");
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return toString("");
	}

}

package io.github.fvarrui.makinito.hardware;

import io.github.fvarrui.makinito.software.Info;

/**
 * Cable o vía que interconecta dos componentes.
 * 
 * @author fvarrui
 */
public class Wire extends Component {

	private Register source;
	private Register target;

	public Wire(String name, Register source, Register target) {
		super();
		setName(name);
		this.source = source;
		this.target = target;
	}

	public Register getSource() {
		return source;
	}

	public void setSource(Register source) {
		this.source = source;
	}

	public Register getTarget() {
		return target;
	}

	public void setTarget(Register target) {
		this.target = target;
	}
	
	public void activate() {
		Info info = source.getContent();
		target.setContent(info);
	}

	@Override
	public void reset() {
	}

}

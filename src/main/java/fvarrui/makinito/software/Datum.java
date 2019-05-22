package fvarrui.makinito.software;

public class Datum extends Info {
	private Integer value;
	
	public Datum() {
		this(0);
	}
	
	public Datum(Integer value) {
		super();
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "" + value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Datum)) return false;
		Datum datum = (Datum) obj;
		return (datum.getValue() == getValue());
	}

}

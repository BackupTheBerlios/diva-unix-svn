package uk.ulancs.diva.metamodel;

public class Property {
	
	private String property,id;
	private int value;
	
	public Property(String property, int value,String id) {
		super();
		this.property = property;
		this.value = value;
		this.id= id;
	}
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getId() {
		return id.toUpperCase();
	}

	public void setId(String id) {
		this.id = id;
	}
}

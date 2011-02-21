package uk.ulancs.diva.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Constraint {
	
	public static final int ENUM=0;
	public static final int BOOL=1;
	public static final int RULE=2;
	public static final int DEPENDENCY=3;
	
	
	private String name,id,raw;
	private List<String> values;
	private int type;
	
	public Constraint(String name, String id,String raw) {
		super();
		values= new ArrayList<String>();
		this.name = name;
		this.id = id;
		this.raw = raw;
		this.type=-1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id.toUpperCase();
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addValue(String value){
		values.add(value);
	}
	
	public List<String> getValues(){
		return values;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

}

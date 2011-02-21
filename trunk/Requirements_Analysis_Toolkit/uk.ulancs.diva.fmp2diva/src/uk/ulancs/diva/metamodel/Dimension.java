package uk.ulancs.diva.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Dimension {
	
	private String name,id,type;
	
	
	public Dimension(String n,String id,String type){
		setName(n);
		setId(id);
		setType(type);
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id.toUpperCase();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

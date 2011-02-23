package uk.ulancs.diva.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Variant {
	
	private String name,dimension,id,type;
	private List<String> dependencies;
	
	public Variant(String n, String d,String id,String type){
		name=n;
		dimension=d;
		this.id= id;
		this.type=type;
		this.dependencies= new ArrayList<String>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getDimension() {
		return dimension;
	}

	public String getId() {
		return id.toUpperCase();
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addDependency(String dependency){
		dependencies.add(dependency);
	}
	
	public List<String> getDependencies(){
		return dependencies;
	}
	
	public String getType(){
		return this.type;
	}

}

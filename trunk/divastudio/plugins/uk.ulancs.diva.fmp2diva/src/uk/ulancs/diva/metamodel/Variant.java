package uk.ulancs.diva.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Variant {
	
	private String name,dimension,id,type;
	private List<String> dependencies;
	private int upper,lower;
	
	public Variant(String n, String d,String id,String type,int upper, int lower){
		name=n;
		dimension=d;
		this.id= id;
		this.type=type;
		this.dependencies= new ArrayList<String>();
		this.upper= upper;
		this.lower= lower;
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

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

}

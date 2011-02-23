package uk.ulancs.diva.DiVAModelCompare.Compare;

import diva.NamedElement;

public class Difference {
	
	public static final String DELETION="Deletion";
	public static final String ADDITION="Addition";
	public static final String CHANGE="Change";
	
	private NamedElement before,after;
	private String type;

	public Difference(NamedElement before, NamedElement after,String type) {
		super();
		this.before = before;
		this.after = after;
		this.type= type;
	}

	public NamedElement getBefore() {
		return before;
	}

	public void setBefore(NamedElement before) {
		this.before = before;
	}

	public NamedElement getAfter() {
		return after;
	}

	public void setAfter(NamedElement after) {
		this.after = after;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

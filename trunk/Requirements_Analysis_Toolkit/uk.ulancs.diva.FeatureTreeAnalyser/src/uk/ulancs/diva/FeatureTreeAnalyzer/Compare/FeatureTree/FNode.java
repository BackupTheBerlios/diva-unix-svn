package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree;

public class FNode {
	private String ID;
	private String Name;
	private String Description;
	private int Depth;
	
	private Boolean IsOptional;
	private Boolean IsOr;
	private Boolean IsXor;
	
	public FNode(String MyID, String MyName, String MyDescript, int MyDepth, Boolean Optional, Boolean Or, Boolean Xor) {
		ID = MyID;
		Name = MyName;
		Description = MyDescript;
		Depth = MyDepth;
		
		IsOptional = Optional;
		IsOr= Or;
		IsXor = Xor;
	}
	
	public String GetID() {
		return ID;
	}
	
	public String GetName() {
		return Name;
	}
	
	public String GetDescription() {
		return Description;
	}
	
	public int GetDepth() {
		return Depth;
	}
	
	public Boolean GetIsOptional() {
		return IsOptional;
	}
	
	public Boolean GetIsMandatory() {
		return !IsOptional;
	}
	
	public Boolean GetIsOr() {
		return IsOr;
	}
	
	public Boolean GetIsXor() {
		return IsXor;
	}
}

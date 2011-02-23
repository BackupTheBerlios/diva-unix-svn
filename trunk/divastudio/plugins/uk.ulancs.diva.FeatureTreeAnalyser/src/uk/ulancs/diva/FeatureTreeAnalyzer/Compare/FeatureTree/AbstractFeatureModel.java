package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree;

import java.util.Vector;

public class AbstractFeatureModel {

    public Vector<FNode> GetAllNodes() {
    	return null;
    }

    public FNode GetParent(FNode CurrentNode) {
    	return null;
    }

    public Boolean IsRoot(FNode CurrentNode) {
    	return null;
    }

  public Boolean NodeIsOptional(FNode TreeNode) {
	  return null;
  }

  	public Boolean NodeIsMandatory(FNode TreeNode) {
  		return null;
  	}

  	public Boolean NodeIsXor(FNode TreeNode) {
  		return null;
  	}

  	public Boolean NodeIsOr(FNode TreeNode) {
  		return null;
  	}
  	
  	public FNode GetRoot() {
  		return null;
  	}
  	
  	public Boolean IsInThisTree(FNode Node) {
		return false;
	}
  	
  	public String GetName() {
  		return "";
  	}
  	
  	public Vector<FNode> GetChildren(FNode Feature) {
  		return null;
  	}
  	
  	public int DistanceToRoot(FNode Feature) {
  		return 0;
  	}
  	
  	public int DistanceToCommonAncestor(FNode Feature1, FNode Feature2) {
  		return 1;
  	}
  	
  	public boolean IsAncestorOf(FNode Feature1, FNode Feature2) {
  		return false;
  	}
}
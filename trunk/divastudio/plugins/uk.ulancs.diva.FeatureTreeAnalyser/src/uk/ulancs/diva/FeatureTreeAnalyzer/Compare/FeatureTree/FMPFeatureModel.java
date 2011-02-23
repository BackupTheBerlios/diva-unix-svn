package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import org.eclipse.core.resources.IFile;

public class FMPFeatureModel extends AbstractFeatureModel {
	String FileHandle = "";
	HashMap<String,FNode> Features;
	
	public FMPFeatureModel(String Handle) {
		FileHandle = Handle;
		
		Features = new HashMap<String,FNode>();
		try {
			//ImportFmpFeatures Import = new ImportFmpFeatures(FileHandle);
			//Iterator<FmpElement> FeatureIt = Import.getFeatureModelElements().iterator();
			//while (FeatureIt.hasNext()) {
			//	FmpElement Elt = FeatureIt.next();
			//	String Id = Elt.getId();
			//	Features.put(Id,Elt);
			//}
		}
		catch (Exception e) {
			
		}
	}
	
	public Vector<FNode> GetAllNodes() {
		Vector<FNode> ReturnVector = new Vector<FNode>();
		//Iterator<FmpElement> FmpEltIt = Features.values().iterator();
		
		//while (FmpEltIt.hasNext()) {
		//	FmpElement CurrentElt = FmpEltIt.next();
		//	ReturnVector.add(new Node(CurrentElt.getId(),CurrentElt.getProperties().getName()));
		//}
    	return ReturnVector;
    }
	
	public FNode GetParent(FNode CurrentNode) {
		//if (!IsRoot(CurrentNode)) {
		//		FmpElement Parent = Features.get(Features.get(CurrentNode.GetID()).getParentId());
		//		return new Node(Parent.getId(),Parent.getProperties().getName());
		//}
		return new FNode("ID", "Name", "Description", -1, false, false, false);
    }

    public Boolean IsRoot(FNode CurrentNode) {
		//FmpElement Feature = Features.get(CurrentNode.GetID());
		return false;
    }

    public Boolean NodeIsOptional(FNode TreeNode) {
    	//FmpElement Feature = Features.get(TreeNode.GetID());
    	return false;
    }

  	public Boolean NodeIsMandatory(FNode TreeNode) {
  		//FmpElement Feature = Features.get(TreeNode.GetID());
  		return false;
  	}

  	public Boolean NodeIsXor(FNode TreeNode) {
  		//FmpElement Feature = Features.get(TreeNode.GetID());
  		return false;
  	}

  	public Boolean NodeIsOr(FNode TreeNode) {
  		//FmpElement Feature = Features.get(TreeNode.GetID());
  		return false;
  	}
  	
  	public FNode GetRoot() {
  		//Iterator<FmpElement> FeatureIt = Features.values().iterator();
  		
  		//while (FeatureIt.hasNext()) {
  		//	FmpElement CurrentFeature = FeatureIt.next();
  		//	if (CurrentFeature.getParentId().equals("")) {
  		//		return new Node(CurrentFeature.getId(),CurrentFeature.getProperties().getName());
  		//	}
  		//}
  		
  		return new FNode("ID", "Name", "Description", -1, false, false, false);
  	}
}
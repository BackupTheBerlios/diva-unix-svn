package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;

import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.impl.FeatureGroupImpl;
import ca.uwaterloo.gp.fmp.system.NodeIdDictionary;
import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import ca.uwaterloo.gp.fmp.util.FmpResourceImpl;

public class IFMPFeatureModel extends AbstractFeatureModel {
	FmpExternalLoader FMPFileLoader;
	HashMap<FNode,FNode> Features;
	HashMap<FNode,Vector<FNode>> Children;
	String Name;
	
	public IFMPFeatureModel(FmpExternalLoader FileLoader, String TreeName, boolean IgnoreSoftGoals) {
		FMPFileLoader = FileLoader;
		Features = new HashMap<FNode,FNode>();
		Children = new HashMap<FNode,Vector<FNode>>();
		Name = TreeName;
		
		IndexNodes(IgnoreSoftGoals);
	}
	
	public String GetName() {
		return Name;
	}
	
	public Vector<FNode> GetAllNodes() {
		Vector<FNode> ReturnVector = new Vector<FNode>();
		
		Iterator<FNode> FeatureIt = Features.keySet().iterator();
		while (FeatureIt.hasNext()) {
			ReturnVector.add(FeatureIt.next());
		}
		
		return ReturnVector;
    }
	
	public Boolean IsInThisTree(FNode Node) {
		return Features.containsKey(Node);
	}
	
	public void IndexNodes(boolean IgnoreSoftGoals) {
		FmpResourceImpl fmpresource= (FmpResourceImpl)FMPFileLoader.getResources().get(0);

		Project project= (Project) fmpresource.getAllContents().next();
		Feature model= project.getModel();		
		NodeIdDictionary.INSTANCE.visit(null, model);
		
		FetchFMPNodes(model.getChildren(),model.getName(),model, null, 0, false, false, IgnoreSoftGoals);
	}
	
	public void FetchFMPNodes(EList l,String owner,Node root, FNode CurrentParent, int CurrentDepth, Boolean IsOr, Boolean IsXor, Boolean IgnoreSoftGoals){
		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			
			if(child instanceof Node){
				Node node= (Node)child;
				
				if(node!=null){
					String nodeName = getName(node);
					
					if (nodeName!=null) {
						if (nodeName.contains("group")) {
							Boolean Or= IsOrGroupNode(node);
							Boolean Xor = IsXOrGroupNode(node);
							FetchFMPNodes(node.getChildren(),nodeName,root,CurrentParent, CurrentDepth, Or, Xor,IgnoreSoftGoals);
						}
						else if (nodeName.equals("softgoal") && CurrentDepth == 0 && IgnoreSoftGoals) {
							// ignore this node and the entire subtree that follows
						}
						else {
							String nodeId = getId(node);
							String nodeDesc = getDescription(node);
							Boolean nodeOpt = getIsOptional(node);

							if(nodeId!=null && nodeDesc!=null){
								FNode NewNode = new FNode(nodeId, nodeName, nodeDesc, CurrentDepth, nodeOpt, IsOr, IsXor);
								Features.put(NewNode, CurrentParent);
								AddChild(CurrentParent,NewNode);
								FetchFMPNodes(node.getChildren(),nodeName,root,NewNode, CurrentDepth+1, IsOr, IsXor,IgnoreSoftGoals);
							}
						}
					}
				}
			}
		}
	}
	
	public void AddChild(FNode Parent, FNode NewChild) {
		Vector<FNode> ChildList = new Vector<FNode>();
		
		if (Children.containsKey(Parent)) {
			ChildList = Children.get(Parent);	
		}
		
		if (!ChildList.contains(NewChild)) {
			ChildList.add(NewChild);
		}
		
		Children.put(Parent, ChildList);
	}
	
	public FNode GetParent(FNode CurrentNode) {
		if (Features.containsKey(CurrentNode)) {
			return Features.get(CurrentNode);
		}
		else {
			return new FNode("ID", "Name", "Description", -1, false, false, false);
		}
    }

    public Boolean IsRoot(FNode CurrentNode) {
		if (Features.containsKey(CurrentNode)) {
			return Features.get(CurrentNode) == null;
		}
		else {
			return false;
		}
    }

    public Boolean NodeIsOptional(FNode TreeNode) {
    	return TreeNode.GetIsOptional();
    }

  	public Boolean NodeIsMandatory(FNode TreeNode) {
  		return TreeNode.GetIsMandatory();
  	}

  	public Boolean NodeIsXor(FNode TreeNode) {
  		return TreeNode.GetIsXor();
  	}

  	public Boolean NodeIsOr(FNode TreeNode) {
  		return TreeNode.GetIsOr();
  	}
  	
  	public FNode GetRoot() {
  		Iterator<FNode> NodeIt = Features.keySet().iterator();
  		
  		while (NodeIt.hasNext()) {
  			FNode Key = NodeIt.next();
  			FNode Value = Features.get(Key);
  			
  			if (Value == null) {
  				return Key; 
  			}
  		}
  		
  		return null;
  	}
  	
  	public String getName(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(0).getTarget();
			if(property instanceof TypedValue){
				return ((TypedValue)property).getStringValue();
			}
		}
		return null;
	}
  	
  	public String getDescription(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			Object property = ((Feature)properties.getChildren().get(3)).getChildren().get(0);
			if (property instanceof FeatureGroupImpl) {
				Feature First = (Feature)((FeatureGroupImpl)property).getChildren().get(2);
				Object Second = ((Feature)First.getChildren().get(0)).eAdapters().get(0).getTarget();
							
				if(Second instanceof TypedValue){
					return ((TypedValue)Second).getStringValue();
				}
			}
		}
		return null;
	}
  	
  	public String getId(Node n){
  		Feature properties= n.getProperties();
  		  				
  		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(1).getTarget();
  		if(property instanceof TypedValue){
  			return ((TypedValue)property).getStringValue();
  		}else{
  			return null;
  		}
  	}
  	
	public boolean getIsOptional(Node n){
		int min= ((Feature)n.getProperties().getChildren().get(4)).getTypedValue().getIntegerValue().intValue();

		if(min==0){
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean IsOrGroupNode(Node n) {
		int min = GetGroupMin(n);

		if(min==0){
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean IsXOrGroupNode(Node n) {
		int min = GetGroupMin(n);
		int max = GetGroupMax(n);

		if(min==1 && max==1){
			return true;
		}
		else {
			return false;
		}
	}
	
	public int GetGroupMin(Node n) {
		Feature properties= n.getProperties();
		
		if(properties!=null){
		Object property= ((Feature)properties.getChildren().get(1)).eAdapters().get(0).getTarget();
			if(property instanceof TypedValue){
				return ((TypedValue)property).getIntegerValue();
			}
		}
		return -1;
	}
	
	public int GetGroupMax(Node n) {
		Feature properties= n.getProperties();
		
		if(properties!=null){
		Object property= ((Feature)properties.getChildren().get(2)).eAdapters().get(0).getTarget();
			if(property instanceof TypedValue){
				return ((TypedValue)property).getIntegerValue();
			}
		}
		return -1;
	}
	
	public Vector<FNode> GetChildren(FNode Feature) {
		if (Children.containsKey(Feature)) {
			return Children.get(Feature);
		}
		else {
			return new Vector<FNode>();
		}
  	}
  	
  	public int DistanceToRoot(FNode Feature) {
  		return Feature.GetDepth();
  	}
  	
  	public int DistanceToCommonAncestor(FNode Feature1, FNode Feature2) {
  		FNode CommonAncestor = GetCommonAncestor(Feature1, Feature2);
  		
  		if (Feature1.GetDepth() > Feature1.GetDepth()) {
  			return Feature1.GetDepth() - CommonAncestor.GetDepth();
  		}
  		else {
  			return Feature2.GetDepth() - CommonAncestor.GetDepth();
  		}
  	}
  	
  	public FNode GetCommonAncestor(FNode Feature1, FNode Feature2) {
  		Vector<FNode> RootTraceFeature1 = GetRootTrace(Feature1);
  		Vector<FNode> RootTraceFeature2 = GetRootTrace(Feature2);
  		Iterator<FNode> TraceIt;
  		Vector<FNode> LongestTrace;
  		
  		if (RootTraceFeature1.size() > RootTraceFeature2.size()) {
  			TraceIt = RootTraceFeature2.iterator();
  			LongestTrace = RootTraceFeature1;
  		}
  		else {
  			TraceIt = RootTraceFeature1.iterator();
  			LongestTrace = RootTraceFeature2;
  		}
  		
  		while (TraceIt.hasNext()) {
  			FNode CurrentFeature = TraceIt.next();
  			if (LongestTrace.contains(CurrentFeature)) {
  				return CurrentFeature;
  			}
  		}
  		
  		return null;
  	}
  	
  	public boolean IsAncestorOf(FNode Feature1, FNode Feature2) {
  		Vector<FNode> RootTrace = GetRootTrace(Feature2);
  		return RootTrace.contains(Feature1);
  	}
  	
  	public Vector<FNode> GetRootTrace(FNode Feature) {
  		Vector<FNode> ReturnVector = new Vector<FNode>();
  		FNode Parent = GetParent(Feature);
  		
  		while (Parent != null) {
  			ReturnVector.add(Parent);
  			Parent = GetParent(Parent);
  		}
  		
  		return ReturnVector;
  	}
}
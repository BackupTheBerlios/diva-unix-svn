package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer.SimilarityCriterion;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.AbstractFeatureModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;

public class Matcher {

	private HashMap<FNode,Double> DifParentNodes;
	private HashMap<FNode,Double> DifSiblingNodes;
	private Vector<FNode> MissingNodes;
	private Vector<FNode> DifLogicalNodes;
	private Vector<FNode> MatchedNodes;
	private Vector<FNode> IgnoredNodes;
    private SemanticMatcher MySemMatcher;
    
    public Matcher() {
    	DifParentNodes = new HashMap<FNode,Double>();
    	DifSiblingNodes = new HashMap<FNode,Double>();
    	MissingNodes = new Vector<FNode>();
    	MatchedNodes = new Vector<FNode>();
    	IgnoredNodes = new Vector<FNode>();
    	DifLogicalNodes = new Vector<FNode>();
    	MySemMatcher = new SemanticMatcher();
    }

    private HashMap<FNode,Double> ComputeDifParentNodes() {
    	HashMap<FNode,Double> ReturnMap = new HashMap<FNode,Double>();
    	Iterator<FNode> MatchedNodeIt = MatchedNodes.iterator();
    	
    	while (MatchedNodeIt.hasNext()) {
    		FNode CurrentNode = MatchedNodeIt.next();
    		Double NodeVal = 1 - MySemMatcher.GetParentSimVal(CurrentNode);
    		ReturnMap.put(CurrentNode, NodeVal);
    	}
    	
    	return ReturnMap;
    }
    
    private HashMap<FNode,Double> ComputeDifSiblingNodes() {
    	HashMap<FNode,Double> ReturnMap = new HashMap<FNode,Double>();
    	Iterator<FNode> MatchedNodeIt = MatchedNodes.iterator();
    	
    	while (MatchedNodeIt.hasNext()) {
    		FNode CurrentNode = MatchedNodeIt.next();
    		Double NodeVal = new Double(1 - MySemMatcher.GetSiblingSimVal(CurrentNode));
    		ReturnMap.put(CurrentNode, NodeVal);
    	}
    	
    	return ReturnMap;
    }

    private Vector<FNode> ComputeMissingNodes(SimilarityCriterion Criterion) {
    	Vector<FNode> ReturnVector = new Vector<FNode>();
    	Iterator<FNode> CritNodeIt = Criterion.GetCriterion().iterator();
    	MatchedNodes = MySemMatcher.GetMatchedNodes();
    	IgnoredNodes = MySemMatcher.GetIgnoredNodes();
    	
    	while (CritNodeIt.hasNext()) {
    		FNode CurrentCritNode = CritNodeIt.next();
    		
    		if (!MatchedNodes.contains(CurrentCritNode) && !IgnoredNodes.contains(CurrentCritNode)) {
    			ReturnVector.add(CurrentCritNode);
    		}
    	}
    	
    	return ReturnVector;
    }

    private Vector<FNode> ComputeDifLogicalNodes() {
    	Vector<FNode> ReturnVector = new Vector<FNode>();
    	Iterator<FNode> MatchedNodeIt = MatchedNodes.iterator();
    	
    	while (MatchedNodeIt.hasNext()) {
    		FNode CurrentMatchedNode = MatchedNodeIt.next();

    		if (!MySemMatcher.HasEqualRole(CurrentMatchedNode)) {
    			ReturnVector.add(CurrentMatchedNode);
    		}
    	}
    	
    	return ReturnVector;
    }

    public void Evaluate(AbstractFeatureModel Tree1, AbstractFeatureModel Tree2, SimilarityCriterion Criterion) {
    	MySemMatcher.MatchNodes(Tree1, Tree2, Criterion);
    	
    	MissingNodes = ComputeMissingNodes(Criterion);
    	DifParentNodes = ComputeDifParentNodes();
    	DifLogicalNodes = ComputeDifLogicalNodes();
    	DifSiblingNodes = ComputeDifSiblingNodes();
    }

    public float GetSemMatchQualityTree1() {
    	return MySemMatcher.GetMatchQualityTree1();
    }
    
    public float GetSemMatchQualityTree2() {
    	return MySemMatcher.GetMatchQualityTree2();
    }
    
    public float GetOVerallSemMatchQuality() {
    	return MySemMatcher.GetOverallMatchQuality();
    }

    public float GetDifParentCount() {
    	float ReturnVal = 0;
    	
    	Iterator<Double> MembershipValIt = DifParentNodes.values().iterator();
    	while (MembershipValIt.hasNext()) {
    		ReturnVal += MembershipValIt.next().floatValue();
    	}
    	return ReturnVal;
    }
    
    public float GetDifSiblingNodeCount() {
    	float ReturnVal = 0;
    	
    	Iterator<Double> MembershipValIt = DifSiblingNodes.values().iterator();
    	while (MembershipValIt.hasNext()) {
    		Double MemVal = MembershipValIt.next(); 
    		ReturnVal += MemVal.floatValue();
    	}
    	return ReturnVal;
    }

    public int GetMissingNodeCount() {
    	return MissingNodes.size();
    }

    public int GetDifLogicalNodeCount() {
    	return DifLogicalNodes.size();
    }
    
    public void PrintNodeMatch() {
    	MySemMatcher.PrintNodeMatch();
    }
    
    public SemanticMatcher GetSemMatcher() {
    	return MySemMatcher;
    }
    
    public Double GetParentSimVal(FNode CritNode) {
    	if (DifParentNodes.containsKey(CritNode)) {
    		return 1 - DifParentNodes.get(CritNode);
    	}
    	else {
    		return new Double(1);
    	}
    }
    
    public Double GetSiblingSimVal(FNode CritNode) {
    	if (DifSiblingNodes.containsKey(CritNode)) {
    		return 1 - DifSiblingNodes.get(CritNode);
    	}
    	else {
    		return new Double(1);
    	}
    }
    
    public Boolean HasSameRole(FNode CritNode) {
    	return !DifLogicalNodes.contains(CritNode);
    }
}
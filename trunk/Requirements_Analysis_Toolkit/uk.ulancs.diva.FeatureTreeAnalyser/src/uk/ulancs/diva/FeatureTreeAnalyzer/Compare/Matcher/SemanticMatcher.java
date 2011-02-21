package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer.SimilarityCriterion;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.AbstractFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.LSA.LSAEvaluator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SemanticMatcher {

	private HashMap<FNode,FNode> MatchTree1;
	private HashMap<FNode,Vector<FNode>> ReverseMatchTree1;
	private HashMap<FNode,Float> MatchValTree1;
	
	private HashMap<FNode,FNode> MatchTree2;
	private HashMap<FNode,Vector<FNode>> ReverseMatchTree2;
	private HashMap<FNode,Float> MatchValTree2;
	
	private AbstractFeatureModel Tree1;
	private AbstractFeatureModel Tree2;
	private SimilarityCriterion CompareCrit;
	private LSAEvaluator MyLSAEval;
	
	private Vector<FNode> MatchedNodes;
	private Vector<FNode> IgnoredNodes;
	private HashMap<FNode,Vector<NodeMatch>> BestMatchResultsTree1;
	private HashMap<FNode,Vector<NodeMatch>> BestMatchResultsTree2;
	private float SemSimilarityTreshold = (float)0.0;
	
	public SemanticMatcher() {
		MatchTree1 = new HashMap<FNode,FNode>();
		ReverseMatchTree1 = new HashMap<FNode,Vector<FNode>>();
		MatchValTree1 = new HashMap<FNode,Float>();
		MatchTree2 = new HashMap<FNode,FNode>();
		ReverseMatchTree2 = new HashMap<FNode,Vector<FNode>>();
		MatchValTree2 = new HashMap<FNode,Float>();
		MatchedNodes = new Vector<FNode>();
		IgnoredNodes = new Vector<FNode>();
		BestMatchResultsTree1 = new HashMap<FNode,Vector<NodeMatch>>();
		BestMatchResultsTree2 = new HashMap<FNode,Vector<NodeMatch>>();
		
		MyLSAEval = new LSAEvaluator();
	}

    public float GetMatchQualityTree1() {
    	Iterator<Float> MatchValIt = MatchValTree1.values().iterator();
    	float ValSum = 0;
    	
    	while (MatchValIt.hasNext()) {
    		Float TempFloat = MatchValIt.next();
    		ValSum += TempFloat.floatValue();
    	}
    	return ValSum / CompareCrit.GetCriterion().size();
    }
    
    public float GetMatchQualityTree2() {
    	Iterator<Float> MatchValIt = MatchValTree2.values().iterator();
    	float ValSum = 0;
    	
    	while (MatchValIt.hasNext()) {
    		Float TempFloat = MatchValIt.next();
    		ValSum += TempFloat.floatValue();
    	}
    	return ValSum / CompareCrit.GetCriterion().size();
    }
    
    public float GetOverallMatchQuality() {
    	Iterator<FNode> CritIt = CompareCrit.GetCriterion().iterator();
    	float ValSum = 0;
    	
    	while (CritIt.hasNext()) {
    		FNode CritNode = CritIt.next();
    		
    		if (MatchValTree1.containsKey(CritNode) && MatchValTree2.containsKey(CritNode)) {
    			float Tree1Val = MatchValTree1.get(CritNode).floatValue();
    			float Tree2Val = MatchValTree2.get(CritNode).floatValue();
    			
    			if (Tree1Val > Tree2Val) {
    				ValSum += Tree2Val;
    			}
    			else {
    				ValSum += Tree1Val;
    			}
    		}
    	}
    	
    	return ValSum / CompareCrit.GetCriterion().size();
    }

    public FNode GetMatchingNodeTree1(FNode MatchedNode) {
    	if (MatchTree1.containsKey(MatchedNode)) {
    		return MatchTree1.get(MatchedNode);
    	}
    	else {
    		return null;
    	}
    }
    
    public FNode GetMatchingNodeTree2(FNode MatchedNode) {
    	if (MatchTree2.containsKey(MatchedNode)) {
    		return MatchTree2.get(MatchedNode);
    	}
    	else {
    		return null;
    	}
    }

    public void MatchNodes(AbstractFeatureModel FTree1, AbstractFeatureModel FTree2, SimilarityCriterion Criterion) {
    	Tree1 = FTree1;
    	Tree2 = FTree2;
    	CompareCrit = Criterion;
    	
    	Iterator<FNode> CritIt = Criterion.GetCriterion().iterator();
    	
    	while (CritIt.hasNext()) {
    		FNode CurrentNode = CritIt.next();
    		Boolean OKInTree1 = false;
    		Boolean OKInTree2 = false;
    		
    		if (FTree1.IsInThisTree(CurrentNode)) {		// Short-circuit eval for full-tree comparison
    			MatchTree1.put(CurrentNode, CurrentNode);
    			AddToReverseMatchTree1(CurrentNode,CurrentNode);
    			MatchValTree1.put(CurrentNode, new Float(1));
    			OKInTree1 = true;
    		}
    		else {
    			OKInTree1 = MatchNode(CurrentNode, true);
    		}
    		
    		if (FTree2.IsInThisTree(CurrentNode)) {		// Short-circuit eval for full-tree comparison
    			MatchTree2.put(CurrentNode, CurrentNode);
    			AddToReverseMatchTree2(CurrentNode,CurrentNode);
    			MatchValTree2.put(CurrentNode, new Float(1));
    			OKInTree2 = true;
    		}
    		else {
    			OKInTree2 = MatchNode(CurrentNode, false);
    		}
    		
    		if (OKInTree1 & OKInTree2) {
    			MatchedNodes.add(CurrentNode);
    		}
    		else if (!OKInTree1 & !OKInTree2) {
    			IgnoredNodes.add(CurrentNode);
    		}
    	}
    }
    
    public boolean MatchNode(FNode ThisNode, Boolean InTree1) {
    	FNode BestMatch = new FNode("", "", "", -1, false, false, false);
    	double BestMatchVal= 0;
    	Boolean FoundMatch = false;
    	AbstractFeatureModel MatchTree;
    	Vector<NodeMatch> BestResults = new Vector<NodeMatch>();
    	
    	if (InTree1) {
    		MatchTree = Tree1;
    	}
    	else {
    		MatchTree = Tree2;
    	}
    	
    	Iterator<FNode> TreeIt = MatchTree.GetAllNodes().iterator();
    	FNode CurrentTreeNode;
    	while (TreeIt.hasNext()) {
    		CurrentTreeNode = TreeIt.next(); 

    		System.out.println("");
    		System.out.println("");
    		System.out.println("=====================================================================");
    		System.out.println("Now comparing: " + ThisNode.GetName() + " with: " + CurrentTreeNode.GetName());
    		System.out.println("=====================================================================");
    		
    		double CurrentMatchVal = DetermineSemSimilarity(ThisNode, CurrentTreeNode);
    		
    		if (CurrentMatchVal > BestMatchVal & CurrentMatchVal >= SemSimilarityTreshold) {
    			BestMatch = CurrentTreeNode;
    			BestMatchVal = CurrentMatchVal;
    			BestResults.add(0,new NodeMatch(ThisNode,BestMatch,BestMatchVal));
    			FoundMatch = true;
    			
    			if (BestResults.size() > 5) {
    				BestResults.setSize(5);
    			}
    			if (BestMatchVal == 1.0) {
    				break;
    			}
    		}
    	}
    	
    	if (FoundMatch) {
    		if (InTree1) {
    			MatchTree1.put(ThisNode, BestMatch);
    			AddToReverseMatchTree1(BestMatch, ThisNode);
    			MatchValTree1.put(ThisNode, new Float(BestMatchVal));
    			BestMatchResultsTree1.put(ThisNode, BestResults);
    		}
    		else {
    			MatchTree2.put(ThisNode, BestMatch);
    			AddToReverseMatchTree2(BestMatch, ThisNode);
    			MatchValTree2.put(ThisNode, new Float(BestMatchVal));
    			BestMatchResultsTree2.put(ThisNode, BestResults);
    		}
    	}
    	
    	return FoundMatch;
    }
    
    public String Normalise(String InputString) {
    	String LastChar = InputString.substring(InputString.length()-1);
    	boolean StringNotOk = LastChar.equals(";") || LastChar.equals(".") || LastChar.equals(":") || LastChar.equals(" ");
    	
    	while (StringNotOk && InputString.length() > 1) {
   			InputString = InputString.substring(0, InputString.length()-1);
   			LastChar = InputString.substring(InputString.length()-1);
   	    	StringNotOk = LastChar.equals(";") || LastChar.equals(".") || LastChar.equals(":") || LastChar.equals(" ");
    	}
    	
    	return InputString;
    }

    public double DetermineSemSimilarity(FNode Node1, FNode Node2) {
    	String Node1Desc = Node1.GetDescription();
    	String Node2Desc = Node2.GetDescription();
    	boolean EmptyStrings = Node1Desc == null || Node1Desc.equals("") || Node2Desc == null || Node2Desc.equals("");  

    	if (!EmptyStrings) {
    		String Desc1 = Normalise(Node1Desc);
			String Desc2 = Normalise(Node2Desc);
    		if (Desc1.equals(Desc2)) {
    			return 1;
    		}
    		if (ThreeWords(Desc1) && ThreeWords(Desc2)) {
        		double SimVal = MyLSAEval.LSA(Desc1, Desc2); 
        		return SimVal;
        	}
        	else {
        		return 0;
        	}
    	}
    	else {
    		return 0;
    	}
    }
    
    public boolean ThreeWords(String Sentence) {
    	String NormSentence = Normalise(Sentence);
    	return NormSentence.split(" ").length > 2;
    }
     
    public double GetParentSimVal(FNode ThisNode) {
    	if (MatchTree1.containsKey(ThisNode) & MatchTree2.containsKey(ThisNode)) {
    		FNode Tree1Node = MatchTree1.get(ThisNode);
    		FNode Tree2Node = MatchTree2.get(ThisNode);
    		
    		if (!Tree1.IsRoot(Tree1Node) & !Tree2.IsRoot(Tree2Node)) {
    			FNode Tree1Parent = Tree1.GetParent(Tree1Node);
    			FNode Tree2Parent = Tree2.GetParent(Tree2Node);
    			
    			return DetermineParentSimilarity(Tree1Parent, Tree2Parent);
    		}
    		else if (Tree1.IsRoot(Tree1Node) & Tree2.IsRoot(Tree2Node)) {
    			return 1;
    		}
    		else {
    			return 0;
    		}
    	}
    	else {
    		return 0;
    	}
    }
    
    public double DetermineParentSimilarity(FNode Parent1, FNode Parent2) {
    	boolean Parent1HasCritMatch = HasCritMatch(Parent1, true);
    	boolean Parent2HasCritMatch = HasCritMatch(Parent2, false);
    	
    	if (Parent1HasCritMatch && Parent2HasCritMatch) {
    		if (ShareCritMatch(Parent1, Parent2)) {
    			return 1;
    		}
    		else {
    			return 0;
    		}
    	}
    	else if (Parent1HasCritMatch || Parent2HasCritMatch) {
    		return 0;
    	}
    	else {
    		return DetermineSemSimilarity(Parent1, Parent2);
    	}
    }
    
    public boolean HasCritMatch(FNode Node, boolean Tree1) {
    	if (Tree1) {
    		return ReverseMatchTree1.containsKey(Node);
    	}
    	else {
    		return ReverseMatchTree2.containsKey(Node);
    	}
    }
    
    public boolean ShareCritMatch(FNode Tree1Node, FNode Tree2Node) {
    	if (ReverseMatchTree1.containsKey(Tree1Node) && ReverseMatchTree2.containsKey(Tree2Node)) {
    		Vector<FNode> Tree1CritNodes = ReverseMatchTree1.get(Tree1Node);
    		Iterator<FNode> Tree2CritNodeIt = ReverseMatchTree2.get(Tree2Node).iterator();
    		while (Tree2CritNodeIt.hasNext()) {
    			boolean InBothVectors = Tree1CritNodes.contains(Tree2CritNodeIt.next());
    			if (InBothVectors) {
    				return true;
    			}
    		}
    		return false;
    	}
    	else {
    		return false;
    	}
    }
    
    public Vector<FNode> GetMatchedNodes() {
    	return MatchedNodes;
    }
    
    public Vector<FNode> GetIgnoredNodes() {
    	return IgnoredNodes;
    }
    
    public Boolean HasEqualRole(FNode ThisNode) {
    	FNode Tree1Node = GetMatchingNodeTree1(ThisNode);
    	FNode Tree2Node = GetMatchingNodeTree2(ThisNode);
    	
    	if (Tree1.NodeIsMandatory(Tree1Node) != Tree2.NodeIsMandatory(Tree2Node)) {
    		return false;
    	}
    	else if (Tree1.NodeIsOptional(Tree1Node) != Tree2.NodeIsOptional(Tree2Node)) {
    		return false;
    	}
    	else if (Tree1.NodeIsXor(Tree1Node) != Tree2.NodeIsXor(Tree2Node)) {
    		return false;
    	}
    	else if (Tree1.NodeIsOr(Tree1Node) != Tree2.NodeIsOr(Tree2Node)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    public void PrintNodeMatch() {
    	Iterator<FNode> CritIt = CompareCrit.GetCriterion().iterator();
    	
    	while (CritIt.hasNext()) {
    		FNode CritNode = CritIt.next();
    		
    		if (MatchTree1.containsKey(CritNode) && MatchTree2.containsKey(CritNode)) {
    			FNode Tree1Node = MatchTree1.get(CritNode);
    			FNode Tree2Node = MatchTree2.get(CritNode);
    			
    			System.out.println("");
    			System.out.println(CritNode.GetDescription());
    			System.out.println(Tree1.GetName() + ":  " + Tree1Node.GetDescription() + " (" + MatchValTree1.get(CritNode).floatValue() + ")");
    			System.out.println(Tree2.GetName() + ":  " + Tree2Node.GetDescription() + " (" + MatchValTree2.get(CritNode).floatValue() + ")");
    			System.out.println("");
    			System.out.println("");
    		}
    		else if (MatchTree1.containsKey(CritNode) && !MatchTree2.containsKey(CritNode)) {
    			FNode Tree1Node = MatchTree1.get(CritNode);
    			
    			System.out.println("");
    			System.out.println(CritNode.GetDescription());
    			System.out.println(Tree1.GetName() + ":  " + Tree1Node.GetDescription() + " (" + MatchValTree1.get(CritNode).floatValue() + ")");
    			System.out.println(Tree2.GetName() + ":  -");
    			System.out.println("");
    			System.out.println("");
    		}
    		else if (!MatchTree1.containsKey(CritNode) && MatchTree2.containsKey(CritNode)) {
    			FNode Tree2Node = MatchTree2.get(CritNode);
    			
    			System.out.println("");
    			System.out.println(CritNode.GetDescription());
    			System.out.println(Tree1.GetName() + ":  -");
    			System.out.println(Tree2.GetName() + ":  " + Tree2Node.GetDescription() + " (" + MatchValTree2.get(CritNode).floatValue() + ")");
    			System.out.println("");
    			System.out.println("");
    		}
    		else {
    			System.out.println(Tree1.GetName() + ":  -");
    			System.out.println(Tree2.GetName() + ":  -");
    		}
    	}
    	
    	System.out.println("");
    	System.out.println("");
    }
    
    public int GetCritSize() {
    	return CompareCrit.GetCriterion().size();
    }
    
    public Vector<FNode> GetCritNodes() {
    	return CompareCrit.GetCriterion();
    }
    
    public boolean MatchTree1Contains(FNode CritNode) {
    	return MatchTree1.containsKey(CritNode);
    }
    
    public boolean MatchTree2Contains(FNode CritNode) {
    	return MatchTree2.containsKey(CritNode);
    }
    
    public FNode GetMatchInTree1(FNode CritNode) {
    	return MatchTree1.get(CritNode);
    }
    
    public FNode GetMatchInTree2(FNode CritNode) {
    	return MatchTree2.get(CritNode);
    }
    
    public Float GetMatchValTree1(FNode CritNode) {
    	return MatchValTree1.get(CritNode);
    }
    
    public Float GetMatchValTree2(FNode CritNode) {
    	return MatchValTree2.get(CritNode);
    }
    
    public Vector<NodeMatch> GetBestMatchResults(FNode CritNode, boolean Tree1) {
    	if (Tree1) {
    		if (BestMatchResultsTree1.containsKey(CritNode)) {
    			return BestMatchResultsTree1.get(CritNode);
    		}
    		else {
    			return new Vector<NodeMatch>();
    		}
    	}
    	else {
    		if (BestMatchResultsTree2.containsKey(CritNode)) {
    			return BestMatchResultsTree2.get(CritNode);
    		}
    		else {
    			return new Vector<NodeMatch>();
    		}
    	}
    }
    
    public void AddToReverseMatchTree1(FNode MatchedNode, FNode CritNode) {
    	if (ReverseMatchTree1.containsKey(MatchedNode)) {
    		Vector<FNode> CritNodes = ReverseMatchTree1.get(MatchedNode);
    		CritNodes.add(CritNode);
    		ReverseMatchTree1.put(MatchedNode, CritNodes);
    	}
    	else {
    		Vector<FNode> CritNodes = new Vector<FNode>();
    		CritNodes.add(CritNode);
    		ReverseMatchTree1.put(MatchedNode, CritNodes);
    	}
    }
    
    public void AddToReverseMatchTree2(FNode MatchedNode, FNode CritNode) {
    	if (ReverseMatchTree2.containsKey(MatchedNode)) {
    		Vector<FNode> CritNodes = ReverseMatchTree2.get(MatchedNode);
    		CritNodes.add(CritNode);
    		ReverseMatchTree2.put(MatchedNode, CritNodes);
    	}
    	else {
    		Vector<FNode> CritNodes = new Vector<FNode>();
    		CritNodes.add(CritNode);
    		ReverseMatchTree2.put(MatchedNode, CritNodes);
    	}
    }
    
    public float GetSiblingSimVal(FNode CritNode) {
    	FNode Tree1Node = GetMatchingNodeTree1(CritNode);
    	FNode Tree2Node = GetMatchingNodeTree2(CritNode);
    	
    	Vector<FNode> Tree1Siblings = GetCritSiblings(Tree1Node, true, CritNode);
    	Vector<FNode> Tree2Siblings = GetCritSiblings(Tree2Node, false, CritNode);
    	
    	Vector<Float> Tree1SibsDegrees = ComputeSibDegrees(Tree2Node, Tree1Siblings, false);
    	Vector<Float> Tree2SibsDegrees = ComputeSibDegrees(Tree1Node, Tree2Siblings, true);
    	int SibsComputed = Tree1SibsDegrees.size() + Tree2SibsDegrees.size();
    	
    	if (SibsComputed > 0) {
    	    	float Tree1SibsAggVal = AggVectorVals(Tree1SibsDegrees);
    	    	float Tree2SibsAggVal = AggVectorVals(Tree2SibsDegrees);
    	    	
    	    	return (Tree1SibsAggVal + Tree2SibsAggVal) / SibsComputed;
    	}
    	else {
    		return 1;
    	}
    }
    
    public Vector<FNode> GetCritSiblings(FNode TreeNode, boolean InTree1, FNode CritNode) {
    	Vector<FNode> ReturnVector = new Vector<FNode>();
    	AbstractFeatureModel Tree;
    	
    	if (InTree1) {
    		Tree = Tree1;
    	}
    	else {
    		Tree = Tree2;
    	}
    	
    	Iterator<FNode> ChildIt = Tree.GetChildren(Tree.GetParent(TreeNode)).iterator();
    	while (ChildIt.hasNext()) {
    		FNode CurrentChild = ChildIt.next();
    		Vector<FNode> MatchedCritNodes = new Vector<FNode>();
    		
    		if (InTree1) {
    			if (ReverseMatchTree1.containsKey(CurrentChild)) {
    				MatchedCritNodes = ReverseMatchTree1.get(CurrentChild);
    			}
    		}
    		else {
    			if (ReverseMatchTree2.containsKey(CurrentChild)) {
    				MatchedCritNodes = ReverseMatchTree2.get(CurrentChild);
    			}
    		}
    		
    		Iterator<FNode> MatchIt = MatchedCritNodes.iterator(); 
    		
    		while (MatchIt.hasNext()) {
    			FNode CurrentMatch = MatchIt.next();
    			if (CurrentMatch != CritNode) {
    				ReturnVector.add(CurrentMatch);
    			}
    		}
    	}
    	
    	return ReturnVector;
    }
    
    public Vector<Float> ComputeSibDegrees(FNode Feature, Vector<FNode> Siblings, boolean InTree1) {
    	Vector<Float> ReturnVector = new Vector<Float>();
    	Iterator<FNode> SiblingIt = Siblings.iterator();
    	AbstractFeatureModel Tree;
    	
    	if (InTree1) {
    		Tree = Tree1;
    	}
    	else {
    		Tree = Tree2;
    	}
    	
    	while (SiblingIt.hasNext()) {
    		FNode CurrentSib = SiblingIt.next();
    		FNode TreeSibling;
    		if (InTree1) {
    			TreeSibling = GetMatchingNodeTree1(CurrentSib);
    		}
    		else {
    			TreeSibling = GetMatchingNodeTree2(CurrentSib);
    		}
    		Float SibDegree = new Float(SiblingDegree(Feature, TreeSibling, Tree));
    		ReturnVector.add(SibDegree);
    	}
    	
    	return ReturnVector;
    }
    
    public float SiblingDegree(FNode Node1, FNode Node2, AbstractFeatureModel Tree) {
    	if (Node2 == null) {
    		return 0;
    	}
    	
    	int Node1RootDistance = Tree.DistanceToRoot(Node1);
    	int Node2RootDistance = Tree.DistanceToRoot(Node2);
    	boolean IsAncestor = Tree.IsAncestorOf(Node1,Node2) || Tree.IsAncestorOf(Node2,Node1);
    	
    	if (Node1RootDistance == 1 && Node2RootDistance == 1) {
    		return 1;
    	}
    	else if (IsAncestor) {
    		return 0;
    	}
    	else {
    		int CommonAncestorDistance = Tree.DistanceToCommonAncestor(Node1,Node2);
    		
    		if (Node1RootDistance > Node2RootDistance) {
    			return ((Node1RootDistance-1)-(CommonAncestorDistance-1)) / (Node1RootDistance-1);
    		}
    		else {
    			return ((Node2RootDistance-1)-(CommonAncestorDistance-1)) / (Node2RootDistance-1);
    		}
    	}
    }
    
    public float AggVectorVals(Vector<Float> FloatVector) {
    	Iterator<Float> FloatIt = FloatVector.iterator();
    	float ReturnVal = 0;
    	
    	while (FloatIt.hasNext()) {
    		ReturnVal += FloatIt.next().floatValue();
    	}
    	
    	return ReturnVal;
    }
}
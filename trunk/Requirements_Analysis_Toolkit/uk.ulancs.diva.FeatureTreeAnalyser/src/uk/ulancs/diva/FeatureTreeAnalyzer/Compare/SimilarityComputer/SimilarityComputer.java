package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer;

import java.util.Vector;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.AbstractFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher.Matcher;

public class SimilarityComputer {

	private Integer SemanticWeight;
	private Integer StructureWeight;
	private Integer LogicalWeight;
	private Integer SiblingWeight;
	private AbstractFeatureModel FeatureTree1;
	private AbstractFeatureModel FeatureTree2;
	private SimilarityCriterion MyCriterion;
	private Matcher MyMatcher;
	
	public SimilarityComputer(AbstractFeatureModel FirstTree, AbstractFeatureModel SecondTree, SimilarityCriterion Crit) {
		StructureWeight = 1;
		LogicalWeight = 1;
		SiblingWeight = 1;
		SemanticWeight = StructureWeight+LogicalWeight+SiblingWeight+1;
		
		FeatureTree1 = FirstTree;
		FeatureTree2 = SecondTree;
		if (Crit.GetCriterion().size() == 0) {
			Vector<FNode> Tree1Nodes = FirstTree.GetAllNodes();
			Vector<FNode> Tree2Nodes = SecondTree.GetAllNodes();
			if (Tree1Nodes.size() < Tree2Nodes.size()) {
				Crit.SetCriterion(Tree1Nodes);
			}
			else {
				Crit.SetCriterion(Tree2Nodes);
			}
		}
		MyCriterion = Crit;
		MyMatcher = new Matcher();
	}

	public Boolean SetSemanticWeight(Integer SemWeight) {
		if (SemWeight >= 0) {
			SemanticWeight = SemWeight;
			return true;
		}
		else {
			return false;
		}
	}

	public Boolean SetStructureWeight(Integer StructWeight) {
		if (StructWeight >= 0) {
			StructureWeight = StructWeight;
			return true;
		}
		else {
			return false;
		}
	}

	public Boolean SetLogicalWeight(Integer LogWeight) {
		if (LogWeight >= 0) {
			LogicalWeight = LogWeight;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void DetermineSimilarity() {
		SimilarityResult Result = new SimilarityResult(MyMatcher,SemanticWeight,StructureWeight,LogicalWeight,SiblingWeight);
		
		long StartTime = System.currentTimeMillis()/1000;
		MyMatcher.Evaluate(FeatureTree1, FeatureTree2, MyCriterion);
		long EndTime = System.currentTimeMillis()/1000;

		long CalcTime = EndTime-StartTime;
		Long Hours = new Long(div(CalcTime,3600));
		Long Minutes = new Long(div(CalcTime,60));
		Long Seconds = new Long (CalcTime % 60);
		
		Result.WriteResult(FeatureTree1, FeatureTree2, Hours, Minutes, Seconds);
	}
	
	public int div(long x, long y) {
		int RetVal = 0;
		
		while (x >= y) {
			x = x - y;
			RetVal++;
		}
		
		return RetVal;
	}
}
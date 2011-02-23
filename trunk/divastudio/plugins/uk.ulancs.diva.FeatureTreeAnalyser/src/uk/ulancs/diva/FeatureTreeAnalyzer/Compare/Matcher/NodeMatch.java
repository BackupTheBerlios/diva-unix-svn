package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;

public class NodeMatch {
	private FNode OriginalNode;
	private FNode MatchedNode;
	private double SimVal;
	
	public NodeMatch(FNode OrgNode, FNode MatchNode, double MatchVal) {
		OriginalNode = OrgNode;
		MatchedNode = MatchNode;
		SimVal = MatchVal;
	}
	
	public FNode GetOriginalNode() {
		return OriginalNode;
	}
	
	public FNode GetMatchedNode() {
		return MatchedNode;
	}
	
	public double GetMatchVal() {
		return SimVal;
	}
}

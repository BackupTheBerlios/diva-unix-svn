package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Vector;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.AbstractFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher.Matcher;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher.NodeMatch;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Matcher.SemanticMatcher;

public class SimilarityResult {
	private Matcher MyMatcher;
	private SemanticMatcher MySemMatcher;
	private Integer SemanticWeight;
	private Integer StructureWeight;
	private Integer LogicalWeight;
	private Integer SiblingWeight;
	
	public SimilarityResult(Matcher TheMatcher, Integer SemWeight, Integer StructWeight, Integer LogWeight, Integer SibWeight) {
		MyMatcher = TheMatcher;
		MySemMatcher = MyMatcher.GetSemMatcher();
		SemanticWeight = SemWeight;
		StructureWeight = StructWeight;
		LogicalWeight = LogWeight;
		SiblingWeight = SibWeight;
	}
	
	public void WriteResult(AbstractFeatureModel Tree1, AbstractFeatureModel Tree2, Long Hours, Long Minutes, Long Seconds) {
		float MissingNodeCount = MyMatcher.GetMissingNodeCount();
		float DifParentCount = MyMatcher.GetDifParentCount();
		float DifLogicalCount = MyMatcher.GetDifLogicalNodeCount();
		float DifSiblingCount = MyMatcher.GetDifSiblingNodeCount();
		
		float TreeDistance = SemanticWeight * MissingNodeCount + StructureWeight * DifParentCount + LogicalWeight * DifLogicalCount + SiblingWeight * DifSiblingCount;
		float MaxDistance = SemanticWeight * MySemMatcher.GetCritSize();
		float TreeSimilarity = 1 - (TreeDistance / MaxDistance);
		
		
		String FileHandle = System.getProperty("user.dir") + File.separator + "TreeSimilarityResults.txt";
		
		try {
			  BufferedWriter Output = new BufferedWriter(new FileWriter(FileHandle));

			  Output.write("=======================================\n");
			  Output.write("|         Similarity Report           |\n");
			  Output.write("=======================================\n");
			  Output.write("");
			  Output.write("Semantic Similarity: " + MyMatcher.GetOVerallSemMatchQuality() + "\n");
			  Output.write("---------------------------------------\n");
			  Output.write("Match quality for " +  Tree1.GetName() + ": " + MyMatcher.GetSemMatchQualityTree1() + "\n");
			  Output.write("Match quality for " +  Tree2.GetName() + ": " + MyMatcher.GetSemMatchQualityTree2() + "\n");
			  Output.write("\n\n");
			  Output.write("Structural similarity: " + TreeSimilarity + "\n");
			  Output.write("---------------------------------------\n");
			  Output.write("Nodes only in one of the trees:    " + MissingNodeCount + "\n");
			  Output.write("Nodes with different parents:      " + DifParentCount + "\n");
			  Output.write("Nodes with different logical role: " + DifLogicalCount + "\n");
			  Output.write("Nodes with different siblings:     " + DifSiblingCount + "\n\n");

			  Output.write("_______________________________________\n");
			  Output.write("Computation Time: " + Hours.toString() + "h " + Minutes.toString() + "m " + Seconds.toString() + "s" + "\n");
			  Output.write("\n\n\n");
			  
			  
			  Output.write("Breakdown of feature matches and similarity\n");
			  Output.write("===========================================\n\n");
			  
			  Iterator<FNode> CritIt = MySemMatcher.GetCritNodes().iterator();
			  
			  while (CritIt.hasNext()) {
				  FNode CritNode = CritIt.next();
		    		
				  if (MySemMatcher.MatchTree1Contains(CritNode) && MySemMatcher.MatchTree2Contains(CritNode)) {
					  FNode Tree1Node = MySemMatcher.GetMatchInTree1(CritNode);
					  Float MatchValTree1 = MySemMatcher.GetMatchValTree1(CritNode);
					  
					  FNode Tree2Node = MySemMatcher.GetMatchInTree2(CritNode);
					  Float MatchValTree2 = MySemMatcher.GetMatchValTree2(CritNode);
					  Iterator<NodeMatch> MatchResultsTree1It = MySemMatcher.GetBestMatchResults(CritNode, true).iterator();
					  Iterator<NodeMatch> MatchResultsTree2It = MySemMatcher.GetBestMatchResults(CritNode, false).iterator();
					  
					  Double ParentSimVal = MyMatcher.GetParentSimVal(CritNode);
					  Boolean SameRole = MyMatcher.HasSameRole(CritNode);
					  Double SiblingSimVal = MyMatcher.GetSiblingSimVal(CritNode);
		    
					  
					  Output.write("==================================================\n");
					  //Output.write(CritNode.GetDescription() + "\n");
					  Output.write(Tree1Node.GetName() + " in tree " + Tree1.GetName() + " was matched to " + Tree2Node.GetName() + " in tree " + Tree2.GetName() + "\n");
					  Output.write("-------------------\n");
					  if (MatchValTree1 == 1) {
						  Output.write("Semantic similarity: " + MatchValTree2.toString() + "\n");
					  }
					  else {
						  Output.write("Semantic similarity: " + MatchValTree1.toString() + "\n");
					  }
					  Output.write("Parent similarity:   " + ParentSimVal.toString() + "\n");
					  Output.write("Sibling similarity:  " + SiblingSimVal.toString() + "\n");
					  Output.write("Same logical role:   " + SameRole + "\n\n");
					  Output.write("-------------------\n");
					  Output.write(Tree1Node.GetName() + " (" + Tree1.GetName() + "):\n" + Tree1Node.GetDescription() + "\n\n");
					  Output.write(Tree2Node.GetName() + " (" + Tree2.GetName() + "):\n" + Tree2Node.GetDescription());
					  
					  if (MatchResultsTree1It.hasNext()) {
						  Output.write("\n-------------------");
						  Output.write("\n\n---------- Alternative match results in " + Tree1.GetName() + " ------------");
					  
						  while (MatchResultsTree1It.hasNext()) {
							  NodeMatch CurrentMatch = MatchResultsTree1It.next();
							  if (CurrentMatch.GetMatchedNode() != Tree1Node) {
								  Output.write("\n\n" + CurrentMatch.GetMatchedNode().GetDescription() + "\n");
								  Output.write("-----------------------------[ " + CurrentMatch.GetMatchVal() + " ]");
							  }
						  }
					  }
					  
					  if (MatchResultsTree2It.hasNext()) {
						  Output.write("\n-------------------");
						  Output.write("\n\n---------- Alternative match results in " + Tree2.GetName() + "------------");
					  
						  while (MatchResultsTree2It.hasNext()) {
							  NodeMatch CurrentMatch = MatchResultsTree2It.next();
							  if (CurrentMatch.GetMatchedNode() != Tree2Node) {
								  Output.write("\n\n" + CurrentMatch.GetMatchedNode().GetDescription() + "\n");
								  Output.write("-----------------------------[ " + CurrentMatch.GetMatchVal() + " ]");
							  }
						  }
					  }
					  
					  Output.write("\n==================================================\n\n\n");
				  }
				  else if (!MySemMatcher.MatchTree1Contains(CritNode) && MySemMatcher.MatchTree2Contains(CritNode)) {
					  FNode Tree2Node = MySemMatcher.GetMatchInTree2(CritNode);
					  Float MatchValTree2 = MySemMatcher.GetMatchValTree2(CritNode);
					  Iterator<NodeMatch> MatchResultsTree2It = MySemMatcher.GetBestMatchResults(CritNode, true).iterator();
					  
					  Output.write("==================================================\n");
					  //Output.write(CritNode.GetDescription() + "\n");
					  Output.write(CritNode.GetName() + " in tree " + Tree2.GetName() + "could not be matched in " + Tree1.GetName() + "\n");
					  Output.write("-------------------\n");
					  Output.write("Semantic similarity: " + MatchValTree2 + " in tree " + Tree2.GetName() + "\n");
					  Output.write("Parent similarity:   0.0\n");
					  Output.write("Same logical role:   false\n\n");
					  Output.write("-------------------\n");
					  Output.write(Tree2Node.GetName() + " (" + Tree2.GetName() + "):\n" + Tree2Node.GetDescription());
					  
					  if (MatchResultsTree2It.hasNext()) {
						  Output.write("\n-------------------");
						  Output.write("\n\n---------- Alternative match results in " + Tree2Node.GetName() + " ------------");
					  
						  while (MatchResultsTree2It.hasNext()) {
							  NodeMatch CurrentMatch = MatchResultsTree2It.next();
							  if (CurrentMatch.GetMatchedNode() != Tree2Node) {
								  Output.write("\n\n" + CurrentMatch.GetMatchedNode().GetDescription() + "\n");
								  Output.write("-----------------------------[ " + CurrentMatch.GetMatchVal() + " ]");
							  }
						  }
					  }
					  
					  Output.write("\n==================================================\n\n\n");
				  }
				  else if (MySemMatcher.MatchTree1Contains(CritNode) && !MySemMatcher.MatchTree2Contains(CritNode)) {
					  FNode Tree1Node = MySemMatcher.GetMatchInTree1(CritNode);
					  Float MatchValTree1 = MySemMatcher.GetMatchValTree1(CritNode);
					  Iterator<NodeMatch> MatchResultsTree1It = MySemMatcher.GetBestMatchResults(CritNode, false).iterator();
					  
					  Output.write("==================================================\n");
					  //Output.write(CritNode.GetDescription() + "\n");
					  Output.write(CritNode.GetName() + " in tree " + Tree1.GetName() + "could not be matched in " + Tree2.GetName() + "\n");
					  Output.write("-------------------\n");
					  Output.write("Semantic similarity: " + MatchValTree1 + " in tree " + Tree1.GetName() + "\n");
					  Output.write("Parent similarity:   0.0\n");
					  Output.write("Same logical role:   false\n\n");
					  Output.write("-------------------\n");
					  Output.write(Tree1Node.GetName() + " (" + Tree1.GetName() + "):\n" + Tree1Node.GetDescription());
					  
					  if (MatchResultsTree1It.hasNext()) {
						  Output.write("\n-------------------");
						  Output.write("\n\n---------- Alternative match results in " + Tree1Node.GetName() + " ------------");
					  
						  while (MatchResultsTree1It.hasNext()) {
							  NodeMatch CurrentMatch = MatchResultsTree1It.next();
							  if (CurrentMatch.GetMatchedNode() != Tree1Node) {
								  Output.write("\n\n" + CurrentMatch.GetMatchedNode().GetDescription() + "\n");
								  Output.write("-----------------------------[ " + CurrentMatch.GetMatchVal() + " ]");
							  }
						  }
					  }
					  
					  Output.write("\n==================================================\n\n\n");
				  }
				  else {
					  Output.write("==================================================\n");
					  //Output.write(CritNode.GetDescription() + "\n");
					  Output.write("An error occured in the matching process\n");
					  Output.write("-------------------\n");
					  Output.write("Semantic similarity: -\n");
					  Output.write("Parent similarity:   -\n");
					  Output.write("Same logical role:   -\n");
					  Output.write("==================================================\n\n\n");
				  }
			  }

			  Output.close();
		}
		catch (Exception e) {
			  System.out.println("Fatal: Tried to read or write requirement files that do not exist");
		}
	}
}

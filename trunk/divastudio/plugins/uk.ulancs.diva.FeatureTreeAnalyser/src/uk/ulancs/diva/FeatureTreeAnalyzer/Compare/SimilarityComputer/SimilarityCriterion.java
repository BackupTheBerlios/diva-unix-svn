package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer;

import java.util.Vector;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;

public class SimilarityCriterion {

  private Vector<FNode> MyCriterion;
  
  public SimilarityCriterion() {
	  MyCriterion = new Vector<FNode>();
  }

  public Boolean SetCriterion(Vector<FNode> NewCriterion) {
	  MyCriterion = NewCriterion;
	  return true;
  }

  public Vector<FNode> GetCriterion() {
	  return MyCriterion;
  }
}
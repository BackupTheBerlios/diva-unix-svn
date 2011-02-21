package uk.ulancs.diva.FeatureTreeAnalyzer.Compare;

import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.IFMPFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FileLoader.CriterionLoader;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer.SimilarityComputer;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer.SimilarityCriterion;

public class Compare {
	
	public void CompareTrees(FmpExternalLoader Tree1, String Tree1Name, FmpExternalLoader Tree2, String Tree2Name, String CritHandle) {
		CriterionLoader CritLoader = new CriterionLoader(CritHandle);
		if (CritLoader.FileFound()) {
			SimilarityComputer SimilarityCalculator = new SimilarityComputer(new IFMPFeatureModel(Tree1,Tree1Name), new IFMPFeatureModel(Tree2,Tree2Name), CritLoader.GetCriterion());
			SimilarityCalculator.DetermineSimilarity();
		}
		else {
			SimilarityComputer SimilarityCalculator = new SimilarityComputer(new IFMPFeatureModel(Tree1,Tree1Name), new IFMPFeatureModel(Tree2,Tree2Name), new SimilarityCriterion());
			SimilarityCalculator.DetermineSimilarity();
		}
	}
}
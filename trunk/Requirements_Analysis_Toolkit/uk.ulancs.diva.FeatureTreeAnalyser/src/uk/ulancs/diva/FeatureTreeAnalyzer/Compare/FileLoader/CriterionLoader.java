package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FileLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FNode;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.SimilarityComputer.SimilarityCriterion;

public class CriterionLoader {
	private Boolean FileFound;
	private String FileHandle;
	
	public CriterionLoader(String FileName) {
		try {
			File CritFile = new File(FileName);
			FileFound = CritFile.exists(); 
			FileHandle = FileName;
		}
		catch (Exception e) {
			FileFound = false;
		}
	}
	
	public CriterionLoader() {
		FileFound = false;
	}
	
	public SimilarityCriterion GetCriterion() {
		SimilarityCriterion NewCrit = new SimilarityCriterion();
		Vector<FNode> Nodes = new Vector<FNode>();
		
		if (FileFound) {
			BufferedReader File;
			  
			try {
				String ID;
				String Name;
				String Description;
				File = new BufferedReader(new FileReader(FileHandle));
				
				while (File.ready()) {
					ID = File.readLine();
					if (!ID.equals("") && File.ready()) {
						Name = File.readLine();
						if (!Name.equals("") && File.ready()) {
							Description = File.readLine();
							Nodes.add(new FNode(ID,Name,Description, -1, false, false, false));
						}
					}
				}
			}
			catch (Exception e) {
				System.out.println("Fatal: Tried to read a criterion file that does not exist");
			}
		}
		
		NewCrit.SetCriterion(Nodes);
		return NewCrit;
	}
	
	public Boolean FileFound() {
		return FileFound;
	}
}

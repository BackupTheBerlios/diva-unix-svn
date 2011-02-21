package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FileLoader;

import java.io.File;

import org.eclipse.core.resources.IFile;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.AbstractFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.FMPFeatureModel;
import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FeatureTree.IFMPFeatureModel;

public class FMPFileLoader extends AbstractFileLoader {
	String FileHandle = "";
	File FMPFile;
	IFile FMPIFile;
	Boolean IsIFile = false;
	
	public FMPFileLoader(String FileName) {
		try {
			// FmpExternalLoader 
			FMPFile = new File(FileName);
			FileFound = FMPFile.exists();
			String userDir = System.getProperty("user.dir");
			FileHandle = userDir + File.separatorChar + FileName;
			//FileHandle = FileName;
		}
		catch (Exception e) {
			FileFound = false;
		}
	}
	
	public FMPFileLoader(IFile File) {
		FileFound = true;
		IsIFile = true;
	}
	
	public FMPFileLoader() {
		FileFound = false;
	}
	
	public AbstractFeatureModel GetFeatureTree() {
		if (FileFound & !IsIFile) {
			return new FMPFeatureModel(FileHandle);
		}
		else {
			return null;
		}
	}
}
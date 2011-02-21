package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FileLoader;

import org.eclipse.core.resources.IFile;

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.FileLoader.AbstractFileLoader;

public class FileLoaderFactory {

	public AbstractFileLoader CreateFileLoader(String FileName) {
		if (FileName.contains(".fmp")) {
			return new FMPFileLoader(FileName);
		}
		else if (FileName.contains(".rdl")) {
			return new ArborcraftFileLoader(FileName);
		}
		else {
			return new FMPFileLoader();
		}
	}
	
	public AbstractFileLoader CreateFileLoader(IFile File) {
		return new FMPFileLoader(File);
	}
	
	public CriterionLoader CreateCritLoader(String FileName) {
		return new CriterionLoader(FileName);
	}
}
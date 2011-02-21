package uk.ulancs.diva.FeatureTreeAnalyzer.Compare.LSA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.Platform;

import lsat.gui.ChunkAnalysisStruct;
import lsat.lsa.LSAResults;
import lsat.structures.DocumentClass;
import lsat.structures.Project;
import ulanc.framework.reqsim.LSAImpl;

public class LSAEvaluator {
	public double LSA(String String1, String String2) {
    	int boundarySize = -3;
		int reducedRows = 26;
		String workspacePath = Platform.getLocation().toPortableString() + File.separator;
		ArrayList<String> filenames = new ArrayList<String>();
		String FileHandle = System.getProperty("user.dir") + File.separator + "TempFile.txt";
		
		try {
			  BufferedWriter Output = new BufferedWriter(new FileWriter(FileHandle));

			  Output.write(String1);
			  Output.newLine();
			  Output.newLine();
			  Output.write(String2);
			  Output.close();
			  filenames.add(FileHandle);
			  // filenames.add(System.getProperty("user.dir") + File.separator + "TempFile2.txt");
		}
		catch (Exception e) {
			  System.out.println("Fatal: Tried to read or write requirement files that do not exist");
			  File DeleteFile = new File(FileHandle);
			  DeleteFile.delete();
			  return 0.0;
		}
		
		///////////////////////////////////////////////////////////
		String doc = FileHandle.split("\\.")[0];
		Project p = new Project(doc + "Project");
		p.addDocumentClass(doc);


		try {
			File inputDoc = new File(FileHandle);
			Iterator<DocumentClass> dcc = p.getDocumentClassCollection().iterator();

			while (dcc.hasNext()) {
				DocumentClass dc = dcc.next();
				if (dc.getDescription().contentEquals(doc)) {
					p.addNewDocument(inputDoc, 1.0f, FileHandle, dc);
				}
			}

			LSAResults results = p.getLSAM().performLSA(p.getDocumentCollection(), p.getDocumentClassCollection(), "^$", -3, 26, workspacePath);
			System.out.println(results.toString());

		} catch (Exception e) {
			System.out.println(e);
		}
		///////////////////////////////////////////////////////////		
		
		
		
		// intW Test = new intW(0);
		
		LSAImpl lsa = new LSAImpl();
		LSAResults lsares = lsa.performLSA(filenames, boundarySize, reducedRows, workspacePath);
		File DeleteFile = new File(FileHandle);
		DeleteFile.delete();
		
		ArrayList<ChunkAnalysisStruct> cas = new ArrayList<ChunkAnalysisStruct>();
		cas.addAll(lsares.getChunksBelongingToClass(1));
		// lsares.
		if (cas.size() == 2) {
			return calculateSimilarity(lsares, cas.get(0), cas.get(1));
		}
		else {
			return 0;
		}
    }
	
	// suspect similarity is equal to the threshold at which contribution is found
	// easy enough to calculate but might well be very slow.
	public double calculateSimilarity(LSAResults results, ChunkAnalysisStruct c1, ChunkAnalysisStruct c2) {
		double increment = 1.0 / 20; 
		for (double i = 1.0; i > 0.0; i = i - increment) {
			Iterator<Integer> contribsIt = results.getContributors(c1.getChunkNo(), i).iterator();
			while (contribsIt.hasNext()) {
				if (contribsIt.next().equals(c2.getChunkNo())) {
					return i;
				}
			}
		}
		return 0.0;
	}
}

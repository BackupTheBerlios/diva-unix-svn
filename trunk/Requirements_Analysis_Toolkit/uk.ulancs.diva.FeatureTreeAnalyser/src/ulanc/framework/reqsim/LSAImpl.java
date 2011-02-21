package ulanc.framework.reqsim;

import lsat.lsa.*;
import lsat.structures.*;
import lsat.gui.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class LSAImpl {

	public LSAResults performLSA(ArrayList<String> filenames, int boundarySize, int reducedRows, String workspacePath) {

		Project p = new Project("ACProject");
//		p.addDocumentClass("ACDoc");


		try {
			Iterator<String> filenamesIt = filenames.iterator();
			while (filenamesIt.hasNext()) {
				String filename = filenamesIt.next();
				File inputDoc = new File(filename);
				
				int slashIndex = filename.lastIndexOf("\\");
				String doc = filename;
				doc = doc.substring(slashIndex + 1, filename.length());
//				String doc = filename.split("\\.")[0];
				p.addDocumentClass(doc); 

				Iterator<DocumentClass> dcc = p.getDocumentClassCollection().iterator();
				while (dcc.hasNext()) {
					DocumentClass dc = dcc.next();
					if (dc.getDescription().contentEquals(doc)) {
						p.addNewDocument(inputDoc, 1.0f, filename, dc);
					}
				}
			}
			LSAResults results = p.getLSAM().performLSA(p.getDocumentCollection(), p.getDocumentClassCollection(), "^$", boundarySize, reducedRows, workspacePath);
			return results;


		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public LSAResults performLSA(String filename, int boundarySize, int reducedRows) {

		String doc = filename.split("\\.")[0];
		//		Project p = new Project("TestProject");
		//		p.addDocumentClass("Test");

		Project p = new Project(doc + "Project");
		p.addDocumentClass(doc);


		try {
			File inputDoc = new File(filename);
			Iterator<DocumentClass> dcc = p.getDocumentClassCollection().iterator();

			while (dcc.hasNext()) {
				DocumentClass dc = dcc.next();
				if (dc.getDescription().contentEquals(doc)) {
					p.addNewDocument(inputDoc, 1.0f, filename, dc);
				}
			}

			LSAResults results = p.getLSAM().performLSA(p.getDocumentCollection(), p.getDocumentClassCollection(), "^$", boundarySize, reducedRows, "");

			return results;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}


}

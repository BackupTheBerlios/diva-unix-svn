package org.kermeta.smartadapters.drools.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.kermeta.smartadapters.drools.utils.TreeIterable;

public class SmartAdaptersDrools {

	public static void weaveConfiguration(String baseURI, String configURI, ArrayList<String> aspectsURI) {
		String params[] = new String[3];
		params[0] = baseURI;
		params[1] = configURI;
		for (int i=0; i<aspectsURI.size(); i++) {
			params[2] = aspectsURI.get(i);
			main(params);
			params[0] = params[1];
		}
		//main(params);
	}

	/**
	 * @param args
	 * arg[0] = URI to the base model
	 * arg[1] = URI to the woven model
	 * arg[2] = URI to the aspect (drl file)
	 */
	public static void main(String[] args){		
		if(args.length == 3){
			String artURI = args[0];
			String wovenArtURI = args[1];
			String aspectURI = args[2];

			System.out.println("base: "+artURI);
			System.out.println("aspect: "+aspectURI);
			System.out.println("woven: "+wovenArtURI);

			SmartAdaptersDrools sad = new SmartAdaptersDrools();

			System.out.println("Loading base model...");
			sad.loadArtModel(artURI);
			System.out.println("Done!");
			System.out.println();


			System.out.println("Processing aspect ");
			sad.process(aspectURI);
			System.out.println("Done!");

			System.out.println();
			System.out.println("Saving woven model...");
			sad.saveArtModel(wovenArtURI, sad.resource);
			System.out.println("Done!");

			artURI = wovenArtURI;
		} else if (args.length >= 3){
			String artURI = args[0];
			String wovenArtURI = args[1];

			for(int i = 2; i<args.length; i++){
				String aspectURI = args[i];

				System.out.println("base: "+artURI);
				System.out.println("aspect: "+aspectURI);
				System.out.println("woven: "+wovenArtURI);

				SmartAdaptersDrools sad = new SmartAdaptersDrools();

				System.out.println("Loading base model...");
				sad.loadArtModel(artURI);
				System.out.println("Done!");
				System.out.println();


				System.out.println("Processing aspect ");
				sad.process(aspectURI);
				System.out.println("Done!");

				System.out.println();
				System.out.println("Saving woven model...");
				sad.saveArtModel(wovenArtURI, sad.resource);
				System.out.println("Done!");

				artURI = wovenArtURI;
			}
		}
	}

	private org.eclipse.emf.ecore.resource.Resource resource;
	private KnowledgeBase kbase;

	public SmartAdaptersDrools(){
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
	}

	void process(String drl){	
		//KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newFileResource(drl), ResourceType.DRL);

		if ( kbuilder.hasErrors() ) {
			java.lang.System.err.println( kbuilder.getErrors().toString() );
		}
		kbase.getKnowledgePackages().clear();
		kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
		kbuilder.getKnowledgePackages().clear();

		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();


		java.util.Map<String,EObject> uniqueobjects = new java.util.HashMap<String,EObject>();
		ksession.setGlobal("uniqueobjects", uniqueobjects);

		java.util.Map<java.util.Map<String,EObject>,java.util.Map<String,EObject>> perRole = new HashMap<Map<String,EObject>, Map<String,EObject>>();
		ksession.setGlobal("perRole", perRole);

		java.util.Map<java.util.Set<EObject>, java.util.Map<String,EObject>> perElem = new java.util.HashMap<Set<EObject>, Map<String,EObject>>();
		ksession.setGlobal("perElem", perElem);


		ksession.execute(new TreeIterable<EObject>(resource.getAllContents()));	
	}

	art.System loadArtModel(String uri){
		ResourceSetImpl rs = new ResourceSetImpl(); 
		org.eclipse.emf.ecore.resource.Resource.Factory.Registry f = rs.getResourceFactoryRegistry();

		java.util.Map<String,Object> m = f.getExtensionToFactoryMap();
		m.put("ecore",new EcoreResourceFactoryImpl());
		m.put("*",new XMIResourceFactoryImpl());

		rs.getPackageRegistry().put(art.ArtPackage.eNS_URI, art.ArtPackage.eINSTANCE);

		URI uri1  = URI.createURI(uri);//.replace("platform:/resource/",EcorePackages.workspaceURI).replace("platform:/plugin/",EcorePackages.pluginURI ));
		resource = rs.getResource(uri1,true);

		art.System s = (art.System) resource.getContents().get(0);
		return s;
	}

	void saveArtModel(String uri, Resource r){
		ResourceSetImpl rs = new ResourceSetImpl(); 
		org.eclipse.emf.ecore.resource.Resource.Factory.Registry f = rs.getResourceFactoryRegistry();

		java.util.Map<String,Object> m = f.getExtensionToFactoryMap();
		m.put("ecore",new EcoreResourceFactoryImpl());
		m.put("*",new XMIResourceFactoryImpl());


		rs.getPackageRegistry().put(art.ArtPackage.eNS_URI, art.ArtPackage.eINSTANCE);

		URI uri1  = URI.createURI(uri);
		r.setURI(uri1);

		try {
			r.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

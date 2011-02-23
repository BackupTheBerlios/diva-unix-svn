package diva.sca.extension.jobs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.JavaInterface;
import org.eclipse.stp.sca.Multiplicity;
import org.eclipse.stp.sca.impl.ComponentImpl;

public class CreateBundleProject extends Job {

	ComponentImpl bundleComponent;

	String BundleQualifiedName;
	String bundleName;
	String componentImplClass;
	StringBuilder importPackage = new StringBuilder();
	StringBuilder exportPackage = new StringBuilder();
	Set<String> exportedServices = new HashSet<String>();
	Set<String> importedServices = new HashSet<String>();

	public CreateBundleProject(String name) {
		super(name);
		setUser(true);
	}

	private void populateParameters() {
		this.setBundleName(bundleComponent.getConstrainingType().toString());
		this.BundleQualifiedName = "tutorial.diva." + bundleName;
		componentImplClass = ((JavaImplementation) bundleComponent.getImplementation()).getClass_();
		for (ComponentService serv : bundleComponent.getService()) {
			try {
				exportedServices.add(((JavaInterface) serv.getInterface()).getInterface());
			} catch (ClassCastException e) {
				exportedServices.add("tutorial.diva.service." + serv.getName());
			}
		}
		for (ComponentReference serv : bundleComponent.getReference()) {
			try {
				importedServices.add(((JavaInterface) serv.getInterface()).getInterface());
			} catch (ClassCastException e) {
				importedServices.add("tutorial.diva.service." + serv.getName());
			}
		}



		Iterator<String> it = importedServices.iterator();
		if(!it.hasNext())
			importPackage.append("\n");
		while(it.hasNext()){
			String s = it.next();
			if (s.contains(".")) {
				s = s.substring(0, s.lastIndexOf("."));
			}	
			if (it.hasNext())
				importPackage.append(",\n " + s);
			else
				importPackage.append(",\n " + s + "\n");
		}


		it = exportedServices.iterator();
		while(it.hasNext()){
			String s = it.next();
			if (s.contains(".")) {
				s = s.substring(0, s.lastIndexOf("."));
			}
			if(it.hasNext())
				exportPackage.append(s+",\n");
			else
				exportPackage.append(s);
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		this.populateParameters();
		//IProgressMonitor progressMonitor = new NullProgressMonitor();

		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(this.getBundleName());
	
		try {
			project.delete(true, null);
			project.create(null);
			project.open(null);

			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID,"org.eclipse.pde.PluginNature"  });
			project.setDescription(description, null);

			IJavaProject javaProject = JavaCore.create(project);

			IFolder binFolder = project.getFolder("bin");
			binFolder.delete(true, null);
			binFolder.create(false, true, null);
			javaProject.setOutputLocation(binFolder.getFullPath(), null);

			List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
			/*IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
			LibraryLocation[] locations = JavaRuntime
					.getLibraryLocations(vmInstall);
			for (LibraryLocation element : locations) {
				entries.add(JavaCore.newLibraryEntry(element
						.getSystemLibraryPath(), null, null));
			}*/

			entries.add(JavaCore.newContainerEntry(new Path(
			"org.eclipse.jdt.launching.JRE_CONTAINER")));
			entries.add(JavaCore.newContainerEntry(new Path(
			"org.eclipse.pde.core.requiredPlugins")));

			// add libs to project class path
			javaProject.setRawClasspath(entries
					.toArray(new IClasspathEntry[entries.size()]), null);

			IFolder sourceFolder = project.getFolder("src");
			sourceFolder.delete(true, null);
			sourceFolder.create(false, true, null);

			IPackageFragmentRoot rootPack = javaProject
			.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(rootPack
					.getPath());
			javaProject.setRawClasspath(newEntries, null);

			IPackageFragment packfactory = rootPack.createPackageFragment(
					"factory", false, null);
			packfactory.createCompilationUnit("Factory.java", createFactory(),
					false, null);
			packfactory.createCompilationUnit("Activator.java",
					createActivator(), false, null);

			this.createInterfaces(rootPack);

			this.createImplementationClass(rootPack);

			IFolder metainf_folder = project.getFolder("META-INF");
			metainf_folder.create(false, true, null);

			IFile manifest = metainf_folder.getFile("MANIFEST.MF");

			String s = this.createManifest();
			manifest.create(new ByteArrayInputStream(s.getBytes()), true, null);

		} catch (CoreException e) {
			e.printStackTrace();
		}

		IStatus res = new Status(IStatus.OK, "Diva-Studio-Plugin", "Bundle "
				+ bundleName + " successfully instantiated");
		return res;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		if(bundleName.contains("}")){
			this.bundleName = bundleName.split("}")[1];
		}
		else{
			this.bundleName = bundleName;
		}
	}

	public ComponentImpl getBundleComponent() {
		return bundleComponent;
	}

	public void setBundleComponent(ComponentImpl bundleComponent) {
		this.bundleComponent = bundleComponent;
	}

	public String createManifest() {
		StringTemplateGroup group = new StringTemplateGroup("myGroup");
		StringTemplate manifest = group.getInstanceOf("template/MANIFEST");
		manifest.setAttribute("BundleName", bundleName);
		manifest.setAttribute("BundleQualifiedName", BundleQualifiedName);
		manifest.setAttribute("importPackage", importPackage);
		manifest.setAttribute("exportPackage", exportPackage);
		return manifest.toString();

	}

	public String createFactory() {
		StringTemplateGroup group = new StringTemplateGroup("myGroup");
		StringTemplate factory = group.getInstanceOf("template/Factory");
		factory.setAttribute("ComponentImplClass", componentImplClass);
		return factory.toString();

	}

	public String createActivator() {
		StringTemplateGroup group = new StringTemplateGroup("myGroup");
		StringTemplate activator = group.getInstanceOf("template/Activator");
		activator.setAttribute("BundleName", bundleName);
		return activator.toString();

	}

	private void createInterfaces(IPackageFragmentRoot proot) {
		Set<String> interfaces = new HashSet<String>();
		interfaces.addAll(exportedServices);
		interfaces.addAll(importedServices);

		for (String interf : interfaces) {
			String packagename = "";
			String interfaceName;
			IPackageFragment pack = null;

			if (interf.contains(".")) {
				packagename = interf.substring(0, interf.lastIndexOf("."));
				interfaceName = interf.substring(interf.lastIndexOf(".") + 1,
						interf.length());
				StringTokenizer token = new StringTokenizer(packagename, ".");
				String tok = "";

				while (token.hasMoreTokens()) {
					IPackageFragment packnew = null;
					tok = tok + token.nextToken();
					packnew = proot.getPackageFragment(tok);
					if (!packnew.exists()) {
						try {
							packnew = proot.createPackageFragment(tok, false, null);
						} catch (JavaModelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					tok = tok + ".";
					pack = packnew;

				}

			} else {
				pack = proot.getPackageFragment("tutorial.diva.services");

				interfaceName = interf;
			}
			StringBuilder buffer = new StringBuilder();
			buffer.append("package " + pack.getElementName() + ";\n");
			buffer.append("\n");
			buffer.append(getDiVAHeaders());
			buffer.append("public interface " + interfaceName
					+ "{\n \t//TODO define method \n}");

			//System.out.println(buffer.toString());

			try {
				pack.createCompilationUnit(interfaceName + ".java", buffer.toString(), false, null);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void createImplementationClass(IPackageFragmentRoot proot) {
		String packagename = "";
		String className;
		IPackageFragment pack = null;

		if (componentImplClass.contains(".")) {
			packagename = componentImplClass.substring(0, componentImplClass
					.lastIndexOf("."));
			className = componentImplClass.substring(componentImplClass
					.lastIndexOf(".") + 1, componentImplClass.length());
			StringTokenizer token = new StringTokenizer(packagename, ".");
			String tok = "";

			while (token.hasMoreTokens()) {
				IPackageFragment packnew = null;
				tok = tok + token.nextToken();
				packnew = proot.getPackageFragment(tok);
				if (!packnew.exists()) {
					try {
						packnew = proot.createPackageFragment(tok, false, null);
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				tok = tok + ".";
				pack = packnew;

			}

		} else {
			pack = proot.getPackageFragment("tutorial.diva.impl");

			className = componentImplClass;
		}

		boolean importHashMap = false;
		for (ComponentReference ref : bundleComponent.getReference()) {
			importHashMap = ref.getMultiplicity() == Multiplicity._0N || ref.getMultiplicity() == Multiplicity._1N;
			if (importHashMap)
				break;
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append("package " + pack.getElementName() + ";\n");
		buffer.append("\n");
		if(importHashMap)
			buffer.append("import java.util.HashMap;\n\n");
		buffer.append(getDiVAHeaders());
		buffer.append("public class " + className );
		exportedServices.add("eu.diva.osgi.component.DiVAComponentOSGi");
		if (exportedServices.size()>0)
			buffer.append( " implements ");
		for (String ex : exportedServices) {
			buffer.append(ex + ",");
		}
		if (exportedServices.size()>0)
			buffer.deleteCharAt(buffer.length() - 1);

		buffer.append("{\n\n ");
		buffer.append("\t/* The following (generated) code deal with binding and unbinding the ports of the component */\n\n");

		buffer.append("\n\tprivate String instanceName;");
		buffer.append("\n\tpublic String getInstanceName(){\n\t\treturn instanceName;\n\t }");
		buffer.append("\n\tpublic void setInstanceName(String instanceName){\n\t\tthis.instanceName = instanceName;\n\t }");
		
		buffer.append("\n\t private BundleContext context;");
		buffer.append("\n\t private void setContext(BundleContext context){\n\t\t this.context = context;\n\t}");
		buffer.append("\n\t private BundleContext getContext(){\n\t\t return context;\n\t}");
		
		buffer.append("\n\t public void start(){}");
		buffer.append("\n\t public void stop(){}");
		
		for (ComponentReference ref : bundleComponent.getReference()) {
			String interfaceclass;
			try {
				interfaceclass = ((JavaInterface) ref.getInterface())
				.getInterface();
				if (!interfaceclass.contains("."))
					interfaceclass = "tutorial.diva.service."
						+ ((JavaInterface) ref.getInterface())
						.getInterface();

			} catch (ClassCastException e) {
				interfaceclass = "tutorial.diva.service." + ref.getName();
				continue;
			}

			if (ref.getMultiplicity().equals(Multiplicity._0N) || ref.getMultiplicity().equals(Multiplicity._1N)){
				buffer.append("\tprivate HashMap<String, " + interfaceclass + "> " + ref.getName()+" = new HashMap<String, " + interfaceclass + ">()"+ ";\n\n");

				buffer.append("\tpublic void set"+ref.getName().substring(0, 1).toUpperCase()+ref.getName().substring(1)+"(String id, "+interfaceclass+" server){\n");
				buffer.append("\t\t"+ref.getName()+".put(id, server);\n\t}\n\n");

				buffer.append("\tpublic void unset"+ref.getName().substring(0, 1).toUpperCase()+ref.getName().substring(1)+"(String id){\n");
				buffer.append("\t\t"+ref.getName()+".remove(id);\n\t}\n\n");

			}
			else{
				buffer.append("\tprivate " + interfaceclass + " " + ref.getName()+ ";\n\n");

				buffer.append("\tpublic void set"+ref.getName().substring(0, 1).toUpperCase()+ref.getName().substring(1)+"("+interfaceclass+" server){\n");
				buffer.append("\t\t"+ref.getName()+" = server;\n\t}\n\n");
			}
		}

		buffer.append("\n\t/* End of generated code. You can now implement the business logic of the component");
		if (exportedServices.size()>0){
			buffer.append("\n\t * (Quick Fix: Add Unimplemented Method)");
			buffer.append("\n\t */");
		}
		else{
			buffer.append("\n\t * This component implements no interface");
			buffer.append("\n\t */");
		}


		/**
		 * This (incomplete) code fragment is supposed to "add unimplemented methods"
		 * It does not work because it is not possible to load a class (or an interface) not yet compiled...
		 */
		/*for (String ex : exportedServices) {
			try {
				Class<?> cl = Class.forName(ex);
				for(Method m : cl.getDeclaredMethods()){
					buffer.append("public " + m.getReturnType().getCanonicalName() + m.getName() + "(");

					buffer.append(")");
				}	

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}*/

		buffer.append("\n} ");


		try {
			pack.createCompilationUnit(className + ".java", buffer.toString(), true, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getDiVAHeaders(){
		String headers = "/**\n";
		headers += "* This file was generated using DiVA Studio.\n";
		headers += "* Visit http://www.ict-diva.eu/ for more details about DiVA.\n";
		headers += "*/\n";
		return headers;
	}
}

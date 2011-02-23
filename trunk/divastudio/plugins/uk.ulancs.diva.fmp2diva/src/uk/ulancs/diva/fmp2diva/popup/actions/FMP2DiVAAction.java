package uk.ulancs.diva.fmp2diva.popup.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import uk.ulancs.diva.fmp2diva.MyExpressionValidator;
import uk.ulancs.diva.metamodel.Dimension;
import uk.ulancs.diva.metamodel.Property;
import uk.ulancs.diva.metamodel.Variant;
import uk.ulancs.diva.metamodel.Constraint;

import bin.ca.uwaterloo.gp.fmp.util.FmpResourceImpl;

import bin.ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.FmpFactory;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionBuilder;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionNode;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionParser;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionScanner;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionParser.ExpressionParseException;
import ca.uwaterloo.gp.fmp.constraints.expression.ui.ExpressionValidator;
import ca.uwaterloo.gp.fmp.impl.ConstraintImpl;
import ca.uwaterloo.gp.fmp.impl.FeatureImpl;
import ca.uwaterloo.gp.fmp.impl.NodeImpl;
import ca.uwaterloo.gp.fmp.system.IdTable;
import ca.uwaterloo.gp.fmp.system.NodeIdDictionary;
import ca.uwaterloo.gp.fmp.system.RoleQuery;

import diva.ContextExpression;
import diva.DivaFactory;
import diva.VariabilityModel;
import diva.VariantExpression;
//import diva.parser.DivaExpressionParser;

import bin.ca.uwaterloo.gp.fmp.util.FmpExternalLoader;

public class FMP2DiVAAction implements IObjectActionDelegate {

	private Shell shell;
	
	private IFile fmpFile;
	
	private Feature model;
	
	private List<Dimension> dimensions;
	
	private List<Variant> variants;
	
	private Hashtable<String,List<Property>> properties;
	
	private Hashtable<String,List<Constraint>> constraints;
	
	private Hashtable<String,List<String>> allocatedIDs;
	
	
	/**
	 * Constructor for Action1.
	 */
	public FMP2DiVAAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}
	
	public String checkID(String id){
		return id;
//		List<String> ids= allocatedIDs.get(id);
//		if(ids==null||ids.size()==0){
//			List<String> newList= new ArrayList<String>();
//			newList.add(id);
//			allocatedIDs.put(id, newList);
//			return id;
//		}else{
//			String num= (new Integer(ids.size())).toString();
//			ids.add(id+num);
//			allocatedIDs.put(id, ids);
//			return id+num;
//		}
 	}
	
	public String createID(String name){
		String id= "";
		if(name==null)
			return checkID(id);
		for(int i=0;i<name.length();i++){
			if(Character.isUpperCase(name.charAt(i))){
				id= id+name.charAt(i);
			}
		}
		
		if(id.length()>1){
			return checkID(id);
		}else if(name.length()>3){
			return checkID(name.substring(0,3).toUpperCase());
		}else{
			return checkID(name.toUpperCase());
		}
		
	
	}
	
	public List<Property> parseIndividualProperties(List<String> propertiesList){
		List<Property> individualProperties= new ArrayList<Property>();
		
		for(Iterator<String> it= propertiesList.iterator();it.hasNext();){
			String property= it.next();
			String propertyName= property.substring(0,property.indexOf("="));
			String propertyValue= property.substring(property.indexOf("=")+1,property.length());
			individualProperties.add(new Property(propertyName,(new Integer(propertyValue)).intValue(),createID(propertyName)));
		}
		return individualProperties;
	}
	
	public List<Property> parseProperties(String properties){
		List<String> propertiesStringList= new ArrayList<String>();
		if(properties!=null){
		StringTokenizer tokenizer= new StringTokenizer(properties, ";");
		
		
		if(!tokenizer.hasMoreTokens()&&!properties.equals("")){
		}else if(!properties.equals("")){
			while(tokenizer.hasMoreTokens()){
				propertiesStringList.add(tokenizer.nextToken());
			}
		}
		}
		return parseIndividualProperties(propertiesStringList);
	}
	
	public List<Property> getProperties(Node n){
		Feature properties= n.getProperties();

		
		Object property= ((Feature)properties.getChildren().get(2)).eAdapters().get(0).getTarget();
		if(property instanceof TypedValue){
			return parseProperties(((TypedValue)property).getStringValue());
		}else{
			return new ArrayList<Property>();
		}
	}
	
	public Constraint parseConstraint(String constraintText){
		String constraintName= constraintText.substring(0,constraintText.indexOf("="));
		Constraint constraint= new Constraint(constraintName,createID(constraintName),constraintText);
		
		String values= constraintText.substring(constraintText.indexOf("=")+1, constraintText.length());
		
		StringTokenizer tokenizer= new StringTokenizer(values,",");
		while(tokenizer.hasMoreTokens()){
			constraint.addValue(tokenizer.nextToken());
		}
		
		return constraint;
	}
	
	public boolean isRule(Constraint constraint){
		if(constraint.getValues().size()==1){
			return true;
		}else{
			return false;
		}
	}
	
	public List<Constraint> identifyConstraints(List<Constraint> constraints){
		List<Constraint> processedConstraints= new ArrayList<Constraint>();
		
		for(Iterator<Constraint> it= constraints.iterator(); it.hasNext();){
			Constraint constraint= it.next();
			List<String> values= constraint.getValues();
			
			if(isRule(constraint)){
				constraint.setType(Constraint.RULE);
			}
			
			if(values.size()==2){
				if((values.get(0).toLowerCase().equals("true")||values.get(0).toLowerCase().equals("false"))&&(values.get(1).toLowerCase().equals("true")||values.get(1).toLowerCase().equals("false"))){
					constraint.setType(Constraint.BOOL);
				}
			}
			
			if(constraint.getType()==-1){
				constraint.setType(Constraint.ENUM);
			}
			processedConstraints.add(constraint);
		}
		
		return processedConstraints;
	}
	
	public String parseDependencyConstraint(String dependency){
		String finalDependency= dependency.replaceAll("!", "not ");
		
		for(Iterator<Dimension> it=dimensions.iterator();it.hasNext();){
			Dimension dimension= it.next();
			finalDependency= finalDependency.replaceAll(dimension.getType(), dimension.getId());
		}
		
		for(Iterator<Variant> it=variants.iterator();it.hasNext();){
			Variant variant= it.next();
			if(variant.getType()!=null)
				finalDependency= finalDependency.replaceAll(variant.getType(), variant.getId());
		}
		
		return finalDependency;
	}
	
	public String getFeature(String constraint){
		if(constraint.startsWith("[")){
			StringTokenizer token= new StringTokenizer(constraint,"[]");
			return token.nextToken();
		}
		return constraint;
	}
	
	public String getConstraint(String constraint){
		if(constraint.startsWith("[")){
			StringTokenizer token= new StringTokenizer(constraint,"[]");
			token.nextToken();
			return token.nextToken().trim();
		}
		return constraint;
	}
	
	@SuppressWarnings("unchecked")
	public List<Constraint> getConstraints(Node n,Node root){
		
		List<Constraint> constraints= new ArrayList<Constraint>();


		if (n instanceof Feature){
			EList<ca.uwaterloo.gp.fmp.Constraint> elist= ((Feature)n).getConstraints();
			if(elist.size()==0){
				elist= new BasicEList<ca.uwaterloo.gp.fmp.Constraint>();
				for(Iterator it=((Feature)n).eAdapters().iterator();it.hasNext();){
					Object object= ((EContentAdapter)it.next()).getTarget();
					if(object instanceof ca.uwaterloo.gp.fmp.Constraint){
						if(!elist.contains((ca.uwaterloo.gp.fmp.Constraint)object))
						elist.add((ca.uwaterloo.gp.fmp.Constraint)object);
					}
				}
				if(completeConstraints.get(getType(n))!=null){
				for(Iterator<ca.uwaterloo.gp.fmp.Constraint> it=completeConstraints.get(getType(n)).iterator();it.hasNext();){
					ca.uwaterloo.gp.fmp.Constraint object= it.next();
					if(!elist.contains(object))
						elist.add(object);
				}
				}
				
			}
			for(Iterator<ca.uwaterloo.gp.fmp.Constraint> it= elist.iterator();it.hasNext();){
				ca.uwaterloo.gp.fmp.Constraint fmpconstraint= it.next();
				IdTable idtable= new IdTable();
				idtable.initialize(root);
				MyExpressionValidator validator = new MyExpressionValidator(idtable);
				String feature="";
				String constraint=fmpconstraint.getText();
				if(fmpconstraint.getText().startsWith("[")){
					feature= getFeature(constraint);
					constraint= getConstraint(constraint);
				}
				if(feature.equals("")||(getType(n).equals(feature))){
				String validation = validator.isValid(constraint);
				if(validation==null){
					ca.uwaterloo.gp.fmp.Constraint temp = FmpFactory.eINSTANCE.createConstraint();
					temp.setText(constraint);
					
					ExpressionNode expr = ((ConstraintImpl) temp).getExpression();
					String expression= expr.reduce().toString();
					Constraint cons= new Constraint(getType(n),createID(getType(n)),expression);
					cons.setType(Constraint.DEPENDENCY);
					constraints.add(cons);
				}else{
					constraints.add(parseConstraint(constraint));
				}
				}else if(!feature.equals("")){
					storeConstraint(feature, fmpconstraint);
				}
				
			}
		}
		return identifyConstraints(constraints);
	}
	
	private Hashtable<String, List<ca.uwaterloo.gp.fmp.Constraint>> completeConstraints= new Hashtable<String,List<ca.uwaterloo.gp.fmp.Constraint>>();
 	
	public void storeConstraint(String feature, ca.uwaterloo.gp.fmp.Constraint constraint){
		List<ca.uwaterloo.gp.fmp.Constraint> constraints= completeConstraints.get(feature);
		if(constraints==null){
			constraints= new ArrayList<ca.uwaterloo.gp.fmp.Constraint>();
		}else
			completeConstraints.remove(feature);
		if(!constraints.contains(constraint))
			constraints.add(constraint);
		completeConstraints.put(feature, constraints);
	}
	
	public String getType(Node n){
Feature properties= n.getProperties();
		
		
		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(1).getTarget();
		if(property instanceof TypedValue){
			return ((TypedValue)property).getStringValue();
		}else{
			return null;
		}
	}
	
	public String getName(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(0).getTarget();
		if(property instanceof TypedValue){
			return ((TypedValue)property).getStringValue();
		}
		}
		return null;
	}
	
	public boolean isNodeAVariant(Node n){
		int min= ((Feature)n.getProperties().getChildren().get(4)).getTypedValue().getIntegerValue().intValue();

		if(n.getChildren().isEmpty()&&min==0){
			return true;
		}
		else
			return false;
	}
	
	public boolean isNodeADimension(Node n){
		EList list= n.getChildren();
		for(Iterator it= list.iterator();it.hasNext();){
			Object child= it.next();
			if(child instanceof Node){
				Node node= (Node)child;
				if(node.getChildren().isEmpty())
					return true;
			}
		}
		return false;
	}
	
	public void processSoftGoals(EList l,String owner,Node root){
		List<Property> properties= new ArrayList<Property>();
		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			if(child instanceof Node){
				Node node= (Node)child;
				String nodeName= getName(node);
				String nodeType= getType(node);

				if(nodeName!=null&&!nodeName.equals("Adaptation")){
					Node goal= (Node)node.getChildren().get(0);
					String goalName= getName(goal);
					System.out.println("Goal name: "+goalName);
					int value =1;
					if(goalName.equals("network-usage"))
						value=0;
					Property property= new Property(goalName,value,createID(goalName));
					properties.add(property);
				}
			}
		}
		this.properties.put(owner,properties);
	}
		
	
	public void FMPIterator(EList l,String owner,Node root){
		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			if(child instanceof Node){
			Node node= (Node)child;
			String nodeName= getName(node);
			String nodeType= getType(node);

			if(nodeName!=null){
			properties.put(nodeName,getProperties(node));

			constraints.put(nodeName,getConstraints(node,root));
			}

			if(!nodeName.startsWith("softgoal")){
			if(isNodeADimension(node)){
				if(nodeName.startsWith("group")){
					nodeName=owner;
				}
				Dimension dimension= new Dimension(nodeName,createID(nodeName),nodeType);
				dimensions.add(dimension);
			}
			
			if(!nodeType.startsWith("group")&&isNodeAVariant(node)){
				Variant variant= new Variant(nodeName,owner,createID(nodeName),nodeType);
				variants.add(variant);
			}
			if(node!=null){
				FMPIterator(node.getChildren(),nodeName,root);
			}
			}else{
				System.out.println("processing soft goals");
				processSoftGoals(node.getChildren(),nodeName,root);
			}
			}
			

		}
	}
	
	public String convertToIDs(String expression){
		String newExpression= expression;
		Enumeration<List<Constraint>> enumConstraints= constraints.elements();
		while(enumConstraints.hasMoreElements()){
			List<Constraint> constraintsList=enumConstraints.nextElement();
		for(Iterator<Constraint> it=constraintsList.iterator();it.hasNext();){
			Constraint constraint= it.next();
			if(constraint.getType()!=Constraint.RULE){
				newExpression= newExpression.replaceAll(constraint.getName(), constraint.getId());
			}
			if(constraint.getType()==Constraint.ENUM){
				for(Iterator<String> it2=constraint.getValues().iterator();it2.hasNext();){
					String value=it2.next();
					newExpression=newExpression.replaceAll(value, createID(value));
				}
			}
		}
		}
		return newExpression;
	}

	public diva.Variant addConstraints(diva.Variant v,String dimension){
		if(v.getName()!=null){
		List<Constraint> constraintsList=constraints.get(dimension);
		
		
		for(Iterator<Constraint> it= constraintsList.iterator();it.hasNext();){
       	 Constraint constraint= it.next();
       	 if(constraint.getType()==Constraint.DEPENDENCY){
       		 VariantExpression expr= DivaFactory.eINSTANCE.createVariantExpression();
       		 expr.setText(parseDependencyConstraint(constraint.getRaw()));
       		 v.setDependency(expr);
       	 }else if(constraint.getType()==Constraint.RULE){
       		ContextExpression expr= DivaFactory.eINSTANCE.createContextExpression();
      		 expr.setText(parseDependencyConstraint(convertToIDs(constraint.getRaw())));
      		 v.setAvailable(expr);
       	 }
       	 
        }
		}
		return v;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		
		variants= new ArrayList<Variant>();
		dimensions= new ArrayList<Dimension>();
		properties= new Hashtable<String,List<Property>>();
		constraints= new Hashtable<String,List<Constraint>>();
		allocatedIDs= new Hashtable<String,List<String>>();
		FmpExternalLoader fmp = new FmpExternalLoader();
		fmp.load(fmpFile);
		
		FmpResourceImpl fmpresource= (FmpResourceImpl)fmp.getResources().get(0);
		Project project= (Project) fmpresource.getAllContents().next();
		
		model= project.getModel();		
		
		NodeIdDictionary.INSTANCE.visit(null, model);
		Node node= model.getDescribedNode();
		FMPIterator(model.getChildren(),model.getName(),model);
		
		
		
		
		
		// java.io.File XMIFile = new java.io.File(XMIFilePathToSave);

         // Create a resource set.
         ResourceSet resourceSet = new ResourceSetImpl();
         

         // Register the default resource factory -- only needed for stand-alone!
         resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
         .put(Resource.Factory.Registry.DEFAULT_EXTENSION,
                         new XMIResourceFactoryImpl());

         // Get the URI of the model file.
         URI fileURI = URI.createFileURI(fmpFile.getLocationURI().getRawPath()+ ".diva");

         // Create a resource for this file.
         Resource resource = resourceSet.createResource(fileURI);
         

         // Add the system to the contents.
         diva.VariabilityModel divamodel = DivaFactory.eINSTANCE.createVariabilityModel();
         
         
        diva.BaseModel basemodel= DivaFactory.eINSTANCE.createBaseModel();
        
        divamodel.setModel(DivaFactory.eINSTANCE.createBaseModel());
        
         
         for(Iterator<Dimension> it=dimensions.iterator();it.hasNext();){
 			 Dimension dimension= it.next();
        	 String dimensionName= dimension.getName();
        	 diva.Dimension divadimension= DivaFactory.eINSTANCE.createDimension();
        	 for(Iterator<Variant> it2=variants.iterator();it2.hasNext();){
        		 Variant v= it2.next();
        		 if(dimensionName.equals(v.getDimension())){
        			 diva.Variant variant= DivaFactory.eINSTANCE.createVariant();
        			 
        			 variant.setModel(DivaFactory.eINSTANCE.createAspectModel());
        			 variant.setName(v.getName());
        			 variant.setId(v.getId());
        			 variant.setAvailable(DivaFactory.eINSTANCE.createContextExpression());
        			 variant.setDependency(DivaFactory.eINSTANCE.createVariantExpression());
        			 variant.setRequired(DivaFactory.eINSTANCE.createContextExpression());
        			 
        			 
        			 
        			 divadimension.getVariant().add(addConstraints(variant,v.getName()));
        		 }
        	 }
        	 
        	 divadimension.setName(dimension.getName());
        	 divadimension.setId(dimension.getId());
         
        	 divamodel.getDimension().add(divadimension);
        	
         }
         
         Enumeration<List<Property>> enumProperties= properties.elements();
 		while(enumProperties.hasMoreElements()){
 			List<Property> propertiesList=enumProperties.nextElement();
         for(Iterator<Property> it=propertiesList.iterator();it.hasNext();){
        	 Property property= it.next();
        	 diva.Property divaProperty= DivaFactory.eINSTANCE.createProperty();
        	 divaProperty.setName(property.getProperty());
        	 divaProperty.setId(property.getId());
        	 divaProperty.setDirection(property.getValue());
        	 
        	 divamodel.getProperty().add(divaProperty);
         }
 		}
 		Enumeration<List<Constraint>> enumConstraints= constraints.elements();
		while(enumConstraints.hasMoreElements()){
			List<Constraint> constraintsList=enumConstraints.nextElement();
         for(Iterator<Constraint> it= constraintsList.iterator();it.hasNext();){
           	 Constraint constraint= it.next();
           	 if(constraint.getType()==Constraint.RULE){
           		 diva.PriorityRule divaRule= DivaFactory.eINSTANCE.createPriorityRule();
           		diva.ContextExpression exp= DivaFactory.eINSTANCE.createContextExpression();
           		exp.setText(convertToIDs(constraint.getRaw()));
           		
           		divaRule.setContext(exp);
           		divaRule.setName(constraint.getName());
           		divamodel.getRule().add(divaRule);
           		
           	
           	 }else{
           		 if(constraint.getType()==Constraint.ENUM){
           			 diva.EnumVariable constraintEnum= DivaFactory.eINSTANCE.createEnumVariable();
           			 constraintEnum.setName(constraint.getName());
           			 constraintEnum.setId(constraint.getId());
           			 for(Iterator<String> it2= constraint.getValues().iterator();it2.hasNext();){
           				 diva.EnumLiteral literal= DivaFactory.eINSTANCE.createEnumLiteral();
           				 String value= it2.next();
           				 literal.setId(createID(value));
           				 literal.setName(value);
           				 constraintEnum.getLiteral().add(literal);
           			 }
           			 divamodel.getContext().add(constraintEnum);
           		 }else if(constraint.getType()==Constraint.BOOL){
           			
           			 diva.BooleanVariable constraintBool= DivaFactory.eINSTANCE.createBooleanVariable();
           			 constraintBool.setId(constraint.getId());
           			 constraintBool.setName(constraint.getName());
           			 divamodel.getContext().add(constraintBool);
           		 }
         
           	 }
            }
		}
		resource.getContents().add(basemodel);
		resource.getContents().add(divamodel);
         

         // Save the contents of the resource to the file system.
         try {
                 resource.save(null/*Collections.EMPTY_MAP*/);
                
                 fmpFile.getProject().refreshLocal(IResource.DEPTH_INFINITE,null);
         } catch (IOException e) {
                 e.printStackTrace();
         } catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageDialog.openInformation(
			shell,
			"Fmp2diva",
			"FMP2DiVA was executed.");
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		Iterator it= ((StructuredSelection) selection).iterator();
		
		while(it.hasNext()){
			fmpFile= (IFile)it.next();
			
		}
	}

}

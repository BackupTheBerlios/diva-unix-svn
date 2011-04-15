package uk.ulancs.diva.FeatureTreeNormaliser.Normaliser;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;

import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.ValueType;
import ca.uwaterloo.gp.fmp.impl.FeatureGroupImpl;
import ca.uwaterloo.gp.fmp.impl.TypedValueImpl;
import ca.uwaterloo.gp.fmp.system.NodeIdDictionary;
import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import ca.uwaterloo.gp.fmp.util.FmpResourceImpl;
import ca.uwaterloo.gp.fmp.FmpFactory;

public class Normaliser {
	
	public void NormaliseTree(FmpExternalLoader FileLoader) {
		System.out.println("Normalisation in progress...");
		
		FmpResourceImpl fmpresource= (FmpResourceImpl)FileLoader.getResources().get(0);

		Project project= (Project) fmpresource.getAllContents().next();
		Feature model= project.getModel();		
		NodeIdDictionary.INSTANCE.visit(null, model);
		
		NormaliseNodes(model.getChildren(),model.getName(),model);
	}
	
	public Vector<String> NormaliseNodes(EList l,String owner,Node root) {
		Vector<String> ReturnVector = new Vector<String>();
		//String AggregateDesc = "";
		
		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			
			if(child instanceof Node){
				Node node= (Node)child;
				
				if(node!=null){
					String nodeName = getName(node);
					
					if (nodeName!=null) {
						
						if (nodeName.contains("softgoal")) {
							return new Vector<String>();
						}
						else if (nodeName.contains("group")) {
							return NormaliseNodes(node.getChildren(),nodeName,root);
						}
						else {
							if (node.getChildren().size() > 0) {
								Vector<String> ChildAggregate = NormaliseNodes(node.getChildren(),nodeName,root);
								setDescription(node, ChildAggregate);
								ReturnVector = AggregateDescs(ReturnVector, ChildAggregate);
							}
							else {
								String NodeDesc = getDescription(node);
								if (NodeDesc.equals("")) {
									Vector<String> NameVector = new Vector<String>();
									NameVector.add(nodeName);
									setDescription(node, NameVector);
									NodeDesc = nodeName;
								}

								if (!Contains(ReturnVector, NodeDesc)) {
										ReturnVector.add(NodeDesc);
								}
							}
						}
					}
				}
			}
		}
		
		return ReturnVector;
	}
	
	public boolean Contains(Vector<String> TheVector, String Desc) {
		Iterator<String> VectorIt = TheVector.iterator();
		
		while (VectorIt.hasNext()) {
			String CurrentString = VectorIt.next();
			if (CurrentString.replaceAll("\\s", "").equals(Desc.replaceAll("\\s", ""))) {
				return true;
			}
		}
		
		return false;
	}
	
	public Vector<String> AggregateDescs(Vector<String> Vector1, Vector<String> Vector2) {
		Vector<String> ReturnVector = new Vector<String>();
		
		Iterator<String> Vector1It = Vector1.iterator();
		
		while (Vector1It.hasNext()) {
			ReturnVector.add(Vector1It.next());
		}
		
		Iterator<String> Vector2It = Vector2.iterator();
		
		while (Vector2It.hasNext()) {
			String CurrentString = Vector2It.next();
			if (!Contains(Vector1, CurrentString)) {
				ReturnVector.add(CurrentString);
			}
		}
		
		return ReturnVector;
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
  	
  	public String getDescription(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			Object property = ((Feature)properties.getChildren().get(3)).getChildren().get(0);
			if (property instanceof FeatureGroupImpl) {
				Feature First = (Feature)((FeatureGroupImpl)property).getChildren().get(2);
				Object Second = ((Feature)First.getChildren().get(0)).eAdapters().get(0).getTarget();
							
				if(Second instanceof TypedValue){
					return ((TypedValue)Second).getStringValue();
				}
			}
		}
		return null;
	}
  	
  	public void setDescription(Node n, Vector<String> AggVal){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			Object property = ((Feature)properties.getChildren().get(3)).getChildren().get(0);
			if (property instanceof FeatureGroupImpl) {
				Feature First = (Feature)((FeatureGroupImpl)property).getChildren().get(2);
				Object Second = ((Feature)First.getChildren().get(0)).eAdapters().get(0).getTarget();
				
				if(Second instanceof TypedValue){
					String CurrentVal = ((TypedValue)Second).getStringValue().trim();
					String Aggregate = "";
					Iterator<String> AggValIt = AggVal.iterator();
					
					while (AggValIt.hasNext()) {
						String ThisVal = AggValIt.next().trim();
						if(ThisVal.lastIndexOf(";")==ThisVal.length()-1)
							ThisVal= ThisVal.substring(0, ThisVal.length()-1);
						if (!CurrentVal.replaceAll("\\s", "").contains(ThisVal.replaceAll("\\s", ""))) {
							Aggregate += ThisVal + " ";
						}
					}
					
					Aggregate += CurrentVal;
					
					((TypedValue)Second).setStringValue(Aggregate);
					// ((Feature)First.getChildren().get(0)).eAdapters().get(0).setTarget((Notifier)Second);
					((Feature)n).setTypedValue(((TypedValue)Second));
				}
			}
		}
	}
}
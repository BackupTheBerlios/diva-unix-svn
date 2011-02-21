package uk.ulancs.diva.FeatureTreeNormaliser.Normaliser;

import java.util.Iterator;

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
	
	public String NormaliseNodes(EList l,String owner,Node root) {
		String AggregateDesc = "";
		
		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			
			if(child instanceof Node){
				Node node= (Node)child;
				
				if(node!=null){
					String nodeName = getName(node);
					
					if (nodeName!=null) {
						
						if (nodeName.contains("group")) {
							return NormaliseNodes(node.getChildren(),nodeName,root);
						}
						else {
							if (node.getChildren().size() > 0) {
								String ChildAggregate = NormaliseNodes(node.getChildren(),nodeName,root);
								setDescription(node, ChildAggregate);
								AggregateDesc += getDescription(node) + " ";
							}
							else {
								if (!AggregateDesc.contains(getDescription(node))) {
									AggregateDesc += getDescription(node) + " ";
								}
							}
						}
					}
				}
			}
		}
		
		return AggregateDesc;
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
  	
  	public String setDescription(Node n, String AggVal){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			Object property = ((Feature)properties.getChildren().get(3)).getChildren().get(0);
			if (property instanceof FeatureGroupImpl) {
				Feature First = (Feature)((FeatureGroupImpl)property).getChildren().get(2);
				Object Second = ((Feature)First.getChildren().get(0)).eAdapters().get(0).getTarget();
				
				if(Second instanceof TypedValue){
					String CurrentVal = ((TypedValue)Second).getStringValue();
					if (!AggVal.contains(CurrentVal)) {
						AggVal += " " + CurrentVal;
					}
					
					((TypedValue)Second).setStringValue(AggVal);
					// ((Feature)First.getChildren().get(0)).eAdapters().get(0).setTarget((Notifier)Second);
					((Feature)n).setTypedValue(((TypedValue)Second));
				}
			}
		}
		return AggVal;
	}
}
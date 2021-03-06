/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.mopp;

public class ArtextPrinter implements art.resource.artext.IArtextTextPrinter {
	
	protected art.resource.artext.IArtextTokenResolverFactory tokenResolverFactory = new art.resource.artext.mopp.ArtextTokenResolverFactory();
	protected java.io.OutputStream outputStream;
	/**
	 * Holds the resource that is associated with this printer. may be null if the
	 * printer is used stand alone.
	 */
	private art.resource.artext.IArtextTextResource resource;
	private java.util.Map<?, ?> options;
	
	public ArtextPrinter(java.io.OutputStream outputStream, art.resource.artext.IArtextTextResource resource) {
		super();
		this.outputStream = outputStream;
		this.resource = resource;
	}
	
	protected static int matchCount(java.util.Map<String, Integer> featureCounter, java.util.Collection<String> needed){
		int pos = 0;
		int neg = 0;
		
		for(String featureName:featureCounter.keySet()){
			if(needed.contains(featureName)){
				int value = featureCounter.get(featureName);
				if (value == 0) {
					neg += 1;
				} else {
					pos += 1;
				}
			}
		}
		return neg > 0 ? -neg : pos;
	}
	
	protected void doPrint(org.eclipse.emf.ecore.EObject element, java.io.PrintWriter out, String globaltab) {
		if (element == null) {
			throw new java.lang.IllegalArgumentException("Nothing to write.");
		}
		if (out == null) {
			throw new java.lang.IllegalArgumentException("Nothing to write on.");
		}
		
		if (element instanceof art.System) {
			print_art_System((art.System) element, globaltab, out);
			return;
		}
		if (element instanceof art.DataType) {
			print_art_DataType((art.DataType) element, globaltab, out);
			return;
		}
		if (element instanceof art.distrib.Node) {
			print_art_distrib_Node((art.distrib.Node) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.PrimitiveInstance) {
			print_art_instance_PrimitiveInstance((art.instance.PrimitiveInstance) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.CompositeInstance) {
			print_art_instance_CompositeInstance((art.instance.CompositeInstance) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.TransmissionBinding) {
			print_art_instance_TransmissionBinding((art.instance.TransmissionBinding) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.DelegationBinding) {
			print_art_instance_DelegationBinding((art.instance.DelegationBinding) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.ValuedAttribute) {
			print_art_instance_ValuedAttribute((art.instance.ValuedAttribute) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.DictionaryValuedAttribute) {
			print_art_instance_DictionaryValuedAttribute((art.instance.DictionaryValuedAttribute) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.DefaultEntry) {
			print_art_instance_DefaultEntry((art.instance.DefaultEntry) element, globaltab, out);
			return;
		}
		if (element instanceof art.instance.OtherEntry) {
			print_art_instance_OtherEntry((art.instance.OtherEntry) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.PrimitiveType) {
			print_art_type_PrimitiveType((art.type.PrimitiveType) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.CompositeType) {
			print_art_type_CompositeType((art.type.CompositeType) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.Operation) {
			print_art_type_Operation((art.type.Operation) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.Parameter) {
			print_art_type_Parameter((art.type.Parameter) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.FunctionalService) {
			print_art_type_FunctionalService((art.type.FunctionalService) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.ControlService) {
			print_art_type_ControlService((art.type.ControlService) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.Port) {
			print_art_type_Port((art.type.Port) element, globaltab, out);
			return;
		}
		if (element instanceof art.implem.FractalComponent) {
			print_art_implem_FractalComponent((art.implem.FractalComponent) element, globaltab, out);
			return;
		}
		if (element instanceof art.implem.OSGiComponent) {
			print_art_implem_OSGiComponent((art.implem.OSGiComponent) element, globaltab, out);
			return;
		}
		if (element instanceof art.group.TypeGroup) {
			print_art_group_TypeGroup((art.group.TypeGroup) element, globaltab, out);
			return;
		}
		if (element instanceof art.group.InstanceGroup) {
			print_art_group_InstanceGroup((art.group.InstanceGroup) element, globaltab, out);
			return;
		}
		if (element instanceof art.implem.OSGiType) {
			print_art_implem_OSGiType((art.implem.OSGiType) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.BasicAttribute) {
			print_art_type_BasicAttribute((art.type.BasicAttribute) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.Dictionary) {
			print_art_type_Dictionary((art.type.Dictionary) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.DictionaryDefaultValue) {
			print_art_type_DictionaryDefaultValue((art.type.DictionaryDefaultValue) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.PortId) {
			print_art_type_PortId((art.type.PortId) element, globaltab, out);
			return;
		}
		if (element instanceof art.type.PortCollection) {
			print_art_type_PortCollection((art.type.PortCollection) element, globaltab, out);
			return;
		}
		
		addWarningToResource("The printer can not handle " + element.eClass().getName() + " elements", element);
	}
	
	protected art.resource.artext.mopp.ArtextReferenceResolverSwitch getReferenceResolverSwitch() {
		return (art.resource.artext.mopp.ArtextReferenceResolverSwitch) new art.resource.artext.mopp.ArtextMetaInformation().getReferenceResolverSwitch();
	}
	
	protected void addWarningToResource(final String errorMessage, org.eclipse.emf.ecore.EObject cause) {
		art.resource.artext.IArtextTextResource resource = getResource();
		if (resource == null) {
			// the resource can be null if the printer is used stand alone
			return;
		}
		resource.addProblem(new art.resource.artext.mopp.ArtextProblem(errorMessage, art.resource.artext.ArtextEProblemType.ERROR), cause);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		this.options = options;
	}
	
	public java.util.Map<?,?> getOptions() {
		return options;
	}
	
	public art.resource.artext.IArtextTextResource getResource() {
		return resource;
	}
	
	/**
	 * Calls {@link #doPrint(EObject, PrintWriter, String)} and writes the result to
	 * the underlying output stream.
	 */
	public void print(org.eclipse.emf.ecore.EObject element) {
		java.io.PrintWriter out = new java.io.PrintWriter(new java.io.BufferedOutputStream(outputStream));
		doPrint(element, out, "");
		out.flush();
		out.close();
	}
	
	public void print_art_System(art.System element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(6);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__NODES));
		printCountingMap.put("nodes", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__SERVICES));
		printCountingMap.put("services", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__TYPES));
		printCountingMap.put("types", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__DATA_TYPES));
		printCountingMap.put("dataTypes", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__GROUPS));
		printCountingMap.put("groups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("system");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(";");
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_System_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
	}
	public void print_art_System_0(art.System element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CompoundDefinition)
		print_art_System_0_0(element, localtab, out, printCountingMap);
	}
	public void print_art_System_0_0(art.System element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		int alt = -1;
		alt=0;
		int matches=		matchCount(printCountingMap, java.util.Arrays.asList(		"nodes"		));
		int tempMatchCount;
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"services"		));
		if (tempMatchCount > matches) {
			alt = 1;
			matches = tempMatchCount;
		}
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"types"		));
		if (tempMatchCount > matches) {
			alt = 2;
			matches = tempMatchCount;
		}
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"dataTypes"		));
		if (tempMatchCount > matches) {
			alt = 3;
			matches = tempMatchCount;
		}
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"groups"		));
		if (tempMatchCount > matches) {
			alt = 4;
			matches = tempMatchCount;
		}
		switch(alt) {
			case 1:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("services");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__SERVICES));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("services", count - 1);
				}
			}
			break;
			case 2:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("types");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__TYPES));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("types", count - 1);
				}
			}
			break;
			case 3:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("dataTypes");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__DATA_TYPES));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("dataTypes", count - 1);
				}
			}
			break;
			case 4:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("groups");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__GROUPS));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("groups", count - 1);
				}
			}
			break;
			default:			// DEFINITION PART BEGINS (Containment)
			count = printCountingMap.get("nodes");
			if (count > 0) {
				Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.SYSTEM__NODES));
				java.util.List<?> list = (java.util.List<?>) o;
				int index = list.size() - count;
				if (index >= 0) {
					o = list.get(index);
				} else {
					o = null;
				}
				if (o != null) {
					doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
				}
				printCountingMap.put("nodes", count - 1);
			}
		}
	}
	
	public void print_art_DataType(art.DataType element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(1);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.DATA_TYPE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("datatype");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.ArtPackage.DATA_TYPE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.ArtPackage.DATA_TYPE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(";");
	}
	
	public void print_art_distrib_Node(art.distrib.Node element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(3);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__URI));
		printCountingMap.put("uri", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__COMPONENTS));
		printCountingMap.put("components", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("node");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_distrib_Node_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("components");
		if (count > 0) {
			java.util.List<?> list = (java.util.List<?>)element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__COMPONENTS));
			int index  = list.size() - count;
			if (index < 0) {
				index = 0;
			}
			java.util.ListIterator<?> it  = list.listIterator(index);
			while (it.hasNext()) {
				Object o = it.next();
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("components", 0);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_distrib_Node_0(art.distrib.Node element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("uri");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("uri");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__URI));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.distrib.DistribPackage.NODE__URI), element));
			}
			printCountingMap.put("uri", count - 1);
		}
	}
	
	public void print_art_instance_PrimitiveInstance(art.instance.PrimitiveInstance element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(8);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__TYPE));
		printCountingMap.put("type", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__STATE));
		printCountingMap.put("state", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__SUPER_COMPONENT));
		printCountingMap.put("superComponent", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__BINDING));
		printCountingMap.put("binding", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__IMPLEM));
		printCountingMap.put("implem", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__GROUPS));
		printCountingMap.put("groups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("primitive");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("instance");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("type");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getComponentInstanceTypeReferenceResolver().deResolve((art.type.ComponentType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__TYPE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__TYPE), element));
			}
			printCountingMap.put("type", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("state");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__STATE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("T_INSTANCE_STATE");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__STATE), element));
			}
			printCountingMap.put("state", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_instance_PrimitiveInstance_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_instance_PrimitiveInstance_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_instance_PrimitiveInstance_0(art.instance.PrimitiveInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("implementation");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("implem");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__IMPLEM));
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("implem", count - 1);
		}
	}
	public void print_art_instance_PrimitiveInstance_1(art.instance.PrimitiveInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CompoundDefinition)
		print_art_instance_PrimitiveInstance_1_0(element, localtab, out, printCountingMap);
	}
	public void print_art_instance_PrimitiveInstance_1_0(art.instance.PrimitiveInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		int alt = -1;
		alt=0;
		int matches=		matchCount(printCountingMap, java.util.Arrays.asList(		"attribute"		));
		int tempMatchCount;
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"binding"		));
		if (tempMatchCount > matches) {
			alt = 1;
			matches = tempMatchCount;
		}
		switch(alt) {
			case 1:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("binding");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__BINDING));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("binding", count - 1);
				}
			}
			break;
			default:			// DEFINITION PART BEGINS (Containment)
			count = printCountingMap.get("attribute");
			if (count > 0) {
				Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.PRIMITIVE_INSTANCE__ATTRIBUTE));
				java.util.List<?> list = (java.util.List<?>) o;
				int index = list.size() - count;
				if (index >= 0) {
					o = list.get(index);
				} else {
					o = null;
				}
				if (o != null) {
					doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
				}
				printCountingMap.put("attribute", count - 1);
			}
		}
	}
	
	public void print_art_instance_CompositeInstance(art.instance.CompositeInstance element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(10);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__TYPE));
		printCountingMap.put("type", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__STATE));
		printCountingMap.put("state", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__SUPER_COMPONENT));
		printCountingMap.put("superComponent", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__BINDING));
		printCountingMap.put("binding", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__IMPLEM));
		printCountingMap.put("implem", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__GROUPS));
		printCountingMap.put("groups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__SUB_COMPONENT));
		printCountingMap.put("subComponent", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__DELEGATION));
		printCountingMap.put("delegation", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("composite");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("instance");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("type");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getComponentInstanceTypeReferenceResolver().deResolve((art.type.ComponentType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__TYPE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__TYPE), element));
			}
			printCountingMap.put("type", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("state");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__STATE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("T_INSTANCE_STATE");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__STATE), element));
			}
			printCountingMap.put("state", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_instance_CompositeInstance_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_instance_CompositeInstance_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_instance_CompositeInstance_0(art.instance.CompositeInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("implementation");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("implem");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__IMPLEM));
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("implem", count - 1);
		}
	}
	public void print_art_instance_CompositeInstance_1(art.instance.CompositeInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CompoundDefinition)
		print_art_instance_CompositeInstance_1_0(element, localtab, out, printCountingMap);
	}
	public void print_art_instance_CompositeInstance_1_0(art.instance.CompositeInstance element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		int alt = -1;
		alt=0;
		int matches=		matchCount(printCountingMap, java.util.Arrays.asList(		"attribute"		));
		int tempMatchCount;
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"binding"		));
		if (tempMatchCount > matches) {
			alt = 1;
			matches = tempMatchCount;
		}
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"subComponent"		));
		if (tempMatchCount > matches) {
			alt = 2;
			matches = tempMatchCount;
		}
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"delegation"		));
		if (tempMatchCount > matches) {
			alt = 3;
			matches = tempMatchCount;
		}
		switch(alt) {
			case 1:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("binding");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__BINDING));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("binding", count - 1);
				}
			}
			break;
			case 2:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("subComponent");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__SUB_COMPONENT));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("subComponent", count - 1);
				}
			}
			break;
			case 3:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("delegation");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__DELEGATION));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("delegation", count - 1);
				}
			}
			break;
			default:			// DEFINITION PART BEGINS (Containment)
			count = printCountingMap.get("attribute");
			if (count > 0) {
				Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.COMPOSITE_INSTANCE__ATTRIBUTE));
				java.util.List<?> list = (java.util.List<?>) o;
				int index = list.size() - count;
				if (index >= 0) {
					o = list.get(index);
				} else {
					o = null;
				}
				if (o != null) {
					doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
				}
				printCountingMap.put("attribute", count - 1);
			}
		}
	}
	
	public void print_art_instance_TransmissionBinding(art.instance.TransmissionBinding element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(4);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER_INSTANCE));
		printCountingMap.put("serverInstance", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__ID));
		printCountingMap.put("id", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__CLIENT));
		printCountingMap.put("client", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER));
		printCountingMap.put("server", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("bind");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("client");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__CLIENT));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTransmissionBindingClientReferenceResolver().deResolve((art.type.AbstractPort) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__CLIENT)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__CLIENT), element));
			}
			printCountingMap.put("client", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("to");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("serverInstance");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER_INSTANCE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getBindingServerInstanceReferenceResolver().deResolve((art.instance.ComponentInstance) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER_INSTANCE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER_INSTANCE), element));
			}
			printCountingMap.put("serverInstance", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("::");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("server");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTransmissionBindingServerReferenceResolver().deResolve((art.type.AbstractPort) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__SERVER), element));
			}
			printCountingMap.put("server", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_instance_TransmissionBinding_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_instance_TransmissionBinding_0(art.instance.TransmissionBinding element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("(");
		// DEFINITION PART BEGINS (CsString)
		out.print("id");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("id");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__ID));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.TRANSMISSION_BINDING__ID), element));
			}
			printCountingMap.put("id", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(")");
	}
	
	public void print_art_instance_DelegationBinding(art.instance.DelegationBinding element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(4);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SERVER_INSTANCE));
		printCountingMap.put("serverInstance", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__ID));
		printCountingMap.put("id", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SOURCE));
		printCountingMap.put("source", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__EXPORTED));
		printCountingMap.put("exported", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("delegate");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("source");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SOURCE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getDelegationBindingSourceReferenceResolver().deResolve((art.type.AbstractPort) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SOURCE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SOURCE), element));
			}
			printCountingMap.put("source", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("to");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("serverInstance");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SERVER_INSTANCE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getBindingServerInstanceReferenceResolver().deResolve((art.instance.ComponentInstance) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SERVER_INSTANCE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__SERVER_INSTANCE), element));
			}
			printCountingMap.put("serverInstance", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("::");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("exported");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__EXPORTED));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getDelegationBindingExportedReferenceResolver().deResolve((art.type.AbstractPort) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__EXPORTED)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__EXPORTED), element));
			}
			printCountingMap.put("exported", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_instance_DelegationBinding_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_instance_DelegationBinding_0(art.instance.DelegationBinding element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("(");
		// DEFINITION PART BEGINS (CsString)
		out.print("id");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("id");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__ID));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.DELEGATION_BINDING__ID), element));
			}
			printCountingMap.put("id", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(")");
	}
	
	public void print_art_instance_ValuedAttribute(art.instance.ValuedAttribute element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__VALUE));
		printCountingMap.put("value", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("attribute");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__ATTRIBUTE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getValuedAttributeAttributeReferenceResolver().deResolve((art.type.BasicAttribute) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__ATTRIBUTE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__ATTRIBUTE), element));
			}
			printCountingMap.put("attribute", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("value");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__VALUE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.VALUED_ATTRIBUTE__VALUE), element));
			}
			printCountingMap.put("value", count - 1);
		}
	}
	
	public void print_art_instance_DictionaryValuedAttribute(art.instance.DictionaryValuedAttribute element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ENTRIES));
		printCountingMap.put("entries", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("attribute");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ATTRIBUTE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getDictionaryValuedAttributeAttributeReferenceResolver().deResolve((art.type.Dictionary) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ATTRIBUTE)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ATTRIBUTE), element));
			}
			printCountingMap.put("attribute", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_instance_DictionaryValuedAttribute_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_instance_DictionaryValuedAttribute_0(art.instance.DictionaryValuedAttribute element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("entries");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ENTRIES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("entries", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_instance_DictionaryValuedAttribute_0_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
	}
	public void print_art_instance_DictionaryValuedAttribute_0_0(art.instance.DictionaryValuedAttribute element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("entries");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DICTIONARY_VALUED_ATTRIBUTE__ENTRIES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("entries", count - 1);
		}
	}
	
	public void print_art_instance_DefaultEntry(art.instance.DefaultEntry element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__VALUE));
		printCountingMap.put("value", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__KEY));
		printCountingMap.put("key", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("[");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("key");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__KEY));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getDefaultEntryKeyReferenceResolver().deResolve((art.type.DictionaryDefaultValue) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__KEY)), element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__KEY), element));
			}
			printCountingMap.put("key", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("->");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("value");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__VALUE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.DEFAULT_ENTRY__VALUE), element));
			}
			printCountingMap.put("value", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("]");
	}
	
	public void print_art_instance_OtherEntry(art.instance.OtherEntry element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__VALUE));
		printCountingMap.put("value", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__KEY));
		printCountingMap.put("key", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("[");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("key");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__KEY));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__KEY), element));
			}
			printCountingMap.put("key", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("->");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("value");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__VALUE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.instance.InstancePackage.OTHER_ENTRY__VALUE), element));
			}
			printCountingMap.put("value", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("]");
	}
	
	public void print_art_type_PrimitiveType(art.type.PrimitiveType element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(5);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__PORT));
		printCountingMap.put("port", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__GROUPS));
		printCountingMap.put("groups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__IMPLEM));
		printCountingMap.put("implem", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("type");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_PrimitiveType_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_PrimitiveType_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_type_PrimitiveType_0(art.type.PrimitiveType element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("implementation");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("implem");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__IMPLEM));
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("implem", count - 1);
		}
	}
	public void print_art_type_PrimitiveType_1(art.type.PrimitiveType element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		int alt = -1;
		alt=0;
		int matches=		matchCount(printCountingMap, java.util.Arrays.asList(		"port"		));
		int tempMatchCount;
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"attribute"		));
		if (tempMatchCount > matches) {
			alt = 1;
			matches = tempMatchCount;
		}
		switch(alt) {
			case 1:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("attribute");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__ATTRIBUTE));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("attribute", count - 1);
				}
			}
			break;
			default:			// DEFINITION PART BEGINS (Containment)
			count = printCountingMap.get("port");
			if (count > 0) {
				Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PRIMITIVE_TYPE__PORT));
				java.util.List<?> list = (java.util.List<?>) o;
				int index = list.size() - count;
				if (index >= 0) {
					o = list.get(index);
				} else {
					o = null;
				}
				if (o != null) {
					doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
				}
				printCountingMap.put("port", count - 1);
			}
		}
	}
	
	public void print_art_type_CompositeType(art.type.CompositeType element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(5);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__PORT));
		printCountingMap.put("port", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__ATTRIBUTE));
		printCountingMap.put("attribute", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__GROUPS));
		printCountingMap.put("groups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__IMPLEM));
		printCountingMap.put("implem", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("compositetype");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_CompositeType_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_CompositeType_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_type_CompositeType_0(art.type.CompositeType element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("implementation");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("implem");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__IMPLEM));
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("implem", count - 1);
		}
	}
	public void print_art_type_CompositeType_1(art.type.CompositeType element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		int alt = -1;
		alt=0;
		int matches=		matchCount(printCountingMap, java.util.Arrays.asList(		"port"		));
		int tempMatchCount;
		tempMatchCount=		matchCount(printCountingMap, java.util.Arrays.asList(		"attribute"		));
		if (tempMatchCount > matches) {
			alt = 1;
			matches = tempMatchCount;
		}
		switch(alt) {
			case 1:			{
				// DEFINITION PART BEGINS (Containment)
				count = printCountingMap.get("attribute");
				if (count > 0) {
					Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__ATTRIBUTE));
					java.util.List<?> list = (java.util.List<?>) o;
					int index = list.size() - count;
					if (index >= 0) {
						o = list.get(index);
					} else {
						o = null;
					}
					if (o != null) {
						doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
					}
					printCountingMap.put("attribute", count - 1);
				}
			}
			break;
			default:			// DEFINITION PART BEGINS (Containment)
			count = printCountingMap.get("port");
			if (count > 0) {
				Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.COMPOSITE_TYPE__PORT));
				java.util.List<?> list = (java.util.List<?>) o;
				int index = list.size() - count;
				if (index >= 0) {
					o = list.get(index);
				} else {
					o = null;
				}
				if (o != null) {
					doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
				}
				printCountingMap.put("port", count - 1);
			}
		}
	}
	
	public void print_art_type_Operation(art.type.Operation element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(3);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__INPUT));
		printCountingMap.put("input", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__OUTPUT));
		printCountingMap.put("output", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("operation");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("(");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_Operation_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_Operation_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(")");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_Operation_2(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_Operation_3(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
	}
	public void print_art_type_Operation_0(art.type.Operation element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("input");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__INPUT));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("input", count - 1);
		}
	}
	public void print_art_type_Operation_1(art.type.Operation element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("input");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__INPUT));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("input", count - 1);
		}
	}
	public void print_art_type_Operation_2(art.type.Operation element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("output");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__OUTPUT));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("output", count - 1);
		}
	}
	public void print_art_type_Operation_3(art.type.Operation element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("output");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.OPERATION__OUTPUT));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("output", count - 1);
		}
	}
	
	public void print_art_type_Parameter(art.type.Parameter element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__TYPE));
		printCountingMap.put("type", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("type");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTypedElementTypeReferenceResolver().deResolve((art.DataType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__TYPE)), element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__TYPE), element));
			}
			printCountingMap.put("type", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PARAMETER__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
	}
	
	public void print_art_type_FunctionalService(art.type.FunctionalService element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.FUNCTIONAL_SERVICE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.FUNCTIONAL_SERVICE__OPERATION));
		printCountingMap.put("operation", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("functional");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("service");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.FUNCTIONAL_SERVICE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.FUNCTIONAL_SERVICE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_FunctionalService_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_type_FunctionalService_0(art.type.FunctionalService element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("operation");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.FUNCTIONAL_SERVICE__OPERATION));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("operation", count - 1);
		}
	}
	
	public void print_art_type_ControlService(art.type.ControlService element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.CONTROL_SERVICE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.CONTROL_SERVICE__OPERATION));
		printCountingMap.put("operation", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("control");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("service");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.CONTROL_SERVICE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.CONTROL_SERVICE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_ControlService_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_type_ControlService_0(art.type.ControlService element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("operation");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.CONTROL_SERVICE__OPERATION));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("operation", count - 1);
		}
	}
	
	public void print_art_type_Port(art.type.Port element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(7);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__LOWER));
		printCountingMap.put("lower", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__UPPER));
		printCountingMap.put("upper", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__SERVICE));
		printCountingMap.put("service", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__ROLE));
		printCountingMap.put("role", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__PROTOCOL));
		printCountingMap.put("protocol", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__URI));
		printCountingMap.put("uri", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("role");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__ROLE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("T_PORT_KIND");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__ROLE), element));
			}
			printCountingMap.put("role", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("port");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("service");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__SERVICE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getAbstractPortServiceReferenceResolver().deResolve((art.type.Service) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__SERVICE)), element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__SERVICE), element));
			}
			printCountingMap.put("service", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("[");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("lower");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__LOWER));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("MULTIPLICITY");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__LOWER), element));
			}
			printCountingMap.put("lower", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("..");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("upper");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__UPPER));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("MULTIPLICITY");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__UPPER), element));
			}
			printCountingMap.put("upper", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("]");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_Port_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_Port_1(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_type_Port_0(art.type.Port element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("protocol");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("protocol");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__PROTOCOL));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__PROTOCOL), element));
			}
			printCountingMap.put("protocol", count - 1);
		}
	}
	public void print_art_type_Port_1(art.type.Port element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("uri");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("=");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("uri");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__URI));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT__URI), element));
			}
			printCountingMap.put("uri", count - 1);
		}
	}
	
	public void print_art_implem_FractalComponent(art.implem.FractalComponent element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTROLLER_DESC));
		printCountingMap.put("controllerDesc", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTENT_DESC));
		printCountingMap.put("contentDesc", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("FractalComponent");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("<");
		// DEFINITION PART BEGINS (CsString)
		out.print("controllerDesc");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("controllerDesc");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTROLLER_DESC));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTROLLER_DESC), element));
			}
			printCountingMap.put("controllerDesc", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("contentDesc");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("contentDesc");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTENT_DESC));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.implem.ImplemPackage.FRACTAL_COMPONENT__CONTENT_DESC), element));
			}
			printCountingMap.put("contentDesc", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print(">");
	}
	
	public void print_art_implem_OSGiComponent(art.implem.OSGiComponent element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(1);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_COMPONENT__IMPLEMENTING_CLASS));
		printCountingMap.put("implementingClass", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("OSGiComponent");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("implementingClass");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_COMPONENT__IMPLEMENTING_CLASS));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_COMPONENT__IMPLEMENTING_CLASS), element));
			}
			printCountingMap.put("implementingClass", count - 1);
		}
	}
	
	public void print_art_group_TypeGroup(art.group.TypeGroup element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(3);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES));
		printCountingMap.put("types", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__SUB_GROUPS));
		printCountingMap.put("subGroups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("typegroup");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_group_TypeGroup_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_group_TypeGroup_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_group_TypeGroup_0(art.group.TypeGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("types");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("types");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTypeGroupTypesReferenceResolver().deResolve((art.type.ComponentType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES)), element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES), element));
			}
			printCountingMap.put("types", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_group_TypeGroup_0_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
	}
	public void print_art_group_TypeGroup_0_0(art.group.TypeGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("types");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTypeGroupTypesReferenceResolver().deResolve((art.type.ComponentType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES)), element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__TYPES), element));
			}
			printCountingMap.put("types", count - 1);
		}
	}
	public void print_art_group_TypeGroup_1(art.group.TypeGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("subGroups");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.TYPE_GROUP__SUB_GROUPS));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("subGroups", count - 1);
		}
	}
	
	public void print_art_group_InstanceGroup(art.group.InstanceGroup element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(3);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES));
		printCountingMap.put("instances", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		temp = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__SUB_GROUPS));
		printCountingMap.put("subGroups", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (CsString)
		out.print("instancegroup");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_group_InstanceGroup_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_group_InstanceGroup_1(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_group_InstanceGroup_0(art.group.InstanceGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("instances");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("instances");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getInstanceGroupInstancesReferenceResolver().deResolve((art.instance.ComponentInstance) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES)), element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES), element));
			}
			printCountingMap.put("instances", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_group_InstanceGroup_0_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
	}
	public void print_art_group_InstanceGroup_0_0(art.group.InstanceGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("instances");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getInstanceGroupInstancesReferenceResolver().deResolve((art.instance.ComponentInstance) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES)), element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__INSTANCES), element));
			}
			printCountingMap.put("instances", count - 1);
		}
	}
	public void print_art_group_InstanceGroup_1(art.group.InstanceGroup element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("subGroups");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.group.GroupPackage.INSTANCE_GROUP__SUB_GROUPS));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("subGroups", count - 1);
		}
	}
	
	public void print_art_implem_OSGiType(art.implem.OSGiType element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(1);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_TYPE__GENERATE_INSTANCE_BUNDLE));
		printCountingMap.put("generateInstanceBundle", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("generateInstanceBundle");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_TYPE__GENERATE_INSTANCE_BUNDLE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("T_IMPLEM");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.implem.ImplemPackage.OS_GI_TYPE__GENERATE_INSTANCE_BUNDLE), element));
			}
			printCountingMap.put("generateInstanceBundle", count - 1);
		}
	}
	
	public void print_art_type_BasicAttribute(art.type.BasicAttribute element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(3);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__TYPE));
		printCountingMap.put("type", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__DEFAULT_VALUE));
		printCountingMap.put("defaultValue", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("attribute");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("type");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTypedElementTypeReferenceResolver().deResolve((art.DataType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__TYPE)), element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__TYPE), element));
			}
			printCountingMap.put("type", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_BasicAttribute_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_type_BasicAttribute_0(art.type.BasicAttribute element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("default");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("defaultValue");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__DEFAULT_VALUE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.BASIC_ATTRIBUTE__DEFAULT_VALUE), element));
			}
			printCountingMap.put("defaultValue", count - 1);
		}
	}
	
	public void print_art_type_Dictionary(art.type.Dictionary element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(4);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__TYPE));
		printCountingMap.put("type", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__VALUE_TYPE));
		printCountingMap.put("valueType", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__KEYS));
		printCountingMap.put("keys", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("attribute");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (CsString)
		out.print("[");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("type");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getTypedElementTypeReferenceResolver().deResolve((art.DataType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__TYPE)), element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__TYPE), element));
			}
			printCountingMap.put("type", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("->");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("valueType");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__VALUE_TYPE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getDictionaryValueTypeReferenceResolver().deResolve((art.DataType) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__VALUE_TYPE)), element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__VALUE_TYPE), element));
			}
			printCountingMap.put("valueType", count - 1);
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("]");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_Dictionary_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
	}
	public void print_art_type_Dictionary_0(art.type.Dictionary element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("default");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("keys");
		if (count > 0) {
			java.util.List<?> list = (java.util.List<?>)element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY__KEYS));
			int index  = list.size() - count;
			if (index < 0) {
				index = 0;
			}
			java.util.ListIterator<?> it  = list.listIterator(index);
			while (it.hasNext()) {
				Object o = it.next();
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("keys", 0);
		}
		// DEFINITION PART BEGINS (LineBreak)
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	
	public void print_art_type_DictionaryDefaultValue(art.type.DictionaryDefaultValue element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(2);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__KEY));
		printCountingMap.put("key", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__VALUE));
		printCountingMap.put("value", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (CsString)
		out.print("[");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("key");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__KEY));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__KEY), element));
			}
			printCountingMap.put("key", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CompoundDefinition)
		sWriter = new java.io.StringWriter();
		out1 = new java.io.PrintWriter(sWriter);
		printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
		print_art_type_DictionaryDefaultValue_0(element, localtab, out1, printCountingMap1);
		if (printCountingMap.equals(printCountingMap1)) {
			out1.close();
		} else {
			out1.flush();
			out1.close();
			out.print(sWriter.toString());
			printCountingMap.putAll(printCountingMap1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("]");
	}
	public void print_art_type_DictionaryDefaultValue_0(art.type.DictionaryDefaultValue element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print("->");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("value");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__VALUE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("STRING_LITERAL");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.DICTIONARY_DEFAULT_VALUE__VALUE), element));
			}
			printCountingMap.put("value", count - 1);
		}
	}
	
	public void print_art_type_PortId(art.type.PortId element, String outertab, java.io.PrintWriter out) {
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(1);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_ID__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		// print collected hidden tokens
		int count;
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_ID__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_ID__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
	}
	
	public void print_art_type_PortCollection(art.type.PortCollection element, String outertab, java.io.PrintWriter out) {
		String localtab = outertab;
		// The printCountingMap contains a mapping from feature names to the number of
		// remaining elements that still need to be printed. The map is initialized with
		// the number of elements stored in each structural feature. For lists this is the
		// list size. For non-multiple features it is either 1 (if the feature is set) or
		// 0 (if the feature is null).
		java.util.Map<String, Integer> printCountingMap = new java.util.LinkedHashMap<String, Integer>(6);
		Object temp;
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__NAME));
		printCountingMap.put("name", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__SERVICE));
		printCountingMap.put("service", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__ROLE));
		printCountingMap.put("role", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__PROTOCOL));
		printCountingMap.put("protocol", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__URI));
		printCountingMap.put("uri", temp == null ? 0 : 1);
		temp = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__IDS));
		printCountingMap.put("ids", temp == null ? 0 : ((java.util.Collection<?>) temp).size());
		// print collected hidden tokens
		int count;
		boolean iterate = true;
		java.io.StringWriter sWriter = null;
		java.io.PrintWriter out1 = null;
		java.util.Map<String, Integer> printCountingMap1 = null;
		// DEFINITION PART BEGINS (LineBreak)
		localtab += "	";
		out.println();
		out.print(localtab);
		// DEFINITION PART BEGINS (PlaceholderUsingSpecifiedToken)
		count = printCountingMap.get("role");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__ROLE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("T_PORT_KIND");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__ROLE), element));
			}
			printCountingMap.put("role", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("port");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("name");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__NAME));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve((Object) o, element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__NAME), element));
			}
			printCountingMap.put("name", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print(":");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (PlaceholderUsingDefaultToken)
		count = printCountingMap.get("service");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__SERVICE));
			if (o != null) {
				art.resource.artext.IArtextTokenResolver resolver = tokenResolverFactory.createTokenResolver("TEXT");
				resolver.setOptions(getOptions());
				out.print(resolver.deResolve(getReferenceResolverSwitch() == null ? null : getReferenceResolverSwitch().getAbstractPortServiceReferenceResolver().deResolve((art.type.Service) o, element, (org.eclipse.emf.ecore.EReference) element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__SERVICE)), element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__SERVICE), element));
			}
			printCountingMap.put("service", count - 1);
		}
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (CsString)
		out.print("{");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("ids");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__IDS));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("ids", count - 1);
		}
		// DEFINITION PART BEGINS (CompoundDefinition)
		iterate = true;
		while (iterate) {
			sWriter = new java.io.StringWriter();
			out1 = new java.io.PrintWriter(sWriter);
			printCountingMap1 = new java.util.LinkedHashMap<String, Integer>(printCountingMap);
			print_art_type_PortCollection_0(element, localtab, out1, printCountingMap1);
			if (printCountingMap.equals(printCountingMap1)) {
				iterate = false;
				out1.close();
			} else {
				out1.flush();
				out1.close();
				out.print(sWriter.toString());
				printCountingMap.putAll(printCountingMap1);
			}
		}
		// DEFINITION PART BEGINS (CsString)
		out.print("}");
	}
	public void print_art_type_PortCollection_0(art.type.PortCollection element, String outertab, java.io.PrintWriter out, java.util.Map<String, Integer> printCountingMap){
		String localtab = outertab;
		int count;
		// DEFINITION PART BEGINS (CsString)
		out.print(",");
		// DEFINITION PART BEGINS (WhiteSpaces)
		out.print(" ");
		// DEFINITION PART BEGINS (Containment)
		count = printCountingMap.get("ids");
		if (count > 0) {
			Object o = element.eGet(element.eClass().getEStructuralFeature(art.type.TypePackage.PORT_COLLECTION__IDS));
			java.util.List<?> list = (java.util.List<?>) o;
			int index = list.size() - count;
			if (index >= 0) {
				o = list.get(index);
			} else {
				o = null;
			}
			if (o != null) {
				doPrint((org.eclipse.emf.ecore.EObject) o, out, localtab);
			}
			printCountingMap.put("ids", count - 1);
		}
	}
	
}

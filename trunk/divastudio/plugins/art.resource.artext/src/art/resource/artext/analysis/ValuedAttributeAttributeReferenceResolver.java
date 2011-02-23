package art.resource.artext.analysis;

public class ValuedAttributeAttributeReferenceResolver implements art.resource.artext.IArtextReferenceResolver<art.instance.ValuedAttribute, art.type.BasicAttribute> {
	
	private art.resource.artext.analysis.ArtextDefaultResolverDelegate<art.instance.ValuedAttribute, art.type.BasicAttribute> delegate = new art.resource.artext.analysis.ArtextDefaultResolverDelegate<art.instance.ValuedAttribute, art.type.BasicAttribute>();
	
	public void resolve(java.lang.String identifier, art.instance.ValuedAttribute container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final art.resource.artext.IArtextReferenceResolveResult<art.type.BasicAttribute> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public java.lang.String deResolve(art.type.BasicAttribute element, art.instance.ValuedAttribute container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend on any option
	}
	
}

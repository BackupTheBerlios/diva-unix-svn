/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package smartadapters4ART.resource.smARText.analysis;

import smartadapters4ART.resource.smARText.analysis.utils.PIDResolverDelegate;

public class SetruntimeNodeComponentsReferenceResolver implements smartadapters4ART.resource.smARText.ISmARTextReferenceResolver<smartadapters4ART.adaptations.SetruntimeNode, art_relaxed.instance_relaxed.ComponentInstance> {
	
	private PIDResolverDelegate<smartadapters4ART.adaptations.SetruntimeNode, art_relaxed.instance_relaxed.ComponentInstance> delegate = new PIDResolverDelegate<smartadapters4ART.adaptations.SetruntimeNode, art_relaxed.instance_relaxed.ComponentInstance>();
	
	public void resolve(java.lang.String identifier, smartadapters4ART.adaptations.SetruntimeNode container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final smartadapters4ART.resource.smARText.ISmARTextReferenceResolveResult<art_relaxed.instance_relaxed.ComponentInstance> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public java.lang.String deResolve(art_relaxed.instance_relaxed.ComponentInstance element, smartadapters4ART.adaptations.SetruntimeNode container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
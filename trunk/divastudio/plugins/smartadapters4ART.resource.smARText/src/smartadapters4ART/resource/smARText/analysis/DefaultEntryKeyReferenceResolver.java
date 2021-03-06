/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package smartadapters4ART.resource.smARText.analysis;

import smartadapters4ART.resource.smARText.analysis.utils.IntraPatternResolverDelegate;

public class DefaultEntryKeyReferenceResolver implements smartadapters4ART.resource.smARText.ISmARTextReferenceResolver<art_relaxed.instance_relaxed.DefaultEntry, art_relaxed.type_relaxed.DictionaryDefaultValue> {
	
	private IntraPatternResolverDelegate<art_relaxed.instance_relaxed.DefaultEntry, art_relaxed.type_relaxed.DictionaryDefaultValue> delegate = new IntraPatternResolverDelegate<art_relaxed.instance_relaxed.DefaultEntry, art_relaxed.type_relaxed.DictionaryDefaultValue>();
	
	public void resolve(java.lang.String identifier, art_relaxed.instance_relaxed.DefaultEntry container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final smartadapters4ART.resource.smARText.ISmARTextReferenceResolveResult<art_relaxed.type_relaxed.DictionaryDefaultValue> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public java.lang.String deResolve(art_relaxed.type_relaxed.DictionaryDefaultValue element, art_relaxed.instance_relaxed.DefaultEntry container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}

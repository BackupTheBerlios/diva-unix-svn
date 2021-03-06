/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.mopp;

/**
 * A representation for a range in a document where a boolean attribute is
 * expected.
 */
public class ArtextExpectedBooleanTerminal extends art.resource.artext.mopp.ArtextAbstractExpectedElement {
	
	private art.resource.artext.grammar.ArtextBooleanTerminal booleanTerminal;
	
	public ArtextExpectedBooleanTerminal(art.resource.artext.grammar.ArtextBooleanTerminal booleanTerminal) {
		super(booleanTerminal.getMetaclass());
		this.booleanTerminal = booleanTerminal;
	}
	
	public art.resource.artext.grammar.ArtextBooleanTerminal getBooleanTerminal() {
		return booleanTerminal;
	}
	
	private org.eclipse.emf.ecore.EStructuralFeature getFeature() {
		return booleanTerminal.getFeature();
	}
	
	public String toString() {
		return "EFeature " + getFeature().getEContainingClass().getName() + "." + getFeature().getName();
	}
	
	public boolean equals(Object o) {
		if (o instanceof ArtextExpectedBooleanTerminal) {
			return getFeature().equals(((ArtextExpectedBooleanTerminal) o).getFeature());
		}
		return false;
	}
	
	public java.util.Set<String> getTokenNames() {
		// BooleanTerminals are associated with two or one token(s)
		java.util.Set<String> tokenNames = new java.util.LinkedHashSet<String>(2);
		String trueLiteral = booleanTerminal.getTrueLiteral();
		if (!"".equals(trueLiteral)) {
			tokenNames.add("'" + trueLiteral + "'");
		}
		String falseLiteral = booleanTerminal.getFalseLiteral();
		if (!"".equals(falseLiteral)) {
			tokenNames.add("'" + falseLiteral + "'");
		}
		return tokenNames;
	}
	
}

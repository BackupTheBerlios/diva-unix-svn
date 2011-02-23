/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.grammar;

/**
 * A class to represent placeholders in a grammar.
 */
public class ArtextPlaceholder extends art.resource.artext.grammar.ArtextTerminal {
	
	private final String tokenName;
	
	public ArtextPlaceholder(org.eclipse.emf.ecore.EStructuralFeature feature, String tokenName, art.resource.artext.grammar.ArtextCardinality cardinality, int mandatoryOccurencesAfter) {
		super(feature, cardinality, mandatoryOccurencesAfter);
		this.tokenName = tokenName;
	}
	
	public String getTokenName() {
		return tokenName;
	}
	
}

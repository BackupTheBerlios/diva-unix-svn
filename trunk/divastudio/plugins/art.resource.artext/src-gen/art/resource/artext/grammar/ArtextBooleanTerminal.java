/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.grammar;

/**
 * A class to represent boolean terminals in a grammar.
 */
public class ArtextBooleanTerminal extends art.resource.artext.grammar.ArtextTerminal {
	
	private String trueLiteral;
	private String falseLiteral;
	
	public ArtextBooleanTerminal(org.eclipse.emf.ecore.EStructuralFeature attribute, String trueLiteral, String falseLiteral, art.resource.artext.grammar.ArtextCardinality cardinality, int mandatoryOccurrencesAfter) {
		super(attribute, cardinality, mandatoryOccurrencesAfter);
		assert attribute instanceof org.eclipse.emf.ecore.EAttribute;
		this.trueLiteral = trueLiteral;
		this.falseLiteral = falseLiteral;
	}
	
	public String getTrueLiteral() {
		return trueLiteral;
	}
	
	public String getFalseLiteral() {
		return falseLiteral;
	}
	
	public org.eclipse.emf.ecore.EAttribute getAttribute() {
		return (org.eclipse.emf.ecore.EAttribute) getFeature();
	}
	
}

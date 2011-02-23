/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.mopp;

public class ArtextProblem implements art.resource.artext.IArtextProblem {
	
	private String message;
	private art.resource.artext.ArtextEProblemType type;
	private java.util.Collection<art.resource.artext.IArtextQuickFix> quickFixes;
	
	public ArtextProblem(String message, art.resource.artext.ArtextEProblemType type) {
		this(message, type, java.util.Collections.<art.resource.artext.IArtextQuickFix>emptySet());
	}
	
	public ArtextProblem(String message, art.resource.artext.ArtextEProblemType type, art.resource.artext.IArtextQuickFix quickFix) {
		this(message, type, java.util.Collections.singleton(quickFix));
	}
	
	public ArtextProblem(String message, art.resource.artext.ArtextEProblemType type, java.util.Collection<art.resource.artext.IArtextQuickFix> quickFixes) {
		super();
		this.message = message;
		this.type = type;
		this.quickFixes = new java.util.LinkedHashSet<art.resource.artext.IArtextQuickFix>();
		this.quickFixes.addAll(quickFixes);
	}
	
	public art.resource.artext.ArtextEProblemType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public java.util.Collection<art.resource.artext.IArtextQuickFix> getQuickFixes() {
		return quickFixes;
	}
	
}

/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext;

public interface IArtextProblem {
	public String getMessage();
	public art.resource.artext.ArtextEProblemType getType();
	public java.util.Collection<art.resource.artext.IArtextQuickFix> getQuickFixes();
}

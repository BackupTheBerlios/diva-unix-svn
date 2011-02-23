/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.ui;

/**
 * A class which can be overridden to customize code completion proposals.
 */
public class ArtextProposalPostProcessor {
	
	public java.util.List<art.resource.artext.ui.ArtextCompletionProposal> process(java.util.List<art.resource.artext.ui.ArtextCompletionProposal> proposals) {
		// the default implementation does returns the proposals as they are
		return proposals;
	}
	
}

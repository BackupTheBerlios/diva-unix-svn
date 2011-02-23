/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.ui;

public class ArtextCompletionProcessor implements org.eclipse.jface.text.contentassist.IContentAssistProcessor {
	
	private art.resource.artext.ui.ArtextEditor editor;
	
	public ArtextCompletionProcessor(art.resource.artext.ui.ArtextEditor editor) {
		this.editor = editor;
	}
	
	public org.eclipse.jface.text.contentassist.ICompletionProposal[] computeCompletionProposals(org.eclipse.jface.text.ITextViewer viewer, int offset) {
		
		org.eclipse.emf.ecore.resource.Resource resource = editor.getResource();
		art.resource.artext.IArtextTextResource textResource = (art.resource.artext.IArtextTextResource) resource;
		String content = viewer.getDocument().get();
		art.resource.artext.ui.ArtextCodeCompletionHelper helper = new art.resource.artext.ui.ArtextCodeCompletionHelper();
		art.resource.artext.ui.ArtextCompletionProposal[] computedProposals = helper.computeCompletionProposals(textResource, content, offset);
		
		// call completion proposal post processor to allow for customizing the proposals
		art.resource.artext.ui.ArtextProposalPostProcessor proposalPostProcessor = new art.resource.artext.ui.ArtextProposalPostProcessor();
		java.util.List<art.resource.artext.ui.ArtextCompletionProposal> computedProposalList = java.util.Arrays.asList(computedProposals);
		java.util.List<art.resource.artext.ui.ArtextCompletionProposal> extendedProposalList = proposalPostProcessor.process(computedProposalList);
		if (extendedProposalList == null) {
			extendedProposalList = java.util.Collections.emptyList();
		}
		java.util.List<art.resource.artext.ui.ArtextCompletionProposal> finalProposalList = new java.util.ArrayList<art.resource.artext.ui.ArtextCompletionProposal>();
		for (art.resource.artext.ui.ArtextCompletionProposal proposal : extendedProposalList) {
			if (proposal.getMatchesPrefix()) {
				finalProposalList.add(proposal);
			}
		}
		org.eclipse.jface.text.contentassist.ICompletionProposal[] result = new org.eclipse.jface.text.contentassist.ICompletionProposal[finalProposalList.size()];
		int i = 0;
		for (art.resource.artext.ui.ArtextCompletionProposal proposal : finalProposalList) {
			String proposalString = proposal.getInsertString();
			String displayString = proposal.getDisplayString();
			String prefix = proposal.getPrefix();
			org.eclipse.swt.graphics.Image image = proposal.getImage();
			org.eclipse.jface.text.contentassist.IContextInformation info;
			info = new org.eclipse.jface.text.contentassist.ContextInformation(image, proposalString, proposalString);
			int begin = offset - prefix.length();
			int replacementLength = prefix.length();
			// if a closing bracket was automatically inserted right before, we enlarge the
			// replacement length in order to overwrite the bracket.
			art.resource.artext.ui.IArtextBracketHandler bracketHandler = editor.getBracketHandler();
			String closingBracket = bracketHandler.getClosingBracket();
			if (bracketHandler.addedClosingBracket() && proposalString.endsWith(closingBracket)) {
				replacementLength += closingBracket.length();
			}
			result[i++] = new org.eclipse.jface.text.contentassist.CompletionProposal(proposalString, begin, replacementLength, proposalString.length(), image, displayString, info, proposalString);
		}
		return result;
	}
	
	public org.eclipse.jface.text.contentassist.IContextInformation[] computeContextInformation(org.eclipse.jface.text.ITextViewer viewer, int offset) {
		return null;
	}
	
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}
	
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	public org.eclipse.jface.text.contentassist.IContextInformationValidator getContextInformationValidator() {
		return null;
	}
	
	public String getErrorMessage() {
		return null;
	}
}

/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.ui;

public class ArtextUIMetaInformation extends art.resource.artext.mopp.ArtextMetaInformation {
	
	public art.resource.artext.IArtextHoverTextProvider getHoverTextProvider() {
		return new art.resource.artext.ui.ArtextHoverTextProvider();
	}
	
	public art.resource.artext.ui.ArtextImageProvider getImageProvider() {
		return art.resource.artext.ui.ArtextImageProvider.INSTANCE;
	}
	
	public art.resource.artext.ui.ArtextColorManager createColorManager() {
		return new art.resource.artext.ui.ArtextColorManager();
	}
	
	/**
	 * @deprecated this method is only provided to preserve API compatibility. Use
	 * createTokenScanner(art.resource.artext.IArtextTextResource,
	 * art.resource.artext.ui.ArtextColorManager) instead.
	 */
	public art.resource.artext.ui.ArtextTokenScanner createTokenScanner(art.resource.artext.ui.ArtextColorManager colorManager) {
		return createTokenScanner(null, colorManager);
	}
	
	public art.resource.artext.ui.ArtextTokenScanner createTokenScanner(art.resource.artext.IArtextTextResource resource, art.resource.artext.ui.ArtextColorManager colorManager) {
		return new art.resource.artext.ui.ArtextTokenScanner(resource, colorManager);
	}
	
	public art.resource.artext.ui.ArtextCodeCompletionHelper createCodeCompletionHelper() {
		return new art.resource.artext.ui.ArtextCodeCompletionHelper();
	}
	
}

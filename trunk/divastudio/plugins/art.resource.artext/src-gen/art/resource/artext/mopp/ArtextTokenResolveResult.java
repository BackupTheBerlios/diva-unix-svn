/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext.mopp;

/**
 * A basic implementation of the ITokenResolveResult interface.
 */
public class ArtextTokenResolveResult implements art.resource.artext.IArtextTokenResolveResult {
	
	private String errorMessage;
	private Object resolvedToken;
	
	public ArtextTokenResolveResult() {
		super();
		clear();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Object getResolvedToken() {
		return resolvedToken;
	}
	
	public void setErrorMessage(String message) {
		errorMessage = message;
	}
	
	public void setResolvedToken(Object resolvedToken) {
		this.resolvedToken = resolvedToken;
	}
	
	public void clear() {
		errorMessage = "Can't resolve token.";
		resolvedToken = null;
	}
	
}
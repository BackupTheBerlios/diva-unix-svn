/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package art.resource.artext;

public interface IArtextReferenceCache {
	public Object get(String identifier);
	public void put(String identifier, Object target);
}

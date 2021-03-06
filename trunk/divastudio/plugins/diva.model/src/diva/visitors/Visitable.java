/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package diva.visitors;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Visitable</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see diva.visitors.VisitorsPackage#getVisitable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Visitable extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<C, R> R accept(Visitor<C, R> visitor, C context);

} // Visitable

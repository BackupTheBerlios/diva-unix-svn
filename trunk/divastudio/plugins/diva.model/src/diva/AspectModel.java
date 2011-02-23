/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package diva;

import diva.visitors.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Aspect Model</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see diva.DivaPackage#getAspectModel()
 * @model
 * @generated
 */
public interface AspectModel extends Model {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='return visitor.visitAspectModel(this, context);'"
	 *        annotation="kermeta body='do\nresult := visitor.visitAspectModel(self, context)\nend' isAbstract='false'"
	 * @generated
	 */
	<C, R> R accept(Visitor<C, R> visitor, C context);

} // AspectModel

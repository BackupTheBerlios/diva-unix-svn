/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package art_relaxed.instance_relaxed.impl;

import art_relaxed.instance_relaxed.ComponentInstance;
import art_relaxed.instance_relaxed.CompositeInstance;
import art_relaxed.instance_relaxed.DelegationBinding;
import art_relaxed.instance_relaxed.Instance_relaxedPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link art_relaxed.instance_relaxed.impl.CompositeInstanceImpl#getSubComponent <em>Sub Component</em>}</li>
 *   <li>{@link art_relaxed.instance_relaxed.impl.CompositeInstanceImpl#getDelegation <em>Delegation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompositeInstanceImpl extends ComponentInstanceImpl implements CompositeInstance {
	/**
	 * The cached value of the '{@link #getSubComponent() <em>Sub Component</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubComponent()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentInstance> subComponent;

	/**
	 * The cached value of the '{@link #getDelegation() <em>Delegation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelegation()
	 * @generated
	 * @ordered
	 */
	protected EList<DelegationBinding> delegation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompositeInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Instance_relaxedPackage.Literals.COMPOSITE_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentInstance> getSubComponent() {
		if (subComponent == null) {
			subComponent = new EObjectContainmentWithInverseEList<ComponentInstance>(ComponentInstance.class, this, Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT, Instance_relaxedPackage.COMPONENT_INSTANCE__SUPER_COMPONENT);
		}
		return subComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DelegationBinding> getDelegation() {
		if (delegation == null) {
			delegation = new EObjectContainmentEList<DelegationBinding>(DelegationBinding.class, this, Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION);
		}
		return delegation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSubComponent()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				return ((InternalEList<?>)getSubComponent()).basicRemove(otherEnd, msgs);
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION:
				return ((InternalEList<?>)getDelegation()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				return getSubComponent();
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION:
				return getDelegation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				getSubComponent().clear();
				getSubComponent().addAll((Collection<? extends ComponentInstance>)newValue);
				return;
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION:
				getDelegation().clear();
				getDelegation().addAll((Collection<? extends DelegationBinding>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				getSubComponent().clear();
				return;
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION:
				getDelegation().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__SUB_COMPONENT:
				return subComponent != null && !subComponent.isEmpty();
			case Instance_relaxedPackage.COMPOSITE_INSTANCE__DELEGATION:
				return delegation != null && !delegation.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CompositeInstanceImpl

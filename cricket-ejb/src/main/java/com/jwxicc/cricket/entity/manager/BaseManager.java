package com.jwxicc.cricket.entity.manager;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;

import com.jwxicc.cricket.interfaces.CricketDataManager;

@Stateless
public abstract class BaseManager<T> implements CricketDataManager<T> {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@Override
	public void persist(T obj) {
		try {
			em.persist(obj);
		} catch (Exception e) {
			System.out.println("Persist EXCEPTION: " + e.getMessage());
			obj = em.merge(obj);
		}
	}

	@Override
	public T merge(T obj) {
		return em.merge(obj);
	}

	@Override
	public T findLazy(Object key) {
		@SuppressWarnings("unchecked")
		// because we are always casting Class<T> to Class<T>
		Class<T> type = this.getClassOfType();
		return (T) em.find(type, key);
	}

	@Override
	public T getReference(Object key) {
		Class<T> type = this.getClassOfType();
		return (T) em.getReference(type, key);
	}

	@Override
	public List<T> getAll() {
		Class<T> type = this.getClassOfType();
		String entityTypeName = StringUtils.substringAfterLast(type.getName(), ".");

		return em.createQuery("from " + entityTypeName).getResultList();
	}

	private Class<T> getClassOfType() {
		return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
	}
}

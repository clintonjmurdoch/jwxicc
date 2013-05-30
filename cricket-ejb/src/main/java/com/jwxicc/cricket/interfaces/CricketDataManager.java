package com.jwxicc.cricket.interfaces;

import java.util.List;

public interface CricketDataManager<T> extends Manager {

	public boolean validateRequiredFields(T obj);

	public void persist(T obj);

	public T merge(T obj);

	public T findLazy(Object key);

	public T getReference(Object key);

	public List<T> getAll();
}

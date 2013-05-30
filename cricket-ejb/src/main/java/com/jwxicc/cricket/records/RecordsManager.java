package com.jwxicc.cricket.records;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public abstract class RecordsManager<T, U> {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	public abstract List<T> getInningsBest();

	public abstract List<U> getByAggregate();

	public abstract List<U> getByAverage();

	public abstract U getPlayerCareerRecord(int playerId);

	public int objToInt(Object o) {
		return Integer.valueOf(o.toString()).intValue();
	}
}

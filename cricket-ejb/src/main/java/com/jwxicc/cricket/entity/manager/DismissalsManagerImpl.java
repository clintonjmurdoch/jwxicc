package com.jwxicc.cricket.entity.manager;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jwxicc.cricket.entity.Howout;
import com.jwxicc.cricket.entity.WicketDetail;
import com.jwxicc.cricket.interfaces.DismissalsManager;

@Stateless(name = "dismissalsManager")
@Local(DismissalsManager.class)
public class DismissalsManagerImpl implements DismissalsManager {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@Override
	public List<Howout> getAllDismissalTypes() {
		return em.createQuery("from Howout").getResultList();
	}

	@Override
	public void persistWicketDetail(WicketDetail wicket) {
		em.persist(wicket);
	}

	@Override
	public Howout findHowout(int id) {
		return em.find(Howout.class, id);
	}

}

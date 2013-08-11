package com.jwxicc.cricket.entity.manager;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.interfaces.InningsFacade;
import com.jwxicc.cricket.interfaces.InningsManagerLocal;

@Stateless(name = "inningsManager")
@Local(InningsManagerLocal.class)
public class InningsManagerImpl extends BaseManager<Inning> implements InningsManagerLocal {

	@Override
	public boolean validateRequiredFields(Inning obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Inning findLazy(Object key) {
		return super.findLazy(key);
	}

	@Override
	public void addBatting(Batting bat) {
		em.persist(bat);
	}

	@Override
	public void addBowling(Bowling bowl) {
		em.persist(bowl);
	}

	@Override
	public void addPartnership(Partnership partnership) {
		em.persist(partnership);
	}

	@Override
	public void addPartnershipPlayer(PartnershipPlayer partnershipPlayer) {
		em.persist(partnershipPlayer);
	}

	@Override
	public void addInnings(Inning inns) {
		super.persist(inns);
	}

	@Override
	public void mergeBatting(Batting bat) {
		em.merge(bat);
	}

	@Override
	public void mergeBowling(Bowling bowl) {
		em.merge(bowl);
	}

}

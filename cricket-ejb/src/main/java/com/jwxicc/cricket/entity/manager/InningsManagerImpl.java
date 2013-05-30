package com.jwxicc.cricket.entity.manager;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.interfaces.InningsManager;

@Stateless(name = "inningsManager")
@Local(InningsManager.class)
public class InningsManagerImpl extends BaseManager<Inning> implements InningsManager {

	private Inning inns;
	private Partnership partnership;

	@Override
	public boolean validateRequiredFields(Inning obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void persist(Inning obj) {
		super.persist(obj);
		this.inns = obj;
	}

	@Override
	public Inning findLazy(Object key) {
		this.inns = super.findLazy(key);
		return this.inns;
	}

	@Override
	public void addBatting(Batting bat) {
		if (this.inns != null) {
			bat.setInning(inns);
		}
		em.persist(bat);
	}

	@Override
	public void addBowling(Bowling bowl) {
		if (this.inns != null) {
			bowl.setInning(inns);
		}
		em.persist(bowl);
	}

	@Override
	public void addPartnership(Partnership partnership) {
		partnership.setInning(inns);
		this.partnership = partnership;
		em.persist(partnership);
	}

	@Override
	public void addPartnershipPlayer(PartnershipPlayer partnershipPlayer) {
		partnershipPlayer.setPartnership(this.partnership);
		em.persist(partnershipPlayer);
	}

}

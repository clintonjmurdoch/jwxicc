package com.jwxicc.cricket.interfaces;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;

@Local
public interface InningsManagerLocal extends InningsFacade, CricketDataManager<Inning> {
	public Batting mergeBatting(Batting bat);

	public Bowling mergeBowling(Bowling bowl);
	
	public Partnership mergePartnership(Partnership partnership);
	
	public PartnershipPlayer mergePartnershipPlayer(PartnershipPlayer partnershipPlayer);
	
	public void removePartnership(int partnershipId);

}
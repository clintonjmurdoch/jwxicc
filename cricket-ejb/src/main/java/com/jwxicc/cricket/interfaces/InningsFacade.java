package com.jwxicc.cricket.interfaces;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;

/**
 * Provides methods for storing innings and its children. The separation from the Manager bean is to
 * allow interim storage of objects for easier management of persistence.
 * 
 * @author cmurdoch
 * 
 */
public interface InningsFacade {
	public void addInnings(Inning inns);

	public void addBatting(Batting bat);

	public void addBowling(Bowling bowl);

	public void addPartnership(Partnership partnership);

	public void addPartnershipPlayer(PartnershipPlayer partnershipPlayer);
}

package com.jwxicc.cricket.interfaces;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Inning;

@Local
public interface InningsManagerLocal extends InningsFacade, CricketDataManager<Inning> {
	public void mergeBatting(Batting bat);

	public void mergeBowling(Bowling bowl);

}
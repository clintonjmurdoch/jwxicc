package com.jwxicc.cricket.interfaces;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Fow;
import com.jwxicc.cricket.entity.FowWicket;
import com.jwxicc.cricket.entity.Inning;

@Local
public interface InningsManager extends CricketDataManager<Inning> {

	public void addBatting(Batting bat);
	public void addBowling(Bowling bowl);
	public void addFOW(Fow fow);
	public void addFowWicket(FowWicket fowWicket);
}

package com.jwxicc.cricket.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Review;

@Local
public interface CompetitionManager extends CricketDataManager<Competition> {

	public List<Competition> findAll();
	public Competition getDetailedCompetition(int compId);
	public Competition getCompetitionByAssociationAndSeason(String assocName, String season);
	public void addSaveReview(Competition comp);
}

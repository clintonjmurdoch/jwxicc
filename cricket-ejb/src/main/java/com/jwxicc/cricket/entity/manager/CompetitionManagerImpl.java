package com.jwxicc.cricket.entity.manager;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Review;
import com.jwxicc.cricket.interfaces.CompetitionManager;

@Stateless(name = "compManager")
@Local(CompetitionManager.class)
public class CompetitionManagerImpl extends BaseManager<Competition> implements CompetitionManager {

	@Override
	public boolean validateRequiredFields(Competition obj) {
		if (StringUtils.isNotEmpty(obj.getAssociationName())
				&& StringUtils.isNotEmpty(obj.getGrade())
				&& StringUtils.isNotEmpty(obj.getSeason())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Competition> findAll() {
		Query q = em.createQuery("from Competition");
		return q.getResultList();
	}

	@Override
	public Competition getDetailedCompetition(int compId) {
		String sqlStr = "from Competition c left join FETCH c.review "
				+ "left join FETCH c.games g " + "left join FETCH g.team1 left join fetch g.team2 "
				+ "where c.competitionId = ?1";
		Query query = em.createQuery(sqlStr);

		query.setParameter(1, compId);

		Competition c = (Competition) query.getSingleResult();
		return c;
	}

	@Override
	public Competition getCompetitionByAssociationAndSeason(String assocName, String season) {
		String jpql = "from Competition c left join FETCH c.games g "
				+ "left join FETCH g.team1 left join FETCH g.team2 left join FETCH g.ground "
				+ "where c.associationName = ? and c.season = ?";

		Query query = em.createQuery(jpql);

		query.setParameter(1, assocName);
		query.setParameter(2, season);

		Competition c = (Competition) query.getSingleResult();
		return c;
	}

	@Override
	public void addSaveReview(Competition comp) {
		Review rev = comp.getReview();
		// Get the database version of the competition
		Competition dbComp = findLazy(comp.getCompetitionId());

		if (dbComp.getReview() == null) {
			em.persist(rev);
			dbComp.setReview(rev);
		} else {
			Review dbReview = em.find(Review.class, dbComp.getReview().getReviewId());
			dbReview.setReviewText(rev.getReviewText());
		}
	}
}

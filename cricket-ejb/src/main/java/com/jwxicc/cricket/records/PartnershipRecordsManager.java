package com.jwxicc.cricket.records;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "partnershipRecordsManager")
@LocalBean
public class PartnershipRecordsManager {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	private static final String MIN_SCORE_SQL = "select min(score) from (select ps.runsScored as score "
			+ "from PARTNERSHIP ps inner join INNINGS i on ps.inningsId = i.inningsId where i.teamId = :jwxi ";
	private static final String MIN_SCORE_ORDER_BY = "order by ps.runsScored desc limit :limit) as T";

	private static final String BASE_PARTNERSHIP_JPQL = "from Partnership p where p.inning.team.teamId = :jwxi and p.runsScored >= :minscore ";
	private static final String PARTNERSHIP_ORDER_BY = "order by p.runsScored desc";
	private static final String WILLOWFEST_QUALIFIER_JPQL = "and p.inning.game.competition.associationName = 'Willowfest' ";

	public List<Partnership> getTopPartnerships(boolean willowfestOnly) {
		String queryString = BASE_PARTNERSHIP_JPQL;
		if (willowfestOnly) {
			queryString += WILLOWFEST_QUALIFIER_JPQL;
		}
		queryString += PARTNERSHIP_ORDER_BY;
		TypedQuery<Partnership> query = em.createQuery(queryString, Partnership.class);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setParameter("minscore", this.getMinScoreForPartnership(willowfestOnly));

		List<Partnership> resultList = query.getResultList();
		this.sortPartnerships(resultList);
		return resultList;

	}

	public List<Partnership> getTopPartnershipsByWicket(int wicket, boolean willowfestOnly) {
		String jpql = BASE_PARTNERSHIP_JPQL + "and p.wicket = :wicket ";
		if (willowfestOnly) {
			jpql += WILLOWFEST_QUALIFIER_JPQL;
		}
		jpql += PARTNERSHIP_ORDER_BY;
		TypedQuery<Partnership> query = em.createQuery(jpql, Partnership.class);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setParameter("minscore", this.getMinScoreForPartnership(wicket, willowfestOnly));
		query.setParameter("wicket", wicket);

		List<Partnership> resultList = query.getResultList();
		this.sortPartnerships(resultList);
		return resultList;

	}

	public List<List<Partnership>> getAllTopPartnershipsByWicket(boolean willowfestOnly) {
		List<List<Partnership>> allPartnershipsList = new ArrayList<List<Partnership>>(10);
		for (int i = 1; i <= 10; i++) {
			List<Partnership> partnershipsByWicket = this.getTopPartnershipsByWicket(i, willowfestOnly);
			// dont add empty collections
			if (CollectionUtils.isNotEmpty(partnershipsByWicket)) {
				allPartnershipsList.add(partnershipsByWicket);
			} else {
				allPartnershipsList.add(null);
			}
		}
		return allPartnershipsList;
	}

	private int getMinScoreForPartnership(boolean willowfestOnly) {
		return getMinScoreForPartnership(0, willowfestOnly);
	}

	private int getMinScoreForPartnership(int wicket, boolean willowfestOnly) {
		String sqlQuery = MIN_SCORE_SQL;
		if (wicket != 0) {
			sqlQuery += "and ps.wicket = " + wicket + " ";
		}
		if (willowfestOnly) {
			sqlQuery += " and i.gameid in (select gameid from GAME g inner join COMPETITION c " 
					+ "on g.competitionid = c.competitionid where c.associationName = 'Willowfest') ";
		}
		sqlQuery += MIN_SCORE_ORDER_BY;

		Query minScoreQuery = em.createNativeQuery(sqlQuery);
		minScoreQuery.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		minScoreQuery.setParameter("limit", JwxiccUtils.RECORDS_TO_SHOW);

		int minScore = (Integer) minScoreQuery.getSingleResult();

		if (minScore < 20) {
			minScore = 20;
		}

		return minScore;
	}

	private void sortPartnerships(List<Partnership> partnerships) {
		Collections.sort(partnerships, new Comparator<Partnership>() {
			@Override
			// partnerships are already sorted by runs, only sort further if runs are the same
			public int compare(Partnership o1, Partnership o2) {
				if (o1.getRunsScored() == o2.getRunsScored()) {
					// compare for not out
					if (o1.isUnbeaten() && !o2.isUnbeaten()) {
						// o1 comes first
						return -1;
					} else if (!o1.isUnbeaten() && o2.isUnbeaten()) {
						// o2 comes first
						return 1;
					} else {
						// the first one to happen comes first
						return o1.getInning().getGame().getDate()
								.compareTo(o1.getInning().getGame().getDate());
					}
				}
				// compare o2 first as we want them in reverse order
				return Integer.valueOf(o2.getRunsScored()).compareTo(
						Integer.valueOf(o1.getRunsScored()));
			}
		});
	}
}

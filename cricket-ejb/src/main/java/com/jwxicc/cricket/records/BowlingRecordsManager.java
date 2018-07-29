package com.jwxicc.cricket.records;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entityutils.EntityMethods;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.util.JwxiccUtils;

/**
 * Session Bean implementation class BowlingRecordsManager
 */
@Stateless(name = "bowlingRecordsManager")
@LocalBean
public class BowlingRecordsManager extends RecordsManager<Bowling, BowlingRecord> {

	private static final String CAREER_BOWLING_BASE_SQL = "select p.playerid, "
			+ "sum(b.overs) as overs, sum(b.overs-floor(b.overs)) as o_parts, "
			+ "sum(b.maidens) as maidens, sum(b.runs) as runs, "
			+ "sum(b.wickets) as wickets, sum(b.runs)/sum(b.wickets) as avg, "
			+ "bb.wickets as bb_wickets, bb.runs bb_runs, count(if(b.wickets>=5,1,null)) as 5I "
			+ "from BOWLING b natural join PLAYER p left join %s bb on p.playerid = bb.playerid "
			+ "where ";
	
	private static final String WILLOWFEST_BEST_BOWLING_TABLE = "(select * from " 
			+ "(select * from ((select * from best_bowling_seasons where competitionid in " 
			+ "(select competitionid from competition where associationNAme = 'Willowfest') "
			+ "order by wickets desc, runs asc) as dfjv) group by playerid) as dfgdg )";

	private static final String COMPETITION_QUALIFIER_SQL = "and b.bowlingid in "
			+ "(select bowlingid from BOWLING b " + COMPETITION_QUALIFIER_END_SQL
			+ ") and bb.competitionId = :comp group by playerid ";

	@Override
	public List<Bowling> getInningsBest(boolean willowfestOnly) {
		// bowlings ordered by wickets, runs, overs
		String queryString = "from Bowling b left join fetch b.player p left join fetch b.inning.game g left join fetch g.ground where p.team.teamId = :jwxi ";
		if (willowfestOnly) {
			queryString += "and g.competition.associationName = 'Willowfest' ";
		}
		queryString += "order by b.wickets desc, b.runs asc, b.overs asc";
		Query query = em.createQuery(queryString);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);
		List<Bowling> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public List<BowlingRecord> getByAggregate(boolean willowfestOnly) {
		String sqlQuery = getCareerBowlingSql(willowfestOnly) + JWXI_TEAM_SQL;
		if (willowfestOnly) {
			sqlQuery += WILLOWFEST_QUALIFIER_SQL;
		}
		sqlQuery += "group by p.playerid, bb.wickets, bb.runs order by wickets desc, runs asc, overs asc";

		return this.getBowlingRecords(sqlQuery, willowfestOnly);
	}

	@Override
	public List<BowlingRecord> getByAverage(boolean willowfestOnly) {
		String sqlQuery = getCareerBowlingSql(willowfestOnly) + JWXI_TEAM_SQL;
		if (willowfestOnly) {
			sqlQuery += WILLOWFEST_QUALIFIER_SQL;
		}
		sqlQuery += "group by p.playerid, bb.wickets, bb.runs "
				+ "having sum(b.wickets) >= " + JwxiccUtils.MIN_WICKETS_FOR_AVERAGE + " "
				+ "order by (0 - sum(b.runs)/sum(b.wickets)) desc, wickets desc, overs asc";

		return this.getBowlingRecords(sqlQuery, willowfestOnly);
	}

	private List<BowlingRecord> getBowlingRecords(String sqlQuery, boolean willowfestOnly) {
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);
		List<Object[]> results = query.getResultList();

		List<BowlingRecord> bowlingRecords = new ArrayList<BowlingRecord>(10);

		for (Object[] rs : results) {
			bowlingRecords.add(this.getRecordFromResult(rs, getMatchesPlayed(objToInt(rs[0]), willowfestOnly)));
		}

		return bowlingRecords;
	}

	private BowlingRecord getRecordFromResult(Object[] rs, int matches) {
		BowlingRecord bowlingRecord = new BowlingRecord();
		bowlingRecord.setMatchesPlayed(matches);
		// 0: playerid
		bowlingRecord.setPlayer(playerManager.findLazy(rs[0]));
		// 1-2: overs, over parts
		BigDecimal overs = buildAggregateOvers(Double.valueOf(rs[1].toString()),
				Double.valueOf(rs[2].toString()));
		bowlingRecord.setOvers(overs);
		// 3: maidens
		bowlingRecord.setMaidens(objToInt(rs[3]));
		// 4: runs
		int runs = objToInt(rs[4]);
		bowlingRecord.setRunsConceded(runs);
		// 5: wickets
		bowlingRecord.setWickets(objToInt(rs[5]));
		// 6: average
		// was getting null pointer for players with no wickets
		if (rs[6] != null) {
			bowlingRecord.setAverage(BigDecimal.valueOf(Double.valueOf(rs[6].toString())));
		}
		// 7: best bowling wickets
		bowlingRecord.setBestBowlingWickets(objToInt(rs[7]));
		// 8: best bowling runs
		bowlingRecord.setBestBowlingRuns(objToInt(rs[8]));
		// 9: 5 wkt innings
		bowlingRecord.setFiveWktInns(objToInt(rs[9]));
		// extra: economy
		BigDecimal economy = EntityMethods.calculateEconomy(runs, overs);
		bowlingRecord.setEconomy(economy);

		return bowlingRecord;
	}

	private BigDecimal buildAggregateOvers(Double aggOvers, Double overParts) {
		System.out.println("building over aggregates");
		System.out.println("agg overs: " + aggOvers + " over parts: " + overParts);
		if (aggOvers.equals(0d)) {
			return BigDecimal.ZERO;
		} else if (overParts.equals(0d) || overParts.doubleValue() < 0.6) {
			return BigDecimal.valueOf(aggOvers);
		} else {
			// there are part overs to deal with
			System.out.println("there are part overs to deal with");
			double completeOvers = aggOvers - overParts;
			System.out.println("complete overs: " + completeOvers);
			BigDecimal oPBigDec = new BigDecimal(overParts.doubleValue());
			double oversToAdd = Math.floor(oPBigDec.divide(BigDecimal.valueOf(0.6d),
					BigDecimal.ROUND_HALF_UP).doubleValue());
			BigDecimal toReturn = BigDecimal.valueOf(completeOvers).add(
					BigDecimal.valueOf(oversToAdd));
			// remainder wont work: BigDecimal remainder =
			// oPBigDec.remainder(BigDecimal.valueOf(0.6d));
			// System.out.println("remainder from the 0.6 division: " +
			// remainder);
			overParts = overParts - (oversToAdd * 0.6);
			if (overParts == 0d) {
				return toReturn;
			} else {
				return toReturn.add(BigDecimal.valueOf(overParts));
			}
		}
	}

	@Override
	public BowlingRecord getPlayerCareerRecord(int playerId, boolean willowfestOnly) {
		String sqlQuery = getCareerBowlingSql(willowfestOnly) 
				+ "p.playerid = :pid group by p.playerid";

		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("pid", playerId);

		// only returns 1 object[]
		List<Object[]> results = query.getResultList();
		if (results.size() == 0) {
			return null;
		}

		return getRecordFromResult(results.get(0), getMatchesPlayed(playerId, willowfestOnly));
	}

	@Override
	public List<BowlingRecord> getBySeason(int competitionId) {
		String sqlQuery = String.format(CAREER_BOWLING_BASE_SQL, "BEST_BOWLING_SEASONS") 
				+ JWXI_TEAM_SQL + COMPETITION_QUALIFIER_SQL
				+ "order by wickets desc";

		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setParameter("comp", competitionId);
		List<Object[]> results = query.getResultList();

		List<BowlingRecord> bowlingRecords = new ArrayList<BowlingRecord>(10);

		for (Object[] rs : results) {
			bowlingRecords.add(this.getRecordFromResult(rs,
					getMatchesPlayedInSeason(objToInt(rs[0]), competitionId)));
		}

		return bowlingRecords;
	}
	
	private String getCareerBowlingSql(boolean willowfestOnly) {
		String sqlQuery = null;
		if (willowfestOnly) {
			sqlQuery = String.format(CAREER_BOWLING_BASE_SQL, WILLOWFEST_BEST_BOWLING_TABLE);
			sqlQuery += " bb.competitionId in (select x.competitionid from COMPETITION x "
					+ "where x.associationName = 'Willowfest') and ";
		} else {
			sqlQuery = String.format(CAREER_BOWLING_BASE_SQL, "BEST_BOWLING");
		}
		
		return sqlQuery;
	}
}
package com.jwxicc.cricket.records;

import java.math.BigDecimal;
import java.util.ArrayList;
import javax.ejb.EJB;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "battingRecordsManager")
@LocalBean
public class BattingRecordsManager extends RecordsManager<Batting, BattingRecord> {

	private static final String CAREER_BATTING_BASE_SQL = "select p.playerid, count(b.battingid) as matches, "
			+ "count(if(b.howoutid not in (0, 16, 17),1,null)) as innings, "
			+ "count(if(b.howoutid in (1, 7, 13),1,null)) as notouts, "
			+ "bb.score as bb_runs, bb.balls as bb_balls, bb.outstatus as bb_outstatus, "
			+ "sum(b.score) as total, sum(b.balls) as ballsfaced, "
			+ "count(if(b.score >= 50,1,null)) as 50s, "
			+ "count(if(b.score >= 100,1,null)) as 100s, "
			+ "sum(b.score)/count(if(b.howoutid not in (0,1,7,13,16,17),1,null)) as avg, "
			+ "sum(if(b.balls > 0,b.score,0))/sum(b.balls) as strikerate "
			+ "from BATTING b natural join PLAYER p left join %s bb on p.playerId = bb.playerId "
			+ "where ";

	private static final String COMPETITION_QUALIFIER_SQL = "and b.battingid in "
			+ "(select battingid from BATTING b " + COMPETITION_QUALIFIER_END_SQL
			+ ") and bb.competitionId = :comp group by playerid, bb.score, bb.balls, bb.outstatus ";
	
	private static final String WILLOWFEST_BEST_BATTING_TABLE = "(select * from "
			+ "(select * from ((select * from best_batting_seasons where competitionid in "
			+ "(select competitionid from competition where associationName = 'Willowfest') " 
			+ "order by score desc, balls asc) as dfjv) group by playerid, bb.score, bb.balls, bb.outstatus) as dfgdg )"; 

	@Override
	public List<Batting> getInningsBest(boolean willowfestOnly) {
		// sql to get the lowest score in the top 10 scores for jwxi, to allow
		// for duplicates
		String sql = "select min(score) from (select b.score from BATTING b natural join PLAYER p where p.teamId = :jwxi ";
		if (willowfestOnly) {
			sql += WILLOWFEST_QUALIFIER_SQL;
		}
		sql += " order by b.score desc limit :limit) as T";
		Query sqlQuery = em.createNativeQuery(sql);
		sqlQuery.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		sqlQuery.setParameter("limit", JwxiccUtils.RECORDS_TO_SHOW);
		Integer score = (Integer) sqlQuery.getSingleResult();
		System.out.println("min score for top 10: " + score);

		// now get all scores for jwxi greater than or equal to this score
		String queryString = "from Batting b left join fetch b.player p left join fetch b.inning.game g left join fetch g.ground " 
				+ "where b.score >= :score and p.team.teamId = :jwxi ";
		if (willowfestOnly) {
			queryString += "and g.competition.associationName = 'Willowfest' ";
		}
		queryString += "order by b.score desc, b.balls asc, g.date asc";
		Query query = em.createQuery(queryString);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setParameter("score", score);
		List<Batting> resultList = query.getResultList();

		return resultList;
	}

	public List<Batting> getInningsMostBallsFaced(boolean willowfestOnly) {
		String sql = "";

		return null;
	}

	@Override
	public List<BattingRecord> getByAggregate(boolean willowfestOnly) {
		// Get the base_sql data ordered by most runs
		String sql = getCareerBattingSql(willowfestOnly)
				+ JWXI_TEAM_SQL;
		if (willowfestOnly) {
			sql += WILLOWFEST_QUALIFIER_SQL;
		}
		sql += "group by playerid, b.battingId, bb.score, bb.balls, bb.outstatus "
				+ "order by total desc, avg desc, strikerate desc, ballsfaced asc";

		return this.getBattingRecords(sql);
	}

	@Override
	public List<BattingRecord> getByAverage(boolean willowfestOnly) {
		// Get the base sql data ordered by average
		String sql = getCareerBattingSql(willowfestOnly) + JWXI_TEAM_SQL;
		if (willowfestOnly) {
			sql += WILLOWFEST_QUALIFIER_SQL;
		}
		sql += "group by playerid, b.battingId, bb.score, bb.balls, bb.outstatus "
				+ "having count(if(b.howoutid not in (0, 16, 17),1,null)) >= "
				+ JwxiccUtils.MIN_INNINGS_FOR_AVERAGE + " "
				+ "order by avg desc, total desc, strikerate desc, ballsfaced asc";

		return this.getBattingRecords(sql);
	}

	private List<BattingRecord> getBattingRecords(String sqlQuery) {
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);
		List<Object[]> results = query.getResultList();

		List<BattingRecord> battingRecords = new ArrayList<BattingRecord>(10);

		for (Object[] rs : results) {
			battingRecords.add(this.getRecordFromResult(rs));
		}

		return battingRecords;
	}

	private BattingRecord getRecordFromResult(Object[] rs) {
		BattingRecord battingRecord = new BattingRecord();
		// 0: playerid
		Player player = playerManager.findLazy(objToInt(rs[0]));
		battingRecord.setPlayer(player);
		// 1: matches
		battingRecord.setMatchesPlayed(objToInt(rs[1]));
		// 2: innings
		battingRecord.setInnings(objToInt(rs[2]));
		// 3: not outs
		battingRecord.setNotOuts(objToInt(rs[3]));
		// 4: best batting runs
		battingRecord.setBestBattingScore(objToInt(rs[4]));
		// 5: best batting balls
		battingRecord.setBestBattingBalls(objToInt(rs[5]));
		// 6: best batting out status
		battingRecord.setBestBattingOutStatus("1".equals(rs[6].toString()));
		// 7: total
		battingRecord.setAggregate(objToInt(rs[7]));
		// 8: balls faced
		battingRecord.setBallsFaced(objToInt(rs[8]));
		// 9: 50s
		battingRecord.setFifties(objToInt(rs[9]));
		// 10: 100s
		battingRecord.setHundreds(objToInt(rs[10]));
		// 11: average
		if (rs[11] != null) {
			battingRecord.setAverage(BigDecimal.valueOf(Double.valueOf(rs[11].toString())));
		}
		// 12: strike rate
		if (rs[12] != null) {
			battingRecord.setStrikeRate(BigDecimal.valueOf(Double.valueOf(rs[12].toString()))
					.scaleByPowerOfTen(2));
		}

		return battingRecord;
	}

	@Override
	public BattingRecord getPlayerCareerRecord(int playerId, boolean willowfestOnly) {
		String sqlQuery = getCareerBattingSql(willowfestOnly) + "p.playerId = :pid group by playerid";

		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("pid", playerId);

		// only returns 1 object[]
		List<Object[]> results = query.getResultList();
		if (results.size() == 0) {
			return null;
		}

		return getRecordFromResult(results.get(0));
	}

	@Override
	public List<BattingRecord> getBySeason(int competitionId) {
		String sqlQuery = String.format(CAREER_BATTING_BASE_SQL, "BEST_BATTING_SEASONS") 
				+ JWXI_TEAM_SQL + COMPETITION_QUALIFIER_SQL
				+ "order by total desc";

		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setParameter("comp", competitionId);
		List<Object[]> results = query.getResultList();

		List<BattingRecord> battingRecords = new ArrayList<BattingRecord>(10);

		for (Object[] rs : results) {
			battingRecords.add(this.getRecordFromResult(rs));
		}

		return battingRecords;
	}
	
	private String getCareerBattingSql(boolean willowfestOnly) {
		String sqlQuery = null;
		if (willowfestOnly) {
			sqlQuery = String.format(CAREER_BATTING_BASE_SQL, WILLOWFEST_BEST_BATTING_TABLE);
			sqlQuery += " bb.competitionId in (select x.competitionid from COMPETITION x "
					+ "where x.associationName = 'Willowfest') and ";
		} else {
			sqlQuery = String.format(CAREER_BATTING_BASE_SQL, "BEST_BATTING");
		}
		
		return sqlQuery;
	}
}
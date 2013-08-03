package com.jwxicc.cricket.records;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang.NotImplementedException;

import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "fieldingRecordsManager")
@LocalBean
public class FieldingRecordsManager extends RecordsManager<FieldingRecord, FieldingRecord> {

	private static final String FIELDING_BASE_SQL = "FROM WICKET_DETAIL wd natural join PLAYER p inner join BATTING b "
			+ "on wd.battingid = b.battingid ";

	private static final String FIELDING_END_SQL = "and " + JWXI_TEAM_SQL + "group by wd.playerid "
			+ "order by total desc";

	private static final String COMPETITION_QUALIFIER_SQL = "and b.battingid in "
			+ "(select battingid from BATTING b " + COMPETITION_QUALIFIER_END_SQL + ") ";

	@Override
	public List<FieldingRecord> getInningsBest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldingRecord> getByAggregate() {
		throw new NotImplementedException("getByAggregate not implemented for fielding");
	}

	@Override
	public List<FieldingRecord> getByAverage() {
		throw new NotImplementedException("getByAverage not implemented for fielding");
	}

	public List<FieldingRecord> getMostCatchesByOutfielder() {
		String sql = "SELECT wd.playerid, count(*) as total " + FIELDING_BASE_SQL
				+ "where wd.wickettype = 'CT' and b.howoutid != 18 " + FIELDING_END_SQL;

		Query query = em.createNativeQuery(sql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);
		List<Object[]> results = query.getResultList();

		List<FieldingRecord> fieldingRecords = new ArrayList<FieldingRecord>();

		for (Object[] obj : results) {
			FieldingRecord record = new FieldingRecord();
			record.setPlayer(playerManager.findLazy(objToInt(obj[0])));
			record.setCatches(objToInt(obj[1]));

			fieldingRecords.add(record);
		}

		return fieldingRecords;
	}

	public List<FieldingRecord> getMostDismissals() {
		String sql = "SELECT wd.playerid, count(if(wd.wickettype = 'CT',1,null)) as catches, "
				+ "count(if(wd.wickettype = 'ST',1,null)) as stumpings, count(*) as total "
				+ FIELDING_BASE_SQL + "where (wd.wickettype = 'CT' and b.howoutid = 18) "
				+ "or wd.wickettype = 'ST' " + FIELDING_END_SQL;

		Query query = em.createNativeQuery(sql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);
		List<Object[]> results = query.getResultList();

		List<FieldingRecord> fieldingRecords = new ArrayList<FieldingRecord>();

		for (Object[] obj : results) {
			FieldingRecord record = new FieldingRecord();
			record.setPlayer(playerManager.findLazy(objToInt(obj[0])));
			record.setWkCatches(objToInt(obj[1]));
			record.setStumpings(objToInt(obj[2]));
			record.setDismissals(objToInt(obj[3]));

			fieldingRecords.add(record);
		}
		return fieldingRecords;
	}

	@Override
	public List<FieldingRecord> getBySeason(int competitionId) {
		String sql = "SELECT wd.playerid, "
				+ "count(if(wd.wickettype = 'CT' and b.howoutid != 18,1,null)) as catches, "
				+ "count(if(wd.wickettype = 'CT' and b.howoutid = 18,1,null)) as wkcatches, "
				+ "count(if(wd.wickettype = 'ST',1,null)) as stumpings, "
				+ "count(if(wd.wickettype like 'RO%',1,null)) as runouts, " + "count(*) as total "
				+ FIELDING_BASE_SQL + "where wd.wickettype != 'B' " + COMPETITION_QUALIFIER_SQL + FIELDING_END_SQL;

		Query query = em.createNativeQuery(sql);
		query.setParameter("comp", competitionId);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		List<Object[]> results = query.getResultList();

		List<FieldingRecord> fieldingRecords = new ArrayList<FieldingRecord>();

		for (Object[] obj : results) {
			FieldingRecord record = new FieldingRecord();
			record.setPlayer(playerManager.findLazy(objToInt(obj[0])));
			record.setCatches(objToInt(obj[1]));
			record.setWkCatches(objToInt(obj[2]));
			record.setStumpings(objToInt(obj[3]));
			record.setRunouts(objToInt(obj[4]));

			fieldingRecords.add(record);
		}
		return fieldingRecords;
	}

	@Override
	public FieldingRecord getPlayerCareerRecord(int playerId) {
		// TODO Auto-generated method stub
		return null;
	}

}

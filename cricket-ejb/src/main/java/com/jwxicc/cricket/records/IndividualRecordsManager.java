package com.jwxicc.cricket.records;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.dbenum.DesignationType;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "indivRecordsManager")
@LocalBean
public class IndividualRecordsManager {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@EJB(name = "playerManager")
	PlayerManager playerManager;

	public List<Record> getMostMatches() {
		String sql = "SELECT p.playerId, count(*) as matches from BATTING natural join PLAYER p "
				+ "where p.teamId = :jwxi group by p.playerId order by matches desc, scorecardName";

		return getIndividualRecord(sql);
	}

	public List<Record> getMostMOTMs() {
		String sql = "SELECT p.playerId, count(*) as matches from GAME_PLAYER_DESIGNATION natural join PLAYER p "
				+ "where p.teamId = :jwxi and designationType = '"
				+ DesignationType.MAN_OF_MATCH.toString()
				+ "' group by p.playerId order by matches desc, scorecardName";

		return getIndividualRecord(sql);
	}

	public List<Record> getMostMatchesAsCaptain() {
		String sql = "SELECT p.playerId, count(*) as matches from GAME_PLAYER_DESIGNATION natural join PLAYER p "
				+ "where p.teamId = :jwxi and designationType = '"
				+ DesignationType.CAPTAIN.toString()
				+ "' group by p.playerId order by matches desc, scorecardName";

		return getIndividualRecord(sql);
	}

	private List<Record> getIndividualRecord(String sql) {
		Query query = em.createNativeQuery(sql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(10);

		List<Object[]> resultList = query.getResultList();

		ArrayList<Record> recordList = new ArrayList<Record>(10);
		for (Object[] result : resultList) {
			recordList.add(getPlayerAndMatches(result));
		}

		return recordList;
	}

	private Record getPlayerAndMatches(Object[] result) {
		Record record = new Record();
		record.setPlayer(playerManager.findLazy(result[0]));
		record.setMatchesPlayed(Integer.valueOf(result[1].toString()).intValue());

		return record;
	}
}

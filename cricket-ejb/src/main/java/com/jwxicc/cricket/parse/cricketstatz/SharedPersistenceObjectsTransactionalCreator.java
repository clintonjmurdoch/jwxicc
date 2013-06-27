package com.jwxicc.cricket.parse.cricketstatz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;
import com.jwxicc.cricket.interfaces.GroundManager;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.interfaces.TeamManager;

@Singleton(name = "sharedObjectsCreator")
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class SharedPersistenceObjectsTransactionalCreator {

	@Resource
	SessionContext ctx;

	@PersistenceUnit(unitName = "Jwxicc_JPA")
	EntityManagerFactory emf;

	@EJB
	TeamManager teamManager;

	@EJB
	GroundManager groundManager;

	@EJB
	PlayerManager playerManager;

	public Player createPlayerInTransaction(Player p) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = emf.createEntityManager();
			em.joinTransaction();
			// replace team with reference from em
			p.setTeam(em.merge(p.getTeam()));
			em.persist(p);
			tx.commit();
			em.close();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return p;
	}

	public Team createTeamInTransaction(Team t) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = emf.createEntityManager();
			em.persist(t);
			tx.commit();
			em.close();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return t;
	}

	public Ground createGroundInTransaction(Ground g) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = emf.createEntityManager();
			em.persist(g);
			tx.commit();
			em.close();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return g;
	}

	public Team getJwxiReference() {
		return teamManager.getJWXIReference();
	}

	public Map<Integer, Team> getAllTeams() {
		List<Team> tList = teamManager.getAll();
		Map<Integer, Team> existingTeams = new HashMap<Integer, Team>(tList.size());
		for (Team t : tList) {
			existingTeams.put(t.getTeamId(), t);
		}
		return existingTeams;
	}

	public Map<Integer, Ground> getAllGrounds() {
		List<Ground> gList = groundManager.getAll();
		Map<Integer, Ground> existingGrounds = new HashMap<Integer, Ground>(gList.size());
		for (Ground g : gList) {
			existingGrounds.put(g.getGroundId(), g);
		}
		return existingGrounds;
	}

	public Map<Integer, Player> getAllPlayers() {
		List<Player> pList = playerManager.getAll();
		Map<Integer, Player> existingPlayers = new HashMap<Integer, Player>(pList.size());
		for (Player p : pList) {
			existingPlayers.put(p.getPlayerId(), p);
		}
		return existingPlayers;
	}

}

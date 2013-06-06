package com.jwxicc.cricket.parse.cricketstatz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;
import com.jwxicc.cricket.interfaces.GroundManager;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.interfaces.TeamManager;

@LocalBean
@Lock(LockType.WRITE)
@Singleton(name = "sharedObjectsFacade")
@TransactionManagement(TransactionManagementType.BEAN)
public class SharedPersistenceObjectsFacade {
	
	private Map<Integer, Ground> existingGrounds;
	private Map<Integer, Team> existingTeams;
	private Map<Integer, Player> existingPlayers;
	private Team jwxiReference;

	@EJB
	GroundManager groundManager;

	@EJB
	TeamManager teamManager;

	@EJB
	PlayerManager playerManager;
	
	@Resource
	SessionContext ctx;

	@PostConstruct
	public void initialiseMaps() {
		System.out.println("Initialising shared object maps");

		List<Ground> gList = groundManager.getAll();
		existingGrounds = new HashMap<Integer, Ground>(gList.size());
		for (Ground g : gList) {
			existingGrounds.put(g.getGroundId(), g);
		}

		List<Team> tList = teamManager.getAll();
		existingTeams = new HashMap<Integer, Team>(tList.size());
		for (Team t : tList) {
			existingTeams.put(t.getTeamId(), t);
		}

		List<Player> pList = playerManager.getAll();
		existingPlayers = new HashMap<Integer, Player>(pList.size());
		for (Player p : pList) {
			existingPlayers.put(p.getPlayerId(), p);
		}

		jwxiReference = teamManager.getJWXIReference();
		System.out.println("Shared object maps initialised");
	}

	public Player getOrCreatePlayer(int id, String name, Team team) {
		Player foundPlayer = existingPlayers.get(id);
		if (foundPlayer == null) {
			System.out.println("Player " + id + " does not exist - creating");
			foundPlayer = new Player();
			foundPlayer.setPlayerId(id);
			foundPlayer.setScorecardName(name);
			foundPlayer.setTeam(team);
			foundPlayer.setStatus(true);
			foundPlayer = createPlayerInTransaction(foundPlayer);
			playerManager.merge(foundPlayer);
			existingPlayers.put(foundPlayer.getPlayerId(), foundPlayer);
		}
		return foundPlayer;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Player createPlayerInTransaction(Player p) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			playerManager.persist(p);
			tx.commit();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return p;
	}

	public Team getOrCreateTeam(int id, String name) {
		Team foundTeam = existingTeams.get(id);
		if (foundTeam == null) {
			System.out.println("Team " + id + " does not exist - creating");
			foundTeam = new Team();
			foundTeam.setTeamId(id);
			foundTeam.setTeamName(name);
			foundTeam = createTeamInTransaction(foundTeam);
			teamManager.merge(foundTeam);
			existingTeams.put(foundTeam.getTeamId(), foundTeam);
		}
		return foundTeam;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Team createTeamInTransaction(Team t) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			teamManager.persist(t);
			tx.commit();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return t;
	}

	public Ground getOrCreateGround(int id, String name) {
		Ground foundGround = existingGrounds.get(id);
		if (foundGround == null) {
			System.out.println("Ground " + id + " does not exist - creating");
			foundGround = new Ground();
			foundGround.setGroundId(id);
			foundGround.setGroundName(name);
			foundGround = createGroundInTransaction(foundGround);
			groundManager.merge(foundGround);
			existingGrounds.put(foundGround.getGroundId(), foundGround);
		}
		return foundGround;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Ground createGroundInTransaction(Ground g) {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			tx.begin();
			groundManager.persist(g);
			tx.commit();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return g;
	}

	public Team getJWXIReference() {
		return jwxiReference;
	}
}
package com.jwxicc.cricket.parse.cricketstatz;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;

@Singleton(name="sharedObjectsController")
@LocalBean
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SharedPersistenceObjectsController implements SharedPersistenceInterface {

	private Map<Integer, Ground> existingGrounds;
	private Map<Integer, Team> existingTeams;
	private Map<Integer, Player> existingPlayers;
	private Team jwxiReference;
	
	@EJB
	SharedPersistenceObjectsTransactionalCreator creator;
	
	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void initialiseMaps() {
		System.out.println("Initialising shared object maps");
		jwxiReference = creator.getJwxiReference();
		existingGrounds = creator.getAllGrounds();
		existingTeams = creator.getAllTeams();
		existingPlayers = creator.getAllPlayers();
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
			foundPlayer = creator.createPlayerInTransaction(foundPlayer);
			existingPlayers.put(foundPlayer.getPlayerId(), foundPlayer);
		}
		return foundPlayer;
	}

	public Team getOrCreateTeam(int id, String name) {
		Team foundTeam = existingTeams.get(id);
		if (foundTeam == null) {
			System.out.println("Team " + id + " does not exist - creating");
			foundTeam = new Team();
			foundTeam.setTeamId(id);
			foundTeam.setTeamName(name);
			foundTeam = creator.createTeamInTransaction(foundTeam);
			existingTeams.put(foundTeam.getTeamId(), foundTeam);
		}
		return foundTeam;
	}

	public Ground getOrCreateGround(int id, String name) {
		Ground foundGround = existingGrounds.get(id);
		if (foundGround == null) {
			System.out.println("Ground " + id + " does not exist - creating");
			foundGround = new Ground();
			foundGround.setGroundId(id);
			foundGround.setGroundName(name);
			foundGround = creator.createGroundInTransaction(foundGround);
			existingGrounds.put(foundGround.getGroundId(), foundGround);
		}
		return foundGround;
	}

	@Override
	public Team getJWXIReference() {
		return jwxiReference;
	}
}

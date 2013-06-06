package com.jwxicc.cricket.parse;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.Validate;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.WinType;
import com.jwxicc.cricket.interfaces.CompetitionManager;
import com.jwxicc.cricket.interfaces.GameFacade;
import com.jwxicc.cricket.interfaces.GameManagerLocal;
import com.jwxicc.cricket.interfaces.InningsFacade;
import com.jwxicc.cricket.interfaces.InningsManagerLocal;

/**
 * Persistence facade for saving parsed cricket data. Each method that adds an object with children
 * stores that object to the class, so that when adding children the client does not need to set its
 * persistent parent. That is the reason for stateful annotation.
 * 
 * @author cmurdoch
 * 
 */
@Stateful(mappedName = "parsePersistenceFacade")
@LocalBean
public class CricketParsePersistenceFacade implements GameFacade, InningsFacade {

	private Competition currentCompetition;
	private Game currentGame;
	private Inning currentInnings;
	private Partnership currentPartnership;

	@EJB
	GameManagerLocal gameManager;

	@EJB
	InningsManagerLocal innsManager;

	@EJB
	CompetitionManager compManager;
	
	@Override
	public void addInnings(Inning inns) {
		Validate.notNull(currentGame, "Adding an Innings requires a game to be set");
		innsManager.addInnings(inns);
		this.currentInnings = inns;
	}

	@Override
	public void addBatting(Batting bat) {
		Validate.notNull(currentInnings, "Adding a batting object requires an innings to be set");
		bat.setInning(currentInnings);
		innsManager.addBatting(bat);
	}

	@Override
	public void addBowling(Bowling bowl) {
		Validate.notNull(currentInnings, "Adding a bowling object requires an innings to be set");
		bowl.setInning(currentInnings);
		innsManager.addBowling(bowl);
	}

	@Override
	public void addPartnership(Partnership partnership) {
		Validate.notNull(currentInnings,
				"Adding a partnership object requires an innings to be set");
		partnership.setInning(currentInnings);
		innsManager.addPartnership(partnership);
		this.currentPartnership = partnership;
	}

	@Override
	public void addPartnershipPlayer(PartnershipPlayer partnershipPlayer) {
		Validate.notNull(currentPartnership,
				"Adding a partnership player requires a partnership to be set");
		partnershipPlayer.setPartnership(currentPartnership);
		innsManager.addPartnershipPlayer(partnershipPlayer);
	}

	@Override
	public void addGame(Game game) {
		Validate.notNull(currentCompetition, "Adding the game requires a competition to be set");
		game.setCompetition(currentCompetition);
		gameManager.addGame(game);
		this.currentGame = game;
	}

	@Override
	public void addDesignations(List<GamePlayerDesignation> designations) {
		Validate.notNull(currentGame, "Adding designations requires a game to be set");
		for (GamePlayerDesignation des : designations) {
			des.setGame(currentGame);
		}
		gameManager.addDesignations(designations);
	}

	@Override
	public WinType getWinTypeRef(int id) {
		return gameManager.getWinTypeRef(id);
	}

	public boolean validateGameFields(Game game) {
		return gameManager.validateRequiredFields(game);
	}
	
	public void setCompetition(int competitionId) {
		this.currentCompetition = compManager.getReference(competitionId);
	}
}
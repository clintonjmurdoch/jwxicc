package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.dbenum.InningsClosureType;
import com.jwxicc.cricket.interfaces.GameManagerLocal;
import com.jwxicc.cricket.interfaces.InningsManagerLocal;
import com.jwxicc.cricket.interfaces.TeamManager;

@ManagedBean(name = "matchManBean")
@ViewScoped
public class MatchManagementBean implements Serializable {

	@EJB
	GameManagerLocal gameManager;

	@EJB
	InningsManagerLocal inningsManager;

	@EJB
	TeamManager teamManager;

	private int selectedMatchId;
	private Game match;
	private int innsToSave;
	private int selectedInningsId;
	private Inning selectedInnings;
	private List<Player> inningsPlayers;

	public void loadMatch() {
		if (match == null) {
			match = gameManager.getGameForManagement(selectedMatchId);
		}
	}

	public void saveMatch() {
		gameManager.merge(match);
	}

	public void expandInnings() {
		if (selectedInningsId == 0) {
			this.selectedInnings = null;
		} else {
			for (Inning i : match.getInnings()) {
				if (i.getInningsId() == selectedInningsId) {
					this.selectedInnings = i;
					break;
				}
			}
			inningsPlayers = new ArrayList<Player>();
			for (Batting b : selectedInnings.getBattings()) {
				inningsPlayers.add(b.getPlayer());
			}
		}
	}

	public void saveInnings() {
		inningsManager.merge(selectedInnings);
		for (Batting b : selectedInnings.getBattings()) {
			inningsManager.mergeBatting(b);
		}
		for (Bowling b : selectedInnings.getBowlings()) {
			inningsManager.mergeBowling(b);
		}
	}

	public int getSelectedMatchId() {
		return selectedMatchId;
	}

	public void setSelectedMatchId(int selectedMatchId) {
		this.selectedMatchId = selectedMatchId;
	}

	public Game getMatch() {
		return match;
	}

	public void setMatch(Game match) {
		this.match = match;
	}

	public InningsClosureType[] getInningsClosureTypes() {
		return InningsClosureType.values();
	}

	public int getInnsToSave() {
		return innsToSave;
	}

	public void setInnsToSave(int innsToSave) {
		this.innsToSave = innsToSave;
	}

	public int getSelectedInningsId() {
		return selectedInningsId;
	}

	public void setSelectedInningsId(int selectedInningsId) {
		this.selectedInningsId = selectedInningsId;
	}

	public Inning getSelectedInnings() {
		return selectedInnings;
	}

	public void setSelectedInnings(Inning selectedInnings) {
		this.selectedInnings = selectedInnings;
	}

	public List<Player> getInningsPlayers() {
		return this.inningsPlayers;
	}

	// Fake getters and setters that work with values in the current match
	// This is because objects cannot be passed properly between model and view
	public Integer getWinner() {
		if (this.match.getWinner() != null) {
			return this.match.getWinner().getTeamId();
		} else
			return null;
	}

	public void setWinner(Integer winnerId) {
		if (winnerId != null && winnerId != 0) {
			this.match.setWinner(teamManager.getReference(winnerId));
		} else {
			this.match.setWinner(null);
		}
	}

	public Integer getWinType() {
		if (this.match.getWinType() != null) {
			return this.match.getWinType().getWinTypeId();
		} else {
			return null;
		}
	}

	public void setWinType(Integer winTypeId) {
		if (winTypeId != null && winTypeId != 0) {
			this.match.setWinType(gameManager.getWinTypeRef(winTypeId));
		} else {
			this.match.setWinType(null);
		}
	}

	public Integer getToss() {
		if (this.match.getToss() != null) {
			return this.match.getToss().getTeamId();
		} else {
			return null;
		}
	}

	public void setToss(Integer tossId) {
		if (tossId != null && tossId != 0) {
			this.match.setToss(teamManager.getReference(tossId));
		} else {
			this.match.setToss(null);
		}
	}

}
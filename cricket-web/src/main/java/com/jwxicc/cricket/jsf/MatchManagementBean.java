package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
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
		if (FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest()) {
			return;
		}
		if (match == null) {
			match = gameManager.getGameForManagement(selectedMatchId);
		}
	}

	public void saveMatch() {
		gameManager.merge(match);
	}

	public void updateScore() {
		System.out.println(selectedInnings.getInningsId() + " update score runs "
				+ selectedInnings.getRunsScored());
		inningsManager.merge(selectedInnings);
	}

	public void updateBatting() {
		// TODO
		for (Batting b : selectedInnings.getBattings()) {
			System.out.println("batting " + b.getBattingId() + " runs " + b.getScore());
			inningsManager.mergeBatting(b);
		}
	}

	public void updateBowling() {
		// TODO
	}

	public void addFOW() {
		System.out.println("addFOW");
		Partnership partnership = new Partnership();
		partnership.setPartnershipPlayers(new HashSet<PartnershipPlayer>(2));
		partnership.setInning(selectedInnings);

		PartnershipPlayer partnershipPlayer1 = new PartnershipPlayer();
		partnershipPlayer1.setPlayer(new Player());
		partnership.getPartnershipPlayers().add(partnershipPlayer1);

		PartnershipPlayer partnershipPlayer2 = new PartnershipPlayer();
		partnershipPlayer2.setPlayer(new Player());
		partnership.getPartnershipPlayers().add(partnershipPlayer2);

		selectedInnings.getPartnerships().add(partnership);
	}

	public void deleteFOW(int partnershipId) {
		System.out.println("delete partnership " + partnershipId);
		Iterator<Partnership> iterator = selectedInnings.getPartnerships().iterator();
		while (iterator.hasNext()) {
			Partnership nextPartnership = iterator.next();
			if (nextPartnership.getPartnershipId() == partnershipId) {
				if (partnershipId != 0) {
					inningsManager.removePartnership(partnershipId);
				}
				iterator.remove();
				break;
			}
		}
	}

	public void updateFOW() {
		System.out.println("update fall of wicket");
		for (Partnership p : selectedInnings.getPartnerships()) {
			Partnership partnership = inningsManager.mergePartnership(p);
			for (PartnershipPlayer player : p.getPartnershipPlayers()) {
				player.setPartnership(partnership);
				System.out.println("partnership player " + player.getPartnershipPlayerId()
						+ " playerid " + player.getPlayer().getPlayerId());
				inningsManager.mergePartnershipPlayer(player);
			}
		}
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
		// TODO remove method
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
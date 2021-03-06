package com.jwxicc.cricket.jsf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.richfaces.event.FileUploadEvent;

import com.jwxicc.cricket.entity.PlayerDetail;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.parse.CricketParseDataException;
import com.jwxicc.cricket.parse.player.JwxiccPlayerDetailParser;
import com.jwxicc.cricket.records.BattingRecord;
import com.jwxicc.cricket.records.BattingRecordsManager;
import com.jwxicc.cricket.records.BowlingRecord;
import com.jwxicc.cricket.records.BowlingRecordsManager;
import com.jwxicc.cricket.util.JwxiccUtils;

@ManagedBean(name = "playerBean")
@ViewScoped
public class PlayerBean implements Serializable {

	@EJB(name = "playerManager")
	PlayerManager playerManager;

	@EJB(name = "battingRecordsManager")
	BattingRecordsManager battingRecords;

	@EJB(name = "bowlingRecordsManager")
	BowlingRecordsManager bowlingRecords;

	@EJB
	JwxiccPlayerDetailParser playerParser;

	private int playerId;
	private Player player;

	private int teamId = JwxiccUtils.JWXICC_TEAM_ID;
	private List<Player> allPlayers;
	private int currentRowId = 1;

	// records for display
	private BattingRecord careerBatting;
	private BowlingRecord careerBowling;

	// 'advanced' player management, parse cap number
	private int parseCapNumber;
	private String parseResponse;

	public void editRow() {
		this.player = findSelectedPlayer(currentRowId);
		if (this.player.getPlayerDetail() == null) {
			this.player.setPlayerDetail(new PlayerDetail());
		}

	}

	public void doFileUpload(FileUploadEvent event) {
		this.player.getPlayerDetail().setImage(event.getUploadedFile().getData());
	}

	public void paintImage(OutputStream stream, Object obj) throws IOException {
		try {
			System.out.println("called paintImage");
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(player.getPlayerDetail()
					.getImage()));
			ImageIO.write(bi, "jpeg", stream);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void loadPlayer() {
		player = findSelectedPlayer(playerId);
	}

	private Player findSelectedPlayer(int id) {
		return playerManager.getPlayerForProfile(id);
	}

	public void savePlayerDetail() {
		if (this.player.getPlayerDetail().getPlayerDetailId() > 0) {
			System.out.println("Existing player detail");
		} else {
			System.out.println("New player detail");
		}
		PlayerDetail savedPlayerDetail = playerManager.savePlayerDetail(this.player
				.getPlayerDetail());
		this.player.setPlayerDetail(savedPlayerDetail);
		playerManager.merge(this.player);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved Player information for "
						+ this.player.getScorecardName(), null));
	}

	public void resetPlayerDetail() {
		this.player = this.allPlayers.get(this.currentRowId);
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Player> getAllPlayers() {
		if (allPlayers == null) {
			this.allPlayers = playerManager.getPlayersFromTeam(teamId);
		}
		return allPlayers;
	}

	public List<Player> getAllPlayersWithDetail() {
		if (allPlayers == null) {
			this.allPlayers = playerManager.getPlayersWithDetailFromTeam(teamId);
		}
		return allPlayers;
	}

	public int getCurrentRowId() {
		return currentRowId;
	}

	public void setCurrentRowId(int currentRowId) {
		this.currentRowId = currentRowId;
	}

	public BattingRecord getCareerBatting() {
		if (this.careerBatting == null) {
			this.careerBatting = battingRecords.getPlayerCareerRecord(playerId, false);
		}
		return careerBatting;
	}

	public void setCareerBatting(BattingRecord careerBatting) {
		this.careerBatting = careerBatting;
	}

	public BowlingRecord getCareerBowling() {
		if (this.careerBowling == null) {
			this.careerBowling = bowlingRecords.getPlayerCareerRecord(playerId, false);
		}
		return careerBowling;
	}

	public void setCareerBowling(BowlingRecord careerBowling) {
		this.careerBowling = careerBowling;
	}

	public int getParseCapNumber() {
		return parseCapNumber;
	}

	public void setParseCapNumber(int parseCapNumber) {
		this.parseCapNumber = parseCapNumber;
	}

	public void doPlayerParse() {
		System.out.println("player parse requested");
		try {
			if (this.parseCapNumber == 4747) {
				playerParser.parseAndSaveAllPlayerDetails();
				this.parseResponse = "Parse all players initiated - you will receive an email upon completion";
			} else {
				Player playerDetails = playerParser.parseAndSavePlayerDetails(parseCapNumber);
				this.parseResponse = "Successfully parsed player with id "
						+ playerDetails.getPlayerId() + ": " + playerDetails.getPlayerName();

			}
		} catch (CricketParseDataException e) {
			e.printStackTrace();
			this.parseResponse = "Failed to get the requested player information: "
					+ e.getMessage();
		}
	}

	public String getParseResponse() {
		return parseResponse;
	}

	public Comparator<Player> getCapNumberComparator() {
		return new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				int cap1 = (o1.getPlayerDetail() == null || o1.getPlayerDetail().getCapNumber() == 0) ? 9999
						: o1.getPlayerDetail().getCapNumber();
				int cap2 = (o2.getPlayerDetail() == null || o2.getPlayerDetail().getCapNumber() == 0) ? 9999
						: o2.getPlayerDetail().getCapNumber();

				return cap1-cap2;
			}
		};
	}
}

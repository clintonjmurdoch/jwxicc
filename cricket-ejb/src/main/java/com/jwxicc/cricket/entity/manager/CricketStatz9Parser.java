package com.jwxicc.cricket.entity.manager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Howout;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;
import com.jwxicc.cricket.entity.WicketDetail;
import com.jwxicc.cricket.entity.dbenum.DesignationType;
import com.jwxicc.cricket.entity.dbenum.InningsClosureType;
import com.jwxicc.cricket.entity.dbenum.WicketDetailType;
import com.jwxicc.cricket.interfaces.DismissalsManager;
import com.jwxicc.cricket.parse.CricketParseDataException;
import com.jwxicc.cricket.parse.CricketParsePersistenceFacade;
import com.jwxicc.cricket.parse.ImportedGameParser;
import com.jwxicc.cricket.parse.cricketstatz.DataTypeConverter;
import com.jwxicc.cricket.parse.cricketstatz.SharedPersistenceObjectsFacade;
import com.jwxicc.cricket.parse.equal_split.KeyMultiValueLine;
import com.jwxicc.cricket.parse.equal_split.KeyValueLine;

/**
 * Session Bean implementation class ParseBean
 */
@Stateless(name = "cs9Parser")
@Local(ImportedGameParser.class)
public class CricketStatz9Parser implements ImportedGameParser {

	@Resource
	SessionContext ctx;

	// These need to be looked up from JNDI
	CricketParsePersistenceFacade persistenceFacade;
	SharedPersistenceObjectsFacade sharedObjects;

	@EJB
	DismissalsManager dismissalsManager;

	/**
	 * Default constructor.
	 */
	public CricketStatz9Parser() {
	}

	@Override
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Future<Game> parseSingleGameText(String text, int competitionId, List<String> logMessages)
			throws CricketParseDataException {

		Game csGame = new Game();
		try {
			// inject the persistence facade per method
			persistenceFacade = (CricketParsePersistenceFacade) ctx
					.lookup("java:module/parsePersistenceFacade");

			sharedObjects = (SharedPersistenceObjectsFacade) ctx
					.lookup("java:module/sharedObjectsFacade");

			// check the text doesnt have leading/ending whitespace
			text = text.trim();
			System.out.println("Parsing Cricket Statz text");
			String[] lines = text.split("\n");

			// used to keep track of the current line
			int i = 0;
			// TODO not relevant for JW games
			boolean followonEnforced = false;
			int innsPerSide = 1;

			// the current line
			KeyValueLine line = new KeyValueLine(lines[i]);

			logMessages.add("Starting new match");
			System.out.println("Start a new match");
			// designations cant be persisted until game is
			List<GamePlayerDesignation> designations = new ArrayList<GamePlayerDesignation>();

			csGame.setInnings(new HashSet<Inning>(2));
			boolean gamePersisted = false;
			// only completed games are added
			csGame.setGameState("Completed");
			// used for home team and result
			int homeTeamValue = 0;
			Team homeTeam;
			Team awayTeam;
			// helper to create match result
			MatchResultHelper resultHelper = new MatchResultHelper(innsPerSide);

			// Teams are out of order so they need to be stored here
			// first
			Team team1 = null;
			Team team2 = null;
			// Loop through a match, stop when endmatch=true
			while (!line.keyEquals("Endmatch")) {
				System.out.println("adding: " + line);

				persistenceFacade.setCompetition(competitionId);

				// If statements to check the line (key)
				if (line.keyEquals("Type")) {
					if (line.getIntValue() == 2) {
						// override default of 1 inns each
						innsPerSide = 2;
						resultHelper = new MatchResultHelper(innsPerSide);
					}
				} else if (line.keyEquals("Date")) {
					try {
						Date gameDate = DataTypeConverter.parseStringDate(line.getValue());

						csGame.setDate(gameDate);
					} catch (ParseException e) {
						throw new CricketParseDataException(
								"Date format given does not match the expected date format of dd/mm/yyyy",
								e);
					}
				} else if (line.keyEquals("Ground")) {
					// Parse 'value'
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					Ground g = sharedObjects.getOrCreateGround(multiLine.getIntValueAt(0),
							multiLine.getValueAt(1));
					csGame.setGround(g);
				} else if (line.keyEquals("Grade")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					Competition c = new Competition();
					c.setCompetitionId(multiLine.getIntValueAt(0));
					c.setGrade(multiLine.getValueAt(1));
					csGame.setCompetition(c);
					// TODO ignoring club
				} else if (line.keyStartsWith("Team")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					Team t = sharedObjects.getOrCreateTeam(multiLine.getIntValueAt(0),
							multiLine.getValueAt(1));
					if (line.keyIsNumber(1)) {
						team1 = t;
					} else if (line.keyIsNumber(2)) {
						team2 = t;
					}
				}
				// captain
				else if (line.keyStartsWith("Captain")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					if (multiLine.getIntValueAt(0) > 0) {
						Team teamForCaptain = null;
						if (line.keyIsNumber(1)) {
							teamForCaptain = team1;
						} else {
							teamForCaptain = team2;
						}
						Player captain = sharedObjects
								.getOrCreatePlayer(multiLine.getIntValueAt(0),
										multiLine.getValueAt(1), teamForCaptain);
						GamePlayerDesignation designation = new GamePlayerDesignation();
						designation.setDesignationType(DesignationType.CAPTAIN);
						designation.setPlayer(captain);
						designations.add(designation);
					}
				}
				// keeper
				else if (line.keyStartsWith("Keeper")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					if (multiLine.getIntValueAt(0) > 0) {
						Team teamForKeeper = null;
						if (line.keyIsNumber(1)) {
							teamForKeeper = team1;
						} else {
							teamForKeeper = team2;
						}
						Player keeper = sharedObjects.getOrCreatePlayer(multiLine.getIntValueAt(0),
								multiLine.getValueAt(1), teamForKeeper);
						GamePlayerDesignation designation = new GamePlayerDesignation();
						designation.setDesignationType(DesignationType.KEEPER);
						designation.setPlayer(keeper);
						designations.add(designation);
					}
				}
				// TODO ignoring umpire
				else if (line.keyEquals("Home")) {
					// specify home team as 0 or 1 and/or 2
					// only allow 1 value
					try {
						homeTeamValue = line.getIntValue();
					} catch (NumberFormatException e) {
						throw new CricketParseDataException("Only 1 home team can be specified", e);
					}
					if (homeTeamValue == 2) {
						// team2 is the home team
						awayTeam = team1;
						homeTeam = team2;
					} else {
						// team1 is the home team
						awayTeam = team2;
						homeTeam = team1;
					}
					csGame.setHomeTeam(homeTeam);
					csGame.setAwayTeam(awayTeam);
				}
				// Only using the follow-on part of config
				else if (line.keyEquals("Config")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					if (innsPerSide == 2 && multiLine.getIntValueAt(4) == 1) {
						followonEnforced = true;
					}
				}
				// TODO ignoring umpires
				// TODO ignoring scorers
				else if (line.keyEquals("Comment")) {
					csGame.setComment(line.getValue());
				} else if (line.keyEquals("Round")) {
					csGame.setRound(line.getValue());
				}
				// TODO ignoring toss
				else if (line.keyEquals("Toss")) {
					if (line.getIntValue() != 0) {
						if (line.getIntValue() == 1) {
							csGame.setToss(team1);
						} else if (line.getIntValue() == 2) {
							csGame.setToss(team2);
						}
					}
				}
				// TODO ignoring bonus
				else if (line.keyEquals("MOM")) {
					KeyMultiValueLine multiLine = new KeyMultiValueLine(line);
					if (multiLine.getIntValueAt(0) > 0) {
						Player mom = sharedObjects.getOrCreatePlayer(multiLine.getIntValueAt(0),
								multiLine.getValueAt(1), sharedObjects.getJWXIReference());
						GamePlayerDesignation designation = new GamePlayerDesignation();
						designation.setDesignationType(DesignationType.MAN_OF_MATCH);
						designation.setPlayer(mom);
						designations.add(designation);
					}
				} else if (line.keyEquals("Result")) {
					// result is 0, 1 or 2
					int res = line.getIntValue();
					switch (res) {
					case 0:
						// no result
						break;
					case 1:
						// first listed team won
						csGame.setWinner(team1);
						break;
					case 2:
						// second listed team won
						csGame.setWinner(team2);
						break;
					}
				} else if (line.keyEquals("Innings")) {
					// All game stuff should be done so persist it
					if (gamePersisted == false) {
						gamePersisted = persistGame(csGame, designations);
						logMessages.add("Created game id " + csGame.getGameId() + " between "
								+ csGame.getHomeTeam().getTeamName() + " and "
								+ csGame.getAwayTeam().getTeamName());
					}
					// if it is still false, it broke
					if (!gamePersisted) {
						throw new CricketParseDataException(
								"Failed to put together sufficient game information", null);
					} else {
						System.out.println("Created game id: " + csGame.getGameId());
					}

					// calculate the score, wickets and overs
					int aggScore = 0;
					int wicketsLost = 0;
					// int array with full overs at pos 0 and part overs at
					// pos 1
					int[] oversBowled = new int[2];

					Inning inning = new Inning();

					Team currentTeam = null;
					Team oppTeam = null;

					// determine innings order and which team they belong to
					int innsNumber = line.getIntValue();
					inning.setInningsOfMatch(innsNumber);
					if (followonEnforced == false) {
						// this is a 1 inns match or a regular 2 inns match
						// inns 1 and 3 (if 2 inns) belong to team 1
						// inns 2 and 4 (if 2 inns) belong to team 2
						if ((innsNumber % 2) == 1) { // odd inns no
							currentTeam = team1;
							oppTeam = team2;
						} else { // even inns no
							currentTeam = team2;
							oppTeam = team1;
						}
					}
					if (followonEnforced) {
						// this is a 2 inns match with follow on enforced
						// TODO - we dont play 2 inns matches
					}

					inning.setTeam(currentTeam);
					inning.setGame(csGame);

					// enough data exists to save the inning
					persistenceFacade.addInnings(inning);

					// add Partnerships helper for the innings
					PartnershipsHelper partnershipsHelper = new PartnershipsHelper(inning);

					// increment i as we know it is innings line
					i++;
					line = new KeyValueLine(lines[i]);
					// Loop through until the IResult line - the while
					while (!line.keyEquals("IResult")) {
						// copied from outer while loop
						System.out.println("adding: " + line);

						// If statements to check the line (key)
						if (line.keyStartsWith("Batsman")) {
							Batting batting = new Batting();
							int batpos = Integer.parseInt(line.getKey().substring(7));
							batting.setBattingPos(batpos);

							// Split up the batting values
							KeyMultiValueLine multiLine = new KeyMultiValueLine(line);

							// pos 0-1: batter id and name
							Player batter = sharedObjects.getOrCreatePlayer(
									multiLine.getIntValueAt(0), multiLine.getValueAt(1),
									currentTeam);
							batting.setPlayer(batter);

							// persist with the innings manager
							persistenceFacade.addBatting(batting);

							// pos 2 - how out
							int howoutid = multiLine.getIntValueAt(2);
							Howout howout = null;
							if (0 <= howoutid && howoutid <= 18) {
								howout = dismissalsManager.findHowout(howoutid);
							} else {
								howout = dismissalsManager.findHowout(0);
							}
							batting.setHowout(howout);
							// if there is a retired not out, add it to the
							// partnerships helper
							// these are ids 7 and 13
							if (howoutid == 7 || howoutid == 13) {
								partnershipsHelper.addRetiredNotOutBattingPosition(batpos);
							}

							// pos 3-6 done in method
							boolean batterOut = assignWicketsToPlayers(batting, multiLine,
									howoutid, oppTeam);
							if (batterOut) {
								wicketsLost += 1;
							}

							// pos 7 - score
							int score = multiLine.getIntValueAt(7);
							batting.setScore(score);
							aggScore += score;

							// pos 8-9 - fow score and out player pos
							// batpos is actually the wicket to fall
							// order
							int fowScore = multiLine.getIntValueAt(8);
							int fowBatPos = multiLine.getIntValueAt(9);
							partnershipsHelper.addBatpos(batpos, batting);
							if (fowBatPos != 0) {
								partnershipsHelper.addFall(fowBatPos, fowScore);
							}

							// pos 10 - 4s
							batting.setFours(multiLine.getIntValueAt(10));

							// pos 11 - 6s
							batting.setSixes(multiLine.getIntValueAt(11));

							// pos 12 - balls
							batting.setBalls(multiLine.getIntValueAt(12));

							// pos 13 - TODO minutes

						} else if (line.keyStartsWith("Bowler")) {
							Bowling bowling = new Bowling();
							int bowlPos = Integer.parseInt(line.getKey().substring(6));
							bowling.setBowlingPos(bowlPos);

							// Split up the bowling values
							KeyMultiValueLine multiLine = new KeyMultiValueLine(line);

							// pos 0-1 - bowler id and name
							Player bowler = sharedObjects.getOrCreatePlayer(
									multiLine.getIntValueAt(0), multiLine.getValueAt(1), oppTeam);
							bowling.setPlayer(bowler);

							// pos 2 - overs
							BigDecimal overs = BigDecimal.valueOf(Double.valueOf(multiLine
									.getValueAt(2)));
							bowling.setOvers(overs);
							oversBowled[0] += overs.intValue();
							oversBowled[1] += (overs.remainder(BigDecimal.ONE)
									.multiply(BigDecimal.TEN)).intValue();
							System.out.println("overs remainder: " + oversBowled[1]);

							// pos 3 - maidens
							bowling.setMaidens(multiLine.getIntValueAt(3));

							// pos 4 - wickets
							bowling.setWickets(multiLine.getIntValueAt(4));

							// pos 5 - runs
							bowling.setRuns(multiLine.getIntValueAt(5));

							// pos 6 - wides
							bowling.setWides(multiLine.getIntValueAt(6));

							// pos 7 - no balls
							bowling.setNoBalls(multiLine.getIntValueAt(7));

							// persist with inns manager
							persistenceFacade.addBowling(bowling);

						} else if (line.keyEquals("LegByes")) {
							inning.setLegByes(line.getIntValue());
							aggScore += line.getIntValue();
						} else if (line.keyEquals("Byes")) {
							inning.setByes(line.getIntValue());
							aggScore += line.getIntValue();
						} else if (line.keyEquals("Wides")) {
							inning.setWides(line.getIntValue());
							aggScore += line.getIntValue();
						} else if (line.keyEquals("NoBalls")) {
							inning.setNoBalls(line.getIntValue());
							aggScore += line.getIntValue();
						} else if (line.keyEquals("Penalties")) {
							inning.setPenalties(line.getIntValue());
							aggScore += line.getIntValue();
						}

						// done, increment the line
						i++;
						line = new KeyValueLine(lines[i]);
					}
					// This is the IResult, as it ended the while-loop for innings
					InningsClosureType closureType = InningsClosureType.forInt(line.getIntValue());
					inning.setClosureType(closureType);

					inning.setRunsScored(aggScore);
					inning.setWicketsLost(wicketsLost);

					logMessages.add("Innings created with score " + wicketsLost + "/" + aggScore
							+ " for " + inning.getTeam().getTeamName() + " in game "
							+ csGame.getGameId());

					// after the inns stuff is done, do the Partnerships
					boolean notoutsCreated = partnershipsHelper.createFallOfWickets();
					if (notoutsCreated) {
						logMessages
								.add("All partnerships were successfully created for innings of "
										+ inning.getTeam().getTeamName() + " in game "
										+ csGame.getGameId());
					} else {
						logMessages
								.add("WARNING: Unable to create partnerships for innings of "
										+ inning.getTeam().getTeamName() + " in game "
										+ csGame.getGameId());
					}

					// create a bigdecimal from the int array
					while (oversBowled[1] >= 6) {
						oversBowled[0] += 1;
						oversBowled[1] -= 6;
					}
					String oversComposition = "" + oversBowled[0] + oversBowled[1];
					System.out.println("oversComposition: " + oversComposition);
					BigInteger bigInteger = new BigInteger(oversComposition);
					inning.setOversFaced(new BigDecimal(bigInteger, 1));

					// inns ended, send to result helper
					resultHelper.addInnings(inning);

					// done with IResult, increment line
					// line will be incremented next
				}

				// Increment the line for the while-loop for the match
				i++;
				line = new KeyValueLine(lines[i]);
			}

			// Match is ended at this point
			// Calculate result/winner stuff
			resultHelper.addWinInfo(csGame);

			logMessages.add("Completed processing game id " + csGame.getGameId());
			System.out.println("Completed Parsing Cricket Statz text for game id "
					+ csGame.getGameId());

			return new AsyncResult<Game>(csGame);
		} catch (Exception e) {
			logMessages.add("Failed to save game id " + csGame.getGameId()
					+ ". All data for this game will be rolled back and it should be retried");
			ctx.setRollbackOnly();
			if (e instanceof CricketParseDataException) {
				throw (CricketParseDataException) e;
			} else {
				throw new CricketParseDataException(e.getMessage(), e);
			}
		}
	}

	private WicketDetail buildWicketDetail(int id, String name, WicketDetailType type, Team team) {
		if (id > 0) {
			WicketDetail wicketDetail = new WicketDetail();
			wicketDetail.setPlayer(sharedObjects.getOrCreatePlayer(id, name, team));
			wicketDetail.setWicketType(type);
			return wicketDetail;
		} else {
			return null;
		}

	}

	private boolean assignWicketsToPlayers(Batting batting, KeyMultiValueLine multiLine,
			int howout, Team team) {

		// pos 3-4: wicket assist
		// pos 5-6: wicket assist or bowler
		// (depending on wicket type)
		// Set the wicket detail based on the wicket
		// type
		int assist1Id = multiLine.getIntValueAt(3);
		String assist1Name = multiLine.getValueAt(4);
		int assist2Id = multiLine.getIntValueAt(5);
		String assist2Name = multiLine.getValueAt(6);

		// use the howoutid to determine how to use
		// assisters
		switch (howout) {
		// 0: dnb
		// 1: not out
		// 2: bowled
		case 2:
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		// 3: caught
		case 3:
			addWicketDetail(batting,
					buildWicketDetail(assist1Id, assist1Name, WicketDetailType.CT, team));
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		// 4: caught and bowled
		case 4:
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.CT, team));
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		// 5: Hit wicket
		// TODO does it go to bowler?
		// 6: LBW
		case 6:
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		// 7: retired hurt
		// 8: run out
		case 8:
			addWicketDetail(batting,
					buildWicketDetail(assist1Id, assist1Name, WicketDetailType.RO_1, team));
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.RO_2, team));
			break;
		// 9: stumped
		case 9:
			addWicketDetail(batting,
					buildWicketDetail(assist1Id, assist1Name, WicketDetailType.ST, team));
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		// 10: obstructed field
		// 11: handled ball
		// 12: retired out
		// 13: retired not out
		// 14: timed out
		// 15: hit the ball twice
		// 16: absent hurt
		// 17: absent ill
		// 18: caught behind
		case 18:
			addWicketDetail(batting,
					buildWicketDetail(assist1Id, assist1Name, WicketDetailType.CT, team));
			addWicketDetail(batting,
					buildWicketDetail(assist2Id, assist2Name, WicketDetailType.B, team));
			break;
		}

		// determine whether or not the player is out
		switch (howout) {
		case 0:
		case 1:
		case 7:
		case 13:
		case 16:
		case 17:
			return false;
		default:
			return true;
		}

	}

	private void addWicketDetail(Batting batting, WicketDetail wicketDetail) {
		if (wicketDetail != null) {
			wicketDetail.setBatting(batting);
			dismissalsManager.persistWicketDetail(wicketDetail);
		}
	}

	private boolean persistGame(Game game, List<GamePlayerDesignation> designations) {
		// validate required fields are set
		if (persistenceFacade.validateGameFields(game)) {
			persistenceFacade.addGame(game);
			persistenceFacade.addDesignations(designations);
			return true;
		} else {
			return false;
		}
	}

	private class MatchResultHelper {
		private int innsPerSide;
		private List<Inning> innsList;

		public MatchResultHelper(int innsPerSide) {
			this.innsPerSide = innsPerSide;
			if (innsPerSide == 1) {
				this.innsList = new ArrayList<Inning>(2);
			} else {
				this.innsList = new ArrayList<Inning>(4);
			}
		}

		public void addInnings(Inning inns) {
			this.innsList.add(inns);
		}

		public void addWinInfo(Game csGame) {
			// deal only with 2 innings games
			if (innsPerSide == 1) {
				// inns have been added in order
				int runsDif = innsList.get(0).getRunsScored() - innsList.get(1).getRunsScored();
				if (runsDif > 0) {
					// first to bat won, so it is a runs victory on 1st inns
					csGame.setWinType(persistenceFacade.getWinTypeRef(1));
					csGame.setWinMargin(runsDif);
				} else if (runsDif < 0) {
					int wkts = 10 - innsList.get(1).getWicketsLost();
					csGame.setWinType(persistenceFacade.getWinTypeRef(2));
					csGame.setWinMargin(wkts);
				}
			}
		}

	}

	private class PartnershipsHelper {
		// needs bat pos, player object as batter, score

		/**
		 * The innings that these Partnersips are for
		 */
		private Inning inning;
		/**
		 * List holding the batting position of that player to be dismissed at each wicket, in order
		 * of wickets
		 */
		private List<Integer> outPlayerBatPosList = new ArrayList<Integer>();
		/**
		 * Map with a key of the batting position and the player batting at that position in this
		 * innings
		 */
		private Map<Integer, Batting> batPosMap = new HashMap<Integer, Batting>();
		/**
		 * List holding the score at each wicket, in order of wickets
		 */
		private List<Integer> scoreAtFowList = new ArrayList<Integer>();
		/**
		 * The first batting position to retire not out
		 */
		private int retiredNotOutBattingPosition;

		public PartnershipsHelper(Inning inning) {
			this.inning = inning;
		}

		public void addBatpos(int currentBatPos, Batting currentBatter) {
			batPosMap.put(Integer.valueOf(currentBatPos), currentBatter);
		}

		public void addFall(int outPlayerBatPos, int scoreAtFow) {
			this.outPlayerBatPosList.add(Integer.valueOf(outPlayerBatPos));
			this.scoreAtFowList.add(Integer.valueOf(scoreAtFow));
		}

		public boolean createFallOfWickets() {
			// loop through the lists and map, matching them up and creating the
			// Partnership structure
			int wicketsLost = outPlayerBatPosList.size();
			boolean canDoNotOuts = true;
			int[] currentBatters = new int[] { 1, 2 };
			int lastWicketScore = 0;
			if (wicketsLost != 0) {

				// if one of the batters retired not out, cant do not out FOWs
				if (this.retiredNotOutBattingPosition == currentBatters[0]
						|| this.retiredNotOutBattingPosition == currentBatters[1]) {
					canDoNotOuts = false;
				}

				for (int i = 0; i < wicketsLost; i++) {
					int wicket = i + 1;

					Partnership partnership = new Partnership();
					partnership.setWicket(wicket);
					int scoreAtEnd = scoreAtFowList.get(i);
					partnership.setScoreAtEnd(scoreAtEnd);
					persistenceFacade.addPartnership(partnership);
					int outPlayerPos = outPlayerBatPosList.get(i);

					// create partnership player for out player
					PartnershipPlayer outPlayer = new PartnershipPlayer();
					outPlayer.setPlayer(batPosMap.get(outPlayerPos).getPlayer());
					outPlayer.setBattingPosition(outPlayerPos);
					outPlayer.setOutStatus(true);
					persistenceFacade.addPartnershipPlayer(outPlayer);

					// create partnership player for not out player
					if (canDoNotOuts) {
						// set runs scored and update last wicket score
						partnership.setRunsScored(scoreAtEnd - lastWicketScore);
						lastWicketScore = scoreAtEnd;
						PartnershipPlayer notOutPlayer = new PartnershipPlayer();
						notOutPlayer.setOutStatus(false);
						// determine which player is not out by keeping track of
						// the current batters, then updating the out batsman to
						// wicket number + 2
						// the first 'current batter' is out
						if (outPlayerPos == currentBatters[0]) {
							notOutPlayer.setPlayer(batPosMap.get(currentBatters[1]).getPlayer());
							notOutPlayer.setBattingPosition(currentBatters[1]);
							currentBatters[0] = wicket + 2;
						}
						// the second 'current batter' is out
						else if (outPlayerPos == currentBatters[1]) {
							notOutPlayer.setPlayer(batPosMap.get(currentBatters[0]).getPlayer());
							notOutPlayer.setBattingPosition(currentBatters[0]);
							currentBatters[1] = wicket + 2;
						}
						persistenceFacade.addPartnershipPlayer(notOutPlayer);
						// check whether not outs can continue to be done
						if (this.retiredNotOutBattingPosition == wicket + 2) {
							canDoNotOuts = false;
						}
					}

				}
			}

			// if the team is not bowled out, there may be a not out Partnership
			// at the end
			// if the last batter is not out, create a new partnership
			// make sure the last batpos is set
			if (canDoNotOuts && !InningsClosureType.ALLOUT.equals(inning.getClosureType())
					&& (batPosMap.get(wicketsLost + 2).getHowout().getHowOutid() == 1)) {
				// there should be a not out partnership at the end
				// for both current batters left over
				Partnership finalPartnership = new Partnership();
				finalPartnership.setScoreAtEnd(inning.getRunsScored());
				finalPartnership.setWicket(wicketsLost + 1);
				finalPartnership.setRunsScored(inning.getRunsScored() - lastWicketScore);
				persistenceFacade.addPartnership(finalPartnership);
				for (int i = 0; i < 2; i++) {
					PartnershipPlayer fowWicket = new PartnershipPlayer();
					fowWicket.setOutStatus(false);
					fowWicket.setPlayer(batPosMap.get(currentBatters[i]).getPlayer());
					fowWicket.setBattingPosition(currentBatters[i]);
					persistenceFacade.addPartnershipPlayer(fowWicket);
				}

			}
			return canDoNotOuts;

		}

		public void addRetiredNotOutBattingPosition(int batPos) {
			// only store the lowest
			if (this.retiredNotOutBattingPosition == 0
					|| batPos < this.retiredNotOutBattingPosition) {
				this.retiredNotOutBattingPosition = batPos;
			}
		}

	}

}
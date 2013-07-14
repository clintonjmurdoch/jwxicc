package com.jwxicc.cricket.parse.player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.PlayerDetail;
import com.jwxicc.cricket.entity.User;
import com.jwxicc.cricket.mail.MailSender;
import com.jwxicc.cricket.parse.CricketParseDataException;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = JwxiccPlayerDetailParser.EJB_NAME)
@LocalBean
public class JwxiccPlayerDetailParser {

	public static final String EJB_NAME = "playerDetailParser";

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@Resource
	SessionContext ctx;

	private static final String FULL_NAME = "Full Name:";
	private static final String BORN = "Born:";
	private static final String NICKNAMES = "Nicknames:";
	private static final String BATTING = "Batting style:";
	private static final String BOWLING = "Bowling style:";
	private static final String FIELDING = "Fielding position:";
	private static final String TEAMS = "Teams:";
	private static final String SHIRT = "Shirt number:";

	private static final String JWXICC_ADDRESS = "http://www.jwxicc.com/";
	private static final String PAGE_EXT = ".php";
	private static final int CONNECT_TIMEOUT = 20000;

	private static final DateFormat formatter = new SimpleDateFormat("MMMMM d, yyyy");

	@Asynchronous
	public void parseAndSaveAllPlayerDetails() {
		// do this async, so send an email on failure
		// get current user
		String user = ctx.getCallerPrincipal().getName();
		System.out.println("parse and save all player details request by: " + user);
		Validate.notEmpty(user);
		StringBuilder mailText = new StringBuilder(500);

		User currentUser = em.find(User.class, user);

		// for transaction boundary issues, this must inject an instance of itself
		JwxiccPlayerDetailParser parser = (JwxiccPlayerDetailParser) ctx.lookup("java:module/"
				+ EJB_NAME);

		for (int i = 1; i <= 110; i++) {
			try {
				parser.parseAndSavePlayerDetails(i);
				mailText.append("Completed player details for cap number [" + i + "]" + "\n");
			} catch (CricketParseDataException e) {
				StringWriter writer = new StringWriter();
				e.printStackTrace(new PrintWriter(writer));
				System.err.println(writer.toString());
				mailText.append("*** FAILED **** Player details not added for cap number " + i
						+ "\n");
				mailText.append(writer + "\n");
			}
		}
		try {
			System.out.println("Sending finalise email");
			new MailSender().sendEmail(currentUser.getEmail(),
					"Parse JWXICC players has completed", mailText.toString());
		} catch (MessagingException e) {
			System.err.println("Mail sending failed");
			e.printStackTrace();
			System.err.println("Tried to send the following text as email");
			System.err.println(mailText.toString());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Player parseAndSavePlayerDetails(int capNumber) throws CricketParseDataException {
		Document jwxiccPlayer;
		String url = JWXICC_ADDRESS + capNumber + PAGE_EXT;
		System.out.println(url);
		try {
			jwxiccPlayer = Jsoup.connect(url).timeout(CONNECT_TIMEOUT).get();
			Element mainDiv = jwxiccPlayer.getElementById("main");

			Elements foundByText;

			// Get full name
			String fullName = "";
			String name = mainDiv.getElementsByTag("h3").get(0).text();
			Player player = null;

			foundByText = mainDiv.getElementsContainingOwnText(FULL_NAME);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				fullName = foundByText.get(0).ownText().replaceAll("(?i)" + FULL_NAME, "").trim();
				System.out.println(fullName);
				player = this.findPlayer(fullName);

			} else {
				player = this.findPlayer(name);

			}

			// determine the player
			if (player == null) {
				throw new NullPointerException("Cannot find the player details for " + name
						+ ". Full name: " + fullName);
			}
			System.out.println("player name: " + player.getScorecardName());

			player.setPlayerName(name);

			PlayerDetail playerDetail;
			if (player.getPlayerDetail() != null) {
				playerDetail = player.getPlayerDetail();
			} else {
				playerDetail = new PlayerDetail();
			}

			playerDetail.setFullname(fullName);
			playerDetail.setCapNumber(capNumber);

			// get nicknames
			foundByText = mainDiv.getElementsContainingOwnText(NICKNAMES);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String nickname = foundByText.get(0).ownText().replaceAll("(?i)" + NICKNAMES, "")
						.trim();
				System.out.println(nickname);
				playerDetail.setNicknames(nickname);
			}

			// get born
			foundByText = mainDiv.getElementsContainingOwnText(BORN);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String born = foundByText.get(0).ownText().replaceAll("(?i)" + BORN, "").trim();
				System.out.println(born);
				if (born.contains(",")) {
					// only if it is set, will it contain ','
					// 1st comma is part of date, 2nd separates from place
					int firstComma = born.indexOf(",");
					// plus 4 chars for the year, 1 for the comma, 1 for the space
					int endOfBirthdate = firstComma + 6;
					Date dob = null;
					try {
						// parse the date
						String dobString = born.substring(0, endOfBirthdate);
						dob = formatter.parse(dobString);
						playerDetail.setBirthdate(dob);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
					// if there is no DOB, dont bother with the place
					if (dob != null) {
						// check the length exceeds the end of birth date
						if (born.length() > endOfBirthdate) {
							// allow for the comma
							String birthplace = born.substring(endOfBirthdate + 1).trim();
							if (StringUtils.isNotBlank(birthplace)) {
								playerDetail.setBirthplace(birthplace);
							}
						}
					}

				}
			}

			// get batting style
			foundByText = mainDiv.getElementsContainingOwnText(BATTING);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String batting = foundByText.get(0).ownText().replaceAll("(?i)" + BATTING, "")
						.trim();
				System.out.println(batting);
				playerDetail.setBattingStyle(batting);
			}

			// get bowling style
			foundByText = mainDiv.getElementsContainingOwnText(BOWLING);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String bowling = foundByText.get(0).ownText().replaceAll("(?i)" + BOWLING, "")
						.trim();
				System.out.println(bowling);
				playerDetail.setBowlingStyle(bowling);
			}

			// get fielding position
			foundByText = mainDiv.getElementsContainingOwnText(FIELDING);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String fielding = foundByText.get(0).ownText().replaceAll("(?i)" + FIELDING, "")
						.trim();
				System.out.println(fielding);
				playerDetail.setFieldingPositions(fielding);
			}

			// get teams
			foundByText = mainDiv.getElementsContainingOwnText(TEAMS);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String teams = foundByText.get(0).ownText().replaceAll("(?i)" + TEAMS, "").trim();
				System.out.println(teams);
				playerDetail.setTeams(teams);
			}

			// get shirt number
			foundByText = mainDiv.getElementsContainingOwnText(SHIRT);
			if (CollectionUtils.isNotEmpty(foundByText)) {
				String shirt = foundByText.get(0).ownText().replaceAll("(?i)" + SHIRT, "").trim();
				System.out.println(shirt);
				playerDetail.setShirtNumber(Integer.parseInt(shirt));
			}

			// get the image
			Elements imgElements = mainDiv.getElementsByTag("img");
			if (!imgElements.isEmpty()) {
				Element imgElement = imgElements.get(0);
				String imgPath = imgElement.attr("src");
				System.out.println(imgPath);
				if (StringUtils.isNotEmpty(imgPath)) {
					try {
						Response imgResponse = Jsoup.connect(JWXICC_ADDRESS + imgPath)
								.timeout(CONNECT_TIMEOUT).ignoreContentType(true).execute();
						playerDetail.setImage(imgResponse.bodyAsBytes());
					} catch (Exception e) {
						System.err.println("Could not get image");
					}
				}
			}

			// get the profile paragraphs
			// find the fb like part
			int fbIndex = mainDiv.getElementsByClass("fb-like").get(0).elementSiblingIndex();
			System.out.println("iframe index: " + fbIndex);
			// get elements after the fb like
			Elements elementsAfterFB = mainDiv.getElementsByIndexGreaterThan(fbIndex);
			// remove leading line breaks
			System.out.println("elements after fb: " + elementsAfterFB.size());

			for (int i = 0; i < elementsAfterFB.size(); i++) {
				Element e = elementsAfterFB.get(i);
				if ("br".equals(e.tagName())) {
					elementsAfterFB.remove(e);
				} else {
					break;
				}
			}

			System.out.println("elements after fb, brs removed: " + elementsAfterFB.size());
			String profileText = "";

			for (int i = 0; i < elementsAfterFB.size(); i++) {
				Element e = elementsAfterFB.get(i);
				if ("p".equals(e.tagName())) {
					System.out.println(e.ownText());
					if (!e.getElementsByTag("a").isEmpty()) {
						break;
					}
					if (StringUtils.isNotBlank(e.ownText())) {
						profileText += e.ownText();
						profileText += "\n\n";
					}
				}

			}

			if (StringUtils.isNotBlank(profileText)) {
				playerDetail.setProfile(profileText.trim());
			}

			if (player.getPlayerDetail() == null) {
				em.persist(playerDetail);
			}
			player.setPlayerDetail(playerDetail);
			
			return player;
		} catch (Exception e) {
			throw new CricketParseDataException("Failed to get player details from url: " + url, e);
		}
	}

	private Player findPlayer(String name) {
		// create query
		Query query = em
				.createQuery("from Player p where teamid = :team and p.scorecardName like :name");
		query.setParameter("team", JwxiccUtils.JWXICC_TEAM_ID);
		String nameParam;

		// try just last name
		int lastSpaceIndex = name.lastIndexOf(" ");
		String lastname = name.substring(lastSpaceIndex + 1);
		System.out.println("Searching: " + lastname);
		query.setParameter("name", "%" + lastname);

		List<Player> resultList = query.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			// cant find the last name, could be a funky name
			// if there are 3 names or more, try second last name
			if (StringUtils.countMatches(name, " ") != 2) {
				// get previous names
				String previousNames = name.substring(0, lastSpaceIndex);
				String secondLastName = previousNames.substring(previousNames.lastIndexOf(" ") + 1);
				System.out.println("Searching: " + secondLastName);
				query.setParameter("name", "%" + secondLastName);

				resultList = query.getResultList();
				if (resultList.size() == 1) {
					return resultList.get(0);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (resultList.size() == 1) {
			return resultList.get(0);
		} else {
			// refine query
			// first initial, last name
			String firstNames = name.substring(0, lastSpaceIndex);
			nameParam = firstNames.substring(0, 1) + "%" + lastname;
			System.out.println("Searching: " + nameParam);
			query.setParameter("name", nameParam);
			resultList = query.getResultList();
			if (CollectionUtils.isEmpty(resultList)) {
				return null;
			} else if (resultList.size() == 1) {
				return resultList.get(0);
			} else {
				// refine to initials with wildcard for first name
				String[] firstNameArray = firstNames.split(" ");
				String initials = "";
				for (String givenName : firstNameArray) {
					initials += givenName.substring(0, 1);
				}
				nameParam = initials + " " + lastname;
				System.out.println("Searching: " + nameParam);
				query.setParameter("name", nameParam);
				resultList = query.getResultList();
				if (resultList.size() == 1) {
					return resultList.get(0);
				} else {
					nameParam = firstNames.substring(0, 2) + nameParam.substring(1);
					System.out.println("Searching: " + nameParam);
					query.setParameter("name", nameParam);
					resultList = query.getResultList();
					return resultList.get(0);
				}
			}

		}
	}

}

package com.jwxicc.cricket.ejb.test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.interfaces.CompetitionManager;
import com.jwxicc.cricket.parse.ImportedGameParser;
import com.jwxicc.cricket.util.parse.CricketStatzParseUtil;

@RunWith(Arquillian.class)
public class PersistenceTest {

	private static final Logger logger = Logger.getLogger(PersistenceTest.class);

	private Competition comp;
	private Game game;

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap
				.create(JavaArchive.class)
				.addPackages(true, "org.apache.commons.io", "org.apache.commons.lang",
						"org.apache.commons.collections")
				.addPackages(true, "com.jwxicc.cricket")
				.addPackages(true, "com.restfb")
				// .addPackage(Game.class.getPackage()).addPackage(DesignationType.class.getPackage())
				// .addPackage(ImportedGameParser.class.getPackage())
				// .addPackage(CricketParsePersistenceFacade.class.getPackage())
				// .addPackage(GameFacade.class.getPackage())
				// .addPackage(SharedPersistenceObjectsFacade.class.getPackage())
				// .addPackage(GameManagerImpl.class.getPackage())
				// .addPackage(KeyMultiValueLine.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("test-cs-data.txt").addAsResource("log4j.properties")
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");

		return archive;
	}

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@EJB(name = "cs9Parser")
	ImportedGameParser cs9Parser;

	@EJB
	CompetitionManager compManager;

	@Before
	public void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();

		comp = new Competition();
		comp.setAssociationName("Test Comp Arquillian");
		comp.setGrade("Test Grade");
		comp.setSeason("2013");

		compManager.persist(comp);
		logger.debug("created comp id: " + comp.getCompetitionId());

		InputStream inputStream = this.getClass().getResourceAsStream("/test-cs-data.txt");

		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		String cricketStatzText = writer.toString();

		String[] gameTextArray = CricketStatzParseUtil
				.splitCricketStatzTextToMatches(cricketStatzText);
		// store the async responses to parse operation
		Future<Game> gameFuture;
		int gamePos = (int) Math.floor(Math.random() * gameTextArray.length);
		gameFuture = cs9Parser.parseSingleGameText(gameTextArray[gamePos], comp.getCompetitionId());
		logger.debug("Submitted job number " + gamePos + " to game parser");

		game = gameFuture.get();
	}

	@After
	public void completeTransaction() throws Exception {
		em.remove(em.getReference(Game.class, game.getGameId()));
		em.remove(em.getReference(Competition.class, comp.getCompetitionId()));
		utx.commit();
		em.clear();
	}

	@Test
	public void testPartnerships() {

		for (Inning inns : game.getInnings()) {
			// calculate partnerships for this innings
			// get the partnership objects
			ArrayList<Partnership> fows = new ArrayList<Partnership>(inns.getPartnerships());
			// order the list
			Collections.sort(fows, new Comparator<Partnership>() {

				@Override
				public int compare(Partnership o1, Partnership o2) {
					// order by overs first
					if (o1.getOvers() < o2.getOvers()) {
						return -1;
					} else if (o1.getOvers() > o2.getOvers()) {
						return 1;
					}
					// order by wicket
					if (o1.getWicket() < o2.getWicket()) {
						return -1;
					} else if (o1.getWicket() > o2.getWicket()) {
						return 1;
					}
					// order by runs
					if (o1.getScoreAtEnd() < o2.getScoreAtEnd()) {
						return -1;
					} else if (o1.getScoreAtEnd() > o2.getScoreAtEnd()) {
						return 1;
					}
					// if still the same, order by out status
					// check if o1 has an out batter
					// if it does, it goes last, return pos int
					for (PartnershipPlayer fw : o1.getPartnershipPlayers()) {
						if (fw.isOutStatus()) {
							return 1;
						}
					}
					// o1 batters are not out, check o2
					// if it does, it goes last, return neg int
					for (PartnershipPlayer fw : o2.getPartnershipPlayers()) {
						if (fw.isOutStatus()) {
							return -1;
						}
					}

					// if still nothing returned, exception
					throw new IllegalArgumentException(
							"Cannot differentiate fall of wickets to order them");
				}
			});

			// for loop to create a partnership by comparing each fow to the
			// previous
			// opening partnership
			Partnership openingFow = fows.get(0);
			logger.debug("Opening partnership worth " + openingFow.getScoreAtEnd() + " in "
					+ openingFow.getOvers() + " overs.");
			// all other partnerships
			for (int i = 1; i < fows.size(); i++) {
				Partnership thisFow = fows.get(i);
				Partnership lastFow = fows.get(i - 1);
				logger.debug("Partnership for wicket " + thisFow.getWicket() + " worth "
						+ (thisFow.getScoreAtEnd() - lastFow.getScoreAtEnd()));
			}
		}

	}

}
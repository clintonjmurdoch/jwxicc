package com.jwxicc.cricket.ejb.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.dbenum.DesignationType;

@RunWith(Arquillian.class)
public class PersistenceTest {

	private static final Logger logger = Logger
			.getLogger(PersistenceTest.class);

	@Deployment
	public static Archive<?> createDeployment() {

		JavaArchive archive = ShrinkWrap
				.create(JavaArchive.class)
				.addPackage(Game.class.getPackage())
				.addPackage(DesignationType.class.getPackage())
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
		logger.debug(archive.toString(true));

		return archive;
	}

	@Test
	public void testGetAGame() {
		Game game = em.find(Game.class, 1);
		System.out
				.println("Game 1 home team: " + game.getTeam2().getTeamName());
	}

	@Test
	public void testPartnerships() {

		Query innsQuery = em
				.createQuery("from Inning i where i.game.gameId = 12");
		List<Inning> innings = innsQuery.getResultList();
		for (Inning inns : innings) {
			// calculate partnerships for this innings
			// get the fall of wicket objects
			ArrayList<Partnership> fows = new ArrayList<Partnership>(inns.getFows());
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
			System.out.println("Opening partnership worth "
					+ openingFow.getScoreAtEnd() + " in " + openingFow.getOvers()
					+ " overs.");
			// all other partnerships
			for (int i = 1; i < fows.size(); i++) {
				Partnership thisFow = fows.get(i);
				Partnership lastFow = fows.get(i-1);
				System.out.println("Partnership for wicket " + thisFow.getWicket() + " worth " + (thisFow.getScoreAtEnd() - lastFow.getScoreAtEnd()));
			}
		}

	}

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Before
	public void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
		em.clear();
	}

	// tests go here
}
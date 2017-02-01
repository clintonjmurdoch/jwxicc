package com.jwxicc.cricket.rest;

import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.entity.PartnershipPlayer;
import com.jwxicc.cricket.entity.WicketDetail;
import com.jwxicc.cricket.interfaces.GameManagerLocal;

@Produces("application/json")
@Consumes("application/json")
@Path("/match")
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class MatchApi {
	
	@EJB
	GameManagerLocal gameBean;
	
	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;
	
	@GET
	@Path("{matchId}")
	public Game getMatch(@PathParam("matchId") String matchId) {
		Game game = gameBean.getGameForScorecard(Integer.valueOf(matchId));
		for (Inning i : game.getInnings()) {
			i.setGame(null);
			for (Batting b : i.getBattings()) {
				b.setInning(null);
				for (WicketDetail w : b.getWicketDetails()) {
					w.setBatting(null);
				}
			}
			for (Bowling b : i.getBowlings()) {
				b.setInning(null);
			}
			for (Partnership p : i.getPartnerships()) {
				p.setInning(null);
				for (PartnershipPlayer pp : p.getPartnershipPlayers()) {
					pp.setPartnership(null);
				}
			}
		}
		
		return game;
	}
}

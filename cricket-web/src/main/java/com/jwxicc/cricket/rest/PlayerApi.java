package com.jwxicc.cricket.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.PlayerManager;

@Produces("application/json")
@Consumes("application/json")
@Path("/player")
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PlayerApi {

	@EJB
	PlayerManager playerBean;
	
	@GET
	@Path("{playerId}")
	public Player getPlayer(@PathParam("playerId") String playerId) {
		Player player = playerBean.getPlayerForProfile(Integer.valueOf(playerId));
		player.getPlayerDetail().setPlayer(null);
		return player;
	}
}

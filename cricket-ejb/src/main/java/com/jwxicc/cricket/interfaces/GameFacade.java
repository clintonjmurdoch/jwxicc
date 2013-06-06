package com.jwxicc.cricket.interfaces;

import java.util.List;

import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.WinType;

/**
 * Provides methods for storing game and its children. The separation from the Manager bean is to
 * allow interim storage of objects for easier management of persistence.
 * 
 * @author cmurdoch
 * 
 */
public interface GameFacade {
	public void addGame(Game game);

	public void addDesignations(List<GamePlayerDesignation> designations);
	
	public WinType getWinTypeRef(int id);
}

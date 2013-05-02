package com.jwxicc.cricket.parse;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Game;

@Local
public interface ImportedGameParser {

	/**
	 * @param text
	 *            The input text
	 * @param competitionId
	 *            The competition to add the game to
	 * @return The list of imported games. Expect lazy initialisation, so only
	 *         the direct properties of game should be accessed
	 * @throws CricketParseDataException
	 *             If the text doesn't match the expected format, or other
	 *             general failures
	 */
	public List<Game> parseGameText(String text, int competitionId)
			throws CricketParseDataException;
}

package com.jwxicc.cricket.parse;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Game;

@Local
public interface ImportedGameParser {

	/**
	 * Takes as its inputs the text for 1 game as exported from another application and the existing
	 * competition id in the database. The method should be asynchronous
	 * 
	 * @param text
	 *            The input text
	 * @param competitionId
	 *            The competition to add the game to
	 * @return A future
	 * @throws CricketParseDataException
	 *             If the text does not match the expected format
	 */
	Future<Game> parseSingleGameText(String text, int competitionId, List<String> log)
			throws CricketParseDataException;

}

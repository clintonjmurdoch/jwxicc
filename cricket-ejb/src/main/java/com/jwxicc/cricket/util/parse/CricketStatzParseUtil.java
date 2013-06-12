package com.jwxicc.cricket.util.parse;

public class CricketStatzParseUtil {
	
	private static final String MATCH_START_TEXT = "Record=Match";
	private static final String MATCH_END_TEXT = "Endmatch=True";

	public static String[] splitCricketStatzTextToMatches(String csText) {
		int lastMatchEnd = csText.lastIndexOf(MATCH_END_TEXT);
		int firstMatchStart = csText.indexOf(MATCH_START_TEXT);
		String strToParse = csText.substring(
				firstMatchStart + MATCH_START_TEXT.length(),
				lastMatchEnd + MATCH_END_TEXT.length());

		String[] gameTextArray;
		// check if there is more than 1 game provided
		if (strToParse.contains(MATCH_START_TEXT)) {
			gameTextArray = strToParse.split(MATCH_START_TEXT);
		} else {
			gameTextArray = new String[] { strToParse };
		}
		System.out.println("Number of games in the provided text: " + gameTextArray.length);
		
		return gameTextArray;
	}
}

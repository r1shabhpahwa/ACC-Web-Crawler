package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class pageRanking {

	public static int pageRankValue = 1;

	public boolean pageRank(String searchQuery) throws Exception {

		Long beginTime;
		Long endTime;
		float totalTime = 0;
		
		pageRankValue = 1;

		//Search query in dictionary
		File dictionaryfile = new File("dictionary.txt");
		boolean isWordFound = false;
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
			ArrayList<String> readDictionary = new ArrayList<String>();
			String fileLine;

			while ((fileLine = bufferReader.readLine()) != null)
				readDictionary.add(fileLine);
			bufferReader.close();

			for (String eachWord : readDictionary) {
				if (searchQuery.toLowerCase().equals(eachWord.toLowerCase())) {
					isWordFound = true;
				}
			}
		}

		// If word is not found in dictionary, provide suggestion for correct word and break
		if (!isWordFound) {
			System.out.println("\nUser entered word does not exist in dictionary");
			SpellCheck suggestion = new SpellCheck();
			suggestion.provideSuggestion(searchQuery);
			return false;
		}

		SearchPattern patternSearching = new SearchPattern();
		beginTime = System.currentTimeMillis();
		Hashtable<String, Integer> pageRank = patternSearching.searchPatterns(searchQuery);
		endTime = System.currentTimeMillis();
		totalTime = endTime - beginTime;

		// if pattern searching return empty ranking
		if (pageRank.size() == 0) {
			SpellCheck suggestion = new SpellCheck();
			suggestion.provideSuggestion(searchQuery);
		} else {
			System.out.println("Searching..\n");
			int totalOccurences = 0;
			for (int occurences : pageRank.values())
				totalOccurences += occurences;

			System.out
			.println("Found " + totalOccurences + " matches in (" + totalTime / 1000 + " seconds)");
			System.out.println("Matches found in " + pageRank.size() + " web pages.\n");
			System.out.println("Rank	Frequency   Page");

			// display pages and frequency of words in those pages
			Map<String, Integer> sortPagesByRank = pageRank.entrySet().stream()
					.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
							LinkedHashMap::new));
			sortPagesByRank.forEach((key, value) -> {
				System.out.println(String.format("%02d", pageRankValue) + "	" + String.format("%02d", value)
				+ "          " + key);
				pageRankValue++;
			});
		}
		return true;
	}

}

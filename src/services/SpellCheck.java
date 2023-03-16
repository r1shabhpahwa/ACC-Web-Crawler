package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for Spell-check
 *
 * @author (Sri Manjunadh Reddy Mukkala)
 */
public class SpellCheck {

	/**
	 * Method to provide search suggestions
	 * 
	 * @param inputQuery (Input query to calculate suggestion)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void provideSuggestion(String inputQuery) throws FileNotFoundException, IOException {

		EditDistance editDistance = new EditDistance();
		File dictionaryfile = new File("dictionary.txt");

		try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
			ArrayList<String> dictionary = new ArrayList<String>();
			String fileLine;

			while ((fileLine = bufferReader.readLine()) != null)
				dictionary.add(fileLine);
			bufferReader.close();

			int distance, firstED = 10, secondED = 10;
			String firstWord = null, secondWord = null;

			for (String eachWord : dictionary) {
				distance = editDistance.editDistance(eachWord, inputQuery);

				if (distance < secondED) {
					if (distance < firstED) {
						firstED = distance;
						firstWord = eachWord;
					} else {
						secondED = distance;
						secondWord = eachWord;
					}
				}
			}
			System.out.println("\nDid you mean: '" + firstWord + "' or '" + secondWord + "' ?");
		}
	}

	/**
	 * Method to check input word spelling
	 * 
	 * @param inputQuery (Input query to calculate suggestion)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void checkSpelling(String inputQuery) throws FileNotFoundException, IOException {

		EditDistance editDistance = new EditDistance();
		
		// object of dictionary file
		File dictionaryfile = new File("dictionary.txt");
		
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
			
			// to store all the words
			ArrayList<String> dictionary = new ArrayList<String>();
			String fileLine;

			// adding all the words in dictionary file to dictionary array list
			while ((fileLine = bufferReader.readLine()) != null)
				dictionary.add(fileLine);
			bufferReader.close();

			int distance, editDistanceOne = 10, editDistanceTwo = 10;
			String wordOne = null;

			// iterating over each word in dictionary array list
			for (String eachWord : dictionary) {
				
				// if correct
				if (eachWord.equals(inputQuery)) {
					System.out.println("Spelling is correct");
					return;
				}
				
				// checking edit distance of word in dictionary to user input
				distance = editDistance.editDistance(eachWord, inputQuery);
				// checking if edit distance is smallest possible, in order to generate only two responses
				if (distance < editDistanceTwo) {
					if (distance < editDistanceOne) {
						editDistanceOne = distance;
						wordOne = eachWord;
					}
				}
			}
			System.out.println("Please check the spelling of your input, did you mean: '" + wordOne + "' ?");
		}
	}
}

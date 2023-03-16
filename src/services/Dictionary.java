package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Class for Dictionary creation
 *
 * @author (Sri Manjunadh Reddy Mukkala)
 */
public class Dictionary {
	/**
	 * Method to create dictionary file
	 * 
	 */
	public void createDictionary() {
		String fileLine;
		File directory = new File("TextFiles/");
		String token;
		// list of files in TextFiles directory
		File[] fileList = directory.listFiles();
		
		// set to store all unique words
		Set<String> set = new HashSet<>();
		
		// list to store all words
		ArrayList<String> dictionary = new ArrayList<String>();

		// iterate all the text files
		for (File eachFile : fileList) {
			try {
				
				BufferedReader bufferReader = new BufferedReader(new FileReader(eachFile));
				while ((fileLine = bufferReader.readLine()) != null) {
					try (Scanner scan = new Scanner(fileLine)) {
						while (scan.hasNext()) {
							token = scan.next().replaceAll("[^a-zA-Z ]", "").toLowerCase();
							set.add(token);
						}
					}
				}
				bufferReader.close();
			} catch (Exception error) {
				System.out.print("Error while creating dictionary (Stack Trace below) \n\n");
				error.printStackTrace();
			}
		}

		dictionary.addAll(set);
		Collections.sort(dictionary);

		try {
			FileWriter fileWriter = new FileWriter("dictionary.txt");
			for (String eachWord : dictionary)
				fileWriter.write(eachWord + System.lineSeparator());
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

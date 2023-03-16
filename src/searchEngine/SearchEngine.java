package searchEngine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import services.*;

public class SearchEngine {

	public static final int crawlerLimit = 100;
	public static final int systemDelay = 500;

	/**
	 * Method to Initialize Search Engine (Crawler + HTML Parser + Dictionary Creation)
	 * 
	 * @param webPageUrl (Input URL to start Crawler)
	 * @throws Exception
	 */
	public static void initializeSearchEngine(String webPageURL) throws Exception {

		Crawler crawler = new Crawler();

		// Crawl Web Pages
		System.out.println("\n######################################");
		System.out.println("#                                    #");
		System.out.println("#  Crawling Web pages from Base URL  #");
		System.out.println("#                                    #");
		System.out.println("######################################\n");
		crawler.crawl(webPageURL, crawlerLimit);
		Thread.sleep(systemDelay);
		System.out.println("\nCrawled Web Pages stored at: ./WebPages/\n");

		// Convert HTML Web pages to Text files		
		System.out.println("\n#########################################");
		System.out.println("#                                       #");
		System.out.println("#  Converting HTML files to Text files  #");
		System.out.println("#                                       #");
		System.out.println("#########################################\n");
		String htmlFilesList[] = crawler.getFileNames("WebPages");
		for (int i = 0; i < htmlFilesList.length; i++) {
			if (!htmlFilesList[i].contains(".html")) {
				String currentDirectoryPath = System.getProperty("user.dir");
				Path filePath = Paths.get(currentDirectoryPath + "\\WebPages\\" + htmlFilesList[i]);
				Files.delete(filePath);
			}
		}
		for (int i = 0; i < htmlFilesList.length; i++) {
			System.out.println("Converting file: " + htmlFilesList[i]);
			String textFileContent = crawler.convertHtmlToText(htmlFilesList[i]);
			crawler.saveTextFile(htmlFilesList[i], textFileContent);
		}
		System.out.println("\nParsed HTML files stored at : ./TextFiles/\n");

		System.out.println("\n#########################################");
		System.out.println("#                                       #");
		System.out.println("#  Creating Dictionary from Text files  #");
		System.out.println("#                                       #");
		System.out.println("#########################################");
		Dictionary dictionary = new Dictionary();
		dictionary.createDictionary();
		System.out.println("\nDictionary created at: ./dictionary.txt");
	}

	/**
	 * Method to delete existing data from file system
	 * 
	 */
	public static void deleteCache() {
		File webDir = new File("./WebPages/");
		File textDir = new File("./TextFiles/");
		File dictionaryFile = new File("./dictionary.txt");
		for(File file: webDir.listFiles()) 
			if (!file.isDirectory()) 
				file.delete();
		for(File file: textDir.listFiles()) 
			if (!file.isDirectory()) 
				file.delete();
		dictionaryFile.delete();
	}

	public static void main(String[] args) throws Exception {

		String userInput = null;
		String searchQuery = null;
		String webURL;

		// Starting sequence
		System.out.println("###########################################");
		System.out.println("#                                         #");
		System.out.println("#      Welcome to the Search Engine       #");
		System.out.println("#                                         #");
		System.out.println("###########################################");
		System.out.println("#                                         #");
		System.out.println("# Developed by:                           #");
		System.out.println("#                                         #");
		System.out.println("# Rishabh Pahwa (110091645)               #");
		System.out.println("# Nikitha Rajashekar Donthi (110093174)	  #");
		System.out.println("# Sri Manjunadh Reddy Mukkala (110090022) #");
		System.out.println("#                                         #");
		System.out.println("###########################################");
		Thread.sleep(systemDelay);

		try (Scanner inputScanner = new Scanner(System.in)) {

			// Check if crawler has already run and depth is reached
			File totalHtmlFiles = new File("./WebPages/");
			if (totalHtmlFiles.list().length < crawlerLimit) {
				System.out.print("\nEnter the URL you want to start Crawling: ");
				URLValidation val = new URLValidation();
				webURL = inputScanner.nextLine();

				while(!val.isValidURL(webURL)) {
					System.out.print("\nPlease enter a valid URL: ");
					webURL = inputScanner.nextLine();
				}

				//Start Crawler + HTML parsing + Dictionary creation
				initializeSearchEngine(webURL);
			}
			else {
				System.out.print("\nFound existing data with " + totalHtmlFiles.list().length + " Web Pages");
				System.out.print("\n\nDo you want to clear the existing data? (Y/N)");
				userInput = inputScanner.nextLine();
				if(userInput.equalsIgnoreCase("Y")){

					//Delete Cache data
					deleteCache();

					// Input Web Page URL from user 
					System.out.print("\nEnter the URL you want to start Crawling: ");
					URLValidation val = new URLValidation();
					webURL = inputScanner.nextLine();

					while(!val.isValidURL(webURL)) {
						System.out.print("\nPlease enter a valid URL: ");
						webURL = inputScanner.nextLine();
					}

					//Start Crawler + HTML parsing + Dictionary creation
					initializeSearchEngine(webURL);
				}
			}


			Thread.sleep(systemDelay);
			System.out.print("\n\nWhat would you like to do?\n\n");
			System.out.print("[1] Search Something \n\n");
			System.out.print("[2] Check Spelling \n\n");
			System.out.print("[3] Count Frequency \n\n");
			System.out.print("Enter F to exit. \n\n");
			System.out.println("So what do you want to do? ");

			userInput = inputScanner.nextLine();

			while (!userInput.toLowerCase().equals("f")) {
				switch (userInput) {

				case "1":
					// Input from user
					System.out.print("Enter something to search: ");
					searchQuery = inputScanner.nextLine();
					pageRanking search = new pageRanking();

					// Call method for searching 
					search.pageRank(searchQuery.toLowerCase());
					break;

				case "2":
					// Input from user
					System.out.print("Enter a word to check it's spelling: ");
					searchQuery = inputScanner.nextLine();
					SpellCheck suggestSpelling = new SpellCheck();

					// Call method for spell-check
					suggestSpelling.checkSpelling(searchQuery.toLowerCase());
					break;

				case "3":
					// Input from user
					System.out.println("Enter a word to count it's frequency : ");
					searchQuery = inputScanner.nextLine();
					FrequencyCounter countFrequency = new FrequencyCounter();

					// Call method for frequency Counter
					countFrequency.countWords(searchQuery);
					break;

				default:
					System.out.println("Please enter a valid input");

				}

				// Loop process till user exits
				Thread.sleep(systemDelay);
				System.out.print("\n[1] Perform Search \n\n");
				System.out.print("[2] Check Word Spelling \n\n");
				System.out.print("[3] Count Word Frequency \n\n");
				System.out.print("Enter F to exit. \n\n");
				System.out.println("So what do you want to do? ");
				userInput = inputScanner.nextLine();
			}

			// Exit 
			System.out.println("Shutting down..");
			Thread.sleep(systemDelay);
			System.out.println("Thanks for using the Search Engine");
		}
	}
}
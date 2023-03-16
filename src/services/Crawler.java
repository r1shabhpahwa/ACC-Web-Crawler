package services;

import java.io.File;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for Web Crawler and HTML to Text functionality
 *
 * @author (Rishabh Pahwa)
 */
public class Crawler {
	
	/**
	 * Method to Crawl Web Pages starting from input URL till defined depth is reached
	 * 
	 * @param webPageURL (Input URL to start Crawler)
	 * @param crawlerLimit (Maximum Depth for Crawler to run)
	 * @throws Exception
	 */
	public void crawl(String webPageURL, int crawlerLimit) throws Exception {
		ArrayList<String> urlList = new ArrayList<String>();
		Set<String> visitedUrls = new HashSet<String>();

		System.out.println("Started Crawling URL: " + webPageURL);

		// Add Web Page URL to the queue
		urlList.add(webPageURL);

		for (int i = 0; i < urlList.size(); i++) {

			// Check if maximum depth has been reached
			File totalHtmlFiles = new File("./WebPages/");
			if (totalHtmlFiles.list().length > crawlerLimit) {
				break;
			}

			String url = urlList.get(i);

			// Skip the URL if it is already visited
			if (visitedUrls.contains(url)) {
				System.out.println("Skipping Duplicate URL : " + url);
				continue;
			}
			
			// Check URL 
			URLValidation val = new URLValidation();
			if (!val.isValidURL(url)) {
				System.out.println("Skipping Invalid URL : " + url);
				continue;
			}

			// Add current URL to the list of visited URLs
			visitedUrls.add(url);

			System.out.println("Now Crawling : " + url);

			try {
				Document jsoupDocument = Jsoup.connect(url).get();
				String webPageTitle = jsoupDocument.title();
				String webPageContent = jsoupDocument.html();
				
				// Save current web page
				if (!webPageTitle.isEmpty()) {
					this.saveWebPage(webPageTitle, webPageContent);
				}
				
				// Fetch hyperlink from current URL and add to crawler queue 
				Elements webPageUrls = jsoupDocument.select("a[href]");
				for (Element webPage : webPageUrls) {
					urlList.add(webPage.attr("abs:href"));
				}
			} catch (IOException error) {
				System.out.println("Error while crawling web page :" + error);
			}
		}
	}

	/**
	 * Save Web Page
	 * 
	 * @param webPageTitle   the title of the web page
	 * @param webPageContent the body content of the web page
	 */
	public void saveWebPage(String webPageTitle, String webPageContent) {
		try {
			webPageTitle = webPageTitle.replaceAll("[^a-zA-Z0-9]", " ");
			webPageTitle = webPageTitle.trim();
			FileWriter fileWriter = new FileWriter("./WebPages/" + webPageTitle + ".html");
			fileWriter.write(webPageContent);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while saving web page: " + webPageTitle);
		}

	}

	/**
	 * Get names of all files in a given directory
	 * 
	 * @param directoryName name of the directory
	 * @return list of file names in a directory
	 */
	public String[] getFileNames(String directoryName) {
		File directoryLocation = new File(directoryName);
		String fileNames[] = directoryLocation.list();
		return fileNames;
	}

	/**
	 * Method used to convert HTML file to Text File
	 * 
	 * @param fileName (HTML file name)
	 * @return (HTML file content)
	 * @throws IOException
	 */
	public String convertHtmlToText(String fileName) throws IOException {
		String filePath = "./WebPages/" + fileName;
		Charset characterEncoding = StandardCharsets.US_ASCII;
		byte[] htmlFileContentInBytes = Files.readAllBytes(Paths.get(filePath));
		String htmlFileContent = new String(htmlFileContentInBytes, characterEncoding);
		Document jsoupDocument = Jsoup.parse(htmlFileContent);
		return jsoupDocument.text();
	}

	/**
	 * Method used to save Text file in the directory ./TextFiles/
	 * 
	 * @param textFileName text file name to use for saving it
	 * @param textContent  text content from converted HTML file
	 * @throws IOException
	 */
	public void saveTextFile(String textFileName, String textFileContent) throws IOException {
		FileWriter fileWriter = new FileWriter(
				"./TextFiles/" + textFileName.substring(0, textFileName.lastIndexOf(".")) + ".txt");
		fileWriter.write(textFileContent);
		fileWriter.close();
	}
}

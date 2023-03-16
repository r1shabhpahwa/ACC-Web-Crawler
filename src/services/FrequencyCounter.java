package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Class for Counting the Frequency
 *
 * @author (Nikitha Rajashekar Donthi)
 */
public class FrequencyCounter {

	public void countWords(String word) throws FileNotFoundException, IOException {
		try {
			File directoryName = new File("TextFiles");
			File[] fileList = directoryName.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				int wordCount = 0;
				if (fileList[i].isFile()) {
					File file = new File("TextFiles/" + fileList[i].getName());
					
					//UTF-8 Unicode Transformation Format 8 Bit to represent Unicode text in web pages
					//InputStreamReader interprets the bytes of an InputStream as text instead of numerical data
					InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
					
					//To read the text from a character-based input stream
					BufferedReader bufferReader = new BufferedReader(inputStreamReader);
					
					//To create mutable (modifiable/editable) string
					StringBuffer stringBuffer = new StringBuffer();
					String tempString = null;
					while ((tempString = bufferReader.readLine()) != null) {
						stringBuffer.append(tempString.toLowerCase());
					}
					Pattern patternObject = Pattern.compile(word.toLowerCase());
					Matcher matcherObject = patternObject.matcher(stringBuffer);
					while (matcherObject.find()) {
						wordCount++;
					}
					bufferReader.close();
					System.out.println(" \nFile Name : " + fileList[i].getName() 
							+ "\nWord Frequency : " + wordCount + "\n");
				}
			}
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}

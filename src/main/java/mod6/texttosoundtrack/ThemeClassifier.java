package mod6.texttosoundtrack;

import java.io.*;
import java.util.*;

/**
 * Created by Peter Verzijl on 6-1-2015.
 */
public class ThemeClassifier {

	// States of mood
	public HashMap<String, List<String>> categories;
	public HashMap<String, File> files;

	/**
	 * Constructor, creates an untrained ThemeClassifier.
	 */
	public ThemeClassifier() {

		categories = new HashMap<>();
		files = new HashMap<>();
	}

	/**
	 * Constructor, creates and trains the ThemeClassifier.
	 * @param path The path to the text files to classify.
	 */
	public ThemeClassifier(String path) {

		categories = new HashMap<>();
		files = new HashMap<>();
		train(path);
	}

	/**
	 * Gets the thematic category of a document.
	 * @param file to find the category of.
	 * @return A string containing the category.
	 */
	public String getCatagory(File file) {

		if (categories.isEmpty()) {
			System.err.println("The database has not been trained yet! Call the train function first!");
			return null;
		}

		int counter;                                            // Counts the amount of matching words for each category
		String currentCategory = "No category found!";
		double currentLowestProbability = -1000000000;          // Should be the lowest value possible
		List<String> wordsInFile = getFileWords(file);  		// Formula for finding the best fitted mood of the text
																// Amount of words that also exist in the category / total words in text
																// Thus: what percentage of words in this text are of this category?

		if (wordsInFile == null || wordsInFile.size() == 0) {
			System.out.println("Error, no words in file!");
			return null;
		}

		for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
			counter = 0;                                        // Counts the amount of found words in category
			for (String w : wordsInFile) {                      // Loop trough all words in the file
				if (entry.getValue().contains(w)) {             // Does word 'w' in the file exist in the 'List<String>' of category 'key'?
					counter++;
				}
			}
			if (counter < 1)                                    // Don't let the universe explode by dividing by zero :P
				continue;

			double probability = Math.log((double)counter / (double)wordsInFile.size()); // Calculate the probability that this category is the real category
			System.out.println("In category " + entry.getKey() + " we found " + counter + " matching words. With a log probability of " + probability + ".");
			if (currentLowestProbability < probability ) {      // Simple part divided by whole
				currentLowestProbability = probability;         // If this is the current highest probability, set it so.
				currentCategory = entry.getKey();
			}
		}

		return currentCategory;                                 // Return highest probable category
	}

	/**
	 * Adds a string to the database
	 * @param word The word that needs to be added to the category.
	 * @param category The category the word needs to be added to.
	 */
	public void addStringToFile(String word, String category) {

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(files.get(category))))) {
			categories.get(category).add(word);                 // Add word to the category
			out.print(word + ", ");                             // Also add the word to the file
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A function that trains the database with files.
	 * @param path Path to all the files that will be used for training.
	 */
	public void train(String path) {

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null || listOfFiles.length < 1) {
			System.out.println("Error listOfFiles at path: " + path + " is empty.");
			return;
		}

		for (int i = 0, listOfFilesLength = listOfFiles.length; i < listOfFilesLength; i++) {
			File file = listOfFiles[i];
			categories.put(file.getName(), getFileWords(file));
			System.out.println("File " + file.getName() + " was read.");
		}

		System.out.println("Successful, " + categories.size() + " files read and loaded.");
	}

	/**
	 * Gets the words from a text file.
	 * @param file The file to get the words from.
	 * @return A list of words
	 */
	private List<String> getFileWords(File file) {

		String splitBy = ", ";
		List<String> words = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			for (String line; (line = br.readLine()) != null; ) {
				String[] strings = getTokens(line);

				words.addAll(Arrays.asList(strings));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (words.size() > 0)
			return words;
		else
			return null;
	}

	/**
	 * Tokenizes the string to an array of tokens.
	 * @param s The string to tokenize.
	 * @return An array of tokens.
	 */
	private String[] getTokens(String s) {

		s = s.replaceAll("[^a-zA-Z\\s]", "");
		s = s.toLowerCase();
		return s.split("\\s+");

	}
}

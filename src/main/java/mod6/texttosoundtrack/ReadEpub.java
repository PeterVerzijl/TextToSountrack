package mod6.texttosoundtrack;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.*;

import java.io.*;
import java.util.*;

/**
 * A class to read an .epub file.
 * 
 * @author Kevin Hetterscheid
 * @version 0.5
 */
public class ReadEpub {
	private Book book = null;
	private HashMap<String,String> lines = null;
	private HashMap<Integer,String> linesOrder = null;
	private int pageNumber = 0;
	private int paraGraph = 0;
	private static final String SAVED_PAGES_FILE_PATH = "res/savedPages.sav";
	private static final int MAX_NUM_WORDS = 100;

	private ThemeClassifier themeClassifier;

	/**
	 * The constructor for this class.
	 * 
	 * @param file
	 *            The .epub file to open.
	 */
	public ReadEpub(String file) {
		EpubReader epubReader = new EpubReader();
		try {
			book = epubReader.readEpub(new FileInputStream(file));
			themeClassifier = new ThemeClassifier("./TrainingData/");
			setUpList();
		} catch (IOException e) {
		}
	}

	private void setUpList() {

		List<Resource> conts = book.getContents();
		String page;
		try {
			page = new String(conts.get(pageNumber).getData(), "UTF-8");
			page = page.replace("&mdash;", "-");
			page = page.replace("&nbsp;", "");
			page = page.replace("&amp;", "&");
			page = page.replaceAll("(<[^p][^<>]*>)", "");
			String[] temp = page.split("(<p[^<>]*>)");
			ArrayList<String> tmp = new ArrayList<String>(Arrays.asList(temp));
			tmp.removeAll(Arrays.asList("", null, "\n", "\t", "\\t", "\\n"));
			for (Iterator<String> it = tmp.iterator(); it.hasNext();) {
				String line = it.next();
				if (line.matches("(\\s\\s)") || line.matches("\\s") || line.matches("\\s\\s\\s\\s\\n")) {
					it.remove();
				}
				line.replaceFirst("\\s\\s\\s", "");
			}

			lines = new HashMap<String, String>();
			linesOrder = new HashMap<Integer, String>();
			int words = 0;
			int count = 0;
			int currentPara = 0;
			String line = "";
			while (currentPara < tmp.size()) {
				words = 0;
				line = "";
				while (words < MAX_NUM_WORDS) {
					if(currentPara == tmp.size()) {
						lines.put(line,themeClassifier.getCategory(line));
						linesOrder.put(count,line);
						count++;
						break;
					}
					words += tmp.get(currentPara).replaceAll("(<[^p][^<>]*>)", "").split(" ").length;
					if(words < MAX_NUM_WORDS) {
						line = line + tmp.get(currentPara).replaceAll("(<[^p][^<>]*>)", "");
						currentPara++;
					}
					else {
						if(line.length() == 0) {
							line = tmp.get(currentPara).replaceAll("(<[^p][^<>]*>)", "");
							currentPara++;
						}
						lines.put(line, themeClassifier.getCategory(line));
						linesOrder.put(count,line);
						count++;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the title of the current book.
	 * 
	 * @return The title of the current book. NULL if there is no book opened.
	 */
	public String getTitle() {
		if (book != null)
			return book.getTitle();
		else
			return null;
	}

	/**
	 * Gets the next paragraph from the book. Automatically changes file if it's
	 * at the end of a file.
	 * 
	 * @return The next paragraph.
	 */
	public String nextParagraph() {
		paraGraph++;
		if (lines == null || paraGraph >= lines.size()-1) {
			pageNumber++;
			setUpList();
			paraGraph = 0;
		}
		String page = linesOrder.get(paraGraph);
		String mood = lines.get(page);
		System.out.println(mood);
		return page.replaceAll("(<[^p][^<>]*>)", "");
	}

	/**
	 * Gets the previous paragraph from the book. Automatically changes file if
	 * it's at the start of a file.
	 * 
	 * @return The previous paragraph.
	 */
	public String previousParagraph() {
		paraGraph--;
		if (lines == null || paraGraph < 0) {
			pageNumber--;
			setUpList();
			paraGraph = lines.size() - 1;
		}
		String page = linesOrder.get(paraGraph);
		String mood = lines.get(page);
		System.out.println(mood);
		return page.replaceAll("(<[^p][^<>]*>)", "");
	}

	/**
	 * Saves the current page to the <code>savedPages.sav</code> file.
	 * Overwrites older saved pages from the same book.
	 */
	public void savePage() {
		BufferedWriter writer;
		BufferedReader reader;
		try {
			File file = new File(SAVED_PAGES_FILE_PATH);
			File tempFile = new File(file.getAbsolutePath() + ".tmp");
			if (!file.exists())
				file.createNewFile();
			if (!tempFile.exists())
				tempFile.createNewFile();
			reader = new BufferedReader(new FileReader(file));
			writer = new BufferedWriter(new FileWriter(tempFile));
			String line;
			ArrayList<String> list = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				if (!line.contains(getTitle())) {
					list.add(line);
				}
			}
			writer.append(getTitle() + "|" + pageNumber + "|" + paraGraph);
			writer.newLine();
			for (int i = 0; i < list.size(); i++) {
				writer.append(list.get(i));
				writer.newLine();
			}
			writer.close();
			reader.close();
			file.delete();
			tempFile.renameTo(file);
		} catch (IOException e) {
		}
	}

	/**
	 * Loads the page corrospondending with the current book from the
	 * <code>savedPages.sav</code> file.
	 */
	public void loadPage() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(SAVED_PAGES_FILE_PATH)));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(getTitle())) {
					String[] details = line.split("[|]");
					pageNumber = Integer.parseInt(details[1]);
					paraGraph = Integer.parseInt(details[2]);
					paraGraph--;
					setUpList();
				}
			}
		} catch (IOException e) {
		}
	}
}

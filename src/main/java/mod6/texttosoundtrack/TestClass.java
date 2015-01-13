package mod6.texttosoundtrack;

import java.io.File;

/**
 * Created by Peter Verzijl on 12-1-2015.
 */
public class TestClass {

	public static ThemeClassifier classifier;

	public static void main(String[] args) {

		classifier = new ThemeClassifier("./TrainingData/");
		String category = classifier.getCatagory(new File("./TommyTest.txt"));
		System.out.println("The category of this text file is: " + category);
	}
}
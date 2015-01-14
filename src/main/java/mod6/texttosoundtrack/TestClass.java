package mod6.texttosoundtrack;

import mod6.texttosoundtrack.echonest.EchonestMood;

import java.io.File;

/**
 * Created by Peter Verzijl on 12-1-2015.
 */
public class TestClass {

	public static ThemeClassifier classifier;

	public static void main(String[] args) {

		classifier = new ThemeClassifier("./TrainingData/");
		EchonestMood category = classifier.getCategory(new File("./TommyTest.txt"));
		System.out.println("The category of this text file is: " + category.toString());
	}
}
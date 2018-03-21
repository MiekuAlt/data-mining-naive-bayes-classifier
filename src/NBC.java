import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class NBC {

	public static List<List<String>> trainDataSt;
	public static List<List<String>> testDataSt;
	private static int targetAttr;
	private static List<String> possibleAttr;
	
	private static String trainFile; // The name of the training file, used for error prevention so the test and train files aren't the same
	
	public static void main(String[] args) {
		possibleAttr = new ArrayList<String>();
		greeting();
		trainDataSt = loadTrainingFile();
		testDataSt = loadTestFile();
		// Removing the duplicates generated in the possible attributes
		possibleAttr = removeDups(possibleAttr);
		targetAttr = chooseAttr();

		// TODO: Remove me, Just for Greg to see how the data currently is built
		printTable(trainDataSt, "Train Data");
		printTable(testDataSt, "Test Data");
	}

	// Displays for the user the different attribute options, asks them to choose one
	// Re-purposed from Ass4
	private static int chooseAttr() {
		int attr = 0;
		System.out.println("Please select the class label attribute (by index):");
		for(int i = 0; i < possibleAttr.size(); i++) {
			System.out.println(i + ". " + possibleAttr.get(i));
		}

		// Error handling to ensure their attribute selection
		Scanner in = new Scanner(System.in);
		int reloop;
		do {
			reloop = 0;
			System.out.print("Attribute: ");
			in = new Scanner(System.in);
			try {
				String inputSt = in.nextLine();		
				
				int checkNum = Integer.parseInt(inputSt);
				if (checkNum >= 0 && checkNum < possibleAttr.size()) {
					attr = checkNum;
				} else {
					System.out.println(checkNum + " is not a valid label attribute");
					System.out.println("Please select the class label attribute (by index):");
					for(int i = 0; i < possibleAttr.size(); i++) {
						System.out.println(i + ". " + possibleAttr.get(i));
					}
					reloop++;
				}
			} catch(Exception e) {
				System.out.println("Incorrect format");
				System.out.println("Please select the class label attribute (by index):");
				for(int i = 0; i < possibleAttr.size(); i++) {
					System.out.println(i + ". " + possibleAttr.get(i));
				}
				reloop++;
			}
		} while(reloop != 0);
		in.close();
		
		return attr;
	}
	
	// Displays the initial greeting for the user
	public static void greeting() {
		System.out.print( "+---------------------------------------------+\n"
				+ "+           Naive Bayes Classifier            +\n"
				+ "+      By: Michael Altair B00599791           +\n"
				+ "+          Greg Mailman   B00695833           +\n"
				+ "+---------------------------------------------+\n"
				+ "\n");
	}
	
	// Requests and gets the training file
	public static List<List<String>> loadTrainingFile()
	{
		List<List<String>> loadedData = new ArrayList<List<String>>();
		System.out.print("Please enter a training file: ");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		loadedData = loadData(input);
		// Recording the name of the training file
		trainFile = input;
		
		return loadedData;
	}
	// Requests and gets the testing file
	public static List<List<String>> loadTestFile() {
		List<List<String>> loadedData = new ArrayList<List<String>>();
		Scanner in = new Scanner(System.in);
		// Error handling to ensure the file isn't the same as the trainer
		String input = "";
		int reloop;
		do {
			reloop = 0;
			System.out.print("Please enter a testing file: ");
			input = in.nextLine();
			
			// Ensuring the names don't match
			if(input.equals(trainFile)) {
				System.out.println("Error: The test file: \"" + input + "\" must not be the same as the training file.");
				reloop++;
			}
			
		} while (reloop != 0);
		
		loadedData = loadData(input);
		
		return loadedData;
	}
	
	// Loads the requested data
	public static List<List<String>> loadData(String filename) {
		List<List<String>> loadedData = new ArrayList<List<String>>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			// Separating the first line and setting up the tags to add to the data
			String line = br.readLine();
			String[] tags = line.split(" +");
			// Converting all the tags into the possible attributes
			for(int i = 0; i < tags.length; i++) {
				possibleAttr.add(tags[i]);
			}

			// Builds the legend and forms the data in string format
			line = br.readLine();
			while (line != null) {
				String[] split = line.split(" +");

				loadedData.add(Arrays.asList(split));
				line = br.readLine();
			}
			
		} catch(Exception e) {
			System.out.println("No file found.");
			System.exit(1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (Exception e) {
				System.out.println("Error closing the reader");
			}
		}
		
		return loadedData;
	}
	
	// Removes duplicates from a list of strings, this was recycled from Ass3
	private static List<String> removeDups(List<String> withDups) {
		// This is derived from an example here https://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
		Set<String> hs = new HashSet<>();
		hs.addAll(withDups);
		List<String> uniques = new ArrayList<String>();
		uniques.addAll(hs);
		
		return uniques;
	}
	
	// TODO: Remove this, for testing only
	private static void printTable(List<List<String>> data, String tableName) {
		System.out.println("+--------------------------------------+");
		System.out.println("Table: " + tableName);
		System.out.println("+--------------------------------------+");
		for(int r = 0; r < data.size(); r++) {
			for(int c = 0; c < data.get(r).size(); c++) {
				System.out.print(data.get(r).get(c) + " ");
			}
			System.out.println("");
		}
		System.out.println("+--------------------------------------+");
	}
}

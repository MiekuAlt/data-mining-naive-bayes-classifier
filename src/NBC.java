import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class NBC {

	public static List<List<String>> trainDataSt;
	public static List<List<String>> testDataSt;
	private static List<String> possibleAttr;
	
	// Data to be used by the Bayes algorithm
	private static List<List<String>> dataLabels;
	private static List<List<Integer>> trainData;
	private static List<List<Integer>> testData;
	private static int targetAttr;
	
	private static String trainFile; // The name of the training file, used for error prevention so the test and train files aren't the same
	
	public static void main(String[] args) {
		//possibleAttr = new ArrayList<String>();
		trainData = new ArrayList<List<Integer>>();
		testData = new ArrayList<List<Integer>>();
		
		greeting();
		trainDataSt = loadTrainingFile();
		testDataSt = loadTestFile();
		targetAttr = chooseAttr();
		dataLabels = new ArrayList<List<String>>();
		genLabels(trainDataSt);
		trainData = convertDataTable(trainDataSt);
		testData = convertDataTable(testDataSt);
		
		// TODO: Remove me, Just for Greg to see how the data currently is built
		printTable(trainDataSt, "Train Data");
		//printTable(testDataSt, "Test Data");
		printIntTable(trainData, "Train Data");
		//printIntTable(testData, "Train Data"); 
		printTable(dataLabels, "Data Labels");
		
		String result = Bayes.Bayes(trainData, testData, dataLabels, targetAttr);
		writeResult(result);
	}
	
	// Writes the input string to a file called Result.txt
	private static void writeResult(String result) {
		BufferedWriter bw = null;
		try {
            File outPut = new File("Result.txt");
            if (!outPut.exists()) {
                outPut.createNewFile();
            }
            FileWriter fw = new FileWriter(outPut);
            bw = new BufferedWriter(fw);
            bw.write(result);

        } catch(Exception e) {
            System.out.println("Error outputting the rules.");
            System.out.println(e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    System.out.println("The result is in the file \'Result.txt\'.");
                }
            } catch(Exception e) {
                System.out.println("Error closing the writer.");
            }
        }
	}
	
	// Converts the data table of strings into one of integers based on the different data label's indexes
	// Ass4's code was adapted and recycled for this method
	private static List<List<Integer>> convertDataTable(List<List<String>> dataStrings) {
		List<List<Integer>> dataInts = new ArrayList<List<Integer>>();
		
		// Converting the strings of data to be ints for easier data tree access based on its dataLabel index
		for(int r = 0; r < dataStrings.size(); r++) {
			List<Integer> curRow = new ArrayList<Integer>();
			for(int c = 0; c < dataStrings.get(r).size(); c++) {
				if(!dataStrings.get(r).get(c).equals("")) {
					for(int i = 0; i < dataLabels.get(c).size(); i++) {
						if(dataStrings.get(r).get(c).equals(dataLabels.get(c).get(i))) {
							curRow.add(i);
						}
					}
				}
			}
			if(curRow.size() > 0)
				dataInts.add(curRow);
		}	
		
		return dataInts;
	}

	// Generates the labels to be used when assigning ints to the data strings
	// Ass4's code was adapted and recycled for this method
	private static void genLabels(List<List<String>> dataStrings) {
		//dataLabels = new ArrayList<List<String>>();
		
		// Compiling all the possible labels
		// Rotating table
		String[][] flipTable = new String[dataStrings.get(0).size()][dataStrings.size()];
		
		for(int r = 0; r < dataStrings.size(); r++) {	
			for(int c = 0; c < dataStrings.get(r).size(); c++) {
				flipTable[c][r] = dataStrings.get(r).get(c);
			}
		}
		
		for(int r = 0; r < flipTable.length; r++) {
			List<String> col = new ArrayList<String>();
			for(int c = 0; c < flipTable[r].length; c++) {
				if(flipTable[r][c] != null) {
					if(!flipTable[r][c].equals("")) {
						col.add(flipTable[r][c]);
					}
				}
			}
			dataLabels.add(col);
		}
		
		List<List<String>> dataLabelsNoDup = new ArrayList<List<String>>();
		for(int r = 0; r < dataLabels.size(); r++) {
			dataLabelsNoDup.add(removeDups(dataLabels.get(r)));
			dataLabelsNoDup.get(r).add(0, possibleAttr.get(r));
		}

		dataLabels = dataLabelsNoDup;
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
	public static List<List<String>> loadTrainingFile() {
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
		possibleAttr = new ArrayList<String>();
		
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
	private static void printIntTable(List<List<Integer>> data, String tableName) {
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

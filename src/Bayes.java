import java.io.File;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;


public final class Bayes {
    //takes in the input and builds a string of the outpit to return.
    public static String Bayes(List<List<Integer>> training, List<List<Integer>> testing, List<List<String>> labels, int classifier) {
        List<Double> targetProbs = BuildTargetProbability(training, labels, classifier);
        List<List<List<Double>>> conditionalProbs = BuildClassProbability(training, labels, classifier);
        return findPrediction(conditionalProbs, targetProbs, testing, labels, classifier);

    }

    //builds the probability array for the target class
    public static List<Double> BuildTargetProbability(List<List<Integer>> input, List<List<String>> labels, int n) {
        double sum = 0;
        List<Double> probabilities = new ArrayList<>();
        //for each value of the target class, sum the number of occurrences of that value over the total number
        for(int i=0; i<labels.get(n).size()-1; i++) {
            for (int j=0; j<input.get(n).size(); j++) {
                if (input.get(n).get(j) == i) {
                    sum++;
                }
            }
            probabilities.add(sum / input.get(n).size());
            sum = 0;
        }
        return probabilities;
    }

    //finds the conditional probability for each of the classes using the training set
    public static List<List<List<Double>>> BuildClassProbability(List<List<Integer>> input, List<List<String>> labels, int n) {
        int sumClass = 0, sumTarget = 0;
        List<List<List<Double>>> probabilities = new ArrayList<>();
        //for each class
        for(int i=0; i<input.size(); i++) {
            //add a new list
            probabilities.add(new ArrayList<>());
            //for each value of each class
            for (int j = 0; j < labels.get(i).size() - 1; j++) {
                //add a new list to the new list
                probabilities.get(i).add(new ArrayList<>());
                //for all values of the target class related to the current class
                for (int k = 0; k < labels.get(n).size() - 1; k++) {
                    //sum all times when they correspond, and the total times the target class is in its value
                    for (int l = 0; l < input.get(i).size(); l++) {
                        if (input.get(i).get(l) == j && input.get(n).get(l) == k) {
                            sumClass++;
                        }
                        if (input.get(n).get(l) == k) {
                            sumTarget++;
                        }
                    }
                    //divide the two totals to get the conditional probability for that class and add it to the class' list
                    probabilities.get(i).get(j).add((double) sumClass / sumTarget);
                    sumClass = 0;
                    sumTarget = 0;
                }
            }
        }
        //return the list of probabilities
        return probabilities;
    }

    //find the prediction for each row in the testing data set.
    public static String findPrediction(List<List<List<Double>>> classProbs, List<Double> targetProbs, List<List<Integer>> input, List<List<String>> labels, int n) {
        double maxProb = 0;
        double curProb = 0;
        int accuracy = 0;
        String output = "";
        String maxLabel = "None";
        //setting up the output string
        for (int i=0; i<labels.size(); i++) {
            output += labels.get(i).get(0) + " ";
        }
        output += "Classification\n";
        //for each row in the input
        for (int i=0; i<input.get(0).size(); i++) {
            //for each value of the target
            for( int j=0; j<labels.get(n).size()-1; j++) {
                curProb = targetProbs.get(j);
                //for each attribute in the current row that is not the target
                for(int k=0; k<input.size(); k++) {
                    //if its not the target attribute, multiply its conditional probability with the current running probability.
                    if(k==n) {
                        continue;
                    }
                    curProb = curProb * classProbs.get(k).get(input.get(k).get(i)).get(j);
                }
                //if its greater than the current greatest probability, replace it.
                if (curProb > maxProb) {
                    maxProb = curProb;
                    maxLabel = labels.get(n).get(j+1);
                }
                curProb = 0;
            }
            //for each row, output the row and the prediction, add 1 to the accuracy if it was correct.
            output += "Prediction of row: ";
            for(int k=0; k<input.size(); k++) {
                output += labels.get(k).get(input.get(k).get(i) + 1) + " ";
            }
            output += "is " + maxLabel + "\n";
            if (maxLabel.equals(labels.get(n).get(input.get(n).get(i)+1))) {
                accuracy++;
            }
        }
        //add the accuracy to the output and return
        output += "Accuracy: " + accuracy +"/"+ input.get(0).size();
        return output;
    }
}


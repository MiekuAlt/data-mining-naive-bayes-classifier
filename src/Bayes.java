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
        //debug output, TODO remove.
        System.out.println("For " + labels.get(n).get(0));
        for(int i=0; i<labels.get(n).size()-1; i++) {
            System.out.println("Probability of " + labels.get(n).get(i+1) + " is: " + probabilities.get(i));
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
            if (maxLabel.equals(labels.get(n).get(input.get(n).get(i)))) {
                accuracy++;
            }
        }
        //add the accuracy to the output and return
        output += "Accuracy: " + accuracy +"/"+ input.get(0).size();
        return output;
    }

    //TODO remove all of this
    public static void main(String[] args) {
        System.out.println("hello world");
        List<List<Integer>> Input = new ArrayList<>();
        List<List<String>> labels = new ArrayList<>();
        List<List<Integer>> testing = new ArrayList<>();

        Input.add(new ArrayList<>());
        Input.add(new ArrayList<>());
        Input.add(new ArrayList<>());
        Input.add(new ArrayList<>());
        Input.add(new ArrayList<>());

        labels.add(new ArrayList<>());
        labels.add(new ArrayList<>());
        labels.add(new ArrayList<>());
        labels.add(new ArrayList<>());
        labels.add(new ArrayList<>());

        testing.add(new ArrayList<>());
        testing.add(new ArrayList<>());
        testing.add(new ArrayList<>());
        testing.add(new ArrayList<>());
        testing.add(new ArrayList<>());

        //outlook
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(2);
        Input.get(0).add(1);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(2);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(0);
        Input.get(0).add(0);
        Input.get(0).add(2);
        Input.get(0).add(0);
        Input.get(0).add(1);
        Input.get(0).add(1);
        Input.get(0).add(2);

        //temp
        Input.get(1).add(0);
        Input.get(1).add(0);
        Input.get(1).add(0);
        Input.get(1).add(0);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(2);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(2);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(1);
        Input.get(1).add(0);
        Input.get(1).add(1);

        //humidity
        Input.get(2).add(0);
        Input.get(2).add(0);
        Input.get(2).add(0);
        Input.get(2).add(0);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(1);
        Input.get(2).add(0);
        Input.get(2).add(1);
        Input.get(2).add(0);

        //windy
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(0);
        Input.get(3).add(1);
        Input.get(3).add(1);
        Input.get(3).add(0);
        Input.get(3).add(1);

        //playtennis
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(0);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(1);
        Input.get(4).add(0);

        labels.get(0).add("outlook");
        labels.get(0).add("sunny");
        labels.get(0).add("overcast");
        labels.get(0).add("rain");

        labels.get(1).add("temp");
        labels.get(1).add("hot");
        labels.get(1).add("mild");
        labels.get(1).add("cool");

        labels.get(2).add("humidity");
        labels.get(2).add("high");
        labels.get(2).add("normal");

        labels.get(3).add("windy");
        labels.get(3).add("false");
        labels.get(3).add("true");

        labels.get(4).add("playtennis");
        labels.get(4).add("n");
        labels.get(4).add("p");


        testing.get(0).add(0);
        testing.get(0).add(0);
        testing.get(0).add(1);
        testing.get(0).add(2);
        testing.get(0).add(2);
        testing.get(0).add(2);
        testing.get(0).add(1);
        testing.get(0).add(0);
        testing.get(0).add(0);
        testing.get(0).add(2);
        testing.get(0).add(0);
        testing.get(0).add(1);
        testing.get(0).add(1);
        testing.get(0).add(2);

        testing.get(1).add(0);
        testing.get(1).add(0);
        testing.get(1).add(0);
        testing.get(1).add(1);
        testing.get(1).add(2);
        testing.get(1).add(2);
        testing.get(1).add(2);
        testing.get(1).add(1);
        testing.get(1).add(2);
        testing.get(1).add(1);
        testing.get(1).add(1);
        testing.get(1).add(1);
        testing.get(1).add(0);
        testing.get(1).add(1);

        testing.get(2).add(0);
        testing.get(2).add(0);
        testing.get(2).add(0);
        testing.get(2).add(0);
        testing.get(2).add(1);
        testing.get(2).add(1);
        testing.get(2).add(1);
        testing.get(2).add(0);
        testing.get(2).add(1);
        testing.get(2).add(1);
        testing.get(2).add(1);
        testing.get(2).add(0);
        testing.get(2).add(1);
        testing.get(2).add(0);

        testing.get(3).add(0);
        testing.get(3).add(1);
        testing.get(3).add(0);
        testing.get(3).add(0);
        testing.get(3).add(0);
        testing.get(3).add(1);
        testing.get(3).add(1);
        testing.get(3).add(0);
        testing.get(3).add(0);
        testing.get(3).add(0);
        testing.get(3).add(1);
        testing.get(3).add(1);
        testing.get(3).add(0);
        testing.get(3).add(1);

        testing.get(4).add(0);
        testing.get(4).add(0);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(0);
        testing.get(4).add(1);
        testing.get(4).add(0);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(1);
        testing.get(4).add(0);

        /*List<Double> targetProbs = BuildTargetProbability(Input, labels, 3);
        List<List<List<Double>>> conditionalProbs = BuildClassProbability(Input, labels, 3);
        findPrediction(conditionalProbs, targetProbs, testing, labels, 3);*/
        System.out.println(Bayes(Input, testing, labels, 2));
    }
}


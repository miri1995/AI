import java.util.ArrayList;
import java.util.List;


public class java_ex2 {

    public static void main(String[] args) {

        Helper helper=new Helper();
        //read the files line by line
        List<List<String>> trainLinesFeatures = helper.readLines("train.txt");
        List<List<String>>  testLineFeatures = helper.readLines("test.txt");


        //read the files column by column
        List<List<String>> trainColumnsFeatures= helper.ReadFileByColumn("train.txt");
        List<List<String>> testColumnFeatures= helper.ReadFileByColumn("test.txt");



        for(int i=0;i<testColumnFeatures.size();i++){
            testColumnFeatures.get(i).remove(0);
        }

        List<String> testClass = testColumnFeatures.get(testColumnFeatures.size()-1);


        //ID3
        List<String> attributes= new ArrayList<String>();
        for (int i = 0; i <trainLinesFeatures.get(0).size()-1 ; i++) {
            attributes.add(trainLinesFeatures.get(0).get(i));
        }
        ID3 id3=new ID3(trainLinesFeatures.get(0),trainLinesFeatures,testLineFeatures, attributes);
        DTree tree=id3.initialize(attributes);
        List<String> DTClassification = id3.classifyAllTestData(tree);


        for(int i=0;i<trainColumnsFeatures.size();i++){
            trainColumnsFeatures.get(i).remove(0);
        }

        //KNN
        KNN knn = new KNN(5);
        List<String> KNNClassification = knn.runAlgo(trainColumnsFeatures,testColumnFeatures);

        //Naive Base
        NaiveBase naive = new NaiveBase(testLineFeatures);
        List<String> naiveClassification = naive.runAlgo(trainColumnsFeatures,testColumnFeatures);


        float DTaccuracy = helper.calcAccuracy(DTClassification,testClass);
        float KNNaccuracy = helper.calcAccuracy(KNNClassification,testClass);
        float naiveaccuracy  = helper.calcAccuracy(naiveClassification,testClass);
        helper.writeResult(DTClassification,KNNClassification,naiveClassification,DTaccuracy,KNNaccuracy,naiveaccuracy);

    }



}




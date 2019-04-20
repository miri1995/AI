import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ID3 {
    private List<String> features;
    private List<List<String>> trainFeatures;
    private List<List<String>> testFeatures;
    private Map<String, List<String>> featuresOriginal;
    private String flagClass="";


    public ID3(List<String> features, List<List<String>> trainFeatures, List<List<String>> testFeatures, List<String> attributes) {
        this.features = features;
        testFeatures.remove(0);
        this.testFeatures = testFeatures;
        trainFeatures.remove(0);
        this.trainFeatures = trainFeatures;
        this.featuresOriginal = new LinkedHashMap<String, List<String>>();
    }



    public DTree initialize(List<String> attributes) {
        for (int i = 0; i < (features.size() - 1); i++) {
            String currFeature = this.features.get(i);
            this.featuresOriginal.put(currFeature, new ArrayList<String>());
            for (List<String> trainVector : this.trainFeatures) {
                String optVal = trainVector.get(i);
                if (!this.featuresOriginal.get(currFeature).contains(optVal)) {
                    this.featuresOriginal.get(currFeature).add(optVal);
                }
            }
        }

        Node defult=new Node(calculateDefult(testFeatures));
        DTree tree=ID3Algo(trainFeatures,attributes,defult);
        tree.printTreeToFile();
        return tree;
    }

    /**
     * implemtes of ID3 ALGO
     * @param examples
     * @param attributes
     * @param defult
     * @return
     */
    public DTree ID3Algo(List<List<String>> examples, List<String> attributes, Node defult) {

        DTree tree = new DTree();
        int yesCount = 0;
        int noCount = 0;
        int temp=0;
        for (List<String> vector : examples) {
            if (vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                    vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal2())) {
                if(vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal1())){
                    temp=1;
                }
                noCount++;
            } else  if (vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())||
                    vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                if(vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())){
                    temp=1;
                }
                yesCount++;

            }
        }

        String Mode;
        if (yesCount >= noCount) {
            if(temp==1){
                Mode=EnumClassifacation.POSITIVE.getClassVal1();
            }else{
                Mode=EnumClassifacation.POSITIVE.getClassVal2();
            }
        } else {
            if(temp==1){
                Mode=EnumClassifacation.NEGATIVE.getClassVal1();
            }else{
                Mode=EnumClassifacation.NEGATIVE.getClassVal2();
            }
        }
        if (examples.isEmpty()) {
            tree.setRoot(defult);
            return tree;
        } else if (yesCount == 0) {
            if (flagClass.equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                    flagClass.equals(EnumClassifacation.POSITIVE.getClassVal1())) {
                tree.setRoot(new Node(EnumClassifacation.NEGATIVE.getClassVal1()));
            } else if(flagClass.equals(EnumClassifacation.NEGATIVE.getClassVal2())||
                    flagClass.equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                tree.setRoot(new Node(EnumClassifacation.NEGATIVE.getClassVal2()));
            }
            return tree;
        } else if (noCount == 0) {
            if (flagClass.equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                    flagClass.equals(EnumClassifacation.POSITIVE.getClassVal1())) {

                tree.setRoot(new Node(EnumClassifacation.POSITIVE.getClassVal1()));

            } else if(flagClass.equals(EnumClassifacation.NEGATIVE.getClassVal2())||
                    flagClass.equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                tree.setRoot(new Node(EnumClassifacation.POSITIVE.getClassVal2()));
            }
            return tree;
        } else if (attributes.isEmpty()) {
            tree.setRoot(new Node(Mode));
            return tree;
        } else {
            String best = chooseAttribute(attributes, examples);
            tree.setRoot(new Node(best));
            for (int i = 0; i < featuresOriginal.get(best).size(); i++) {
                String currVal= featuresOriginal.get(best).get(i);
                List<List<String>> examplesI = new ArrayList<List<String>>();
                for (List<String> vector : examples) {
                    if (vector.get(features.indexOf(best)).equals(currVal)) {
                        examplesI.add(vector);
                    }
                }
                List<String> copyCategories = new ArrayList<String>();
                copyCategories.addAll(attributes);
                if (copyCategories.contains(best)) {
                    copyCategories.remove(best);
                }
                DTree subTree = ID3Algo(examplesI, copyCategories, new Node(Mode));
                tree.getRoot().addChild(featuresOriginal.get(best).get(i), subTree.getRoot());
            }
            return tree;
        }
    }

    /**
     * calculate the numbears of yes and no and return the biggest of them
     * @param examples
     * @return yes/no/true/false
     */
    public String calculateDefult(List<List<String>> examples) {
        int yesCount = 0;
        int noCount = 0;
        int temp=0;
        this.flagClass=examples.get(0).get(features.size()-1);
        for (List<String> vector : examples) {

            if (vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                    vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal2())) {
                if(vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal1())){
                    temp=1;
                }
                noCount++;
            } else  if (vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())||
                    vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                if(vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())){
                    temp=1;
                }
                yesCount++;

            }
        }
        String Mode;
        if (yesCount >= noCount) {
            if(temp==1){
                Mode=EnumClassifacation.POSITIVE.getClassVal1();
            }else{
                Mode=EnumClassifacation.POSITIVE.getClassVal2();
            }
        } else {
            if(temp==1){
                Mode=EnumClassifacation.NEGATIVE.getClassVal1();
            }else{
                Mode=EnumClassifacation.NEGATIVE.getClassVal2();
            }
        }
        return Mode;

    }


    /**
     * calculate the gain and return the max gain
     * @param attributes
     * @param examples
     * @return
     */
    public String chooseAttribute(List<String> attributes, List<List<String>> examples) {
        if (attributes.size() == 1) {
            return attributes.get(0);
        }
        double maxGain = -10.0;
        String best = null;
        int countYes = 0;
        int countNo = 0;
        for (List<String> vector : examples) {
            if (vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())||
                    vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                countYes++;
            } else {
                countNo++;
            }
        }
        double entropyTotal = entropy(countYes, countNo, examples.size());
        for (String attribute : attributes) {
            double gain = entropyTotal;
            for (String value : this.featuresOriginal.get(attribute)) {
                int countRelevantExamples = 0;
                countYes = 0;
                countNo = 0;
                for (List<String> vector : examples) {
                    if (vector.get(features.indexOf(attribute)).equals(value)) {
                        countRelevantExamples++;
                        if (vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal1())||
                                vector.get(features.size() - 1).equals(EnumClassifacation.POSITIVE.getClassVal2())) {
                            countYes++;
                        } else if (vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                                vector.get(features.size() - 1).equals(EnumClassifacation.NEGATIVE.getClassVal2())) {
                            countNo++;
                        }
                    }
                }
                double entropy = entropy(countYes, countNo, countRelevantExamples);
                gain -= ((double) countRelevantExamples / examples.size()) * entropy;
            }
            if (gain > maxGain) {
                maxGain = gain;
                best = attribute;
            }

        }
        return best;
    }

    /**
     * calculate the entropy
     * @param countYes
     * @param countNo
     * @param total
     * @return
     */
    public double entropy(int countYes, int countNo, int total) {
        if (countNo == 0 || countYes == 0) {
            return 0.0;
        }
        double entropy = (-1) * ((double) countYes / total) * Math.log((double) countYes / total)
                - ((double) countNo / total) * Math.log((double) countNo / total);
        return entropy;
    }

    /**
     * over all classiffication in test file and return accordingly
     * the root after id3 algo
     * @param dtl
     * @return
     */
    public List<String> classifyAllTestData(DTree dtl) {
        List<String> classification = new ArrayList<>(testFeatures.size());
        for (List<String> testVector : testFeatures) {
            Node currNode = dtl.getRoot();
            while (!currNode.isLeaf()) {
                String attVal = testVector.get(features.indexOf(currNode.getData()));
                currNode = currNode.getChildren().get(attVal);
            }

            classification.add(currNode.getData());
        }
        return classification;
    }
}

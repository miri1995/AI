import java.io.*;
import java.util.*;

public class Helper {


    public Helper(){

    }

    public  List<List<String>> ReadFileByColumn(String fileName) {
        List<List<String>> columns=new ArrayList<>();
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                Error();
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = br.readLine();
            String[] tokens = line.split("\\s+");

            for(int i=0;i<tokens.length;i++) {
                columns.add(new ArrayList<>());
                columns.get(i).add(tokens[i]);
            }
            while ((line = br.readLine()) != null){
                String[] tokens2 = line.split("\\s+");
                for(int i=0;i<tokens.length;i++) {
                    columns.get(i).add(tokens2[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columns;

    }


    public  List<List<String>> readLines(String fileName){
        List<List<String>> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                Error();
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = br.readLine();
            List<String> tokens = Arrays.asList(line.split("\\s+"));
            lines.add(tokens);
            while ((line = br.readLine()) != null){
                List<String> tokens2 = Arrays.asList(line.split("\\s+"));
                lines.add(tokens2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void Error(){
        System.out.println("Couldn't found input file");
        System.exit(1);
    }


    public  void writeResult(List<String> DTClassification,List<String> KNNClassification,List<String> naiveClassification,
                                   float DTaccuracy,float KNNaccuracy,float naiveaccuracy) {
        PrintWriter writer=null;
        try {
            writer = new PrintWriter("output.txt");
            writer.println("Num"+"\t"+"DT"+"\t"+"KNN"+"\t"+"naiveBase");

            for (int i=1;i<=KNNClassification.size();i++){
                writer.println(i+"\t"+DTClassification.get(i-1)+"\t"+KNNClassification.get(i-1)+"\t"+naiveClassification.get(i-1));
            }
            writer.println("\t"+String.format("%.2f",DTaccuracy)+"\t"+String.format("%.2f",KNNaccuracy)+"\t"+String.format("%.2f",naiveaccuracy));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(writer!=null) {
                writer.close();
            }
        }
    }


    public  float calcAccuracy(List<String> algoClassification, List<String>testClass) {
        int examples = testClass.size();
        int right =0;
        float accuracy=1;
        for(int i=0;i<algoClassification.size();i++){
            if(algoClassification.get(i).equals(testClass.get(i))){
                right++;
            }
        }
        accuracy = (float)right/examples;
        return accuracy;
    }

    /**
     * create map of categories(title of features)
     * @param trainEx
     * @return
     */
    public LinkedHashMap checkKFeature(List<String> trainEx) {
        LinkedHashMap categories = new LinkedHashMap<List<String>, String>();
        for (int j = 0; j < trainEx.size(); j++) {
            if (!categories.containsKey(trainEx.get(j))) {
                categories.put(trainEx.get(j), trainEx);
            }
        }

        return categories;

    }






}

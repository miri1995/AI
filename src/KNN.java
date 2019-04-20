

import java.util.ArrayList;
import java.util.List;

public class KNN {

    private int k;
    private ArrayList<double[][]> list = new ArrayList<double[][]>();
    private int maxK=100;

    public KNN(int k) {
        this.k = k;
    }

    /**
     * The function calculate the hamming distance
     * between 2 strings.
     * @param str1
     * @param str2
     * @return distance
     */
    public double hammingDist(String str1, String str2) {
        double count = 0;
       if(str1.equals(str2)){
            count=0;
        }
        else{
            count=1;
        }
        return count;
    }


    public double[][] distanceMatrix(List<String> trainEx, List<String> sol) {
        double[][] distance = new double[sol.size()][trainEx.size()];
        for (int i = 0; i < sol.size(); i++) {
            for (int j = 0; j < trainEx.size(); j++) {
                distance[i][j] = hammingDist(sol.get(i), trainEx.get(j));
            }
        }
        return distance;
    }

    public int[][] sumDistance(ArrayList<double[][]> list, int sol, int ex) {
        int[][] d = new int[sol][ex];
        for (int i = 0; i < sol; i++) {
            for (int j = 0; j < ex; j++) {
                d[i][j]=0;
            }
        }
        for (int i = 0; i < sol; i++) {
            for (int j = 0; j < ex; j++) {
                for (double[][] matrix : list) {

                    d[i][j] += matrix[i][j];
                }

            }
        }
        return d;
    }


    /**
     * the function run the algo:
     * go to distanceMatrix,sumDistance function
     * and calculate the num of yes and no in the classification.
     * @param featuresClass
     * @param features
     * @return
     */
    public List<String>  runAlgo(List<List<String>> featuresClass ,  List<List<String>> features) {
        int sol = features.get(0).size();
        int ex = featuresClass.get(0).size();
        for(int i=0;i<features.size()-1;i++){
            list.add(new double[sol][ex]);
            list.set(i,distanceMatrix(featuresClass.get(i), features.get(i)));
        }


        int[][] finalMatrix;
        finalMatrix = sumDistance(list, sol, ex);
        List<List<Integer>> BestSol = new ArrayList<>();
        List<String> finalClass = new ArrayList<>();
        for (int i = 0; i < sol; i++) {
            BestSol.add(new ArrayList<>());
            BestSol.set(i,topK(finalMatrix[i]));

            List<Integer>sol2=BestSol.get(i);
            List<String> survivedEx=featuresClass.get(featuresClass.size()-1);
            String classification;

            int counterNo=0;
            int counterYes=0;
            int temp=0;
            for(int j=0; j<this.k ;j++){

                if(survivedEx.get(sol2.get(j)).equals(EnumClassifacation.POSITIVE.getClassVal1())||
                        survivedEx.get(sol2.get(j)).equals(EnumClassifacation.POSITIVE.getClassVal2())){
                    if(survivedEx.get(sol2.get(j)).equals(EnumClassifacation.POSITIVE.getClassVal1())){
                        temp=1;
                    }
                    counterYes++;
                }
                else {
                    if(survivedEx.get(sol2.get(j)).equals(EnumClassifacation.NEGATIVE.getClassVal1())){
                        temp=1;
                    }
                    counterNo++;
                }
            }
            if (counterNo>counterYes){
                if(temp==1){
                    classification= EnumClassifacation.NEGATIVE.getClassVal1();
                }else{
                    classification= EnumClassifacation.NEGATIVE.getClassVal2();
                }
            }
            else{
                if(temp==1){
                    classification= EnumClassifacation.POSITIVE.getClassVal1();
                }else{
                    classification= EnumClassifacation.POSITIVE.getClassVal2();
                }
            }

            finalClass.add(classification);

        }
        return finalClass;
    }

    public List<Integer> topK(int[] matrix) {
        List<Integer> topK = new ArrayList<Integer>();
        int bestEx = 0;
        for(int i=0;i<this.k;i++) {
            int min = matrix[0];
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j] < min) {
                    min = matrix[j];
                    bestEx=j;
                }
            }
            topK.add(bestEx);
            matrix[bestEx] = maxK;
        }
        return topK;
    }

}


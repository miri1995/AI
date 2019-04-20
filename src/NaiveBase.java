import java.util.*;

public class NaiveBase {
    private List<List<String>> lines = new ArrayList<>();


    public NaiveBase(List<List<String>> lines) {
        this.lines = lines;
    }



    /**
     * calculate Intersection
     * @param categories
     * @param trainExFeature
     * @param trainExSurvived
     * @param classification
     * @return
     */
    public List<int[]> checkIntersection(LinkedHashMap categories,List<String> trainExFeature,List<String> trainExSurvived,String[] classification) {
        Iterator it = categories.entrySet().iterator();
        List<int[]> howMany = new ArrayList<int[]>();
        while (it.hasNext()) {
            howMany.add(new int[2]);
            Map.Entry pair = (Map.Entry) it.next();
        }
        int count1=0;
        int count2=0;
        int t=0;
        it = categories.entrySet().iterator();
        for (int i = 0; i <trainExFeature.size(); i++) {
            while (it.hasNext()) {
                // howMany.add(new int[2]);
                Map.Entry pair = (Map.Entry)it.next();
                if(trainExFeature.get(i).equals(pair.getKey())){
                    if(trainExSurvived.get(i).equals(classification[0])){
                        count1++;
                    }
                    else{
                        count2++;
                    }
                }
                howMany.get(t)[0]+=count1;
                howMany.get(t)[1]+=count2;
                t++;
                count1=0;
                count2=0;

            }
            t=0;
            it = categories.entrySet().iterator();

        }
        //smooth
        for(int i=0;i<howMany.size();i++){
            howMany.get(i)[0]+=1;
            howMany.get(i)[1]+=1;
        }
        return howMany;

    }


    /**
     * calculate P of classifcation
     * @param trainSurvived
     * @param classification
     * @return
     */
    public float[] calculatePClassify(List<String> trainSurvived, String[] classification){
        float[] probClassify = new float[2];
        int count1=0;
        int count2 =0;
        for (int i = 0; i <trainSurvived.size(); i++){
            if(trainSurvived.get(i).equals(classification[0])){
                count1++;
            }
            else{
                count2++;
            }
        }
        probClassify[0] = (float)count1/(float)trainSurvived.size();
        probClassify[1] = (float)count2/(float)trainSurvived.size();
        return probClassify;

    }

    /**
     * smppth P
     * @param countClassify
     * @param howMany
     * @return
     */
    public List<float[]> smoothprobCond(int[] countClassify ,List<int[]> howMany ){
        List<float[]> probability = new ArrayList<float[]>();
        for (int i=0;i<howMany.size();i++){
            probability.add(new float[2]);
            probability.get(i)[0]= (float)howMany.get(i)[0]/(countClassify[0]+ howMany.size());
            probability.get(i)[1]= (float)howMany.get(i)[1]/(countClassify[1]+ howMany.size());
        }
        return probability;
    }

    /**
     * cond of yes/no
     * @param line
     * @param columnsCategories
     * @param Probline
     * @param probClassification
     * @param classifications
     * @return
     */
    public String YesNoCond(List<String> line ,List<LinkedHashMap> columnsCategories,List<List<float[]>>Probline,float[] probClassification,String[] classifications ) {
        float[] yes = new float[columnsCategories.size()];
        float[] no = new float[columnsCategories.size()];
        for (int i = 0; i < columnsCategories.size(); i++) {
            Iterator it = columnsCategories.get(i).entrySet().iterator();
            int j = 0;

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (line.get(i).equals(pair.getKey())) {
                    yes[i] = Probline.get(i).get(j)[1];
                    no[i] = Probline.get(i).get(j)[0];
                    break;
                }

                j++;

            }

        }
        float multNo=probClassification[0];
        float multYes=probClassification[1];
        for(int i=0;i<yes.length;i++){
            multNo*=no[i];
            multYes*=yes[i];
        }
        if(multYes>multNo)
        {
            return classifications[1];
        }
        else{
            return classifications[0];
        }

    }


    /**
     * implement of naive base
     * @param featuresClass
     * @param features
     * @return
     */
    public List<String>  runAlgo(List<List<String>> featuresClass, List<List<String>> features) {
        Helper helper=new Helper();
        List<LinkedHashMap> columnsCategories = new ArrayList();
        for (int i = 0; i < featuresClass.size()-1; i++) {
            columnsCategories.add(new LinkedHashMap());
            columnsCategories.set(i,helper.checkKFeature(featuresClass.get(i)));

        }

        List<String> classVal=featuresClass.get(featuresClass.size()-1);
        String[] classifications=new String[2];
        if(classVal.get(0).equals(EnumClassifacation.NEGATIVE.getClassVal1())||
                classVal.get(0).equals(EnumClassifacation.POSITIVE.getClassVal1())){

            classifications[0]=EnumClassifacation.NEGATIVE.getClassVal1();
            classifications[1]=EnumClassifacation.POSITIVE.getClassVal1();
        }else if (classVal.get(0).equals(EnumClassifacation.NEGATIVE.getClassVal2())||
                classVal.get(0).equals(EnumClassifacation.POSITIVE.getClassVal2())){
            classifications[0]=EnumClassifacation.NEGATIVE.getClassVal2();
            classifications[1]=EnumClassifacation.POSITIVE.getClassVal2();
        }



        List<List<int[]>> countIntersectionsCol = new ArrayList();
        for(int i=0;i<featuresClass.size()-1;i++){
            List<int[]> countIntersection = checkIntersection(columnsCategories.get(i),featuresClass.get(i),featuresClass.get(featuresClass.size()-1),classifications);
            countIntersectionsCol.add(countIntersection);
        }
        float[] probClassification = calculatePClassify(featuresClass.get(featuresClass.size()-1),classifications);


        List<String> trainSurvived=featuresClass.get(featuresClass.size()-1);
        String[] classification=classifications;
        int[] countClassify = new int[2];
        int count1=0;
        int count2 =0;
        for (int i = 0; i <trainSurvived.size(); i++){
            if(trainSurvived.get(i).equals(classification[0])){
                count1++;
            }
            else{
                count2++;
            }
        }
        countClassify[0] = count1;
        countClassify[1]  = count2;



        List<List<float[]>> condProbs = new ArrayList();
        for(int i=0;i<countIntersectionsCol.size();i++){
            List<float[]> cond = smoothprobCond(countClassify,countIntersectionsCol.get(i));
            condProbs.add(cond);
        }
        List<String> yesNo = new ArrayList<>();
        for(int i=0;i<this.lines.size();i++){
            yesNo.add(YesNoCond(lines.get(i),columnsCategories,condProbs,probClassification,classifications));
        }

        return yesNo;

    }



}
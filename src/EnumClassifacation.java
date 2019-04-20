import java.util.List;

public enum EnumClassifacation {
    POSITIVE  ("yes","true"),

    NEGATIVE("no","false");


    private final String classVal1;
    private final String classVal2;

    private EnumClassifacation(String classVal1,String classVal2) {
        this.classVal1 = classVal1;
        this.classVal2=classVal2;
    }

    public String getClassVal1(){
        return this.classVal1;
    }
    public String getClassVal2(){
        return this.classVal2;
    }

}

import java.util.HashMap;

public class DocumentNumericVector {

    public int totalWord;
    public double edOrW;
    public String topic;
    public HashMap numericVector;

    public DocumentNumericVector(String t) {
        totalWord = 0;
        edOrW = -1;
        topic = t;
        numericVector = new HashMap();
    }

    public int getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(int totalWord) {
        this.totalWord = totalWord;
    }

    public String getTopic() {
        return topic;
    }

    public double getEdOrW() {
        return edOrW;
    }

    public void setEdOrW(double edOrW) {
        this.edOrW = edOrW;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public HashMap getNumericVector() {
        return numericVector;
    }

    public void setNumericVector(HashMap numericVector) {
        this.numericVector = numericVector;
    }

    public boolean containsWord(String str) {
        return numericVector.containsKey(str);
    }

    public double wordCount(String str) {
        if (!containsWord(str)) {
            return 0.0;
        }
        return (double) numericVector.get(str);
    }

    public double getWeight(String str) {
        if (!containsWord(str)) {
            return 0.0;
        }
        return (double) numericVector.get(str);
    }
    
    public void addWord(String str) {
        numericVector.put(str, 1);
    }

    public void updateKeyValue(String str) {
        int v = (int) numericVector.get(str);
        v++;
        numericVector.put(str, v);
    }

    public static void main(String[] args) {
        DocumentNumericVector dnv = new DocumentNumericVector("Test");
        dnv.numericVector.put("the", 3);
        dnv.numericVector.put("dell", 6);
        dnv.numericVector.put("a4", 1);

        System.out.println(Math.sqrt(Math.pow(-2, 2)));
    }
}

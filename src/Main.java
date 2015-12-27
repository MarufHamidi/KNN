
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Test.reset();

        String skipListFile = "skip.data";
        String sourceListFile = "source.data";
        String trainFile = "training.data";
        String testFile = "test.data";

        DataReader dr = new DataReader(skipListFile, sourceListFile);
        Worker wrk = new Worker();

        Test.booleanVectorTest(testFile);
        Test.numericVectorTest(testFile);

        ArrayList testBVL = dr.createBooleanVector(testFile);
        ArrayList testNVL = dr.createNumericVector(testFile);
        ArrayList testTIL = wrk.getTF_IDF(testNVL);

        ArrayList trainfBVL = dr.createBooleanVector(trainFile);
        ArrayList trainNVL = dr.createNumericVector(trainFile);
        ArrayList trainTIL = wrk.getTF_IDF(trainNVL);

        //wrk.decideByHamming(testBVL, trainfBVL, 3);
        //wrk.decideByEuclidean(testNVL, trainNVL, 3);
        //wrk.decideByCosineSimilarity(testTIL, trainTIL, 1);
        
        for (int k = 1; k <= 5; k = k + 2) {
            wrk.decideByHamming(testBVL, trainfBVL, k);
            wrk.decideByEuclidean(testNVL, trainNVL, k);
            wrk.decideByCosineSimilarity(testTIL, trainTIL, k);
        }
    }
}

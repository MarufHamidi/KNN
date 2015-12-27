
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Worker {

    public HDComparator hdc;
    public EDComparator edc;
    public CSComparator csc;

    public Worker() {
        hdc = new HDComparator();
        edc = new EDComparator();
        csc = new CSComparator();
    }

    public ArrayList getHammingDistance(DocumentBooleanVector dbv, ArrayList dbvList) {
        //System.out.println("getting hamming distance ... ");
        Iterator itr = dbvList.iterator();
        HashSet mainKeySet = new HashSet((Set) dbv.getBooleanVector().keySet());
        HashSet tempKeySet;
        DocumentBooleanVector tempDBV;

        Iterator itr2;
        String tempKey;
        int tempHD;
        int count = 1;
        while (itr.hasNext()) {
            //System.out.println(">>>>>>>>>>>>>>>>>hamming distance>>>>>>>>>>>>>>>>>>>>" + (count++));
            tempDBV = (DocumentBooleanVector) itr.next();
            tempKeySet = new HashSet((Set) tempDBV.getBooleanVector().keySet());
            tempKeySet.addAll(mainKeySet);

            tempHD = 0;
            itr2 = tempKeySet.iterator();
            while (itr2.hasNext()) {
                tempKey = (String) itr2.next();
                if (dbv.containsWord(tempKey) && tempDBV.containsWord(tempKey)) {
                } else {
                    tempHD++;
                }
            }
            tempDBV.setHd(tempHD);
            //System.out.println("HD : " + tempHD);
        }
        //System.out.println("return.  ");
        return dbvList;
    }

    public ArrayList getEuclideanDistance(DocumentNumericVector dnv, ArrayList dnvList) {
        //System.out.println("getting euclidean distance ... ");
        Iterator itr = dnvList.iterator();
        HashSet mainKeySet = new HashSet((Set) dnv.getNumericVector().keySet());
        HashSet tempKeySet;
        DocumentNumericVector tempDNV;

        Iterator itr2;
        String tempKey;
        double tempED;
        int count = 1;
        while (itr.hasNext()) {
            //System.out.println("<<<<<<<<<<<<<<<euclidean distance<<<<<<<<<<<<<<<<<<<<<<<" + (count++));
            tempDNV = (DocumentNumericVector) itr.next();
            tempKeySet = new HashSet((Set) tempDNV.getNumericVector().keySet());
            tempKeySet.addAll(mainKeySet);

            tempED = 0.0;
            itr2 = tempKeySet.iterator();
            while (itr2.hasNext()) {
                tempKey = (String) itr2.next();
                tempED = tempED + Math.sqrt(Math.pow((dnv.wordCount(tempKey) - tempDNV.wordCount(tempKey)), 2));
            }
            tempDNV.setEdOrW(tempED);
            //System.out.println("HD : " + tempED);
        }
        //System.out.println("return. ");
        return dnvList;
    }

    public ArrayList getCosineSimilarity(DocumentNumericVector dnv, ArrayList dnvList) {
        //System.out.println("getting cosine similarity ... ");

        Iterator mkItr = dnv.getNumericVector().keySet().iterator();
        double d1Mod = calculateDocMod(dnv);

        DocumentNumericVector tempDNV;
        double tempW;
        double d2Mod;
        String tempKey;
        int count = 1;
        Iterator itr = dnvList.iterator();
        while (itr.hasNext()) {
            //System.out.println("<<<<<<<<<<<<<<<cosine similarity<<<<<<<<<<<<<<<<<<<<<<<" + (count++));
            tempDNV = (DocumentNumericVector) itr.next();
            d2Mod = calculateDocMod(tempDNV);

            tempW = 0.0;
            while (mkItr.hasNext()) {
                tempKey = (String) mkItr.next();
                tempW = tempW
                        + ((dnv.getWeight(tempKey) * 1.0 * tempDNV.getWeight(tempKey))
                        / (d1Mod * 1.0 * d2Mod));

            }
            tempDNV.setEdOrW(tempW);
            //System.out.println("d1: " + d1Mod);
            //System.out.println("d2: " + d2Mod);
            //System.out.println("CosX : " + tempW);
        }
        //System.out.println("return. ");
        return dnvList;
    }

    public ArrayList getTF(ArrayList dnvList) {
        ArrayList tfList = new ArrayList();
        DocumentNumericVector tempDNV;
        HashMap tempHash;
        Iterator tempItr;
        String tempKey;
        int tempTotal;
        double tempValue;
        for (int i = 0; i < dnvList.size(); i++) {
            tempDNV = (DocumentNumericVector) dnvList.get(i);
            tempTotal = tempDNV.getTotalWord();
            tempHash = tempDNV.getNumericVector();

            tempItr = tempHash.keySet().iterator();
            while (tempItr.hasNext()) {
                tempKey = (String) tempItr.next();
                tempValue = (int) tempHash.get(tempKey);
                tempValue = (tempValue * 1.0) / tempTotal;
                tempHash.put(tempKey, tempValue);
            }
            tempDNV.setNumericVector(tempHash);
            tfList.add(tempDNV);
        }
        return tfList;
    }

    public ArrayList getIDF(ArrayList dnvList) {
        int totalDocument = dnvList.size();
        ArrayList idfList = new ArrayList();
        DocumentNumericVector tempDNV;
        DocumentNumericVector tempDNV2;
        HashMap tempHash;
        Iterator tempItr;
        String tempKey;
        double tempValue;
        int docCount;
        double tempIdf;
        for (int i = 0; i < dnvList.size(); i++) {
            tempDNV = (DocumentNumericVector) dnvList.get(i);
            tempHash = tempDNV.getNumericVector();

            tempItr = tempHash.keySet().iterator();
            while (tempItr.hasNext()) {
                tempKey = (String) tempItr.next();
                docCount = 0;
                for (int j = 0; j < dnvList.size(); j++) {
                    tempDNV2 = (DocumentNumericVector) dnvList.get(j);
                    if (tempDNV2.containsWord(tempKey)) {
                        docCount++;
                        ////System.out.println(tempKey+"++"+docCount);
                    }
                }
                tempIdf = Math.log(((totalDocument * 1.0) / docCount));
                //System.out.print(totalDocument+" "+" "+docCount+" "+tempKey+" "+tempIdf);
                ////System.out.println("");
                tempHash.put(tempKey, tempIdf);
            }
            tempDNV.setNumericVector(tempHash);
            idfList.add(tempDNV);
        }
        return idfList;
    }

    public ArrayList getTF_IDF(ArrayList dnvList) {
        ArrayList tf_idfList = new ArrayList();
        ArrayList testTF = getTF(dnvList);
        ArrayList testIDF = getIDF(dnvList);

        HashMap tempHash;
        DocumentNumericVector tempTFD;
        DocumentNumericVector tempIDFD;
        Iterator itr;

        double tempT;
        double tempF;
        String tempKey;
        for (int i = 0; i < dnvList.size(); i++) {
            tempTFD = (DocumentNumericVector) testTF.get(i);
            tempIDFD = (DocumentNumericVector) testIDF.get(i);

            tempHash = tempTFD.getNumericVector();
            itr = tempHash.keySet().iterator();
            while (itr.hasNext()) {
                tempKey = (String) itr.next();
                tempT = (double) tempTFD.getNumericVector().get(tempKey);
                tempF = (double) tempIDFD.getNumericVector().get(tempKey);
                tempHash.put(tempKey, (tempT * 1.0 * tempF));
            }
            tempTFD.setNumericVector(tempHash);
            tf_idfList.add(tempTFD);
        }
        return tf_idfList;
    }

    public double calculateDocMod(DocumentNumericVector dnv) {
        double d1Mod = 0.0;
        double tempW;
        String tempKey;
        Iterator mkItr = dnv.getNumericVector().keySet().iterator();
        while (mkItr.hasNext()) {
            tempKey = (String) mkItr.next();
            tempW = (double) dnv.getNumericVector().get(tempKey);
            d1Mod = d1Mod + (tempW * 1.0 * tempW);
        }
        return d1Mod;
    }

    public void decideByHamming(ArrayList testVectorList, ArrayList booleanVectorList, int K) {
        //System.out.println("Deciding by hamming ... ");
        String wc = new String("h" + K);
        Test.output("Deciding by hamming distance ", wc);
        Test.output("Total train doc : " + booleanVectorList.size(), wc);
        Test.output("Total test doc : " + testVectorList.size(), wc);
        Test.output("Key, K = " + K, wc);
        Test.output("=================================================", wc);
        HashMap tempMap;
        int tempCount;

        int misClass = 0;
        String actual;
        String predicted;

        DocumentBooleanVector tempTest;
        ArrayList hdList;
        for (int i = 0; i < testVectorList.size(); i++) {
            //Test.output("#news" + (i + 1), wc);
            //System.out.println("+++++++++++++++++++++hamming+++++++++++++++++++++++++++++++++" + (i + 1));
            System.out.println(i+1);
            tempTest = (DocumentBooleanVector) testVectorList.get(i);
            actual = tempTest.getTopic();
            //Test.output("Actual topic : " + tempTest.getTopic(), wc);
            hdList = getHammingDistance(tempTest, booleanVectorList);

            Collections.sort(hdList, hdc);
            Iterator itr = hdList.iterator();
            DocumentBooleanVector temp;
            int count = 0;
            tempMap = new HashMap();
            while (itr.hasNext()) {
                if (count == K) {
                    break;
                }
                temp = (DocumentBooleanVector) itr.next();
                if (!tempMap.containsKey(temp.getTopic())) {
                    tempMap.put(temp.getTopic(), 1);
                } else {
                    tempCount = (int) tempMap.get(temp.getTopic());
                    tempCount++;
                    tempMap.put(temp.getTopic(), tempCount);
                }
                count++;
            }

            predicted = getPredictedTopic(tempMap);
            Test.output("Predicted topic : " + predicted, wc);
            if (!actual.equals(predicted)) {
                misClass++;
            }
            //Test.output("---------------------------------------------", wc);
        }
        Test.output("=================================================", wc);
        Test.output("===================Summary=======================", wc);
        Test.output("Total : " + testVectorList.size(), wc);
        Test.output("Misclassified : " + misClass, wc);
        Test.output("Accuracy : " + ((misClass * 1.0 * 100) / testVectorList.size()) + "% ", wc);
        //System.out.println("return.");
    }

    public void decideByEuclidean(ArrayList testVectorList, ArrayList numericVectorList, int K) {
        //System.out.println("Deciding by euclidean ... ");
        String wc = new String("e" + K);
        Test.output("Deciding by euclidean distance ", wc);
        Test.output("Total train doc : " + numericVectorList.size(), wc);
        Test.output("Total test doc : " + testVectorList.size(), wc);
        Test.output("Key, K = " + K, wc);
        Test.output("=================================================", wc);
        HashMap tempMap;
        int tempCount;

        int misClass = 0;
        String actual;
        String predicted;

        DocumentNumericVector tempTest;
        ArrayList edList;
        for (int i = 0; i < testVectorList.size(); i++) {
            //Test.output("#news" + (i + 1), wc);
            //System.out.println("++++++++++++++++++++euclidean++++++++++++++++++++++++++++++++++" + (i + 1));
            System.out.println(i+1);
            tempTest = (DocumentNumericVector) testVectorList.get(i);
            actual = tempTest.getTopic();
            //Test.output("Actual topic : " + tempTest.getTopic(), wc);
            edList = getEuclideanDistance(tempTest, numericVectorList);

            Collections.sort(edList, edc);
            Iterator itr = edList.iterator();
            DocumentNumericVector temp;
            int count = 0;
            tempMap = new HashMap();
            while (itr.hasNext()) {
                if (count == K) {
                    break;
                }
                temp = (DocumentNumericVector) itr.next();
                if (!tempMap.containsKey(temp.getTopic())) {
                    tempMap.put(temp.getTopic(), 1);
                } else {
                    tempCount = (int) tempMap.get(temp.getTopic());
                    tempCount++;
                    tempMap.put(temp.getTopic(), tempCount);
                }
                count++;
            }

            predicted = getPredictedTopic(tempMap);
            //Test.output("Predicted topic : " + predicted, wc);
            if (!actual.equals(predicted)) {
                misClass++;
            }
            //Test.output("---------------------------------------------", wc);
        }
        Test.output("=================================================", wc);
        Test.output("===================Summary=======================", wc);
        Test.output("Total : " + testVectorList.size(), wc);
        Test.output("Misclassified : " + misClass, wc);
        Test.output("Accuracy : " + ((misClass * 1.0 * 100) / testVectorList.size()) + "% ", wc);
        //System.out.println("return.");
    }

    public void decideByCosineSimilarity(ArrayList testTIL, ArrayList trainTIL, int K) {
        //System.out.println("Deciding by Cosine similarity ... ");
        String wc = new String("c" + K);
        Test.output("Deciding by Cosine similarity ", wc);
        Test.output("Total train doc : " + trainTIL.size(), wc);
        Test.output("Total test doc : " + testTIL.size(), wc);
        Test.output("Key, K = " + K, wc);
        Test.output("=================================================", wc);
        HashMap tempMap;
        int tempCount;

        int misClass = 0;
        String actual;
        String predicted;

        int totalTrain = trainTIL.size();
        ArrayList wList;
        DocumentNumericVector tempTest;
        Iterator itr;
        String key;
        double d;
        for (int i = 0; i < testTIL.size(); i++) {
            //Test.output("#news" + (i + 1), wc);
            //System.out.println("++++++++++++++++++++cosine similarity+++++++++++++++++++++++++++++" + (i + 1));
            System.out.println(i+1);
            tempTest = (DocumentNumericVector) testTIL.get(i);
            actual = tempTest.getTopic();
            //Test.output("Actual topic : " + tempTest.getTopic(), wc);
            wList = getCosineSimilarity(tempTest, trainTIL);

            Collections.sort(wList, csc);
            itr = wList.iterator();
            DocumentNumericVector temp;
            int count = 0;
            tempMap = new HashMap();
            while (itr.hasNext()) {
                if (count == K) {
                    break;
                }
                temp = (DocumentNumericVector) itr.next();
                if (!tempMap.containsKey(temp.getTopic())) {
                    tempMap.put(temp.getTopic(), 1);
                } else {
                    tempCount = (int) tempMap.get(temp.getTopic());
                    tempCount++;
                    tempMap.put(temp.getTopic(), tempCount);
                }
                count++;
            }
            predicted = getPredictedTopic(tempMap);
            //Test.output("Predicted topic : " + predicted, wc);
            if (!actual.equals(predicted)) {
                misClass++;
            }
            //Test.output("---------------------------------------------", wc);
        }
        Test.output("=================================================", wc);
        Test.output("===================Summary=======================", wc);
        Test.output("Total : " + testTIL.size(), wc);
        Test.output("Misclassified : " + misClass, wc);
        Test.output("Accuracy : " + ((misClass * 1.0 * 100) / testTIL.size()) + "% ", wc);
        //System.out.println("return.");
        //System.out.println("return.");
    }

    public String getPredictedTopic(HashMap tempMap) {
        //System.out.println("Predicting ... ");
        String maxTopic;
        String tempStr;
        int maxCount;
        int tempCount;
        Set s = tempMap.keySet();
        Iterator itr = s.iterator();
        maxTopic = "";
        maxCount = -1;
        while (itr.hasNext()) {
            tempStr = (String) itr.next();
            tempCount = (int) tempMap.get(tempStr);
            if (tempCount > maxCount) {
                maxCount = tempCount;
                maxTopic = tempStr;
            }
            //System.out.println(tempStr + " " + tempCount);
        }
        //System.out.println("Ans: " + maxTopic);
        return maxTopic;
    }

    public HDComparator getHdc() {
        return hdc;
    }

    public EDComparator getEdc() {
        return edc;
    }

    class HDComparator implements Comparator<DocumentBooleanVector> {

        @Override
        public int compare(DocumentBooleanVector o1, DocumentBooleanVector o2) {
            if (o1.hd < o2.hd) {
                return -1;
            } else if (o1.hd > o2.hd) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    class EDComparator implements Comparator<DocumentNumericVector> {

        @Override
        public int compare(DocumentNumericVector o1, DocumentNumericVector o2) {
            if (o1.edOrW < o2.edOrW) {
                return -1;
            } else if (o1.edOrW > o2.edOrW) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    class CSComparator implements Comparator<DocumentNumericVector> {

        @Override
        public int compare(DocumentNumericVector o1, DocumentNumericVector o2) {
            if (o1.edOrW > o2.edOrW) {
                return -1;
            } else if (o1.edOrW < o2.edOrW) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

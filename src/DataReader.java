
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    public Skiper skiper;
    public BufferedReader br;
    public String[] strArr;
    public String str;

    public DataReader(String skp, String src) {
        skiper = new Skiper(skp, src);
    }

    public void readDocument(String fileName) {
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    // got the topic no need to go further
                    System.out.println((++count) + str);
                    continue;
                }

                //others words in a line
                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i])) {
                        System.out.print(strArr[i] + " ");
                    }
                }
                System.out.println("");
            }
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
        }
    }

    public ArrayList createBooleanVector(String fileName) {
        ArrayList documentVectors = new ArrayList();
        DocumentBooleanVector tempDBV = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    if (tempDBV != null) {
                        documentVectors.add(tempDBV);
                    }
                    tempDBV = new DocumentBooleanVector(str);
                    continue;
                }

                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i]) && !tempDBV.containsWord(strArr[i])) {
                        tempDBV.addWord(strArr[i]);
                    }
                }
            }

            // EOF reached - add the last vector - return
            if (tempDBV != null) {
                documentVectors.add(tempDBV);
            }
            return documentVectors;
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
            return null;
        }
    }

    public ArrayList createNumericVector(String fileName) {
        ArrayList documentVectors = new ArrayList();
        DocumentNumericVector tempDNV = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    if (tempDNV != null) {
                        tempDNV.setTotalWord(count);
                        documentVectors.add(tempDNV);
                    }
                    tempDNV = new DocumentNumericVector(str);
                    count = 0;
                    continue;
                }

                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i])) {
                        count++;
                        if (tempDNV.containsWord(strArr[i])) {
                            tempDNV.updateKeyValue(strArr[i]);
                        }
                        else{
                            tempDNV.addWord(strArr[i]);
                        }
                    }
                }
            }

            // EOF reached - add the last vector - return
            if (tempDNV != null) {
                tempDNV.setTotalWord(count);
                documentVectors.add(tempDNV);
            }
            return documentVectors;
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
            return null;
        }
    }

    // skips all numeric values, punctuation (except in word hyphen)
    public String[] getWords(String s) {
        String[] arr;
        s = s.trim().toLowerCase();

        s = s.replaceAll("[^ a-zA-Z.\\-]", " ")
                .replaceAll("[.]", "")
                .replaceAll(" {2,}", " ");
        arr = s.split(" ");
        return arr;
    }

    public boolean isBlankLine(String[] arr) {
        return (arr.length == 1 && arr[0].length() == 0);
    }

    public String getTopic() throws IOException {
        // 1st blank
        if (isBlankLine(strArr)) {
            // read next line - and it is not null
            if ((str = br.readLine()) != null) {
                strArr = getWords(str);
                // 2nd blank line
                if (isBlankLine(strArr)) {
                    while (true) {
                        if ((str = br.readLine()) == null) {
                            // EOF reached - return 
                            return null;
                        }
                        strArr = getWords(str);
                        // skip the succeeding blank lines
                        if (isBlankLine(strArr)) {
                            continue;
                        }
                        // some text found at last - this is the topic - return it
                        // if the topic contains space seperated string it will protect that
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < strArr.length; i++) {
                            sb.append(strArr[i] + " ");
                        }
                        return sb.toString().trim();
                    }
                }
                // only one blank - not the topic - our read line won't be
                // lost as it is read in a global variable
                // go back and process that - return
            }
            // EOF reached - return
            return null;
        }
        // not blank - return
        return null;
    }

    // considered words -- 
    // length more than 1 -- single lettered words are skipped
    // word not in the skip.data
    // word not in the source.data
    public boolean consider(String str) {
        return str.length() > 1 && !skiper.skip(str) && !skiper.source(str);
    }
}

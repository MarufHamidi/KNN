
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Test {

    public static BufferedWriter outputFile;
    public static BufferedWriter logFile;
    public static BufferedWriter treesFile;

    public static HashMap brHash;
    
    public static void reset() {
        try {
            Test.outputFile = new BufferedWriter(new FileWriter(new File("output.txt")));
            Test.logFile = new BufferedWriter(new FileWriter(new File("log.txt")));
            
            Test.brHash = new HashMap();
            Test.brHash.put("h1", new BufferedWriter(new FileWriter(new File("output/oh1.out"))));
            Test.brHash.put("h3", new BufferedWriter(new FileWriter(new File("output/oh3.out"))));
            Test.brHash.put("h5", new BufferedWriter(new FileWriter(new File("output/oh5.out"))));
            
            Test.brHash.put("e1", new BufferedWriter(new FileWriter(new File("output/oe1.out"))));
            Test.brHash.put("e3", new BufferedWriter(new FileWriter(new File("output/oe3.out"))));
            Test.brHash.put("e5", new BufferedWriter(new FileWriter(new File("output/oe5.out"))));
            
            Test.brHash.put("c1", new BufferedWriter(new FileWriter(new File("output/oc1.out"))));
            Test.brHash.put("c3", new BufferedWriter(new FileWriter(new File("output/oc3.out"))));
            Test.brHash.put("c5", new BufferedWriter(new FileWriter(new File("output/oc5.out"))));
            
        } catch (Exception ex) {
            Test.output("rwerwe");
        }
    }

    public static void readTest(String docFile) {
        String skipListFile = "skip.data";
        String sourceListFile = "source.data";

        DataReader dr = new DataReader(skipListFile, sourceListFile);

        dr.readDocument(docFile);
    }

    public static void booleanVectorTest(String docFile) {
        System.out.println("Boolean vector test running ...");
        String skipListFile = "skip.data";
        String sourceListFile = "source.data";

        DataReader dr = new DataReader(skipListFile, sourceListFile);

        ArrayList v = dr.createBooleanVector(docFile);
        Test.output("Total news : " + v.size());
        Test.output("");
        Test.output("****************************");
        Test.output("");
        DocumentBooleanVector dbv;
        Iterator itr = v.iterator();
        Iterator itr2;
        int count = 0;
        while (itr.hasNext()) {
            dbv = (DocumentBooleanVector) itr.next();
            Test.output("Topic#"+(++count)+ " : " + dbv.getTopic());
            Test.output("---------Words---------");
            itr2 = dbv.getBooleanVector().keySet().iterator();
            String s;
            while (itr2.hasNext()) {
                s = (String) itr2.next();
                Test.output(s + " - " + dbv.getBooleanVector().get(s));
            }
            Test.output("");
            Test.output("****************************");
            Test.output("");
        }
        System.out.println("See 'output.txt'.");
    }

    public static void numericVectorTest(String docFile) {
        System.out.println("Numeric vector test running ...");
        String skipListFile = "skip.data";
        String sourceListFile = "source.data";

        DataReader dr = new DataReader(skipListFile, sourceListFile);

        ArrayList v = dr.createNumericVector(docFile);
        Test.output("Total news : " + v.size());
        Test.output("");
        Test.output("****************************");
        Test.output("");
        DocumentNumericVector dnv;
        Iterator itr = v.iterator();
        Iterator itr2;
        int count = 0;
        while (itr.hasNext()) {
            dnv = (DocumentNumericVector) itr.next();
            Test.output("Topic#"+(++count)+ " : " + dnv.getTopic());
            Test.output("---------Words---------");
            itr2 = dnv.getNumericVector().keySet().iterator();
            String s;
            while (itr2.hasNext()) {
                s = (String) itr2.next();
                Test.output(s + " - " + dnv.getNumericVector().get(s));
            }
            Test.output("");
            Test.output("****************************");
            Test.output("");
        }
        System.out.println("See 'output.txt'.");
    }

    public static void output(Object obj) {
        String nl = System.getProperty("line.separator");
        try {
            outputFile.write(obj + nl);
            outputFile.flush();
        } catch (Exception ex) {
            System.out.println("Exception in Test.outout - " + ex);
        }
    }
    
    public static void output(Object obj, String str) {
        String nl = System.getProperty("line.separator");
        try {
            BufferedWriter outputFileCK = (BufferedWriter) brHash.get(str);
            outputFileCK.write(obj + nl);
            outputFileCK.flush();
        } catch (Exception ex) {
            System.out.println("Exception in Test.outoutH1 - " + ex);
        }
    }
    

    public static void log(Object obj) {
        String nl = System.getProperty("line.separator");
        try {
            logFile.write(obj + nl);
            logFile.flush();
        } catch (Exception ex) {
            System.out.println("Exception in Test.log - " + ex);
        }
    }
}

package projectint;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

/**
 *
 * @author josephine
 */
public class ProjectINT {

    private static ArrayList fairytales;
    //Hashmap of EmoLex: Word - anger/anticipation/disgust/fear/joy/negative/positive/sadness/surprise/trust
    private static HashMap<String, int[]> words;
    //Hashmap of the Text Emotion Database
    private static HashMap<Integer, HashMap<Integer, float[]>> ted;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/inar";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        words = new HashMap<String, int[]>();

        //Fill up HashMap words with the EmoLex
        File f = new File("C:\\Users\\josephine\\ProjectINT\\lexicon.txt");
        if (f.isFile()) {

            ArrayList<String> text = ImportFile(f);

            int counter = 0;
            int[] values = new int[10];
            for (String t : text) {

                String[] regel = t.split("\\t");
                if (!words.containsKey(regel[0])) {
                    //System.out.println("print"+regel[0]);
                    values = new int[10];
                    values[0] = Integer.parseInt(regel[2]);
                    words.put(regel[0], values);
                    counter = 1;
                } else {
                    values[counter] = Integer.parseInt(regel[2]);
                    words.put(regel[0], values);
                    counter++;

                }
            }

        }

        //Fill up ArrayList fairytales with all the fairytales and fill up the Text Emotion Database and hashmap with the values of the sentences of these fairytales
        fairytales = new ArrayList();
        ted = new HashMap<Integer, HashMap<Integer, float[]>>();
        File folder = new File("C:\\Users\\josephine\\ProjectINT\\Stories\\");
        File[] listOfFiles = folder.listFiles();
        int textid = 0;
        for (File file : listOfFiles) {

            if (file.isFile()) {
                ArrayList<String> text = ImportFile(file);
                if (text != null) {
                    fairytales.add(text);
                    textid++;
                    int sentenceid = 0;
                    HashMap sed = new HashMap<Integer, float[]>();
                    for (String sentence : text) {

                        if (!sentence.replace("'", "").replace(",", "").replace(" ", "").replace("-", "").isEmpty()) {
                            sentenceid++;
// System.out.println("Sentence: " + sentence);
                            float[] results = readEmotions(sentence.replace(",", "").replace("'", "").replace("-", ""));
                            writetoTED(textid, sentenceid, results[0], results[1], results[2], results[3], results[4], results[5], results[6], results[7], results[8], results[9]);
                            System.out.println(sentence);
//float anger 0, float anticipation 1, float disgust 2, float fear 3, float joy 4, float negative 5, float positive 6, float sadness 7, float surprise 8, float trust 9
                            //positive + ", " + negative + ", " + anger + ", " + anticipation + ", " + disgust + ", " + fear + ", " + joy + ", " + sadness + ", " + surprise + ", " + trust + ")";
                            sed.put(sentenceid, new float[]{results[6], results[5], results[0], results[1], results[2], results[3], results[4], results[7], results[8], results[9]});
                        }

                    }
                    ted.put(textid, sed);
                } else {
                    System.out.println("Story had not enough sentences");
                }
            }
        }

        //Fill up the Text Chunck Database
        boolean tceddone = false;
        int textID = 0;
        while (tceddone == false) {
            textID++;
            if (ted.containsKey(textID)) {
                HashMap<Integer, float[]> sed = ted.get(textID);

                float[] totalemo = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                float[] totalemo1 = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                float[] totalemo2 = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                float[] totalemo3 = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                float emomax = 0;
                float emomax1 = 0;
                float emomax2 = 0;
                float emomax3 = 0;
                boolean seddone = false;
                int sentID = 0;

                int size = sed.size();
                int half = size / 2;
                while (seddone == false) {
                    sentID++;

                    if (sed.containsKey(sentID)) {
                        int emoid = 0;
                        for (float emo : sed.get(sentID)) {
                            totalemo[emoid] = totalemo[emoid] + emo;
                            if (emo > emomax) {
                                emomax = emo;
                            }

                            if (sentID <= 10) {
                                totalemo1[emoid] = totalemo1[emoid] + emo;
                                if (emo > emomax1) {
                                    emomax1 = emo;
                                }
                            } else if (sentID >= half - 4 && sentID <= half + 5) {
                                totalemo2[emoid] = totalemo2[emoid] + emo;
                                if (emo > emomax2) {
                                    emomax2 = emo;
                                }
                            } else if (sentID >= size - 10) {
                                totalemo3[emoid] = totalemo3[emoid] + emo;
                                if (emo > emomax3) {
                                    emomax3 = emo;
                                }
                            }

                            emoid++;
                        }
                    } else {
                        seddone = true;
                    }

                }
                float[] meanemo = new float[]{totalemo[0] / size, totalemo[1] / size, totalemo[2] / size, totalemo[3] / size, totalemo[4] / size, totalemo[5] / size, totalemo[6] / size, totalemo[7] / size, totalemo[8] / size, totalemo[9] / size};
                float[] meanemo1 = new float[]{totalemo1[0] / size, totalemo1[1] / size, totalemo1[2] / size, totalemo1[3] / size, totalemo1[4] / size, totalemo1[5] / size, totalemo1[6] / size, totalemo1[7] / size, totalemo1[8] / size, totalemo1[9] / size};
                float[] meanemo2 = new float[]{totalemo2[0] / size, totalemo2[1] / size, totalemo2[2] / size, totalemo2[3] / size, totalemo2[4] / size, totalemo2[5] / size, totalemo2[6] / size, totalemo2[7] / size, totalemo2[8] / size, totalemo2[9] / size};
                float[] meanemo3 = new float[]{totalemo3[0] / size, totalemo3[1] / size, totalemo3[2] / size, totalemo3[3] / size, totalemo3[4] / size, totalemo3[5] / size, totalemo3[6] / size, totalemo3[7] / size, totalemo3[8] / size, totalemo3[9] / size};
                writeToTCED(textID, emomax, meanemo, emomax1, meanemo1, emomax2, meanemo2, emomax3, meanemo3);
            } else {
                tceddone = true;
            }
        }
    }

    //Import a file to text
    public static ArrayList<String> ImportFile(File file) {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        ArrayList<String> text = new ArrayList<String>();
        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            // dis.available() returns 0 if the file does not have more lines.
            String story = "";
            int amountofsen = 0;
            while (dis.available() != 0) {

                // this statement reads the line from the file and print it to
                // the console.
                String[] sentences = dis.readLine().split("\\.");

                for (String sentence : sentences) {
                    text.add(sentence);
                    amountofsen++;
                }

            }
            //We don't read in stories with less then 30 sentences
            if (amountofsen < 30) {
                return null;
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static float[] readEmotions(String sentence) {
        String[] wordsinsen = sentence.split(" ");
        float[] emonumber = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int counter = 0;
        for (String word : wordsinsen) {
            //Check if word is in the EmoLex
            if (words.containsKey(word.toLowerCase())) {
                int[] emo = words.get(word.toLowerCase());
                for (int i = 0; i < 10; i++) {
                    emonumber[i] += emo[i];
                }
            }
            counter++;
        }
        for (int i = 0; i < 10; i++) {
            emonumber[i] = emonumber[i] / counter;
        }
        return emonumber;

    }

    //Write to Text Emotion Database
    public static boolean writetoTED(int textid, int sentenceid, float anger, float anticipation, float disgust, float fear, float joy, float negative, float positive, float sadness, float surprise, float trust) {
        Connection conn = null;
        Statement stmt = null;
        boolean succes = false;
        try {
            //STEP 2: Register JDBC driver

            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, null);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();

            String sql = "INSERT INTO ted "
                    + "VALUES (" + textid + ", " + sentenceid + ", " + positive + ", " + negative + ", " + anger + ", " + anticipation + ", " + disgust + ", " + fear + ", " + joy + ", " + sadness + ", " + surprise + ", " + trust + ")";
            stmt.executeUpdate(sql);

            succes = true;
            System.out.println("Inserted record into the table...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                return false;
            }//end finally try
        }//end try

        return succes;
    }

    // Write to Text Chunk Emotion Database
    private static boolean writeToTCED(int textID, float emomax, float[] meanemo, float emomax1, float[] meanemo1, float emomax2, float[] meanemo2, float emomax3, float[] meanemo3) {
        boolean succes = false;
        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver

            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, null);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();

            String sql = "INSERT INTO tced "
                    + "VALUES (" + textID + ", " + emomax + ", 0, " + meanemo[0] + ", " + meanemo[1] + ", " + meanemo[2] + ", " + meanemo[3] + ", " + meanemo[4] + ", " + meanemo[5] + ", " + meanemo[6] + ", " + meanemo[7] + ", " + meanemo[8] + ", " + meanemo[9] + ", "
                    + emomax1 + ", 0, " + meanemo1[0] + ", " + meanemo1[1] + ", " + meanemo1[2] + ", " + meanemo1[3] + ", " + meanemo1[4] + ", " + meanemo1[5] + ", " + meanemo1[6] + ", " + meanemo1[7] + ", " + meanemo1[8] + ", " + meanemo1[9] + ", "
                    + emomax2 + ", 0, " + meanemo2[0] + ", " + meanemo2[1] + ", " + meanemo2[2] + ", " + meanemo2[3] + ", " + meanemo2[4] + ", " + meanemo2[5] + ", " + meanemo2[6] + ", " + meanemo2[7] + ", " + meanemo2[8] + ", " + meanemo2[9] + ", "
                    + emomax3 + ", 0, " + meanemo3[0] + ", " + meanemo3[1] + ", " + meanemo3[2] + ", " + meanemo3[3] + ", " + meanemo3[4] + ", " + meanemo3[5] + ", " + meanemo3[6] + ", " + meanemo3[7] + ", " + meanemo3[8] + ", " + meanemo3[9] + ", "
                    + check(emomax1 / emomax2) + ", 0, " + check(meanemo1[0] / meanemo2[0]) + ", " + check(meanemo1[1] / meanemo2[1]) + ", " + check(meanemo1[2] / meanemo2[2]) + ", " + check(meanemo1[3] / meanemo2[3]) + ", " + check(meanemo1[4] / meanemo2[4]) + ", " + check(meanemo1[5] / meanemo2[5]) + ", " + check(meanemo1[6] / meanemo2[6]) + ", " + check(meanemo1[7] / meanemo2[7]) + ", " + check(meanemo1[8] / meanemo2[8]) + ", " + check(meanemo1[9] / meanemo2[9]) + ", "
                    + check(emomax1 / emomax3) + ", 0, " + check(meanemo1[0] / meanemo3[0]) + ", " + check(meanemo1[1] / meanemo3[1]) + ", " + check(meanemo1[2] / meanemo3[2]) + ", " + check(meanemo1[3] / meanemo3[3]) + ", " + check(meanemo1[4] / meanemo3[4]) + ", " + check(meanemo1[5] / meanemo3[5]) + ", " + check(meanemo1[6] / meanemo3[6]) + ", " + check(meanemo1[7] / meanemo3[7]) + ", " + check(meanemo1[8] / meanemo3[8]) + ", " + check(meanemo1[9] / meanemo3[9]) + ", "
                    + check(emomax2 / emomax3) + ", 0, " + check(meanemo2[0] / meanemo3[0]) + ", " + check(meanemo2[1] / meanemo3[1]) + ", " + check(meanemo2[2] / meanemo3[2]) + ", " + check(meanemo2[3] / meanemo3[3]) + ", " + check(meanemo2[4] / meanemo3[4]) + ", " + check(meanemo2[5] / meanemo3[5]) + ", " + check(meanemo2[6] / meanemo3[6]) + ", " + check(meanemo2[7] / meanemo3[7]) + ", " + check(meanemo2[8] / meanemo3[8]) + ", " + check(meanemo2[9] / meanemo3[9])
                    + ")";
            System.out.println("%" + sql + "%");
            stmt.executeUpdate(sql);

            succes = true;
            System.out.println("Inserted record into the table...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                return false;
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return succes;
    }

    private static float check(float f) {
        if (Double.isNaN(f) || Double.isInfinite(f)) {
            f = 0;
        }
        return f;
    }

}

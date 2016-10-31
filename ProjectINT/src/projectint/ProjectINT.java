/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    /**
     * @param args the command line arguments
     */
    private static ArrayList fairytales;
    //Word - anger/anticipation/disgust/fear/joy/negative/positive/sadness/surprise/trust
    private static HashMap<String, int[]> words;
    private static HashMap<Integer, HashMap<Integer, float[]>> ted;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/inar";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        words = new HashMap<String, int[]>();

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

        fairytales = new ArrayList();

        File folder = new File("C:\\Users\\josephine\\ProjectINT\\Stories\\");
        File[] listOfFiles = folder.listFiles();
        int textid = 0;
        for (File file : listOfFiles) {

            if (file.isFile()) {
                ArrayList<String> text = ImportFile(file);
                fairytales.add(text);
                textid++;
                int sentenceid = 0;
                HashMap sed = new HashMap<Integer, float[]>();
                for (String sentence : text) {

                    if (!sentence.replace("'", "").replace(",", "").replace(" ", "").replace("-", "").isEmpty()) {
                        sentenceid++;
// System.out.println("Sentence: " + sentence);
                        float[] results = readEmotions(sentence.replace(",", "").replace("'", "").replace("-", ""));
                        writetoDB(textid, sentenceid, results[0], results[1], results[2], results[3], results[4], results[5], results[6], results[7], results[8], results[9]);
                        //float anger 0, float anticipation 1, float disgust 2, float fear 3, float joy 4, float negative 5, float positive 6, float sadness 7, float surprise 8, float trust 9
                        //positive + ", " + negative + ", " + anger + ", " + anticipation + ", " + disgust + ", " + fear + ", " + joy + ", " + sadness + ", " + surprise + ", " + trust + ")";
                        sed.put(sentenceid, new float[]{results[6], results[5], results[0], results[1], results[2], results[3], results[4], results[7], results[8], results[9]});
                    }

                }
                ted.put(textid, sed);
            }
        }
        boolean teddone = false;
        int textID = 0;
        while (teddone == false) {
            textID ++;
            if(ted.containsKey(textID)) {
                HashMap<Integer, float[]> sed = ted.get(textID);
                float[] totalemo = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                float emomax = 0;
                boolean seddone = false;
                int sentID = 0;
                while (seddone == false) {
                    sentID ++;
                    if (sed.containsKey(sentID)){  
                        int emoid = 0;
                        for(float emo : sed.get(sentID)) {
                            totalemo[emoid] = totalemo[emoid] + emo;
                            if(emo > emomax) {
                                emomax = emo;
                            }
                            emoid ++;
                        }
                    } else {
                        seddone = true;
                    }     
                }     
            } else { 
                teddone = true;
            }
        }

        //for(float result :readEmotions("how the frog does talk")) {
        //    System.out.print(result + ",");
        //}
    }

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
            while (dis.available() != 0) {

                // this statement reads the line from the file and print it to
                // the console.
                String[] sentences = dis.readLine().split("\\.");
                for (String sentence : sentences) {
                    text.add(sentence);
                    //System.out.println(sentence + "+:");
                }

            }
            /* for(String sentence : story.split("\\.")) {
          text.add(sentence);
      }*/

            // dispose all the resources after using them.
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
            if (words.containsKey(word.toLowerCase())) {
                int[] emo = words.get(word.toLowerCase());
                for (int i = 0; i < 10; i++) {
                    emonumber[i] += emo[i];
                }
                //System.out.print("In dic" + word);
            } else {
                //System.out.print("Not in dic" + word);
            }
            counter++;
        }
        for (int i = 0; i < 10; i++) {
            emonumber[i] = emonumber[i] / counter;
        }
        return emonumber;

    }

    public static boolean writetoDB(int textid, int sentenceid, float anger, float anticipation, float disgust, float fear, float joy, float negative, float positive, float sadness, float surprise, float trust) {
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
        System.out.println("Goodbye!");

        return succes;
    }

}

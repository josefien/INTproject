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
    private static HashMap <String, int[]> words;
    public static void main(String[] args) {
        words = new HashMap<String, int[]>();
        
        
        
        File f = new File("C:\\Users\\josephine\\ProjectINT\\lexicon.txt");
            if (f.isFile()) {
                ArrayList<String> text = ImportFile(f);
                int counter = 0;
                int [] values = new int[10];
                for (String t : text) {
                    
                    String [] regel = t.split("\\t");
                    if(!words.containsKey(regel[0])) {
                        System.out.println("print"+regel[0]);
                        values = new int[10];
                        values[0] = Integer.parseInt(regel[2]);
                        words.put(regel[0], values);
                        counter = 1;
                    } else {
                        values[counter] = Integer.parseInt(regel[2]);
                        words.put(regel[0], values);
                        counter ++;
                       
                    }
                    
                    
                    
                }
            for(int a:words.get("idiotic")) {
                System.out.println(a);
            }
            
        }
        
        
        fairytales = new ArrayList();
        
        
        File folder = new File("C:\\Users\\josephine\\ProjectINT\\");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                ArrayList<String> text = ImportFile(file);
                for (String t : text) {
                    System.out.println(t);
                }
                fairytales.add(text);
            }
        }
        
      
    }
    
    public static ArrayList<String> ImportFile (File file) {
    
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
      while (dis.available() != 0) {
 
      // this statement reads the line from the file and print it to
        // the console.
        text.add(dis.readLine());
      }
 
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
    
}

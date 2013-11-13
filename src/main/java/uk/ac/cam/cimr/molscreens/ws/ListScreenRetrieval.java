package uk.ac.cam.cimr.molscreens.ws;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * This screen retriever is based on AIDs present on a file.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/11/13
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ListScreenRetrieval extends AbstractScreenRetrieval implements ScreenRetrieval {



    public ListScreenRetrieval(String pathToListOfAIDs) {
        super();
        this.AIDs = new HashSet<String>();
        readAIDsFromPath(pathToListOfAIDs);
    }

    private void readAIDsFromPath(String pathToListOfAIDs) {
        BufferedReader reader=null;
        try {
            reader = new BufferedReader(new FileReader(pathToListOfAIDs));
            String line;
            while((line=reader.readLine())!=null) {
                this.AIDs.add(line);
            }
            reader.close();
        } catch (IOException e) {

        } finally {
            try {
            reader.close();
            } catch (IOException e) {

            }
        }
    }
}

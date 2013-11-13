package uk.ac.cam.cimr.molscreens.ws;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 16/10/13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class BlackListBAFilter implements BioAssayFilter{

    private static final Logger LOGGER = Logger.getLogger(BlackListBAFilter.class);

    private Set<String> blackList;


    public BlackListBAFilter(InputStream streamToBlackList) {
        init();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamToBlackList));
            fillBlackList(reader);
        } catch (IOException e) {
            LOGGER.error("Could not use provided file to fill the blacklist",e);
            throw new RuntimeException(e);
        }
    }
    /**
     * Constructor receives a path to a file where each line only contains a single identifier to be black listed.
     *
     * @param pathToBlackList path to the file with the identifiers to be black listed.
     */
    public BlackListBAFilter(String pathToBlackList) {
        init();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToBlackList));
            fillBlackList(reader);
        } catch (IOException e) {
            LOGGER.error("Could not use provided file to fill the blacklist",e);
            throw new RuntimeException(e);
        }
    }

    private void init() {
        blackList = new HashSet<String>();
    }

    private void fillBlackList(BufferedReader reader) throws IOException {
        String line;
        while((line = reader.readLine())!=null) {
            blackList.add(line);
        }
        reader.close();
    }

    @Override
    public Set<String> filterAssays(Set<String> aiDs) {
        Set<String> tmp = new HashSet<>(aiDs);
        tmp.removeAll(blackList);
        return tmp;
    }
}

package uk.ac.cam.cimr.autophagy.ws;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    /**
     * Constructor receives a path to a file where each line only contains a single identifier to be black listed.
     *
     * @param pathToBlackList path to the file with the identifiers to be black listed.
     */
    public BlackListBAFilter(String pathToBlackList) {
        blackList = new HashSet<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToBlackList));
            fillBlackList(reader);
        } catch (IOException e) {
            LOGGER.error("Could not use provided file to fill the blacklist",e);
            throw new RuntimeException(e);
        }
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

package uk.ac.cam.cimr.autophagy.exec;

import org.kohsuke.args4j.Option;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/11/13
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class CLIOptions {

    @Option(name="-p",usage="Path to file with AIDs to use")
    private String pathToListOfAIDs;

    @Option(name="-q",usage = "Query to be submitted to pubchem")
    private String query;

    @Option(name = "-o",
            usage = "Path for output. Black list file aid_blacklist.txt will be searched on this path as well.",
            required = true)
    private String ouputPath;

    @Option(name="-t",usage = "Integer for setting a limit of AIDs to use for testing purposes.")
    private Integer testingLimit;

    public String getPathToListOfAIDs() {
        return pathToListOfAIDs;
    }

    public String getQuery() {
        return query;
    }

    public String getOuputPath() {
        return ouputPath;
    }

    public Integer getTestingLimit() {
        return testingLimit;
    }
}

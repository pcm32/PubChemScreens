package uk.ac.cam.cimr.molscreens.exec;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

/**
 * This bean holds the options that the main CLI entry point can receive.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/11/13
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class CLIOptions {

    @Option(name="-p",usage="Path to file with AIDs to use")
    private String pathToListOfAIDs;

    @Option(name="-q",usage = "Query to be submitted to pubchem, or file path to a file containing a query (use -f to indicate this)")
    private String query;

    @Option(name = "-o",
            usage = "Path for output. Black list file aid_blacklist.txt will be searched on this path as well.",
            required = true)
    private String ouputPath;

    @Option(name="-t",usage = "Integer for setting a limit of AIDs to use for testing purposes.")
    private Integer testingLimit;

    @Option(name = "-b",usage = "Path to a list of AIDs that should be neglected if the appear in the retrieval steps.")
    private String pathToBlackList;

    @Option(name="-f",handler=BooleanOptionHandler.class,usage = "Flag to indicate whether the string provided for -q is actually a file containing the query")
    private Boolean queryIsFilePath = false;

    /**
     * True if the string provided for option -q is supposed to be a file path.
     * @return
     */
    public Boolean getQueryIsFilePath() {
        return queryIsFilePath;
    }

    /**
     * Gets the path to a file with a list of PubChem BioAssay AIDs, one per line.
     *
     * @return the path.
     */
    public String getPathToListOfAIDs() {
        return pathToListOfAIDs;
    }

    /**
     * Gets the string query to be submitted to PubChem.
     *
     * @return the string query.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets the output path for files produced by the summary process.
     *
     * @return the path as String as given to the options.
     */
    public String getOuputPath() {
        return ouputPath;
    }

    /**
     * The testing limit allows to cap the process to go through only this many AIDs. If not set this value returns null.
     * This is mostly for testing purposes.
     *
     * @return the max number of AIDs to process.
     */
    public Integer getTestingLimit() {
        return testingLimit;
    }

    /**
     * A black list is a file containing AIDs that we want to avoid, as they have been isolated as irrelevant results for
     * some reason. The blacklist should just contain AIDs, one per line.
     *
     * @return
     */
    public String getPathToBlackList() {
        return pathToBlackList;
    }
}

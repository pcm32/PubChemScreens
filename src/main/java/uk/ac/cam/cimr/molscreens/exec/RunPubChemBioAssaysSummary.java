package uk.ac.cam.cimr.molscreens.exec;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import uk.ac.cam.cimr.molscreens.criteria.BioAssayKeywordScoreCriterion;
import uk.ac.cam.cimr.molscreens.criteria.PubChemCountActiveMolInAssay;
import uk.ac.cam.cimr.molscreens.criteria.PubChemIC50uMolBelow;
import uk.ac.cam.cimr.molscreens.io.BAssayBagAssaySummaryWriter;
import uk.ac.cam.cimr.molscreens.io.BioAssayBagWriter;
import uk.ac.cam.cimr.molscreens.io.Compound2BioAssayWriter;
import uk.ac.cam.cimr.molscreens.io.HighlyActiveCompoundsBioAssayBagWriter;
import uk.ac.cam.cimr.molscreens.scoring.BioAssayScorer;
import uk.ac.cam.cimr.molscreens.scoring.SimpleDictionaryBasedAssayScorer;
import uk.ac.cam.cimr.molscreens.ws.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the CLI entry point to run a summary of PubChem BioAssays.
 *
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class RunPubChemBioAssaysSummary {

    private static final Logger LOGGER = Logger.getLogger(RunPubChemBioAssaysSummary.class);

    List<BioAssayBagWriter> writers;
    BioAssayFilter[] filters;
    ScreenRetrieval ret;

    /**
     * Class for setting up the run of a screen set retrieval and summary.
     *
     * @param ret the type of {@link ScreenRetrieval} to use.
     * @param writers to be used to generate the output.
     */
    public RunPubChemBioAssaysSummary(ScreenRetrieval ret, BioAssayBagWriter... writers) {
        this.ret = ret;
        this.writers = Arrays.asList(writers);
    }

    public void run() {
        //TermQueryScreenRetrieval ret = new TermQueryScreenRetrieval();
        BioAssayScorer scorer = new SimpleDictionaryBasedAssayScorer(SimpleDictionaryBasedAssayScorer.class.getResourceAsStream("keywords.txt"));
        BioAssayBag bag = ret.getAssays();
        LOGGER.info("Retrieved bag with "+bag.getAssays().size()+" assays");
        bag.addCriteria(new PubChemCountActiveMolInAssay(), new PubChemIC50uMolBelow(20.0f), new BioAssayKeywordScoreCriterion(scorer));
        BioAssayAnnotator annotator = new BioAssayAnnotator();
        annotator.annotate(bag);
        LOGGER.info("Annotated the bag.");
        bag.compute();
        LOGGER.info("Computed the bag");
        for (BioAssayBagWriter writer : writers) {
            writer.write(bag);
            LOGGER.info("Writer "+writer.getClass().toString()+" done.");
        }

    }


    public static void main(String[] args) throws IOException {

        CLIOptions options = new CLIOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            System.err.println();
            throw new RuntimeException("Some problem with the arguments given.");
        }


        String path = options.getOuputPath();
        try {
            Files.createDirectory(FileSystems.getDefault().getPath(path));
        } catch (FileAlreadyExistsException e) {

        }
        BioAssayBagWriter highlyActiveCompoundsBioAssayBagWriter = new HighlyActiveCompoundsBioAssayBagWriter(path);
        BioAssayBagWriter bioAssaySummaryWriter = new BAssayBagAssaySummaryWriter(path);
        BioAssayBagWriter comp2AssayWriter = new Compound2BioAssayWriter(path);

        ScreenRetrieval retrieval=null;
        if(options.getQuery()!=null && options.getPathToListOfAIDs()!=null) {
            if(options.getQueryIsFilePath())
                retrieval = new CombinedRetrieval(new TermQueryScreenRetrieval(new File(options.getQuery())),
                        new ListScreenRetrieval(options.getPathToListOfAIDs()));
            else
                retrieval = new CombinedRetrieval(new TermQueryScreenRetrieval(options.getQuery()),
                    new ListScreenRetrieval(options.getPathToListOfAIDs()));
        }
        else if(options.getQuery()!=null) {
            if(options.getQueryIsFilePath())
                retrieval = new TermQueryScreenRetrieval(new File(options.getQuery()));
            else
                retrieval = new TermQueryScreenRetrieval(options.getQuery());
        } else if(options.getPathToListOfAIDs()!=null) {
            retrieval = new ListScreenRetrieval(options.getPathToListOfAIDs());
        } else {
            System.err.println("Either -q for query or -p for path to a list of AIDs needs to be provided.");
            parser.printUsage(System.err);
            return;
        }
        File blackList = new File(path + File.separator + "aid_blacklist.txt");
        if (blackList.exists()) {
            LOGGER.info("Using black list of identifiers in "+blackList.getAbsolutePath());
            retrieval.setBioAssayFilters(new BlackListBAFilter(blackList.getAbsolutePath()));
        } else if(options.getPathToBlackList()!=null) {
            LOGGER.info("Using black list of identifiers in "+options.getPathToBlackList());
            retrieval.setBioAssayFilters(new BlackListBAFilter(options.getPathToBlackList()));
        }
        if (options.getTestingLimit()!=null) {
            retrieval.setTestingLimit(options.getTestingLimit());
        }
        RunPubChemBioAssaysSummary runner = new RunPubChemBioAssaysSummary(retrieval, highlyActiveCompoundsBioAssayBagWriter,
                bioAssaySummaryWriter,
                comp2AssayWriter);
        runner.run();
    }
}

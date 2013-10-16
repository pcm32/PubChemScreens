package uk.ac.cam.cimr.autophagy.exec;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.criteria.PubChemCountActiveMolInAssay;
import uk.ac.cam.cimr.autophagy.criteria.PubChemIC50uMolBelow;
import uk.ac.cam.cimr.autophagy.io.BAssayBagAssaySummaryWriter;
import uk.ac.cam.cimr.autophagy.io.BioAssayBagWriter;
import uk.ac.cam.cimr.autophagy.io.Compound2BioAssayWriter;
import uk.ac.cam.cimr.autophagy.io.HighlyActiveCompoundsBioAssayBagWriter;
import uk.ac.cam.cimr.autophagy.ws.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class RunPubChemAutophagyRet {

    private static final Logger LOGGER = Logger.getLogger(RunPubChemAutophagyRet.class);

    String query;
    Integer testingLimit = 0;
    List<BioAssayBagWriter> writers;
    BioAssayFilter[] filters;

    public RunPubChemAutophagyRet(String query, BioAssayBagWriter... writers) {
        this.query = query;
        this.writers = Arrays.asList(writers);
    }

    public void setTestingLimit(Integer limit) {
        testingLimit = limit;
    }

    public void run() {
        ScreenRetrieval ret = new ScreenRetrieval();
        if(testingLimit>0)
            ret.setTestingLimit(testingLimit);
        if(filters!=null && filters.length>0)
            ret.setBioAssayFilters(filters);
        BioAssayBag bag = ret.getAssaysForQuery(query);
        LOGGER.info("Retrieved bag with "+bag.getAssays().size()+" assays");
        bag.addCriteria(new PubChemCountActiveMolInAssay(), new PubChemIC50uMolBelow(20.0f));
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
        String path = args[0];
        try {
            Files.createDirectory(FileSystems.getDefault().getPath(path));
        } catch (FileAlreadyExistsException e) {

        }
        BioAssayBagWriter highlyActiveCompoundsBioAssayBagWriter = new HighlyActiveCompoundsBioAssayBagWriter(path);
        BioAssayBagWriter bioAssaySummaryWriter = new BAssayBagAssaySummaryWriter(path);
        BioAssayBagWriter comp2AssayWriter = new Compound2BioAssayWriter(path);

        RunPubChemAutophagyRet runner = new RunPubChemAutophagyRet("autophagy", highlyActiveCompoundsBioAssayBagWriter,
                bioAssaySummaryWriter,
                comp2AssayWriter);

        File blackList = new File(path + File.separator + "aid_blacklist.txt");
        if (blackList.exists()) {
            LOGGER.info("Using black list of identifiers in "+blackList.getAbsolutePath());
            runner.setFilters(new BlackListBAFilter(blackList.getAbsolutePath()));
        }
        if (args.length > 1) {
            runner.setTestingLimit(Integer.parseInt(args[1]));
        }
        runner.run();
    }


    public void setFilters(BioAssayFilter... filters) {
        this.filters = filters;
    }
}

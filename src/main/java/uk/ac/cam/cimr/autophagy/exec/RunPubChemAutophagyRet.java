package uk.ac.cam.cimr.autophagy.exec;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.criteria.PubChemCountActiveMolInAssay;
import uk.ac.cam.cimr.autophagy.criteria.PubChemIC50uMolBelow;
import uk.ac.cam.cimr.autophagy.io.BAssayBagAssaySummaryWriter;
import uk.ac.cam.cimr.autophagy.io.BioAssayBagWriter;
import uk.ac.cam.cimr.autophagy.io.Compound2BioAssayWriter;
import uk.ac.cam.cimr.autophagy.io.HighlyActiveCompoundsBioAssayBagWriter;
import uk.ac.cam.cimr.autophagy.ws.BioAssayAnnotator;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.cam.cimr.autophagy.ws.ScreenRetrieval;

import java.io.IOException;
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
        Files.createDirectory(FileSystems.getDefault().getPath(path));
        BioAssayBagWriter highlyActiveCompoundsBioAssayBagWriter = new HighlyActiveCompoundsBioAssayBagWriter(path);
        BioAssayBagWriter bioAssaySummaryWriter = new BAssayBagAssaySummaryWriter(path);
        BioAssayBagWriter comp2AssayWriter = new Compound2BioAssayWriter(path);

        RunPubChemAutophagyRet runner = new RunPubChemAutophagyRet("autophagy",highlyActiveCompoundsBioAssayBagWriter,
                bioAssaySummaryWriter,
                comp2AssayWriter);
        if(args.length > 1) {
            runner.setTestingLimit(Integer.parseInt(args[1]));
        }
        runner.run();
    }


}

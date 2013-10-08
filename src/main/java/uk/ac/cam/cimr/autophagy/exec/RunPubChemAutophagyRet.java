package uk.ac.cam.cimr.autophagy.exec;

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

    String query;
    List<BioAssayBagWriter> writers;

    public RunPubChemAutophagyRet(String query, BioAssayBagWriter... writers) {
        this.query = query;
        this.writers = Arrays.asList(writers);
    }

    public void run() {
        ScreenRetrieval ret = new ScreenRetrieval();
        ret.setTestingLimit(30);
        BioAssayBag bag = ret.getAssaysForQuery(query);
        bag.addCriteria(new PubChemCountActiveMolInAssay(), new PubChemIC50uMolBelow(20.0f));
        BioAssayAnnotator annotator = new BioAssayAnnotator();
        annotator.annotate(bag);
        bag.compute();
        for (BioAssayBagWriter writer : writers) {
            writer.write(bag);
        }
    }


    public static void main(String[] args) throws IOException {
        String path = "/tmp/";
        BioAssayBagWriter highlyActiveCompoundsBioAssayBagWriter = new HighlyActiveCompoundsBioAssayBagWriter(path);
        BioAssayBagWriter bioAssaySummaryWriter = new BAssayBagAssaySummaryWriter(path);
        BioAssayBagWriter comp2AssayWriter = new Compound2BioAssayWriter(path);

        RunPubChemAutophagyRet runner = new RunPubChemAutophagyRet("autophagy",highlyActiveCompoundsBioAssayBagWriter,
                bioAssaySummaryWriter,
                comp2AssayWriter);

        runner.run();
    }


}

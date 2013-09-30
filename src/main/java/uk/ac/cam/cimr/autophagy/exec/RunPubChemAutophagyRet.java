package uk.ac.cam.cimr.autophagy.exec;

import uk.ac.cam.cimr.autophagy.criteria.PubChemCountActiveMolInAssay;
import uk.ac.cam.cimr.autophagy.criteria.PubChemIC50uMolBelow;
import uk.ac.cam.cimr.autophagy.io.BioAssayWriter;
import uk.ac.cam.cimr.autophagy.io.HighlyActiveCompoundsBioAssayWriter;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.cam.cimr.autophagy.ws.ScreenRetrieval;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class RunPubChemAutophagyRet {

    String query;
    BioAssayWriter writer;

    public RunPubChemAutophagyRet(String query, BioAssayWriter writer) {
        this.query = query;
        this.writer = writer;
    }

    public void run() {
        ScreenRetrieval ret = new ScreenRetrieval();
        ret.setTestingLimit(30);
        BioAssayBag bag = ret.getAssaysForQuery(query);
        bag.addCriteria(new PubChemCountActiveMolInAssay(), new PubChemIC50uMolBelow(20.0f));
        bag.compute();
        writer.write(bag);
    }


    public static void main(String[] args) throws IOException {
        String path = "/tmp/";
        BioAssayWriter writer = new HighlyActiveCompoundsBioAssayWriter(path);

        RunPubChemAutophagyRet runner = new RunPubChemAutophagyRet("autophagy",writer);

        runner.run();
    }


}
